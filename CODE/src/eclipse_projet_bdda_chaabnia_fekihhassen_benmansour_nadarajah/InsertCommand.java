package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.util.ArrayList;

public class InsertCommand {
    private String nom_relation;
    private ArrayList<String> values;
    FileManager fileManager = FileManager.getInstance();

    public InsertCommand(String command) {
        String[] cmd = command.split(" ");
        this.nom_relation = cmd[2];
        this.values = new ArrayList<>();
        String[] vals = cmd[4].substring(1, cmd[4].length() - 1).split(",");
        for (String val : vals) {
            values.add(val);
        }
    }

    public void execute() {
        TableInfo tableInfo = DataBaseInfo.getInstance().getTableInfo(nom_relation);
        if (tableInfo != null) {
            fileManager.insertIntoTable(tableInfo, values);
        } else {
            System.out.println("Table " + nom_relation + " does not exist.");
        }
    }
}