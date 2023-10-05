package eclipse_projet_bdda;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Stack;
import java.io.*;

public final class DiskManager {

	private static DiskManager instance = new DiskManager();
	Stack<PageId> pagesDispo = new Stack<PageId>();
	private static int count = 0;

	private DiskManager() {
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

	public PageId AllocPage() {
		PageId PageId = null;
		if (!pagesDispo.isEmpty()) {
			PageId = pagesDispo.pop();
		} else {
			//trouver le plus petit fichier
			//rajouter une page de taille 4095 a la fin du fichier 
			//note les pages qui nont pas encore une page rajouter sont prioritaire
			}
		count++;
		return PageId;
	}

	void ReadPage(PageId PageId, ByteBuffer buff) throws IOException {
		try (RandomAccessFile file = new RandomAccessFile(DBParams.DBPath, "r")) {
			file.seek(PageId.getPageIdx() * DBParams.SGBDPageSize);
			file.read(buff.array());
			file.close();
		}
	}
	

	void WritePage(PageId PageId, ByteBuffer buff) throws IOException {
		try (RandomAccessFile file = new RandomAccessFile(DBParams.DBPath, "rw")) {
			file.seek(PageId.getPageIdx() * DBParams.SGBDPageSize);
			file.write(buff.array());
			file.close();
		}
	}

	void DeallocPage(PageId PageId) {
		pagesDispo.push(PageId);
		count--;
	}

	int GetCurrentCountAllocPages() {
		return count;
	}

}
