package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

public class TestDiskManager {

    public static void main(String[] args) {
    	testEcritureLecturePage();
        testAllocDeallocPage();
        // Ajoutez d'autres tests au besoin
    }

    private static void testEcritureLecturePage() {
        System.out.println("TestEcritureLecturePage:");

        DBParams.SGBDPageSize = 5;

        DiskManager diskManager = DiskManager.getInstance();

        // Allocation d'une page
        PageId pageId = diskManager.allocPage();
        System.out.println("Page allouée : " + pageId);

        // Données à écrire dans la page
        byte[] dataToWrite = "Hello".getBytes();

        // Écriture des données dans la page
        diskManager.writePage(pageId, dataToWrite);

        // Lecture des données depuis la page
        byte[] dataRead = new byte[DBParams.SGBDPageSize];
        diskManager.readPage(pageId, dataRead);

        // Affichage des résultats
        System.out.println("Données écrites : " + new String(dataToWrite));
        System.out.println("Données lues    : " + new String(dataRead));

        // Vérification
        if (new String(dataToWrite).equals(new String(dataRead))) {
            System.out.println("Test réussi.");
        } else {
            System.out.println("Test échoué.");
        }
        
        diskManager.deallocPage(pageId);

        // Remise de la taille de page par défaut (4096 octets)
        DBParams.SGBDPageSize = 4096;
    }

    private static void testAllocDeallocPage() {
        System.out.println("\nTestAllocDeallocPage:");

        DBParams.SGBDPageSize = 4;

        DiskManager diskManager = DiskManager.getInstance();

        // Allocation de deux pages
        PageId pageId1 = diskManager.allocPage();
        PageId pageId2 = diskManager.allocPage();
        System.out.println("Pages allouées : " + pageId1 + ", " + pageId2);

        // Désallocation de la première page
        diskManager.deallocPage(pageId1);

        // Allocation d'une nouvelle page
        PageId pageId3 = diskManager.allocPage();
        System.out.println("Nouvelle page allouée : " + pageId3);

        // Vérification du nombre de pages allouées
        int countAllocPages = diskManager.getCurrentCountAllocPages();
        System.out.println("Nombre de pages allouées : " + countAllocPages);

        // Vérification
        if (countAllocPages == 2) {
            System.out.println("Test réussi.");
        } else {
            System.out.println("Test échoué.");
        }

        // Remise de la taille de page par défaut (4096 octets)
        DBParams.SGBDPageSize = 4096;
    }
}
