package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Record {
    private TableInfo tabInfo;
    private List<String> recValues = new ArrayList<>();

    public Record(TableInfo tabInfo) {
        this.tabInfo = tabInfo;
    }

    public int writeToBuffer(byte[] buff, int pos) {
        int offset = pos;

        for (String value : recValues) {
            if (tabInfo.getTypeColonnes().get(recValues.indexOf(value)).startsWith("VARSTRING")) {
                int length = Integer.parseInt(tabInfo.getTypeColonnes().get(recValues.indexOf(value)).substring(10,
                        tabInfo.getTypeColonnes().get(recValues.indexOf(value)).length() - 1));
                byte[] strBytes = value.getBytes();
                System.arraycopy(strBytes, 0, buff, offset, strBytes.length);
                offset += length;
            } else if (tabInfo.getTypeColonnes().get(recValues.indexOf(value)).equals("INT")) {
                int intValue = Integer.parseInt(value);
                byte[] intBytes = ByteBuffer.allocate(4).putInt(intValue).array();
                System.arraycopy(intBytes, 0, buff, offset, intBytes.length);
                offset += 4;
            } else if (tabInfo.getTypeColonnes().get(recValues.indexOf(value)).equals("FLOAT")) {
                float floatValue = Float.parseFloat(value);
                byte[] floatBytes = ByteBuffer.allocate(4).putFloat(floatValue).array();
                System.arraycopy(floatBytes, 0, buff, offset, floatBytes.length);
                offset += 4;
            }
        }

        return offset - pos;
    }

    public int readFromBuffer(byte[] buff, int pos) {
        int offset = pos;

        for (String colType : tabInfo.getTypeColonnes()) {
            if (colType.startsWith("VARSTRING")) {
                int length = Integer.parseInt(colType.substring(10, colType.length() - 1));
                byte[] strBytes = new byte[length];
                System.arraycopy(buff, offset, strBytes, 0, length);
                recValues.add(new String(strBytes));
                offset += length;
            } else if (colType.equals("INT")) {
                byte[] intBytes = new byte[4];
                System.arraycopy(buff, offset, intBytes, 0, 4);
                recValues.add(String.valueOf(ByteBuffer.wrap(intBytes).getInt()));
                offset += 4;
            } else if (colType.equals("FLOAT")) {
                byte[] floatBytes = new byte[4];
                System.arraycopy(buff, offset, floatBytes, 0, 4);
                recValues.add(String.valueOf(ByteBuffer.wrap(floatBytes).getFloat()));
                offset += 4;
            }
        }

        return offset - pos;
    }

    // a verifier particulierement le set on on veut ajouter juste une entree
    public List<String> getRecValues() {
        return recValues;
    }

    public void setRecValues(List<String> recValues) {
        this.recValues = recValues;
    }

    public void addValue(String value) {
        this.recValues.add(value);
    }
}
