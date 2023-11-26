package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

public class DiskManager {
	private static final int pageSize = 4096;
	// pourquoi 4096 au lieu de DBParams.SGBDPagesize
	private static DiskManager instance = new DiskManager();

	private int[] tailleFichier;
	private ArrayList<PageId> pagesDesalloue;
	private HashMap<PageId, ByteBuffer> contenuDePage;

	private DiskManager() {
		tailleFichier = new int[10];
		// pourquoi 10 au lieu de DBParams.DMFileCount
		pagesDesalloue = new ArrayList<>();
		contenuDePage = new HashMap<>();

		try {
			for (int i = 0; i < DBParams.DMFileCount; i++) {
				String fileName = DBParams.DBPath + "f" + i + ".data";
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

	public PageId allocPage() {

		PageId pageId = null;

		if (!pagesDesalloue.isEmpty()) {
			pageId = pagesDesalloue.iterator().next();
			pagesDesalloue.remove(pageId);
		} else {
			int numFic = getMinFic();
			int numPage = tailleFichier[numFic] / pageSize;
			tailleFichier[numFic] = tailleFichier[numFic] + pageSize;
			pageId = new PageId(numFic, numPage);
		}

		ByteBuffer page = ByteBuffer.allocate(pageSize);
		contenuDePage.put(pageId, page);

		return pageId;
	}

	public void readPage(PageId pageId, ByteBuffer buff) {
		ByteBuffer page = contenuDePage.get(pageId);
		int copyLength = Math.min(buff.remaining(), pageSize);
		page.limit(copyLength);
		buff.put(page);
		page.rewind();
	}

	public void writePage(PageId pageId, ByteBuffer buff) {
		ByteBuffer page = contenuDePage.get(pageId);
		int copyLength = Math.min(buff.remaining(), pageSize);
		buff.limit(copyLength);
		page.put(buff);
		page.rewind();
	}

	public void deallocPage(PageId pageId) {
		pagesDesalloue.add(pageId);
		contenuDePage.remove(pageId);
	}

	public int getCurrentCountAllocPages() {
		return contenuDePage.size();
	}

	private int getMinFic() {
		int minTailleFic = Integer.MAX_VALUE;
		int numFic = 0;

		for (int i = 0; i < tailleFichier.length; i++) {
			if (tailleFichier[i] < minTailleFic) {
				minTailleFic = tailleFichier[i];
				numFic = i;
			}
		}

		return numFic;
	}
}
