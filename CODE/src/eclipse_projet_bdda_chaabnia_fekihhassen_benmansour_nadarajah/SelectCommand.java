package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SelectCommand {
  private String relationName;
  private FileManager fileManager;
  private String condition; // New field to store the condition

  public SelectCommand(String command) {
    String[] commandParts = command.split(" ");
    // Assuming the table name is always the fourth part of the command
    this.relationName = commandParts[3];
    this.fileManager = FileManager.getInstance();

    // Assuming the condition is always the sixth part of the command
    if (commandParts.length > 5) {
      this.condition = commandParts[5];
    }
  }

  public void execute() throws IOException, PageNotFoundException {
    // get table info
    TableInfo tableInfo = DataBaseInfo.getInstance().getTableInfo(relationName);

    if (tableInfo == null) {
      System.out.println("Table \"" + relationName + "\" does not exist.");
      return;
    }

    // get all the records
    List<Record> records = fileManager.GetAllRecords(tableInfo);
    // Filter the records based on the condition
    if (condition != null) {
      records = records.stream()
          .filter(record -> evaluateCondition(record, condition))
          .collect(Collectors.toList());
    }

    for (Record record : records) {
      ArrayList<String> values = record.getRecvalues();
      for (String value : values) {
        System.out.print(value + " ; ");
      }
      System.out.println(".");
    }

    System.out.println("Total records = " + records.size());
  }

  // This is a placeholder method. You'll need to implement it based on your
  // specific requirements.
  private boolean evaluateCondition(Record record, String condition) {
    // Evaluate the condition for the given record
    return true;
  }
}