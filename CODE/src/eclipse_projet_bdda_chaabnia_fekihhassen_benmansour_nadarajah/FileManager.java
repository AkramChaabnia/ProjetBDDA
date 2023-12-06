package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.io.IOException;
import java.nio.ByteBuffer;

public class FileManager {

    // instance unique
    private static FileManager instance;

    
    private FileManager() {
        
    }

   
    public static FileManager getInstance() {
        if (instance == null) {
        
            instance = new FileManager();
        }
        return instance;
    }

    public PageId createNewHeaderPage() throws IOException, PageNotFoundException {
        // Étape 1 : Allouer une nouvelle page via le DiskManager
        DiskManager dm = DiskManager.getInstance();
        PageId newHeaderPageId = dm.allocPage();
    
        // Étape 2 : Récupérer le ByteBuffer correspondant à la nouvelle page via le BufferManager
        BufferManager bm = BufferManager.getInstance();
        ByteBuffer headerPageBuffer = bm.getPage(newHeaderPageId);
    
        // Étape 3 : Écrire les deux PageIds factices dans le ByteBuffer
        // Vous devez écrire deux PageIds factices dans le ByteBuffer ici
    
        // Étape 4 : Libérer la page auprès du BufferManager avec le flag dirty
        bm.freePage(newHeaderPageId, true);
    
        // Étape 5 : Retourner le PageId de la nouvelle Header Page
        return newHeaderPageId;
    }
    
    
}