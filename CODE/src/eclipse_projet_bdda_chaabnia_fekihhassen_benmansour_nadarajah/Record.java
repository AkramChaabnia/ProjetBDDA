package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.nio.ByteBuffer;
import java.util.ArrayList;

//Ajouter des exceptions  -> try catch()
public class Record {
	private TableInfo tabInfo;
	private ArrayList<String> recvalues;

	public Record(TableInfo tabInfo) {
		this.tabInfo = tabInfo;
		this.recvalues = new ArrayList<String>();
	}

	// FAIRE UN DEUXIEME CONSTRUCTEUR

	public int writeToBuffer(ByteBuffer buff, int pos) {
		// Position the cursor at the specified position
		buff.position(pos);

		for (int i = 0; i < tabInfo.getColInfoList().size(); i++) {
			if (tabInfo.getColInfoList().get(i).getType().equals("INT")) {
				int intValue = Integer.parseInt(recvalues.get(i));
				buff.putInt(intValue);
			}
			if (tabInfo.getColInfoList().get(i).getType().equals("FLOAT")) {
				float floatValue = Float.parseFloat(recvalues.get(i));
				buff.putFloat(floatValue);
			}
			if (tabInfo.getColInfoList().get(i).getType().equals("STRING")) {
				String stringValue = recvalues.get(i);
				for (char c : stringValue.toCharArray()) {
					buff.putChar(c);
				}
			}
			if (tabInfo.getColInfoList().get(i).getType().equals("VARSTRING")) {
				String stringValue = recvalues.get(i);
				buff.putInt(stringValue.length());
				for (char c : stringValue.toCharArray()) {
					buff.putChar(c);
				}
			}
		}

		// Return the total size (number of bytes) written to the buffer
		return buff.position() - pos;
	}

	public int readFromBuffer(ByteBuffer buff, int pos) {
		// Position the cursor at the specified position
		buff.position(pos);

		// Clear the existing values in the recvalues list
		recvalues.clear();

		for (int i = 0; i < tabInfo.getColInfoList().size(); i++) {
			if (tabInfo.getColInfoList().get(i).getType().equals("INT")) {
				int intValue = buff.getInt();
				recvalues.add(Integer.toString(intValue));
			} else if (tabInfo.getColInfoList().get(i).getType().equals("FLOAT")) {
				float floatValue = buff.getFloat();
				recvalues.add(Float.toString(floatValue));
			} else if (tabInfo.getColInfoList().get(i).getType().equals("STRING")) {
				StringBuilder stringValue = new StringBuilder();
				while (buff.hasRemaining()) {
					char c = buff.getChar();
					if (c == '\0') {
						break;
					}
					stringValue.append(c);
				}
				recvalues.add(stringValue.toString());
			} else if (tabInfo.getColInfoList().get(i).getType().equals("VARSTRING")) {
				int length = buff.getInt();
				StringBuilder stringValue = new StringBuilder();
				for (int j = 0; j < length; j++) {
					char c = buff.getChar();
					stringValue.append(c);
				}
				recvalues.add(stringValue.toString());
			}
		}
		// Return the total size (number of bytes) read from the buffer
		return buff.position() - pos;
	}

	public TableInfo getTabInfo() {
		return tabInfo;
	}

	public void setTabInfo(TableInfo tabInfo) {
		this.tabInfo = tabInfo;
	}

	public ArrayList<String> getRecvalues() {
		return recvalues;
	}

	public void setRecvalues(ArrayList<String> recvalues) {
		this.recvalues = recvalues;
	}

	// public int writeToBuffer1(ByteBuffer buff, int pos) {
	// // Position the cursor at the specified position
	// buff.position(pos);

	// // Write the offset directory
	// int numValues = tabInfo.getColInfoList().size();
	// int[] offsetDirectory = new int[numValues + 1];
	// int offset = pos + (numValues + 1) * Integer.BYTES;
	// for (int i = 0; i < numValues; i++) {
	// offsetDirectory[i] = offset;
	// String value = recvalues.get(i);
	// if (tabInfo.getColInfoList().get(i).getType().equals("INT")) {
	// int intValue = Integer.parseInt(value);
	// buff.putInt(intValue);
	// offset += Integer.BYTES;
	// } else if (tabInfo.getColInfoList().get(i).getType().equals("FLOAT")) {
	// float floatValue = Float.parseFloat(value);
	// buff.putFloat(floatValue);
	// offset += Float.BYTES;
	// } else if (tabInfo.getColInfoList().get(i).getType().equals("STRING")) {
	// for (char c : value.toCharArray()) {
	// buff.putChar(c);
	// offset += Character.BYTES;
	// }
	// buff.putChar('\0'); // Null-terminate the string
	// offset += Character.BYTES;
	// } else if (tabInfo.getColInfoList().get(i).getType().equals("VARSTRING")) {
	// int length = value.length();
	// buff.putInt(length);
	// offset += Integer.BYTES;
	// for (char c : value.toCharArray()) {
	// buff.putChar(c);
	// offset += Character.BYTES;
	// }
	// }
	// }
	// offsetDirectory[numValues] = offset;

	// // Write the offset directory
	// for (int i = 0; i <= numValues; i++) {
	// buff.putInt(offsetDirectory[i]);
	// }

	// // Return the total size (number of bytes) written to the buffer
	// return offset - pos;
	// }
}
