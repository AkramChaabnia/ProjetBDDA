package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.util.ArrayList;

public class TableInfo {
    private String nom_relation;
    private int nb_colonnes;
    private ArrayList<ColInfo> colInfoList;
    private PageId headerPageId;

    public TableInfo(String nom_relation, int nb_colonnes, PageId headerPageId) {
        this.nom_relation = nom_relation;
        this.nb_colonnes = nb_colonnes;
        this.colInfoList = new ArrayList<>();
        this.headerPageId = headerPageId;
    }

    public TableInfo(String nom_relation, ArrayList<ColInfo> colInfoList, PageId headerPageId) {
        this.nom_relation = nom_relation;
        this.nb_colonnes = colInfoList.size();
        this.colInfoList = colInfoList;
        this.headerPageId = headerPageId;
    }

    // PREVOIR UN DEUXIEME CONSTRUCTEUR
    public String getNom_relation() {
        return nom_relation;
    }

    public void setNom_relation(String nom_relation) {
        this.nom_relation = nom_relation;
    }

    public int getNb_colonnes() {
        return nb_colonnes;
    }

    public void setNb_colonnes(int nb_colonnes) {
        this.nb_colonnes = nb_colonnes;
    }

    public ArrayList<ColInfo> getColInfoList() {
        return colInfoList;
    }

    public void setColInfoList(ArrayList<ColInfo> colInfoList) {
        this.colInfoList = colInfoList;
    }

    public PageId getHeaderPagerId() {
        return headerPageId;
    }

    public void setHeaderPageId(PageId headerPageId) {
        this.headerPageId = headerPageId;
    }

}