package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.io.File;

public class ResetDBCommand {

  public void execute() {
    // Delete all files in the DB folder
    File path = new File(DBParams.DBPath);
    if (path.exists()) {
      File[] files = path.listFiles();
      for (File file : files) {
        file.delete();
      }
    }

    // Reset BufferManager
    BufferManager.getInstance().reset();

    // Reset DBInfo and FileManager
    DataBaseInfo.getInstance().reset();
    // FileManager.getInstance().reset(); pas besoin je crois ?
    
    DiskManager.getInstance().reset();
    
  }
}