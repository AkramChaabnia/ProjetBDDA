package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

public class RecordId {
    private PageId pageId;
    private int slotIdx;

    public RecordId(PageId pageId, int slotIdx) {
        this.pageId = pageId;
        this.slotIdx = slotIdx;
    }

    public PageId getPageId() {
        return pageId;
    }

    public int getSlotIdx() {
        return slotIdx;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        RecordId recordId = (RecordId) obj;
        return slotIdx == recordId.slotIdx && pageId.equals(recordId.pageId);
    }

    // todo : hashCode
}
