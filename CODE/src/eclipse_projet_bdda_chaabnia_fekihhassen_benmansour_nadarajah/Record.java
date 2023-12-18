package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Record {
    private TableInfo tabInfo;
    private ArrayList<String> recvalues;
    private int size;

    public Record(TableInfo tabInfo) {
        this.tabInfo = tabInfo;
        this.recvalues = new ArrayList<>();
        this.size = calculateSize();
    }

    private int calculateSize() {
        int recordSize = 0;

        for (int i = 0; i < tabInfo.getColInfoList().size(); i++) {
            String colType = tabInfo.getColInfoList().get(i).getType();

            switch (colType) {
                case "INT":
                    recordSize += Integer.BYTES;
                    break;
                case "FLOAT":
                    recordSize += Float.BYTES;
                    break;
                case "STRING":
                    recordSize += recvalues.get(i).length() * Character.BYTES + Character.BYTES;

                    break;
                case "VARSTRING":
                    recordSize += Integer.BYTES + recvalues.get(i).length() * Character.BYTES;
                    break;
                default:
                    throw new IllegalArgumentException("Type de colonne inconnu : " + colType);
            }
        }

        return recordSize;
    }

    public int writeToBuffer(ByteBuffer buff, int pos) {
        // Positionne le curseur au début de l'écriture
        buff.position(pos);

        for (int i = 0; i < tabInfo.getColInfoList().size(); i++) {
            String colType = tabInfo.getColInfoList().get(i).getType();
            String value = recvalues.get(i);

            switch (colType) {
                case "INT":
                    int intValue = Integer.parseInt(value);
                    buff.putInt(intValue);
                    break;
                case "FLOAT":
                    float floatValue = Float.parseFloat(value);
                    buff.putFloat(floatValue);
                    break;
                case "STRING":
                    for (char c : value.toCharArray()) {
                        buff.putChar(c);
                    }
                    buff.putChar('\0');
                    break;
                case "VARSTRING":
                    int length = value.length();
                    buff.putInt(length);
                    for (char c : value.toCharArray()) {
                        buff.putChar(c);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("type de colonne inconnu : " + colType);
            }
        }

        return buff.position() - pos;
    }

    public int readFromBuffer(byte[] buff, int pos) {
        int offset = pos;
        recvalues.clear();

        for (ColInfo colInfo : tabInfo.getColInfoList()) {
            String colType = colInfo.getType();
            byte[] bytesToRead;

            if (colType.startsWith("VARSTRING")) {
                int length = Integer.parseInt(colType.substring(10, colType.length() - 1));
                bytesToRead = new byte[length];
                System.arraycopy(buff, offset, bytesToRead, 0, length);
                recvalues.add(new String(bytesToRead));
                offset += length;
            } else if (colType.equals("INT")) {
                bytesToRead = new byte[Integer.BYTES];
                System.arraycopy(buff, offset, bytesToRead, 0, Integer.BYTES);
                recvalues.add(String.valueOf(ByteBuffer.wrap(bytesToRead).getInt()));
                offset += Integer.BYTES;
            } else if (colType.equals("FLOAT")) {
                bytesToRead = new byte[Float.BYTES];
                System.arraycopy(buff, offset, bytesToRead, 0, Float.BYTES);
                recvalues.add(String.valueOf(ByteBuffer.wrap(bytesToRead).getFloat()));
                offset += Float.BYTES;
            }
        }

        return offset - pos;
    }

    // a verifier particulierement le set on on veut ajouter juste une entree
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

    public void addValue(String value) {
        this.recvalues.add(value);
    }

    public int getSize() {
        return size;
    }

}