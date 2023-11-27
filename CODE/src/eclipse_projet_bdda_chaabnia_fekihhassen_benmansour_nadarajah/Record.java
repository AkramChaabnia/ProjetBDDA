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

	private int writeToBuffer(ByteBuffer buff, int pos) {
		// Position the cursor at the specified position
		buff.position(pos);

		for (int i = 0; i < tabInfo.getColInfoList().size(); i++) {
			if (tabInfo.getColInfoList().get(i).getName().equals("INT")) {
				int intValue = Integer.parseInt(recvalues.get(i));
				buff.putInt(intValue);
			}
			if (tabInfo.getColInfoList().get(i).equals("FLOAT")) {
				float floatValue = Float.parseFloat(recvalues.get(i));
				buff.putFloat(floatValue);
			}
			if (tabInfo.getColInfoList().get(i).equals("STRING")) {
				String stringValue = recvalues.get(i);
				for (char c : stringValue.toCharArray()) {
					buff.putChar(c);
				}
			}
			if (tabInfo.getColInfoList().get(i).equals("VARSTRING")) {
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

	private int readFromBuffer(ByteBuffer buff, int pos) {
		// Position the cursor at the specified position
		buff.position(pos);

		// Clear the existing values in the recvalues list
		recvalues.clear();

		for (int i = 0; i < tabInfo.getColInfoList().size(); i++) {
			if (tabInfo.getColInfoList().get(i).getName().equals("INT")) {
				int intValue = buff.getInt();
				recvalues.add(Integer.toString(intValue));
			} else if (tabInfo.getColInfoList().get(i).getName().equals("FLOAT")) {
				float floatValue = buff.getFloat();
				recvalues.add(Float.toString(floatValue));
			} else if (tabInfo.getColInfoList().get(i).getName().equals("STRING")) {
				StringBuilder stringValue = new StringBuilder();
				while (buff.hasRemaining()) {
					char c = buff.getChar();
					if (c == '\0') {
						break;
					}
					stringValue.append(c);
				}
				recvalues.add(stringValue.toString());
			} else if (tabInfo.getColInfoList().get(i).getName().equals("VARSTRING")) {
				int length = buff.getInt();
				StringBuilder stringValue = new StringBuilder();
				for (int j = 0; j < length; j++) {
					char c = buff.getChar();
					stringValue.append(c);
				}
				recvalues.add(stringValue.toString());
			}

			// Return the total size (number of bytes) read from the buffer
			return buff.position() - pos;
		}
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

}