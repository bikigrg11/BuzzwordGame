package data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class wordController {
    ArrayList<String> foundWords;
    public wordController(){}

    public  boolean wordSearch(Set<String> dict, char[][] gameBoard){
        foundWords= new ArrayList<>();
        boolean[][] inBoard = new boolean[gameBoard.length][gameBoard[0].length];
        for(int i=0; i<gameBoard.length; i++){
            for(int j=0; j<gameBoard[0].length; j++){
                search(dict, gameBoard, i, j, "", inBoard);
            }
        }
        return true;
    }

    private  void search(Set<String> dict, char[][] gameBoard, int colNum, int rowNum, String curr, boolean[][] inBoard){
        curr += gameBoard[colNum][rowNum];
        inBoard[colNum][rowNum] = true;

        if(dict.contains(curr)){
            if(!foundWords.contains(curr)){
                foundWords.add(curr);

                System.out.println(curr);
            }else{
                // do nothing
            }
        }

        List<GameList> nextWords = getNextWords(gameBoard, colNum, rowNum);

        for(GameList sideWords : nextWords){
            if(!inBoard[sideWords.col][sideWords.row]){
                search(dict, gameBoard, sideWords.col, sideWords.row, curr, inBoard);
            }
        }
        inBoard[colNum][rowNum] = false;
    }

    private  List<GameList> getNextWords(char[][] gameBoard, int colNum, int rowNum){
        List<GameList> nextWords = new LinkedList<GameList>();
        int colB = ((colNum - 1) > -1) ? (colNum - 1) : colNum;
        int rowB = ((rowNum - 1) > -1) ? (rowNum - 1) : rowNum;
        int colE = ((colNum + 1) < gameBoard.length) ? (colNum + 1) : colNum;
        int rowE = ((rowNum + 1) < gameBoard[0].length) ? (rowNum + 1) : rowNum;

        for(int i = colB; i <= colE; i++){
            for(int j = rowB; j <= rowE; j++){
                nextWords.add(new GameList(i, j));
            }
        }
        return nextWords;
    }

    private  class GameList {
        public int col;
        public int row;

        public GameList(int col, int row){
            this.col = col;
            this.row = row;
        }
    }

    public ArrayList getTotalWords(){
        return foundWords;
    }
}