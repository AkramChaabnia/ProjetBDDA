package eclipse_projet_bdda;

import java.nio.ByteBuffer;

public class BufferManager {
    private static BufferManager instance = new BufferManager();

    // informations associées à chaque frame
    private ByteBuffer[] buffers;
    private PageId[] pageIds;
    private int[] pinCounts;
    private boolean[] flagDirty;
    private int[] accessCpt;

    public static BufferManager getInstance() {
        return instance;
    }

   


}
