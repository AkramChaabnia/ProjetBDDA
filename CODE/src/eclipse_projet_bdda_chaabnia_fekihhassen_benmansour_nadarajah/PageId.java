package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.util.Objects;

public class PageId {
    private int fileId;
    private int pageId;

    public PageId(int fileId, int pageId) {
        this.fileId = fileId;
        this.pageId = pageId;
    }

    public int getFileIdx() {
        return fileId;
    }

    public int getPageIdx() {
        return pageId;
    }

    @Override
    public String toString() {
        return "PageId{" + "File Id=" + fileId + ", Numero de page=" + pageId + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PageId pageId = (PageId) obj;
        return fileId == pageId.fileId && this.pageId == pageId.pageId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileId, pageId);
    }
}