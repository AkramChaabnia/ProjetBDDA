package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

  // instance unique
  private static FileManager instance;

  private FileManager() {

  }

  public static FileManager getInstance() {
    if (instance == null) {

      instance = new FileManager();
    }
    return instance;
  }

  public PageId createNewHeaderPage() throws IOException, PageNotFoundException {
    DiskManager dm = DiskManager.getInstance();
    PageId newHeaderPageId = dm.allocatePage();

    BufferManager bm = BufferManager.getInstance();
    ByteBuffer headerPageBuffer = bm.getPage(newHeaderPageId);

    headerPageBuffer.putInt(-1);
    headerPageBuffer.putInt(-1);

    bm.freePage(newHeaderPageId, 1);

    return newHeaderPageId;
  }

  public PageId addDataPage(TableInfo tabInfo) throws IOException, PageNotFoundException {
    DiskManager dm = DiskManager.getInstance();
    PageId newDataPageId = dm.allocatePage();

    BufferManager bm = BufferManager.getInstance();
    ByteBuffer newDataPageBuffer = bm.getPage(newDataPageId);

    // initialise la nouvelle page de donnees
    for (int i = 0; i < DBParams.SGBDPageSize / 8; i++) {
      newDataPageBuffer.putInt(4 + i * 8, 0); // initialise les slots
      newDataPageBuffer.putInt(8 + i * 8, 0); // initialise les tailles
    }
    bm.freePage(newDataPageId, 1);

    PageId headerPageId = tabInfo.getHeaderPageId();
    ByteBuffer headerPageBuffer = bm.getPage(headerPageId);

    int firstFreePageFileIdx = headerPageBuffer.getInt(0);
    int firstFreePagePageIdx = headerPageBuffer.getInt(4);

    if (firstFreePageFileIdx == -1 && firstFreePagePageIdx == 0) {
      headerPageBuffer.putInt(0, newDataPageId.getFileIdx());
      headerPageBuffer.putInt(4, newDataPageId.getPageIdx());
    } else {
      PageId lastPageId = new PageId(firstFreePageFileIdx, firstFreePagePageIdx);
      ByteBuffer lastPageBuffer = bm.getPage(lastPageId);
      while (lastPageBuffer.getInt(0) != -1) {
        lastPageId = new PageId(lastPageBuffer.getInt(0), lastPageBuffer.getInt(4));
        lastPageBuffer = bm.getPage(lastPageId);
      }

      lastPageBuffer.putInt(0, newDataPageId.getFileIdx());
      lastPageBuffer.putInt(4, newDataPageId.getPageIdx());
      bm.freePage(lastPageId, 1);
    }

    bm.freePage(headerPageId, 1);
    return newDataPageId;
  }

  public PageId getFreeDataPageId(TableInfo tabInfo, int sizeRecord) throws IOException, PageNotFoundException {
    BufferManager bm = BufferManager.getInstance();
    PageId headerPageId = tabInfo.getHeaderPageId();
    ByteBuffer headerPageBuffer = bm.getPage(headerPageId);

    int numDataPages = headerPageBuffer.getInt(0);

    for (int i = 0; i < numDataPages; i++) {
      int dataPageFileIdx = headerPageBuffer.getInt(4 + i * 12);
      int dataPagePageIdx = headerPageBuffer.getInt(8 + i * 12);
      int freeSpace = headerPageBuffer.getInt(12 + i * 12);

      PageId dataPageId = new PageId(dataPageFileIdx, dataPagePageIdx);
      ByteBuffer dataPageBuffer = bm.getPage(dataPageId);

      int slotCount = (DBParams.SGBDPageSize - 8) / 8;
      boolean pageHasSpace = false;

      for (int j = 0; j < slotCount; j++) {
        int slotStart = dataPageBuffer.getInt(4 + j * 8);
        int slotSize = dataPageBuffer.getInt(8 + j * 8);

        if (slotStart == 0 && slotSize == 0) {
          if (sizeRecord <= freeSpace) {
            pageHasSpace = true;
            break;
          }
        }
      }

      if (pageHasSpace) {
        bm.freePage(headerPageId, 0);
        bm.freePage(dataPageId, 0);
        return dataPageId;
      }

      bm.freePage(dataPageId, 0);
    }

    bm.freePage(headerPageId, 0);
    return null;
  }

  public RecordId writeRecordToDataPage(Record record, PageId pageId) throws IOException, PageNotFoundException {

    BufferManager bm = BufferManager.getInstance();
    ByteBuffer dataPageBuffer = bm.getPage(pageId);

    ByteBuffer recordBuffer = ByteBuffer.allocate(record.getSize());
    record.writeToBuffer(recordBuffer, 0);
    recordBuffer.flip();

    int slotCount = (DBParams.SGBDPageSize - 8) / 8;
    int freeSlotIndex = -1;

    for (int i = 0; i < slotCount; i++) {
      int slotStart = dataPageBuffer.getInt(4 + i * 8);
      int slotSize = dataPageBuffer.getInt(8 + i * 8);

      if (slotStart == 0 && slotSize == 0) {
        freeSlotIndex = i;
        break;
      }
    }

    if (freeSlotIndex == -1) {
      bm.freePage(pageId, 0);
      throw new IOException("Aucun emplacement libre sur la page de données.");
    }

    int recordStart = freeSlotIndex * 8 + 8;
    dataPageBuffer.putInt(4 + freeSlotIndex * 8, recordStart);
    dataPageBuffer.putInt(8 + freeSlotIndex * 8, recordBuffer.remaining());

    dataPageBuffer.position(recordStart);
    dataPageBuffer.put(recordBuffer);

    bm.freePage(pageId, 1);

    RecordId recordId = new RecordId(pageId, freeSlotIndex);
    return recordId;
  }

  public List<Record> getRecordsInDataPage(TableInfo tabInfo, PageId pageId) throws IOException, PageNotFoundException {
    List<Record> records = new ArrayList<>();
    BufferManager bm = BufferManager.getInstance();
    byte[] dataPageBuffer = bm.getPage(pageId).array(); // Convert ByteBuffer to byte array

    try {
      int slotCount = (DBParams.SGBDPageSize - 8) / 8;

      for (int i = 0; i < slotCount; i++) {
        int slotStart = ByteBuffer.wrap(dataPageBuffer, 4 + i * 8, 4).getInt();
        int slotSize = ByteBuffer.wrap(dataPageBuffer, 8 + i * 8, 4).getInt();

        if (slotStart > 0 && slotSize > 0) {
          byte[] recordBuffer = new byte[slotSize];
          System.arraycopy(dataPageBuffer, slotStart, recordBuffer, 0, slotSize);

          Record record = new Record(tabInfo);
          record.readFromBuffer(recordBuffer, 0);
          records.add(record);
        }
      }

      return records;
    } finally {
      bm.freePage(pageId, 0);
    }
  }

  public List<PageId> getDataPages(TableInfo tabInfo) throws IOException, PageNotFoundException {
    List<PageId> dataPageIds = new ArrayList<>();
    BufferManager bm = BufferManager.getInstance();
    PageId headerPageId = tabInfo.getHeaderPageId();
    ByteBuffer headerPageBuffer = bm.getPage(headerPageId);

    try {
      int numDataPages = headerPageBuffer.getInt(0);
      for (int i = 0; i < numDataPages; i++) {
        int dataPageFileIdx = headerPageBuffer.getInt(4 + i * 12);
        int dataPagePageIdx = headerPageBuffer.getInt(8 + i * 12);
        PageId dataPageId = new PageId(dataPageFileIdx, dataPagePageIdx);
        dataPageIds.add(dataPageId);
      }

      return dataPageIds;
    } finally {
      bm.freePage(headerPageId, 0);
    }
  }

  public RecordId InsertRecordIntoTable(Record record) throws IOException, PageNotFoundException {
    BufferManager bm = BufferManager.getInstance();
    TableInfo tabInfo = record.getTabInfo();
    PageId dataPageId = getFreeDataPageId(tabInfo, record.getSize());

    if (dataPageId == null) {
      dataPageId = addDataPage(tabInfo);
    }

    ByteBuffer dataPageBuffer = bm.getPage(dataPageId);
    int offset = dataPageBuffer.getInt(DBParams.SGBDPageSize - 4);

    int writtenSize = record.writeToBuffer(dataPageBuffer, offset);
    dataPageBuffer.putInt(DBParams.SGBDPageSize - 4, offset + writtenSize);

    int recordCount = dataPageBuffer.getInt(DBParams.SGBDPageSize - 8);
    dataPageBuffer.putInt(DBParams.SGBDPageSize - 8, recordCount + 1);

    dataPageBuffer.putInt(DBParams.SGBDPageSize - (8 + (recordCount + 1) * 8), offset);
    dataPageBuffer.putInt(DBParams.SGBDPageSize - (8 + (recordCount + 1) * 8) + 4, writtenSize);

    bm.freePage(dataPageId, 1);

    ByteBuffer headerPageBuffer = bm.getPage(tabInfo.getHeaderPageId());
    int slotCount = (DBParams.SGBDPageSize - 8) / 8;

    for (int i = 0; i < slotCount; i++) {
      int slotStart = headerPageBuffer.getInt(4 + i * 8);
      int slotSize = headerPageBuffer.getInt(8 + i * 8);

      if (slotStart == 0 && slotSize == 0) {
        headerPageBuffer.putInt(4 + i * 8, dataPageId.getPageIdx());
        headerPageBuffer.putInt(8 + i * 8, record.getSize());
        break;
      }
    }

    bm.freePage(tabInfo.getHeaderPageId(), 1);

    RecordId rId = new RecordId(dataPageId, recordCount + 1);
    return rId;
  }

  public List<Record> GetAllRecords(TableInfo tabInfo) throws IOException, PageNotFoundException {
    List<Record> records = new ArrayList<>();
    List<PageId> dataPageIds = getDataPages(tabInfo);
    BufferManager bm = BufferManager.getInstance();

    for (PageId dataPageId : dataPageIds) {
      List<Record> pageRecords = getRecordsInDataPage(tabInfo, dataPageId);
      records.addAll(pageRecords);
    }

    return records;
  }

}