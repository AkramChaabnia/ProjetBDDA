package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ImportCommand {
  private String relationName;
  private String csvFileName;

  public ImportCommand(String command) {
    parseCommand(command);
  }

  private void parseCommand(String command) {
    String[] commandParts = command.split(" ");
    if (commandParts.length != 4 || !commandParts[0].equalsIgnoreCase("IMPORT") || !commandParts[2].equalsIgnoreCase("INTO")) {
      throw new IllegalArgumentException("Commande d'importation incorrecte.");
    }

    this.relationName = commandParts[1];
    this.csvFileName = commandParts[3];
  }

}
