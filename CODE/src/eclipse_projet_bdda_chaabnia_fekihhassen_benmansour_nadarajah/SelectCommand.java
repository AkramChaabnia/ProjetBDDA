package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SelectCommand {
  private String relationName;
  private FileManager fileManager;
  private ArrayList<SelectCondition> conditions; // New field to store the conditions
  private boolean condition = false;

  public SelectCommand(String command) {
    String[] commandParts = command.split(" ");
    if (commandParts.length < 4) {
      throw new IllegalArgumentException("Commande mal formée");
    }
    this.relationName = commandParts[3];
    this.fileManager = FileManager.getInstance();

    for (String elements : commandParts) {
      if (elements.equals("WHERE")) {
        this.condition = true;
        break;
      }
    }

    if (this.condition) {
      String conditionsStr = command.substring(command.indexOf("WHERE") + 6).trim();
      this.conditions = parseConditions(conditionsStr);
    }
  }

  private ArrayList<SelectCondition> parseConditions(String conditionsStr) {
    ArrayList<SelectCondition> parsedConditions = new ArrayList<>();

    if (!conditionsStr.isEmpty()) {
      String[] conditionsSplit = conditionsStr.split(" AND ");
      for (String conditionStr : conditionsSplit) {
        parsedConditions.add(parseEachCondition(conditionStr.trim()));
      }
    } else {
      parsedConditions.add(new SelectCondition());
    }

    return parsedConditions;
  }

  private SelectCondition parseEachCondition(String conditionStr) {
    String[] parts = conditionStr.split("=|<|>|<=|>=|<>");
    String columnName = parts[0].trim();
    String value = parts[1].trim();
    String operator = conditionStr.substring(columnName.length(), conditionStr.length() - value.length()).trim();

    return new SelectCondition(columnName, operator, value);
  }

  public void execute() {
    System.out.println("Executing SELECT command...");

    try {
      TableInfo tableInfo = DataBaseInfo.getInstance().getTableInfo(relationName);

      if (tableInfo == null) {
        System.out.println("Table \"" + relationName + "\" does not exist.");
        return;
      }

      System.out.println("Fetching all records from table \"" + relationName + "\"...");
      System.out.println("Number of columns = " + tableInfo.getNb_colonnes());
      System.out.println(
          "Column names = " + tableInfo.getColInfoList().stream().map(ColInfo::getName).collect(Collectors.toList()));
      System.out.println("Header page id = " + tableInfo.getHeaderPageId());
      System.out.println("Number of records = " + fileManager.GetAllRecords(tableInfo).size());
      List<Record> records = fileManager.GetAllRecords(tableInfo);

      if (this.condition) {
        System.out.println("Filtering records based on conditions...");
        records = records.stream()
            .filter(record -> {
              System.out.println("Checking conditions for record: " + record);
              boolean satisfies = satisfiesConditions(record);
              System.out.println("Does record satisfy conditions? " + satisfies);
              return satisfies;
            })
            .collect(Collectors.toList());
      }

      System.out.println("Printing records...");
      for (Record record : records) {
        ArrayList<String> values = record.getRecvalues();
        for (String value : values) {
          System.out.print(value + " ; ");
        }
        System.out.println(".");
      }

      System.out.println("Total records = " + records.size());
    } catch (IOException e) {
      System.out.println("An IOException occurred while executing the SELECT command: " + e.getMessage());
    } catch (PageNotFoundException e) {
      System.out.println("A PageNotFoundException occurred while executing the SELECT command: " + e.getMessage());
    }
  }

  private boolean satisfiesConditions(Record record) {
    for (SelectCondition condition : conditions) {
      if (!condition.isSatisfiedBy(record)) {
        return false;
      }
    }

    return true;
  }
}