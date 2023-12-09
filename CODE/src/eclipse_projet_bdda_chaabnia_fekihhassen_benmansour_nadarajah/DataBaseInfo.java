package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.util.ArrayList;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

//Ajouter des exceptions  -> try catch() dans init et finish

public class DataBaseInfo {
	private ArrayList<TableInfo> tab;
	private int compteur;

	private DataBaseInfo(int compteur) {
		// Taille a verifier
		tab = new ArrayList<TableInfo>();
		// Initialisation requise ou initialiser a 0;
		this.setCompteur(compteur);
	}
	// FAIRE UN DEUXIEME CONSTRUCTEUR

	// CREATION D'UNE SEULE ET UNIQUE INSTANCE
	public static DataBaseInfo getInstance() {
		return new DataBaseInfo(0);
	}

	public ArrayList<TableInfo> getTab() {
		return tab;
	}

	public int getCompteur() {
		return compteur;
	}

	private void setCompteur(int compteur) {
		this.compteur = compteur;
	}

	// Ajouter try catch() A verifier notamment le remplissage de databaseinfo avec
	// les infos

	public void AddTableInfo(TableInfo t) {
		tab.add(t);
		compteur++;
	}

	public TableInfo GetTableInfo(String nom_table) {
		for (TableInfo table : tab) {
			if (table.getNom_relation().equals(nom_table)) {
				return table;
			}
		}
		return null;
	}

	public void Init() {
		File file = new File("DB/DBIfo.save");

		if (file.exists()) {
			try {
				FileInputStream fic = new FileInputStream(file);
				ObjectInputStream obj = new ObjectInputStream(fic); // deserialise l'objet

				DataBaseInfo dbInfo = (DataBaseInfo) obj.readObject();
				this.tab = dbInfo.getTab();
				this.compteur = dbInfo.getCompteur();

				obj.close();
				fic.close();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			// Fichier DBInfo.save n'existe pas, rien à remplir
		}
	}

	public void Finish() {
		try {
			File file = new File("DB/DBInfo.save");
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(this);
			objectOutputStream.close();
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// A TESTER

}