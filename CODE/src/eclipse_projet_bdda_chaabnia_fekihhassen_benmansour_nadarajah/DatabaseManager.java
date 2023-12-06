/* package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;


public class DatabaseManager {
    private static DatabaseManager instance = null;

    private DatabaseManager() {
        // Constructeur privé pour assurer une seule instance
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void init() {
        // Appel à la méthode init du DataBaseInfo
        DataBaseInfo.getInstance().Init();

        // Appel à la méthode init du BufferManager
        BufferManager.getInstance().init();

        // Si une méthode init du DiskManager existe, ajoutez cet appel ici
    }

    public void finish() {
        // Si une méthode finish du DiskManager existe, ajoutez cet appel ici

        // Appel à la méthode FlushAll (ou Finish) du BufferManager
        BufferManager.getInstance().flushAll();

        // Appel à la méthode finish du DataBaseInfo
        DataBaseInfo.getInstance().finish();
    }

    public void processCommand(String command) {
        // Vous allez implémenter le traitement des commandes ici
        // Utilisez un switch/case ou des if/else pour gérer différentes commandes
        // Par exemple :
        // if (command.startsWith("SELECT")) {
        //     // Traitement de la commande SELECT
        // } else if (command.startsWith("INSERT")) {
        //     // Traitement de la commande INSERT
        // } else if (...) {
        //     // Autres commandes
        // }
    }
}
 */