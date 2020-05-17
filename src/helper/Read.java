package helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Read {
	
	private static final Logger LOGGER = Logger.getLogger(Read.class.getName());
	
	public static List<String> readTxtFile(String fileLocation) throws IOException {
		List<String> cardList = new ArrayList<>();
		File file = new File(fileLocation);
		try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String cardName; 
			while ((cardName = reader.readLine()) != null) {
				if(cardName.trim().length() > 0) {
					cardList.add(cardName.trim());
				}
			}	
		} catch(FileNotFoundException f) {
			LOGGER.log(Level.WARNING, f.getMessage());
		}
		return(cardList);
	}
}
