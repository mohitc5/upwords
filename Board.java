import java.util.Arrays;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Board {
    private HashMap<String, Space> board;
    private ArrayList<String> wordbank;
    private HashMap<String,Integer> HorizontalWord;
    private HashMap<String,Integer> VerticalWord;


    public Board() {
       this.board = new HashMap<String, Space>();
       for(int i = 1; i<9;i++){
            for(int a = 1; a<9;a++){
                this.board.put(Integer.toString(i)+Integer.toString(a), new Space());
            }
       } 
       this.wordbank = AddWords();
       this.HorizontalWord = new HashMap<String, Integer>();
       this.VerticalWord = new HashMap<String, Integer>();
    }
    
    public ArrayList<String> AddWords(){
        ArrayList<String> words = new ArrayList<String>();
        try{
            File file = new File("words.txt");
            Scanner input = new Scanner(file);
            while (input.hasNext()) {
                words.add(input.next());
            }
            input.close();
        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }
        return words;
  }

    
    public String toString(){
        String art ="   "+"-".repeat(33);
        art += "\n   | A | B | C | D | E | F | G | H |";
        int i = 1;
            for (int row = 1; row < 9; row ++){
                    art+= "\n   |---|---|---|---|---|---|---|---|\n "+Integer.valueOf(i)+" ";
                for (int col = 1; col < 9; col ++){
                   art += "|" + board.get(Integer.toString(col)+Integer.toString(row));
                }
                art+="|";
                i++;
            }
            art+="\n   "+"-".repeat(33);
        return art;
    }

    public void PlaceALetter(String location, String letter)throws Exception{
        location = getnumber(location.substring(0,1))+location.substring(1);
        if(location.matches("[1-8][1-8]") == false | letter.matches("[A-Z]|Qu") == false){
            throw new Exception("Not a valid input");
        }
        Space space = this.board.get(location);
        if(space.getHeight() == 5){
            throw new Exception();
        }
        if(space.getLetter().equals(letter)){
            throw new Exception();
        }
        space.setLetter(letter);   
    }
    public String getnumber(String other){
        other = other.toLowerCase();
        if(other.equals("a")){
            return "1";
        }
        if(other.equals("b")){
            return "2";
        }
        if(other.equals("c")){
            return "3";
        }
        if(other.equals("d")){
            return "4";
        }
        if(other.equals("e")){
            return "5";
        }
        if(other.equals("f")){
            return "6";
        }
        if(other.equals("g")){
            return "7";
        } if(other.equals("h")){
        return "8";
        }
        return "9";
    }

    public boolean checkHorizontalWords(){
        this.HorizontalWord = new HashMap<String, Integer>();
        int score = 0;
        String word = "";
        int check = 0;
        for(int i = 1; i<9;i++){
            for(int a = 1; a<9;a++){
                String key = Integer.toString(i)+Integer.toString(a);
                if((board.get(key)).getLetter().equals("") &&  word.equals("") == false){
                    if(wordbank.contains(word.toLowerCase()) == false && word.matches("[A-Z]|Qu") == false ){
                        return false;
                    }
                    if(word.contains("Qu")&& check == 0){
                        score+=2;
                    }
                    if(word.matches("[A-Z]|Qu")==false){
                        HorizontalWord.put(word,score);
                    }
                    score = 0;
                    word = "";
                }
                if((board.get(key)).getLetter().equals("") == false){
                    word += (board.get(key)).getLetter();
                    score+=(board.get(key)).getHeight()-1+2;
                }
            }
            if(wordbank.contains(word.toLowerCase()) == false && word.matches("[A-Z]|Qu|") == false ){
                return false;
            }
            word = "";
        }
        return true;
    }
    public boolean checkVerticalWords(){
        this.VerticalWord = new HashMap<String, Integer>();
        int score = 0;
        String word = "";
        int check = 0;
        for(int a = 1; a<9;a++){
            for(int i = 1; i<9;i++){
                String key = Integer.toString(i)+Integer.toString(a);
                if((board.get(key)).getLetter().equals("") &&  word.equals("") == false){
                    if(wordbank.contains(word.toLowerCase()) == false && word.matches("[A-Z]|Qu") == false ){
                        return false;
                    }
                    if(word.contains("Qu")&& check == 0){
                        score+=2;
                    }
                    if(word.matches("[A-Z]|Qu")==false){
                        VerticalWord.put(word,score);
                    }
                    score = 0;
                    word = "";
                }
                if((board.get(key)).getLetter().equals("") == false){
                    word += (board.get(key)).getLetter();
                    score+=(board.get(key)).getHeight()-1+2;
                    
                }
            }
            if(wordbank.contains(word.toLowerCase()) == false && word.matches("[A-Z]|Qu|") == false ){
                return false;
            }
            word = "";
        }
        return true;
    }

    public Board clone(){
        Board clone = new Board();
        HashMap<String, Space> boardclone = clone.getBoard();
        for(int i = 1; i<9;i++){
            for(int a = 1; a<9;a++){
                boardclone.put(Integer.toString(i)+Integer.toString(a), (board.get(Integer.toString(i)+Integer.toString(a))).clone());
            }
       } 
    return clone;
    }

    public boolean BoardisEmpty(){
        for(int i = 1; i<9;i++){
            for(int a = 1; a<9;a++){
                Space space = board.get(Integer.toString(i)+Integer.toString(a));
                if(space.getHeight()>0){
                    return false;
                }
            }
       } 
       return true;
    }

    public boolean BoardHasOneLetter(){
        int check = 0;
        for(int i = 1; i<9;i++){
            for(int a = 1; a<9;a++){
                Space space = board.get(Integer.toString(i)+Integer.toString(a));
                if(space.getHeight()>0){
                    check++;
                }
            }
       } 
       if(check == 1){
        return true;
       }
       return false;
    }

    public boolean equals(Board other){
        HashMap<String, Space> thisboard = this.getBoard();
        HashMap<String, Space> otherboard = other.getBoard();
        for(int i = 1; i<9;i++){
            for(int a = 1; a<9;a++){
                String key = Integer.toString(i) +Integer.toString(a);
                if((thisboard.get(key)).getLetter().equals((otherboard.get(key)).getLetter()) == false){
                    return false;
                }
            }
       }
       return true;
    }
    public static void main(String[] args) {
        Board g = new Board();
        try{
            g.PlaceALetter("a1","Qu");
            g.PlaceALetter("a2","I");
            g.PlaceALetter("a3","T");     
            g.PlaceALetter("b1","I");
            g.PlaceALetter("c1","T");
            g.PlaceALetter("d1","E");     
      
        }
        catch(Exception a){
            System.out.println("akdhb");
        }
        System.out.println(g);
        System.out.println(g.checkHorizontalWords());
        System.out.println(g.checkVerticalWords());
        System.out.println(g.HorizontalWord);
        if(g.HorizontalWord.get("ajhdyg") == null){
        }
    }
    public HashMap<String, Space> getBoard() {
        return this.board;
    }
    public void setBoard(HashMap<String, Space> board) {
        this.board = board;
    }


    public ArrayList<String> getWordBank() {
        return this.wordbank;
    }


    public void setWordBank(ArrayList<String> words) {
        this.wordbank = words;
    }

    public ArrayList<String> getWordbank() {
        return wordbank;
    }

    public void setWordbank(ArrayList<String> wordbank) {
        this.wordbank = wordbank;
    }

    public HashMap<String, Integer> getHorizontalWord() {
        return HorizontalWord;
    }

    public void setHorizontalWord(HashMap<String, Integer> horizontalWord) {
        HorizontalWord = horizontalWord;
    }

    public HashMap<String, Integer> getVerticalWord() {
        return VerticalWord;
    }

    public void setVerticalWord(HashMap<String, Integer> verticalWord) {
        VerticalWord = verticalWord;
    }
}
