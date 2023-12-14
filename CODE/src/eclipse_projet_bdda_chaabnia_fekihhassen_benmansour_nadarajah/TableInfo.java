package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.util.List;

public class TableInfo {
    private String nomRelation;
    private int numColonnes;
    private List<String> nomColonnes;
    private List<String> typeColonnes;

    // private PageId headerPageId;

    public TableInfo(String nomRelation, int numColonnes, List<String> nomColonnes, List<String> typeColonnes/*
                                                                                                              * , PageId
                                                                                                              * headerPageId
                                                                                                              */) {
        this.nomRelation = nomRelation;
        this.numColonnes = numColonnes;
        this.nomColonnes = nomColonnes;
        this.typeColonnes = typeColonnes;

        // this.headerPageId = headerPageId;
    }

    public String GetNomRelation() {
        return nomRelation;
    }

    public void setNomRelation(String nomRelation) {
        this.nomRelation = nomRelation;
    }

    public int getNumColonnes() {
        return numColonnes;
    }

    public void setNumColonnes(int numColonnes) {
        this.numColonnes = numColonnes;
    }

    public List<String> getNomColonnes() {
        return nomColonnes;
    }

    public void setNomColonnes(List<String> nomColonnes) {
        this.nomColonnes = nomColonnes;
    }

    public List<String> getTypeColonnes() {
        return typeColonnes;
    }

    public void setTypeColonnes(List<String> typeColonnes) {
        this.typeColonnes = typeColonnes;
    }
    /*
     * public PageId getHeaderPageId() {
     * return headerPageId;
     * }
     * 
     * public void setHeaderPageId(PageId headerPageId) {
     * this.headerPageId = headerPageId;
     * }
     */
}
