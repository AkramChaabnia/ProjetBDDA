package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.NoSuchElementException;

public class RecordIterator {
  private final TableInfo tabInfo;
  private final PageId pageId;
  private ByteBuffer dataPageBuffer;
  private int currentOffset;

  public RecordIterator(TableInfo tabInfo, PageId pageId) {
    this.tabInfo = tabInfo;
    this.pageId = pageId;
    this.dataPageBuffer = null;
    this.currentOffset = 0;
  }

  public Record getNextRecord() throws IOException, PageNotFoundException {
    if (dataPageBuffer == null) {
      BufferManager bm = BufferManager.getInstance();
      dataPageBuffer = bm.getPage(pageId);
    }

    int pageSize = DBParams.SGBDPageSize;
    int recordCount = dataPageBuffer.getInt(pageSize - 8);

    if (currentOffset < recordCount) {
      int slotStart = dataPageBuffer.getInt(pageSize - (8 + (currentOffset + 1) * 8));
      int slotSize = dataPageBuffer.getInt(pageSize - (8 + (currentOffset + 1) * 8) + 4);

      ByteBuffer recordBuffer = ByteBuffer.allocate(slotSize);
      dataPageBuffer.position(slotStart);
      dataPageBuffer.limit(slotStart + slotSize);
      recordBuffer.put(dataPageBuffer);
      recordBuffer.rewind();

      Record record = new Record(tabInfo);
      record.readFromBuffer(recordBuffer, 0);

      currentOffset++;
      return record;
    } else {
      close();
      return null;
    }
  }

  public void close() throws PageNotFoundException {
    if (dataPageBuffer != null) {
      BufferManager bm = BufferManager.getInstance();
      bm.freePage(pageId, 0);
      dataPageBuffer = null;
    }
  }

  public void reset() {
    currentOffset = 0;
  }
}