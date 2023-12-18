package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.io.IOException;

public class DataBaseManager {

  private static DataBaseManager instance = new DataBaseManager();

  private DataBaseManager() {
  }

  public static DataBaseManager getInstance() {
    if (instance == null) {
      instance = new DataBaseManager();
    }
    return instance;
  }

  public void init() {
    // Initialisation de DatabaseInfo
    DataBaseInfo.getInstance().init();

    // Initialisation de BufferManager, si nécessaire
    BufferManager.getInstance().init();

    // DiskManager.getInstance().init();
  }

  public void finish() {
    DataBaseInfo.getInstance().finish();
    BufferManager.getInstance().flushAll();
    // DiskManager.getInstance().finish(); // getPAge ou on cree une methode
    // finish()!!!!!!!
  }

  public void processCommand(String chaineCommande) {
    try {
      String[] commande = chaineCommande.trim().toUpperCase().split("\\s+"); // ignorer la casse ??

      switch (commande[0]) {
        case "CREATE":
          if (commande.length > 2 && commande[1].equals("TABLE")) {
            CreateTableCommand createTableCommand = new CreateTableCommand(chaineCommande);
            createTableCommand.execute();
          } else {
            System.out.println(
                "Syntaxe incorrecte pour CREATE TABLE. Usage attendu : CREATE TABLE nom_table (col1:type1, col2:type2, ...)");
          }
          break;
        case "RESETDB":
          ResetDBCommand resetDBCommand = new ResetDBCommand();
          resetDBCommand.execute();
          break;
        case "INSERT":
          if (commande.length >= 5) {
            InsertCommand insertCommand = new InsertCommand(chaineCommande);
            insertCommand.execute();
          } else {
            System.out.println(
                "Syntaxe incorrecte pour INSERT. Usage attendu : INSERT INTO nom_table VALUES (val1, val2, ...)");
          }
          break;
        case "SELECT":
          if (commande.length >= 4) {
            SelectCommand selectCommand = new SelectCommand(chaineCommande);
            selectCommand.execute();
          } else {
            System.out
                .println("Syntaxe incorrecte pour SELECT. Usage attendu : SELECT * FROM nom_table WHERE condition");
          }
          break;
        default:
          System.out.println("Commande non reconnue : " + commande[0]);
          break;
      }
    } catch (Exception e) {
      System.out.println("Une erreur s'est produite lors du traitement de la commande : " + e.getMessage());
    }
  }

}