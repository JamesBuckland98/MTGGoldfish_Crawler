package helper;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Card;

public class Write {
	
	private static final Logger LOGGER = Logger.getLogger(Write.class.getName());
	
	public static void writeToCsv(List<Card> data, String outputFile) throws IOException {
		LOGGER.log(Level.INFO, "Writting to CSV...");
		try (FileWriter csvWriter = new FileWriter(outputFile)){
			for(Card rowData: data) {
				csvWriter.append(rowData.getName() + ",");
				csvWriter.append(rowData.getSet() + ",");
				csvWriter.append(rowData.getPrice() + ",");
				csvWriter.append("\n");
			}
			csvWriter.flush();
			LOGGER.log(Level.INFO, "Writting to CSV complete!");
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, e.getMessage());
		}
	}
}
