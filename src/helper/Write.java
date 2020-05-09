package helper;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import model.Card;

public class Write {
	
	public static void writeToCsv(ArrayList<Card> data, String outputFile) throws IOException {
		System.out.println("Writting to CSV...");
		FileWriter csvWriter = new FileWriter(outputFile);
		for(Card rowData: data) {
			csvWriter.append(rowData.getName() + ",");
			csvWriter.append(rowData.getSet() + ",");
			csvWriter.append(rowData.getPrice() + ",");
			csvWriter.append("\n");
		}
		csvWriter.flush();
		csvWriter.close();
		System.out.println("Writting to CSV complete!");
	}

}
