package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.nio.ByteBuffer;

public class Frame {
	private ByteBuffer buffer;
	private int pinCount;
	private boolean dirty;

	public Frame(ByteBuffer buffer) {
		// this.buffer = new byte[bufferSize];
		// this.buffer = new byte[DBParams.SGBDPageSize];
		// this.buffer = ByteBuffer.allocate(DBParams.SGBDPageSize);
		this.buffer = buffer;
		this.pinCount = 1;
		this.dirty = false;
	}

	public void incrementerPinCount() {
		pinCount++;
	}

	// est ce que il peut inferieur a 0;
	public void decrementerPinCount() {
		if (pinCount > 0) {
			pinCount--;
		} else {
			pinCount = 0;
		}
	}
	/*
	 * public void decrementerPinCount() {
	 * if(pinCount==0) {
	 * break;
	 * }
	 * if(pinCount>0) {
	 * pinCount--;
	 * }
	 * 
	 * }
	 */

	public ByteBuffer getBuffer() {
		return buffer;
	}

	public int getPinCount() {
		return pinCount;
	}

	public boolean getDirty() {
		return dirty;
	}

	public void setBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	public void setPinCount(int pinCount) {
		this.pinCount = pinCount;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

}
