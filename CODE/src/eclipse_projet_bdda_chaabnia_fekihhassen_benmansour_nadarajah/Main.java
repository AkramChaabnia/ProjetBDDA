package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import javax.xml.catalog.Catalog;

public class Main {

	public static void main(String[] args) {

		// DBParams.DBPath = "DB//";
		// DBParams.DBPath = args[0];

		// DBParams.SGBDPageSize = 4096;
		// DBParams.DMFileCount = 4;
		// DBParams.FrameCount = 2;

		// Call the init method
		DataBaseManager dbManager = DataBaseManager.getInstance();
		dbManager.init();

		Scanner scanner = new Scanner(System.in);
		String command;
		String[] commands = { "HELP", "EXIT", "CREATE RELATION <Relation> (c1:type,c2:type,...)", "RESETDB",
				"INSERT INTO <Relation> VALUES (val1,val2)",
				"SELECT * FROM <RELATION>", };

		do {
			System.out.println("Bonjour, Veuillez saisir une commande ?\n(HELP pour l'aide et EXIT pour quitter)\n");
			command = scanner.nextLine();

			switch (command) {
				case "HELP":
					System.out.println("Commandes disponibles : " + Arrays.toString(commands));
					break;
				case "EXIT":
					System.out.println("Au revoir!\n");
					dbManager.finish();
					break;
				default:
					dbManager.processCommand(command);
					break;
			}
		} while (!command.equals("EXIT"));

		scanner.close();
	}
}
