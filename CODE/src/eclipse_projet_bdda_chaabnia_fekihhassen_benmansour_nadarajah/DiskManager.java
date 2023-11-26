package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class DiskManager {
	private static final int pageSize = 4096;
	private static DiskManager instance = new DiskManager();
	
	private int[] tailleFichier;
	private ArrayList<PageId> pagesDesalloue;
	private HashMap<PageId, byte[]> contenuDePage;
	
	private DiskManager() {
		tailleFichier = new int[10];
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
		
		if(!pagesDesalloue.isEmpty()) {
			pageId = pagesDesalloue.iterator().next();
			pagesDesalloue.remove(pageId);
		}
		else {
			int numFic = getMinFic();
			int numPage = tailleFichier[numFic]/pageSize;
			tailleFichier[numFic] = tailleFichier[numFic] + pageSize;
			pageId = new PageId(numFic,numPage);
		}
		
		byte[] page = new byte[pageSize];
		contenuDePage.put(pageId, page);
		
		return pageId;
		
		/*
	    PageId pageId = null;

	    if (!pagesDesalloue.isEmpty()) {
	        pageId = pagesDesalloue.remove(0); // Prendre la première page désallouée
	    } else {
	        int numFic = getMinFic();
	        int numPage = tailleFichier[numFic] / pageSize;
	        tailleFichier[numFic] = tailleFichier[numFic] + pageSize;
	        pageId = new PageId(numFic, numPage);
	    }

	    byte[] page = new byte[pageSize];
	    contenuDePage.put(pageId, page);

	    return pageId;
	    */
	}
	
	public void readPage(PageId pageId, byte[] buff) {
		byte[] page = contenuDePage.get(pageId);
		//System.arraycopy(page, 0, buff, 0, pageSize);
	    int copyLength = Math.min(buff.length, pageSize);
	    System.arraycopy(page, 0, buff, 0, copyLength);
	}

	public void writePage(PageId pageId, byte[] buff) {
		byte[] page = contenuDePage.get(pageId);
		//System.arraycopy(buff, 0, page, 0, pageSize);
	    int copyLength = Math.min(buff.length, pageSize);
	    System.arraycopy(buff, 0, page, 0, copyLength);
	}
	
	public void deallocPage(PageId pageId) {
		pagesDesalloue.add(pageId);
		contenuDePage.remove(pageId);
		//contenuDePage.put(pageId, new byte[pageSize]);
	}
	
	public int getCurrentCountAllocPages() {
		return contenuDePage.size();
	}
	
	private int getMinFic() {
		int minTailleFic = Integer.MAX_VALUE;
		int numFic = 0;
		
		for(int i=0;i<tailleFichier.length;i++) {
			if(tailleFichier[i]<minTailleFic) {
				minTailleFic = tailleFichier[i];
				numFic = i;
			}
		}
		
		return numFic;
	}
	
	

}
