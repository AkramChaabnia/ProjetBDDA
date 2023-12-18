package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

public class ColInfo {
    private String name;
    private String type;

    public ColInfo(String name, String type) {
        if (!type.equals("INT") && !type.equals("FLOAT") && !type.startsWith("STRING")
                && !type.startsWith("VARSTRING")) {
            throw new IllegalArgumentException("Invalid type: " + type);
        }
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}