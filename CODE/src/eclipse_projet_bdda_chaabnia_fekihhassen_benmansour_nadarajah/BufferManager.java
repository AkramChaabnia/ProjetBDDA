package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class BufferManager {
	private static BufferManager instance;
	public Map<PageId, Frame> bufferPool;

	private BufferManager() {
		bufferPool = new HashMap<>();
	}

	public static BufferManager getInstance() {
		if (instance == null) {
			instance = new BufferManager();
		}
		return instance;
	}

	public void init() {
		bufferPool.clear();
	}

	public ByteBuffer getPage(PageId pageId, ByteBuffer buff) {
		try {
			if (bufferPool.containsKey(pageId)) {
				Frame frame = bufferPool.get(pageId);
				frame.incrementerPinCount();
				return frame.getBuffer();
			} else {
				ByteBuffer pageData = DiskManager.getInstance().readPage(pageId);
				if (pageData == null) {
					System.err.println("Error: Null pageData encountered for getPage: " + pageId);
					// Potential causes: Disk read issues, missing page, etc.

					return null;
				} else {
					System.out.println("PageData is not null in getPage for getPage: " + pageId);
				}
				Frame newFrame = new Frame(pageData);
				bufferPool.put(pageId, newFrame);
				return newFrame.getBuffer();
			}
		} catch (

		Exception e) {
			System.err.println("Error in getPage for PageId: " + pageId + ". Reason: " + e.getMessage());
			// Log or handle the exception appropriately
			return null; // Or handle with appropriate error handling mechanism
		}

	}

	public ByteBuffer getPage(PageId pageId) {
		try {

			if (bufferPool.containsKey(pageId)) {
				Frame frame = bufferPool.get(pageId);
				frame.incrementerPinCount();
				return frame.getBuffer();
			} else {
				ByteBuffer pageData = DiskManager.getInstance().readPage(pageId);
				if (pageData == null) {
					System.err.println("Error: Null pageData encountered for getPage: " + pageId);
				} else {
					System.out.println("PageData is not null in getPage for getPage: " + pageId);
				}
				Frame newFrame = new Frame(pageData);
				bufferPool.put(pageId, newFrame);
				return newFrame.getBuffer();
			}
		} catch (

		Exception e) {
			System.err.println("Error in getPage for PageId: " + pageId + ". Reason: " + e.getMessage());
			// Log or handle the exception appropriately
			return null; // Or handle with appropriate error handling mechanism
		}

	}

	public void freePage(PageId pageId, int valDirty) {
		if (bufferPool.containsKey(pageId)) {
			Frame frame = bufferPool.get(pageId);
			frame.decrementerPinCount();
			;

			if (valDirty == 1) {
				frame.setDirty(true);
			}

			if (frame.getPinCount() == 0) {
				// Si le pinCount est egale à 0, supprime la page de la mémoire tampon
				bufferPool.remove(pageId);

				// Desallouer la page dans le DiskManager
				DiskManager.getInstance().deallocatePage(pageId);
			}
		}
	}

	public void flushAll() {
		for (Map.Entry<PageId, Frame> entry : bufferPool.entrySet()) {
			Frame frame = entry.getValue();
			if (frame.getDirty()) {
				// Si dirty, réécrire la page dans DiskManager
				DiskManager.getInstance().writePage(entry.getKey(), frame.getBuffer());
				frame.setDirty(false);
			}
		}

		// Vider la memoire tampon
		bufferPool.clear();
	}

	public void reset() {
		for (Frame frame : bufferPool.values()) {
			frame.setPinCount(0);
			frame.setDirty(false);
			frame.setBuffer(ByteBuffer.allocate(DBParams.SGBDPageSize));
		}
		bufferPool.clear();
	}

}