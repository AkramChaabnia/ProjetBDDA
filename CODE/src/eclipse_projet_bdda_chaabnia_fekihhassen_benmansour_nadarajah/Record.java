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

    // Ajoutez un deuxième constructeur si nécessaire

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

    public int readFromBuffer(ByteBuffer buff, int pos) {
        // Positionne le curseur au début de la lecture
        buff.position(pos);
    
        // Efface les valeurs existantes dans la liste recvalues
        recvalues.clear();
    
        for (int i = 0; i < tabInfo.getColInfoList().size(); i++) {
            String colType = tabInfo.getColInfoList().get(i).getType();
    
            switch (colType) {
                case "INT":
                    int intValue = buff.getInt();
                    recvalues.add(Integer.toString(intValue));
                    break;
                case "FLOAT":
                    float floatValue = buff.getFloat();
                    recvalues.add(Float.toString(floatValue));
                    break;
                case "STRING":
                    StringBuilder stringValue = new StringBuilder();
                    char c;
                    while ((c = buff.getChar()) != '\0') {
                        stringValue.append(c);
                    }
                    recvalues.add(stringValue.toString());
                    break;
                case "VARSTRING":
                    int length = buff.getInt();
                    StringBuilder varStringValue = new StringBuilder();
                    for (int j = 0; j < length; j++) {
                        char varChar = buff.getChar(); 
                        varStringValue.append(varChar);
                    }
                    recvalues.add(varStringValue.toString());
                    break;
                default:
                    throw new IllegalArgumentException("Type de colonne inconnu : " + colType);
            }
        }
    
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
}