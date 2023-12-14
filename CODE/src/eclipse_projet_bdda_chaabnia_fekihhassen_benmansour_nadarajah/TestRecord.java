package eclipse_projet_bdda_chaabnia_fekihhassen_benmansour_nadarajah;

import java.util.List;

public class TestRecord {
	public static void main(String[] args) {
		DataBaseInfo databaseInfo = new DataBaseInfo();

		databaseInfo.init();

		TableInfo tableInfo = new TableInfo("MaTable", 2, List.of("Colonne1", "Colonne2"), List.of("INT", "VARSTRING(10)"));
		databaseInfo.addTableInfo(tableInfo);

		databaseInfo.finish();

		databaseInfo.init();

		Record record = new Record(tableInfo);
		record.addValue("42");
		record.addValue("Hello");

		byte[] buffer = new byte[1024];

		int bytesWritten = record.writeToBuffer(buffer, 0);

		Record readRecord = new Record(tableInfo);
		int bytesRead = readRecord.readFromBuffer(buffer, 0);

		System.out.println("Ecriture: " + bytesWritten);
		System.out.println("Lecture: " + bytesRead);
		System.out.println("Record values: " + readRecord.getRecValues());

		databaseInfo.finish();
	}
}
