package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.util.ArrayList;

public class InsertCommand {
    private String nomRelation;
    private ArrayList<String> values;
    FileManager fileManager = FileManager.getInstance();

    public InsertCommand(String command) {
        parseCommand(command);
    }

    private void parseCommand(String command) {
        String[] parts = command.split("VALUES");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Format incorrect pour la commande INSERT");
        }

        this.nomRelation = parts[0].split("\\s+")[2].trim();
        String valuePart = parts[1].trim();
        valuePart = valuePart.substring(1, valuePart.length() - 1);
        this.values = new ArrayList<>();
        for (String val : valuePart.split(",")) {
            values.add(val.trim());
        }
    }


    public void execute() {
        try {
            TableInfo tableInfo = DataBaseInfo.getInstance().getTableInfo(nomRelation);
            if (tableInfo == null) {
                throw new IllegalArgumentException("Table non trouvée : " + nomRelation);
            }


            Record record = new Record(tableInfo);
            for (String value : values) {
                record.addValue(value);
            }

            // inserer record ici?
        } catch (Exception e) {
            System.out.println("Erreur lors de l'exécution de la commande INSERT : " + e.getMessage());
        }
    }
}