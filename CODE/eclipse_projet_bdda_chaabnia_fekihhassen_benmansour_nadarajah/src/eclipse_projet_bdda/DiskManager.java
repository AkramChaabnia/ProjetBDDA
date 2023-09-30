package eclipse_projet_bdda;

import java.nio.ByteBuffer;
import java.util.Stack;

public final class DiskManager {
	
	private static DiskManager instance = new DiskManager();
	public DiskManager GetInstance() {
		return instance;
		//singletone
	}

	public PageId AllocPage() {
		Stack<PageId> pile = new Stack<PageId>();
		if(DeallocPage()) {
			
		}
		else {
			
		}
		
		
		return null;
	}

	void ReadPage(PageId PageId, ByteBuffer buff) {
	}

	void WritePage(PageId PageId, ByteBuffer buff) {
	}

	void DeallocPage(PageId PageId) {
	}

	int GetCurrentCountAllocPages() {
		return 0;
	}

}
