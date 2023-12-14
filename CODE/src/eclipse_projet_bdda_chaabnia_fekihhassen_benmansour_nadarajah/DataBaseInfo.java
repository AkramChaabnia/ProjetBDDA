package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DataBaseInfo {
    List<TableInfo> tableInfoList = new ArrayList<>();

    public void init() {
        readFromFile();
        System.out.println("DatabaseInfo initialisé.");
    }

    public void finish() {
        saveToFile();
        System.out.println("DatabaseInfo fini.");
    }

    public void addTableInfo(TableInfo tableInfo) {
        tableInfoList.add(tableInfo);
    }

    public TableInfo getTableInfo(String nomRelation) {
        for (TableInfo tableInfo : tableInfoList) {
            if (tableInfo.GetNomRelation().equals(nomRelation)) {
                return tableInfo;
            }
        }
        return null;
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("DBInfo.save"))) {
            oos.writeObject(tableInfoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("DBInfo.save"))) {
            File file = new File("DBInfo.save");
            if (file.exists()) {
                tableInfoList = (List<TableInfo>) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
