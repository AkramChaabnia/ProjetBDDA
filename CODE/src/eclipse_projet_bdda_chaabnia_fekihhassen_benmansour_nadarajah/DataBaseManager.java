package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

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
    DataBaseInfo.getInstance().init();
    BufferManager.getInstance().init(); // getPAge ou on cree une methode init()!!!!!!!
  }

  public void finish() {
    DataBaseInfo.getInstance().finish();
    BufferManager.getInstance().flushAll();
  }

  public void processCommand(String chaineCommande) {
    String[] commande = chaineCommande.split(" ");

    switch (commande[0]) {
      case "CREATE":
        if (commande.length > 2) {
          CreateTableCommand createTableCommand = new CreateTableCommand(chaineCommande);
          createTableCommand.execute();
        } else {
          System.out.println("Instruction incomplet");
        }
        break;
      case "RESETDB":
        ResetDBCommand resetDBCommand = new ResetDBCommand(chaineCommande);
        resetDBCommand.execute();
        break;
      case "INSERT":
        if (commande.length == 5) {
          InsertCommand insertCommand = new InsertCommand(chaineCommande);
          insertCommand.execute();
        } else {
          System.out.println("Instruction incomplet");
        }
        break;
      case "SELECT":
        if (commande.length > 4) {
          SelectCommand selectCommand = new SelectCommand(chaineCommande);
          selectCommand.execute();
        } else {
          System.out.println("Instruction incomplet");
        }
        break;
      default:
        System.out.println("Mauvaise commande\n");
        break;
    }
  }
}
