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
            } else {
                System.out.println("Table trouvée : " + nomRelation);
            }

            if (tableInfo.getNb_colonnes() != values.size()) {
                throw new IllegalArgumentException(
                        "Le nombre de valeurs fournies ne correspond pas au nombre de colonnes dans la table");
            } else {
                System.out.println("Le nombre de valeurs fournies correspond au nombre de colonnes dans la table "
                        + tableInfo.getNb_colonnes() + " " + values.size());
            }

            Record record = new Record(tableInfo);
            for (int i = 0; i < values.size(); i++) {
                String value = values.get(i);
                String colType = tableInfo.getColInfoList().get(i).getType();
                switch (colType.toUpperCase()) {
                    case "INT":
                        record.addValue(Integer.parseInt(value));
                        System.out.println("Added INT value: " + value);
                        break;
                    case "FLOAT":
                        record.addValue(Float.parseFloat(value));
                        System.out.println("Added FLOAT value: " + value);
                        break;
                    case "STRING":
                        record.addValue(value);
                        System.out.println("Added STRING value: " + value);
                        break;
                    default:
                        if (colType.toUpperCase().startsWith("VARSTRING")) {
                            record.addValue(value);
                            System.out.println("Added VARSTRING value: " + value);
                        } else {
                            throw new IllegalArgumentException("Type de colonne non supporté : " + colType);
                        }
                }
            }

            FileManager.getInstance().InsertRecordIntoTable(record);
        } catch (Exception e) {
            System.out.println("An exception occurred while executing the INSERT command.");
            e.printStackTrace();
        }
    }
}