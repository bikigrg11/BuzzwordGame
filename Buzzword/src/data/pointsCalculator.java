package data;

import ui.AppMessageDialogSingleton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by BG on 12/1/16.
 */
public class pointsCalculator {
    private  char[][] gameBoard;
    wordController bGamedata;
    HashSet<String> dict;
    boolean flag=true;



    public pointsCalculator(String gameB) throws IOException {
        gameBoard= new char[4][4];
        boardReading(gameB);
        bGamedata= new wordController();
        dict= new HashSet<String>();
    }


    public  void boardReading(final String boardFile) throws IOException {
        String strBoard=boardFile;
        strBoard = strBoard.toLowerCase();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                gameBoard[i][j] = strBoard.toCharArray()[i * 4 + j];
            }
        }
    }

    public  void readingDict(String gamemodeString) throws IOException {
        String dictFile="";
        switch (gamemodeString) {
            case "ANIMALS":
                dictFile = "/Users/BG/Dropbox/BuzzwordGame/Buzzword/resources/words/dictionary.txt";
                break;
            case "DICTIONAIRY":
                dictFile = "/Users/BG/Dropbox/BuzzwordGame/Buzzword/resources/words/dictionary.txt";

                break;
            case "PEOPLE":
                dictFile = "/Users/BG/Dropbox/BuzzwordGame/Buzzword/resources/words/dictionary.txt";

                break;
            case "POKEMON":
                dictFile = "/Users/BG/Dropbox/BuzzwordGame/Buzzword/resources/words/dictionary.txt";
                break;
            default:
                AppMessageDialogSingleton wrongInput = AppMessageDialogSingleton.getSingleton();
                wrongInput.show("Bad or No File to Read", "Please check your path to the dictionary.");
                break;
        }
                // List<String> words = Files.readAllLines(new File(dictFile).toPath(), StandardCharsets.UTF_8);

        File data = new File(dictFile);
        Scanner scanner=null;
        int count = 0;
        try {
            scanner = new Scanner(data);
            while (scanner.hasNextLine()) {
               dict.add(scanner.nextLine().toLowerCase());
                count++;
            }
        } catch (FileNotFoundException fnfe) {}

    }

    public String toString() {
        String str = "";
        for (int r = 0; r < gameBoard.length; r++) {
            for (char ch : gameBoard[r]) {
                str += ch + " ";
            }
            if (r < gameBoard.length-1) {
                str += "\n";
            }
        }
        return str;
    }

    public ArrayList readData(String gameModeString) throws IOException {
        if(flag){
            readingDict(gameModeString);
            flag=false;
        }
        bGamedata.wordSearch(dict, gameBoard);
        return bGamedata.getTotalWords();
    }

}

