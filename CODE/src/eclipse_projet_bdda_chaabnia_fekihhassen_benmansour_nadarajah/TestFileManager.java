package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah.PageNotFoundException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

public class TestFileManager {

    public static void main(String[] args) {
        testFileManager();
    }

    public static void testFileManager() {
        try {
            FileManager fileManager = FileManager.getInstance();

            // Test 1: créer une nouvelle page d'en-tête
            System.out.println("Test 1: Créer une nouvelle page d'en-tête");
            PageId headerPageId = fileManager.createNewHeaderPage();
            System.out.println("Page d'en-tête créée avec succès : " + headerPageId);

            // Test 2: Ajouter une page de données
            System.out.println("Test 2: Ajouter une page de données");
            TableInfo tableInfo = new TableInfo(headerPageId);
            PageId dataPageId = fileManager.addDataPage(tableInfo);
            System.out.println("Page de données ajoutée avec succès : " + dataPageId);

        } catch (IOException | PageNotFoundException e) {
            e.printStackTrace();
        }
    }
}
