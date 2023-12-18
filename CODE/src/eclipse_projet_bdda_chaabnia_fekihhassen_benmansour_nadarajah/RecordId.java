package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

public class RecordId {
    private PageId pageId;
    private int slotIdx;

    public RecordId(PageId pageId, int slotIndex) {
        this.pageId = pageId;
        this.slotIdx = slotIndex;
    }

    public PageId getPageId() {
        return pageId;
    }

    public void setPageId(PageId pageId) {
        this.pageId = pageId;
    }

    public int getSlotIdx() {
        return slotIdx;
    }

    public void setSlotIdx(int slotIdx) {
        this.slotIdx = slotIdx;
    }
}
