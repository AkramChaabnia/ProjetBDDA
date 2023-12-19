package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.io.*;
import java.util.ArrayList;

public class DataBaseInfo {
    private static DataBaseInfo instance;
    private ArrayList<TableInfo> tableInfoList;
    private int compteur;

    private DataBaseInfo() {
        tableInfoList = new ArrayList<>();
        compteur = 0;
    }

    public static DataBaseInfo getInstance() {
        if (instance == null) {
            instance = new DataBaseInfo();
        }
        return instance;
    }

    public void init() {
        readFromFile();
        System.out.println("DatabaseInfo initialisé.");
    }

    public void finish() {
        saveToFile();
        System.out.println("DatabaseInfo fini.");
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("BD\\DBInfo.save"))) {
            oos.writeObject(tableInfoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFromFile() {
        File file = new File(DBParams.DBPath + "\\DBInfo.save");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                tableInfoList = (ArrayList<TableInfo>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void addTableInfo(TableInfo tableInfo) {
        tableInfoList.add(tableInfo);
        compteur++;
    }

    public TableInfo getTableInfo(String tableName) {
        for (TableInfo tableInfo : tableInfoList) {
            if (tableInfo.getNom_relation().equals(tableName)) {
                return tableInfo;
            }
        }
        return null;
    }

    public ArrayList<TableInfo> getTableInfoList() {
        return tableInfoList;
    }

    public int getCompteur() {
        return compteur;
    }

    public boolean tableExists(String tableName) {
        for (TableInfo table : tableInfoList) {
            if (table.getNom_relation().equals(tableName)) {
                return true;
            }
        }
        return false;
    }

    public void reset() {
        tableInfoList.clear();
        compteur = 0;
        System.out.println("DatabaseInfo reset.");
    }

}