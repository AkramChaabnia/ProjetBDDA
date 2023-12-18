package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelectCommand {
  private String relationName;
  private FileManager fileManager;

  public SelectCommand(String command) {
    String[] commandParts = command.split(" ");
    this.relationName = commandParts[1];
    this.fileManager = FileManager.getInstance();
  }

  public void execute() throws IOException, PageNotFoundException {
    TableInfo tableInfo = fileManager.getTableInfo(relationName);
    List<Record> records = fileManager.GetAllRecords(tableInfo);

    for (Record record : records) {
      ArrayList<String> values = record.getRecvalues();
      for (String value : values) {
        System.out.print(value + " ; ");
      }
      System.out.println(".");
    }

    System.out.println("Total records = " + records.size());
  }
}