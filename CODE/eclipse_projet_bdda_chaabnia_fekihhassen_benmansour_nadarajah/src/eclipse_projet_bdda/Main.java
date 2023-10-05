package eclipse_projet_bdda;


public class Main {
    public static void main(String[] args) {
    	
//		DBParams.DBPath = args[0];
		DBParams.DBPath = "..\\..\\BD\\";
    		DBParams.SGBDPageSize = 4096; 
    		DBParams.DMFileCount = 4;
    		
    		DiskManager.getInstance();

        System.out.println("coucou!!!");

    }

}
