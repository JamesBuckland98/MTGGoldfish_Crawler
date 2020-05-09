package helper;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Write {
	
	public static void writeToCsv(ArrayList<ArrayList<String>> data, String outputFile) throws IOException {
		System.out.println("Writting to CSV...");
		FileWriter csvWriter = new FileWriter(outputFile);
		for(ArrayList<String> rowData: data) {
			csvWriter.append(String.join(",", rowData));
			csvWriter.append("\n");
		}
		csvWriter.flush();
		csvWriter.close();
		System.out.println("Writting to CSV complete!");
	}

}
