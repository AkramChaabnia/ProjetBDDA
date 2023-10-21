package eclipse_projet_bdda;

import java.nio.ByteBuffer;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
import java.util.Stack;
import java.io.*;

/**
 * Classe DiskManager qui gère l'allocation et la désallocation de pages sur le disque.
 * Elle propose une API permettant d'allouer, lire, écrire et désallouer des pages.
 */
public final class DiskManager {

	// instance unique du DiskManager
	private static DiskManager instance = new DiskManager();
	// pile contenant les identifiants des pages dispo
	Stack<PageId> pagesDispo = new Stack<PageId>();
	// compteur du nombre total de pages allouées
	private static int count = 0;

	/**
	 * Constructeur privé initialisant les fichiers du DM
	 */
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

	/**
	 * Obtenir l'instance unique du DM
	 * @return l'instance unique du DM
	 */
	public static DiskManager getInstance() {
		return instance;
	}

	 /**
     * Alloue une nouvelle page
     *
     * @return l'identifiant de la nouvelle page.
     */
	public PageId AllocPage() {
		PageId PageId = null;
		if (!pagesDispo.isEmpty()) {
			PageId = pagesDispo.pop();
		} else {
			//trouver le plus petit fichier
			//note les pages qui nont pas encore une page rajouter sont prioritaire
	        
			long taille = Long.MAX_VALUE;
			File petit_fic = null;
			int fileidx = -1;
			
			for (int i = 0; i < DBParams.DMFileCount; i++) {
				String fileName = DBParams.DBPath + "f" + i + ".data";
				File fic = new File(fileName);
								
				if(fic.length() < taille) {
					petit_fic = fic;
					taille = fic.length();
					fileidx = i;
				}
			}
			
			//rajouter une page de taille 4095 a la fin du fichier 

            FileOutputStream fileOutputStream;
			try {
				fileOutputStream = new FileOutputStream(petit_fic);
				// OU DBparams.SGBDPagesize
	            byte[] nouvelle_page = new byte[DBParams.SGBDPageSize];
	            fileOutputStream.write(nouvelle_page);
	            fileOutputStream.close();
	            
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            //creez une pageId pour la nouvelle page
            PageId = new PageId(fileidx, count);
			

			
		}
		count++;
		return PageId;
	}

	/**
	 * Lit le contenu d'une page dans un buffer
	 * @param PageId l'id de la page à lire
	 * @param buff le buffer sur lequel le contenu sera lu
	 * @throws IOException en cas d'erreur I/O
	 */
	void ReadPage(PageId PageId, ByteBuffer buff) throws IOException {
		try (RandomAccessFile file = new RandomAccessFile(DBParams.DBPath, "r")) {
			file.seek(PageId.getPageIdx() * DBParams.SGBDPageSize);
			file.read(buff.array());
			file.close();
		}
	}
	
    /**
	 * Ecrit le contenu d'un buffer dans une page
	 * @param PageId l'id de la page
	 * @param buff le buffer sur lequel le contenu sera lu
	 * @throws IOException en cas d'erreur I/O
	 */
	void WritePage(PageId PageId, ByteBuffer buff) throws IOException {
		try (RandomAccessFile file = new RandomAccessFile(DBParams.DBPath, "rw")) {
			file.seek(PageId.getPageIdx() * DBParams.SGBDPageSize);
			file.write(buff.array());
			file.close();
		}
	}

	/**
	 * Désalloue une page et l'ajoute à la liste des pages dispo
	 * @param PageId
	 */
	void DeallocPage(PageId PageId) {
		pagesDispo.push(PageId);
		count--;
	}

	/**
	 * Obtient le nombre actuel des pages allouées
	 * @return le nb actuel des pages allouées
	 */
	int GetCurrentCountAllocPages() {
		return count;
	}

}