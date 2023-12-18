package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Record {
    private TableInfo tabInfo;
    private ArrayList<String> recvalues;

    public Record(TableInfo tabInfo) {
        this.tabInfo = tabInfo;
        this.recvalues = new ArrayList<>();
    }

    public int writeToBuffer(byte[] buff, int pos) {
        int offset = pos;

        for (String value : recvalues) {
            String type = tabInfo.getColInfoList().get(recvalues.indexOf(value)).getType();
            byte[] bytesToWrite;

            if (type.startsWith("VARSTRING")) {
                int length = Integer.parseInt(type.substring(10, type.length() - 1));
                bytesToWrite = value.getBytes();
                System.arraycopy(bytesToWrite, 0, buff, offset, bytesToWrite.length);
                offset += length;
            } else if (type.equals("INT")) {
                int intValue = Integer.parseInt(value);
                bytesToWrite = ByteBuffer.allocate(Integer.BYTES).putInt(intValue).array();
                System.arraycopy(bytesToWrite, 0, buff, offset, bytesToWrite.length);
                offset += Integer.BYTES;
            } else if (type.equals("FLOAT")) {
                float floatValue = Float.parseFloat(value);
                bytesToWrite = ByteBuffer.allocate(Float.BYTES).putFloat(floatValue).array();
                System.arraycopy(bytesToWrite, 0, buff, offset, bytesToWrite.length);
                offset += Float.BYTES;
            }
        }

        return offset - pos;
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

}