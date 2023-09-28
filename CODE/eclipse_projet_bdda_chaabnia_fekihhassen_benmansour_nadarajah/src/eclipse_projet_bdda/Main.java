package eclipse_projet_bdda;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	private static List<Personne> personnes = new ArrayList<>();

	public static void main(String[] args) {
		String file = "..\\\\..\\\\BD\\\\personnes.txt";
        loadFromFile(file); 
		Scanner scanner = new Scanner(System.in);
		
		while(true) {
			System.out.println("entrez une commande (ADD 'nom d'une persone', LIST, QUIT) ");
			String mssg = scanner.nextLine().trim();
			
			if(mssg.equalsIgnoreCase("QUIT")) {
				addToFile(file);
				System.out.println("au revoir");
				break;
			} else if(mssg.toLowerCase().startsWith("add ")) {
				addPersonne(new Personne(mssg.substring(4)));
//	            addToFile(file);
			} else if(mssg.equalsIgnoreCase("LIST")) {
				listPersonnes();
            } else {
                System.out.println("Commande invalide.");
            }
			}
	}

    private static void addPersonne(Personne personne) {
        personnes.add(personne);
        System.out.println(personne.getName() + " ajouté à la liste.");
    }

    private static void listPersonnes() {
        if (personnes.isEmpty()) {
            System.out.println("La liste est vide.");
        } else {
            System.out.println("Liste des personnes :");
            for (Personne personne : personnes) {
                System.out.println(personne.getName());
            }
        }
    }
    
    
    private static void loadFromFile(String fileName) {
        personnes.clear(); 
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                addPersonne(new Personne(line));
            }
            System.out.println("Données chargées avec succès !");
        } catch (IOException e) {
            System.out.println("Fichier de données introuvable. La liste est vide.");
        }
    }

    
    
    private static void addToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Personne personne : personnes) {
                writer.write(personne.getName());
                writer.newLine(); 
            }
            System.out.println("Personnes ajouter avec succès !");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
