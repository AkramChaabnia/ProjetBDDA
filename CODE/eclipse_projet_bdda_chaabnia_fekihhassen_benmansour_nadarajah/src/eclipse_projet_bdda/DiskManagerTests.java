package eclipse_projet_bdda;

import java.io.IOException;
import java.nio.ByteBuffer;

public class DiskManagerTests {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testAllocPage();

	}

	// test allocation de page
	public static void testAllocPage() {
        DiskManager diskManager = DiskManager.getInstance();
        PageId pageId1 = diskManager.AllocPage();
        PageId pageId2 = diskManager.AllocPage();
        
		// on vérifie que les PageId sont différents
        assert pageId1 != null;
        assert pageId2 != null;
        assert pageId1.getPageIdx() != pageId2.getPageIdx();
        
        System.out.println("testAllocPage passé");
    }

	 // test de lecture de page
	 public static void testReadPage() {
        DiskManager diskManager = DiskManager.getInstance();
        PageId pageId = diskManager.AllocPage();
        ByteBuffer buffer = ByteBuffer.allocate(DBParams.SGBDPageSize);
        byte[] data = "coucou".getBytes();
        buffer.put(data);
        buffer.flip();

        try {
            diskManager.WritePage(pageId, buffer);
            buffer.clear();
            diskManager.ReadPage(pageId, buffer);
            buffer.flip();
            byte[] result = new byte[buffer.remaining()];
            buffer.get(result);
            assert new String(result).equals("coucou");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println("testReadPage passé");
    }

	  // test d'écriture de page
	  public static void testWritePage() {
        DiskManager diskManager = DiskManager.getInstance();
        PageId pageId = diskManager.AllocPage();
        ByteBuffer buffer = ByteBuffer.allocate(DBParams.SGBDPageSize);
        byte[] data = "coucou".getBytes();
        buffer.put(data);
        buffer.flip();

        try {
            diskManager.WritePage(pageId, buffer);
            buffer.clear();
            diskManager.ReadPage(pageId, buffer);
            buffer.flip();
            byte[] result = new byte[buffer.remaining()];
            buffer.get(result);
            assert new String(result).equals("coucou");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println("testWritePage passé");
    }
}