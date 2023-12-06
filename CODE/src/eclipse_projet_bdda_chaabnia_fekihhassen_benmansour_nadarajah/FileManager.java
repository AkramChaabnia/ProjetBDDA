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
    
        DiskManager dm = DiskManager.getInstance();
        PageId newHeaderPageId = dm.allocPage();
    

        BufferManager bm = BufferManager.getInstance();
        ByteBuffer headerPageBuffer = bm.getPage(newHeaderPageId);

        headerPageBuffer.putInt(0); 
        headerPageBuffer.putInt(0); 


        bm.freePage(newHeaderPageId, true);
    
        return newHeaderPageId;
    }
    
    
    
}