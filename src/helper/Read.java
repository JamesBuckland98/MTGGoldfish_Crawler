package helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Read {
	
	public static ArrayList<String> readTxtFile(String fileLocation) throws IOException {
		ArrayList<String> cardList = new ArrayList<String>();
		File file = new File(fileLocation);
		BufferedReader reader = new BufferedReader(new FileReader(file)); 
		String cardName; 
		while ((cardName = reader.readLine()) != null) {
			cardList.add(cardName);
		}
		reader.close();
		return(cardList);
	}

}
