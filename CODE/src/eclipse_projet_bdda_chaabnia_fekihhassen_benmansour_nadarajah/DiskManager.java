package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DiskManager {
	private static final int pageSize = 4096;
	private static DiskManager instance = new DiskManager();
	private int[] fileSize;
	private ArrayList<PageId> deallocatedPages;
	private HashMap<PageId, ByteBuffer> pageContents;

	private DiskManager() {
		fileSize = new int[10];
		deallocatedPages = new ArrayList<>();
		pageContents = new HashMap<>();

		try {
			for (int i = 0; i < DBParams.DMFileCount; i++) {
				String fileName = DBParams.DBPath + "f" + i + ".data";
				// Creation fichier
				FileOutputStream fileOutputStream = new FileOutputStream(fileName);
				fileOutputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static DiskManager getInstance() {
		return instance;
	}

	public PageId allocatePage() {
		PageId pageId = null;

		if (!deallocatedPages.isEmpty()) {
			pageId = deallocatedPages.remove(0); // Reusing deallocated page
		} else {
			int fileNumber = getMinFile();
			int pageNumber = fileSize[fileNumber] / pageSize;

			// Check for file space limit
			if (fileSize[fileNumber] + pageSize > fileSize[fileNumber + 1]) {
				fileNumber++;
				pageNumber = 0;
			}

			pageId = new PageId(fileNumber, pageNumber);
			fileSize[fileNumber] += pageSize; // Increment the page count
		}

		ByteBuffer page = ByteBuffer.allocate(pageSize);
		pageContents.put(pageId, page);
		System.out.println("Allocated page with id: " + pageId);
		return pageId;
	}

	public ByteBuffer readPage(PageId pageId) {
		ByteBuffer page = pageContents.get(pageId);
		if (page == null) {
			// If the page is not present, allocate a new ByteBuffer and put it in the map
			page = ByteBuffer.allocate(pageSize);
			pageContents.put(pageId, page);
			System.out.println("Page with id " + pageId + " not found. A new page has been allocated.");
		}

		int copyLength = Math.min(page.remaining(), pageSize);
		ByteBuffer resultBuffer = ByteBuffer.allocate(copyLength);
		// Set position to 0 before putting data
		page.position(0);
		page.limit(copyLength);
		resultBuffer.put(page);
		resultBuffer.flip();
		System.out.println("Read page with id: " + pageId);
		return resultBuffer;
	}

	public void writePage(PageId pageId, ByteBuffer buff) {
		ByteBuffer page = pageContents.get(pageId);
		if (page == null) {
			// Handle case where page is not present
			System.out.println("Page with id " + pageId + " not found");
			return;
		}

		int copyLength = Math.min(buff.remaining(), pageSize);
		// Set position to 0 before putting data
		buff.position(0);
		buff.limit(copyLength);
		page.put(buff);
		page.flip(); // Flip the page buffer
		System.out.println("Wrote to page with id: " + pageId);
	}

	public void deallocatePage(PageId pageId) {
		if (pageContents.containsKey(pageId)) {
			deallocatedPages.add(pageId); // Adding deallocated page
			pageContents.remove(pageId);
			System.out.println("Deallocated page with id: " + pageId);

			// Additional logic to clear remnants or inconsistencies
			// Clearing any remnants or performing necessary cleanup after deallocation
			// This might involve resetting the ByteBuffer or marking the space as free
			// to avoid future data corruption or inconsistencies
			ByteBuffer clearedBuffer = ByteBuffer.allocate(pageSize);
			pageContents.put(pageId, clearedBuffer);
		} else {
			System.err.println("Page with id " + pageId + " not found for deallocation!");
			// Handle the case where the page to deallocate is not found
			// This might involve throwing an exception or logging the issue
		}

	}

	public int getCurrentAllocatedPageCount() {
		return pageContents.size();
	}

	private int getMinFile() {
		int minFileSize = Integer.MAX_VALUE;
		int fileNumber = 0;

		for (int i = 0; i < fileSize.length; i++) {
			if (fileSize[i] < minFileSize) {
				minFileSize = fileSize[i];
				fileNumber = i;
			}
		}

		return fileNumber;
	}

	public void reset() {
		Arrays.fill(fileSize, 0);
		deallocatedPages.clear();
		pageContents.clear();

		try {
			for (int i = 0; i < DBParams.DMFileCount; i++) {
				String fileName = DBParams.DBPath + "f" + i + ".data";
				File file = new File(fileName);

				if (file.exists()) {
					file.delete();
				}

				FileOutputStream fileOutputStream = new FileOutputStream(fileName);
				fileOutputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}