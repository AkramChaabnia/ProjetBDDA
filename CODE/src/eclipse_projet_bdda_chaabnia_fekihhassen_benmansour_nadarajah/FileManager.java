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

        headerPageBuffer.putInt(-1); 
        headerPageBuffer.putInt(-1); 


        bm.freePage(newHeaderPageId, true);
    
        return newHeaderPageId;
    }
    
    public PageId addDataPage(TableInfo tabInfo) throws IOException, PageNotFoundException {
        DiskManager dm = DiskManager.getInstance();
        PageId newDataPageId = dm.allocPage();
    
        BufferManager bm = BufferManager.getInstance();
        ByteBuffer newDataPageBuffer = bm.getPage(newDataPageId);
    
        // initialise la nouvelle page de donnees
        for (int i = 0; i < DBParams.SGBDPageSize / 8; i++) {
            newDataPageBuffer.putInt(4 + i * 8, 0); // initialise les slots
            newDataPageBuffer.putInt(8 + i * 8, 0); // initialise les tailles
        }
        bm.freePage(newDataPageId, true);
    
        PageId headerPageId = tabInfo.getHeaderPageId();
        ByteBuffer headerPageBuffer = bm.getPage(headerPageId);
    
        int firstFreePageFileIdx = headerPageBuffer.getInt(0);
        int firstFreePagePageIdx = headerPageBuffer.getInt(4);
    
        if (firstFreePageFileIdx == -1 && firstFreePagePageIdx == 0) {
            headerPageBuffer.putInt(0, newDataPageId.getFileIdx());
            headerPageBuffer.putInt(4, newDataPageId.getPageIdx());
        } else {
            PageId lastPageId = new PageId(firstFreePageFileIdx, firstFreePagePageIdx);
            ByteBuffer lastPageBuffer = bm.getPage(lastPageId);
            while (lastPageBuffer.getInt(0) != -1) {
                lastPageId = new PageId(lastPageBuffer.getInt(0), lastPageBuffer.getInt(4));
                lastPageBuffer = bm.getPage(lastPageId);
            }
    
            lastPageBuffer.putInt(0, newDataPageId.getFileIdx());
            lastPageBuffer.putInt(4, newDataPageId.getPageIdx());
            bm.freePage(lastPageId, true);
        }
    
        bm.freePage(headerPageId, true);
        return newDataPageId;
    }
    
    
    
}