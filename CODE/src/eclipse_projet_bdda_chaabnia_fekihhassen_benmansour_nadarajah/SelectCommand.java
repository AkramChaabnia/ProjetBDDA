package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class SelectCommand {
  private String relationName;
  private FileManager fileManager;

  public SelectCommand(String command) {
    String[] commandParts = command.split(" ");
    // Assuming the table name is always the fourth part of the command
    this.relationName = commandParts[3];
    this.fileManager = FileManager.getInstance();
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