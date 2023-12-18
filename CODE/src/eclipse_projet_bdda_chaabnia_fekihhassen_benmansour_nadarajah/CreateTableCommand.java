package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.util.ArrayList;
import java.util.List;

public class CreateTableCommand {
  private String nom_relation;
  private ArrayList<ColInfo> colInfoList;
  FileManager fileManager = FileManager.getInstance();

  public CreateTableCommand(String command) {
    
    String[] cmd = command.split(" ");
    this.nom_relation = cmd[2];
    this.colInfoList = new ArrayList<>();
    String[] columns = cmd[3].substring(1, cmd[3].length() - 1).split(",");
    for (String column : columns) {
      String[] colInfo = column.split(":");
      String colName = colInfo[0];
      String colType = colInfo[1];
      ColInfo col = new ColInfo(colName, colType);
      colInfoList.add(col);
    }
  }

  public void execute() {
    DataBaseInfo databaseInfo = DataBaseInfo.getInstance();
    if (databaseInfo.tableExists(nom_relation)) {
      System.out.println("Relation \"" + nom_relation + "\" already exists.");
      return;
    }
    fileManager.createNewHeaderPage();
    PageId headerPageId = fileManager.getHeaderPageId();
    TableInfo tableInfo = new TableInfo(nom_relation, colInfoList, headerPageId);
    databaseInfo.addTableInfo(tableInfo);
  }
}
