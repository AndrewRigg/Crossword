package Crossword;
import java.util.*;
import java.io.*;

/**
 *	Read in words and definitions from file and put into an ArrayList.
 */
public class ReadWords {	
	
	public static ArrayList<Word> getWordsandDefs(String filename)throws IOException{
		ArrayList<Word> words = new ArrayList<Word>();
		ArrayList<String> definitions = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(filename));
		String s = "";
		Scanner scan = new Scanner(s);
		while(true){
			try{
				s = in.readLine();
				scan = new Scanner(s);
				String word = scan.next();
				String definition = "";
				scan.useDelimiter(";");
				
				//attempt to use delimiters to break up definitions to read random definition
				
				//int noDefinitions = 0;
				while(true){
					String temp = "";
					try{
						String nextBit = scan.next();
						definition = definition +  nextBit + " ";						
					}catch(NoSuchElementException nsee){
						break;
					}
					temp = definition;
					definitions.add(temp);
					//noDefinitions++;
				}		
				definitions.add(definition);
				Word wordObj = new Word(word.trim(), definition.trim());		
				//Word wordObj = new Word(word, definitions, noDefinitions);	
				words.add(wordObj);				
			}catch(NullPointerException e){
				break;
			}
		}	
		in.close();
		scan.close();
		return words;
	}
}
