package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;


import java.io.IOException;
import java.nio.ByteBuffer;

public class TestBufferManager {

    public static void main(String[] args) {
        testGetPage();
        
    }

    public static void testGetPage() {
        BufferManager bufferManager = BufferManager.getInstance();
        PageId pageId = new PageId(0, 1); 
        try {
            ByteBuffer buffer = bufferManager.getPage(pageId);
            assert buffer != null : "le buffer ne devrait pas être null";
        
            ByteBuffer sameBuffer = bufferManager.getPage(pageId);
            assert sameBuffer == buffer : "devrait retourner le même buffer pour la même page";
        
            System.out.println("testGetPage passé");
        } catch (IOException e) {
            e.printStackTrace(); 
        }
        
    }

}