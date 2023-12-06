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

	/*
	 * public String Tostring() {
	 * return "identifiant : "+ fileId + "numero : "+ pageId;
	 * }
	 */
	public String toString() {
		return "PageId{" + "fileid=" + fileId + ", pageId=" + pageId + '}';
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		PageId pageIdi = (PageId) obj;
		return fileId == pageIdi.fileId && (pageId == pageIdi.pageId);
	}

	public int hashcode() {
		return Objects.hash(fileId, pageId);
	}

}