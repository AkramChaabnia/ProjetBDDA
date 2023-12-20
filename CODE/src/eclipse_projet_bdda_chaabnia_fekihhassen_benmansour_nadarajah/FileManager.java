package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
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

    // Initialize the new data page
    int slotCount = (DBParams.SGBDPageSize - 4) / 8; // Calculate total slots based on available space

    for (int i = 0; i < slotCount; i++) {
      newDataPageBuffer.putInt(4 + i * 8, 0); // Initialize slots
      newDataPageBuffer.putInt(8 + i * 8, 0); // Initialize sizes
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

      int nextPageFileIdx = lastPageBuffer.getInt(0);
      int nextPagePageIdx = lastPageBuffer.getInt(4);

      while (nextPageFileIdx != -1 && nextPagePageIdx != 0) {
        lastPageId = new PageId(nextPageFileIdx, nextPagePageIdx);
        lastPageBuffer = bm.getPage(lastPageId);
        nextPageFileIdx = lastPageBuffer.getInt(0);
        nextPagePageIdx = lastPageBuffer.getInt(4);
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
    byte[] dataPageArray = new byte[dataPageBuffer.capacity()];
    dataPageBuffer.get(dataPageArray);

    int slotCount = (DBParams.SGBDPageSize - 8) / 8;
    int freeSlotIndex = -1;
    int recordStart = -1;

    for (int i = 0; i < slotCount; i++) {
      int slotStart = ByteBuffer.wrap(dataPageArray, 4 + i * 8, 4).getInt();
      int slotSize = ByteBuffer.wrap(dataPageArray, 8 + i * 8, 4).getInt();

      if (slotStart == 0 && slotSize == 0) {
        freeSlotIndex = i;
        recordStart = 8 + i * 8;
        break;
      }
    }

    if (freeSlotIndex == -1) {
      bm.freePage(pageId, 0);
      throw new IOException("Aucun emplacement libre sur la page de données.");
    }

    byte[] recordArray = new byte[record.getSize()];
    record.writeToBuffer(recordArray, 0);
    System.arraycopy(recordArray, 0, dataPageArray, recordStart, recordArray.length);

    ByteBuffer.wrap(dataPageArray).putInt(4 + freeSlotIndex * 8, recordStart);
    ByteBuffer.wrap(dataPageArray).putInt(8 + freeSlotIndex * 8, recordArray.length);

    dataPageBuffer.clear();
    dataPageBuffer.put(dataPageArray);

    bm.freePage(pageId, 1);

    return new RecordId(pageId, freeSlotIndex);
  }

  public List<Record> getRecordsInDataPage(TableInfo tabInfo, PageId pageId) throws IOException, PageNotFoundException {
    if (tabInfo == null) {
      System.out.println("tabInfo is null");
      return Collections.emptyList();
    }
    if (pageId == null) {
      System.out.println("pageId is null");
      return Collections.emptyList();
    }

    List<Record> records = new ArrayList<>();
    BufferManager bm = BufferManager.getInstance();
    byte[] dataPageBuffer = bm.getPage(pageId).array();
    if (dataPageBuffer == null) {
      System.out.println("dataPageBuffer is null");
      return Collections.emptyList();
    }

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
    if (tabInfo == null) {
      System.out.println("tabInfo is null");
      return Collections.emptyList();
    }

    List<PageId> dataPageIds = new ArrayList<>();
    BufferManager bm = BufferManager.getInstance();
    PageId headerPageId = tabInfo.getHeaderPageId();
    ByteBuffer headerPageBuffer = bm.getPage(headerPageId);
    if (headerPageBuffer == null) {
      System.out.println("headerPageBuffer is null");
      return Collections.emptyList();
    }

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

  public RecordId InsertRecordIntoTable(Record record) {
    try {
      BufferManager bm = BufferManager.getInstance();
      TableInfo tabInfo = record.getTabInfo();
      PageId dataPageId = getFreeDataPageId(tabInfo, record.getSize());

      if (dataPageId == null) {
        dataPageId = addDataPage(tabInfo);
        System.out.println("Added new data page: " + dataPageId);
      } else {
        System.out.println("Found free data page: " + dataPageId);
      }

      ByteBuffer dataPageBuffer = bm.getPage(dataPageId);
      byte[] dataPageArray = new byte[dataPageBuffer.capacity()];
      dataPageBuffer.get(dataPageArray);

      int offset = ByteBuffer.wrap(dataPageArray, DBParams.SGBDPageSize - 4, 4).getInt();
      byte[] recordArray = new byte[record.getSize()];
      record.writeToBuffer(recordArray, 0);
      System.arraycopy(recordArray, 0, dataPageArray, offset, recordArray.length);

      ByteBuffer.wrap(dataPageArray).putInt(DBParams.SGBDPageSize - 4, offset + recordArray.length);
      int recordCount = ByteBuffer.wrap(dataPageArray, DBParams.SGBDPageSize - 8, 4).getInt();
      ByteBuffer.wrap(dataPageArray).putInt(DBParams.SGBDPageSize - 8, recordCount + 1);
      ByteBuffer.wrap(dataPageArray).putInt(DBParams.SGBDPageSize - (8 + (recordCount + 1) * 8), offset);
      ByteBuffer.wrap(dataPageArray).putInt(DBParams.SGBDPageSize - (8 + (recordCount + 1) * 8) + 4,
          recordArray.length);

      dataPageBuffer.clear();
      dataPageBuffer.put(dataPageArray);

      bm.freePage(dataPageId, 1);

      ByteBuffer headerPageBuffer = bm.getPage(tabInfo.getHeaderPageId());
      byte[] headerPageArray = new byte[headerPageBuffer.capacity()];
      headerPageBuffer.get(headerPageArray);

      int slotCount = (DBParams.SGBDPageSize - 8) / 8;

      for (int i = 0; i < slotCount; i++) {
        int slotStart = ByteBuffer.wrap(headerPageArray, 4 + i * 8, 4).getInt();
        int slotSize = ByteBuffer.wrap(headerPageArray, 8 + i * 8, 4).getInt();

        if (slotStart == 0 && slotSize == 0) {
          ByteBuffer.wrap(headerPageArray).putInt(4 + i * 8, dataPageId.getPageIdx());
          ByteBuffer.wrap(headerPageArray).putInt(8 + i * 8, record.getSize());
          break;
        }
      }

      headerPageBuffer.clear();
      headerPageBuffer.put(headerPageArray);

      bm.freePage(tabInfo.getHeaderPageId(), 1);

      return new RecordId(dataPageId, recordCount + 1);
    } catch (IOException e) {
      System.out.println("An IOException occurred while inserting the record into the table: " + e.getMessage());
    } catch (PageNotFoundException e) {
      System.out
          .println("A PageNotFoundException occurred while inserting the record into the table: " + e.getMessage());
    }

    return null;
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