package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;

public class Main {

	public static void main(String[] args) throws IOException, PageNotFoundException {

		DBParams.DBPath = "BD//";
		// DBParams.DBPath = args[0];

		DBParams.SGBDPageSize = 4096;
		DBParams.DMFileCount = 4;
		DBParams.FrameCount = 2;

		// Call the init method
		DataBaseManager dbManager = DataBaseManager.getInstance();
		dbManager.init();

		String command;
		String upperCaseCommand;
		String[] commands = { "HELP", "EXIT", "CREATE RELATION <Relation> (c1:type,c2:type,...)", "RESETDB",
				"INSERT INTO <Relation> VALUES (val1,val2)",
				"SELECT * FROM <RELATION>", };

		List<String> testCommands = Arrays.asList(
				"RESETDB",
				"CREATE TABLE R (C1:INT,C2:VARSTRING(3),C3:INT)",
				"INSERT INTO R VALUES (1,aab,2)",
				"INSERT INTO R VALUES (2,ab,2)",
				"INSERT INTO R VALUES (1,agh,1)",
				"SELECT * FROM R",
				"SELECT * FROM R WHERE C1=1",
				"SELECT * FROM R WHERE C3=1",
				"SELECT * FROM R WHERE C1=1 AND C3=2",
				"SELECT * FROM R WHERE C1<2",
				"EXIT");

		Iterator<String> testCommandIterator = testCommands.iterator();

		do {
			if (testCommandIterator.hasNext()) {
				command = testCommandIterator.next();
			} else {
				break;
			}

			System.out.println("Executing command: " + command);

			upperCaseCommand = command.toUpperCase();
			switch (upperCaseCommand) {
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
		} while (!upperCaseCommand.equals("EXIT"));
	}
}