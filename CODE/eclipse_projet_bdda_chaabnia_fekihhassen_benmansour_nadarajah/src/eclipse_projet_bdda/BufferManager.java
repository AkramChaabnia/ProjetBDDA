package eclipse_projet_bdda;

import java.nio.ByteBuffer;

public class BufferManager {
    private static BufferManager instance = new BufferManager();

    public static BufferManager getInstance() {
        return instance;
    }
    
    public ByteBuffer GetPage(PageId pageId) {
		return null;
    	
    }
    
    public void FreePage(PageId pageId, boolean valDirty) {
//    	int pinCount = getPageIdx();
    	
    }
    
    public void FlushAll() {
    	
    }
    
    
}
