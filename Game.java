
import java.util.HashMap;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    public Board gameboard;
    public Player[] players;
    public Bag letters;
    public int playerturn;
    public String currentPlacedLocation;
    public String currentPlacedLetter;
    public ArrayList<String> CurrrentPlayerhand;
    public ArrayList<String> enteredLocations;
    public ArrayList<String> enteredLetters;
    public int NumberofSkips;
    public String TakenOverLetters;
    public HashMap<String,Integer> Highscores;


    public Game(int numberofPlayers) {
        enteredLetters = new ArrayList<String>();
        gameboard = new Board();
        letters = new Bag();
        playerturn = 0;
        players = new Player[numberofPlayers];
        for(int i = 0;i<numberofPlayers;i++){
            players[i] = new Player(letters);
        }
        currentPlacedLocation = "";
        currentPlacedLetter = "";
        CurrrentPlayerhand = new ArrayList<String>();
        enteredLocations = new ArrayList<String>();
        NumberofSkips = 0;
        TakenOverLetters = "";
        Highscores = AddHighscores();
    }
    public HashMap<String,Integer> AddHighscores(){
        HashMap<String,Integer> Highscore = new HashMap<String,Integer>();
        try{
            File file = new File("Highscore.txt");
            Scanner input = new Scanner(file);
            while (input.hasNext()) {
                String score= input.nextLine();
                if(Highscore.get(score.substring(0,2)) != null){
                    if(Highscore.get(score.substring(0,2))<Integer.valueOf(score.substring(3))){
                            Highscore.put(score.substring(0,2),Integer.valueOf(score.substring(3)));
                    }
                }
                else{
                    Highscore.put(score.substring(0,2),Integer.valueOf(score.substring(3)));
                }
            }
            input.close();
        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }
        return Highscore;
    }

    public void printHighscore(){
        ArrayList<String> scores = new ArrayList<String>();
        for(String a: Highscores.keySet()){
            int k = Highscores.get(a);
            int currentlength = scores.size();
            for(int i=0;i<scores.size();i++){
                if(Highscores.get(scores.get(i))<=k){
                    scores.add(i,a);
                    break;
                }
            }
            if(currentlength == scores.size()){
            scores.add(a);
            }
        }
        System.out.println("Top 10 Scores:");
        for(int i =0;i<10;i++){
            if(scores.size() == i){
                break;
            }
            System.out.println(scores.get(i)+ " "+Highscores.get(scores.get(i)));
        }
        return;
    }

    public void PlayGame(Scanner input) {
        while(NumberofSkips!=players.length){
            TakenOverLetters = "";
            if (playerturn == players.length){
                playerturn = 0;
            }
            CurrrentPlayerhand = players[playerturn].getHand();
            PlayLetters(input);
            playerturn++;
        }
        FinalScore(input);
        run();
    }

    public void FinalScore(Scanner input){
        int highestscore = 0;
        for(int i = 0; i<players.length;i++){
            int numberoftilesleft = players[i].getHand().size();
            if(players[i].getScore()-numberoftilesleft>=highestscore){
                highestscore = players[i].getScore()-numberoftilesleft;
                playerturn = i;
            }
        }
        System.out.println("The higehst score was "+highestscore+" this score was set by player "+(playerturn+1)+". Therefore player "+(playerturn+1)+" wins!!!!!!");
        String name = "";
        while(true){
        System.out.println("player "+(playerturn+1)+" write your initials in capital letters to track your highscore");
        name = input.nextLine();
            if (name.matches("[A-Z][A-Z]")){
                break;
            }
        }
        try{
            File file = new File("Highscore.txt");
            FileWriter writer = new FileWriter(file,true);
            writer.append("\n"+name +" "+highestscore );
            writer.close();
        }
        catch (IOException e) {
            System.out.println("File cannot be opened.");
        }
        return;
    }

    
    

    public void GetTurnOrder(){
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int highest = 27;
        int player = 0; 
        Bag bag = new Bag();
        for(int i = 0;i<players.length;i++){
            String letter = bag.Draw1Letter();
            System.out.println("Player "+Integer.toString(i+1)+" drew an "+letter);
            if(highest>alphabet.indexOf(letter)){
                highest = alphabet.indexOf(letter);
                player = i+1;
            }
        }
        System.out.println("Player "+ player+" was closest to A.");
        playerturn = player-1;
    }
    public void PlayLetters(Scanner input){
        currentPlacedLetter ="";
        currentPlacedLocation ="";
        enteredLocations = new ArrayList<String>();
        enteredLetters = new ArrayList<String>();
        Board cloneBoard = gameboard.clone();
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nPlayer "+Integer.toString(playerturn+1)+"s turn\nEnter anything to continue");
            String b = input.nextLine();
        while(CurrrentPlayerhand.size()>0){
            playeroption(input, cloneBoard);
            TryPlacingaLetter(input, cloneBoard);
            removeletter();    
        }
        CheckValidity(cloneBoard, input);
    }

    public void CheckValidity(Board cloneBoard, Scanner input){
        try{
            BoardisValid(cloneBoard);
            while(CurrrentPlayerhand.size()<7){
                CurrrentPlayerhand.add(letters.Draw1Letter());
            }
            gameboard = cloneBoard;
            NumberofSkips = 0;  
            playerturn++;
            PlayGame(input);
        }
        catch(Exception a){
            while(enteredLetters.size()>0){
                CurrrentPlayerhand.add(enteredLetters.get(0));
                enteredLetters.remove(0);
            }
            PlayLetters(input);
        }
       
    }



    public void playeroption(Scanner input, Board cloneBoard){
            String a ="";
            while(a.matches("1|2|3|4|5") == false){
                System.out.println(cloneBoard+"\n\nYour current hand is\n"+CurrrentPlayerhand);
                System.out.println("Current score is " +players[playerturn].getScore());
                System.out.println("Press 1 to restart, 2 to finish your turn, 3 to skip turn, 4 to continue, or 5 to exchange one of your peices.");
                a = input.nextLine();
            }
            if(a.equals("1")){
                playerOptionOne(input);
            }
            if (a.equals("2")){
            playerOptionTwo(input, cloneBoard); 
            }
            if(a.equals("5")){
                a = playerOptionFive(input); 
            }  
            if (a.equals("3")){
                playerOptionThree(input);
            }
           
    }

    public void playerOptionOne(Scanner input){
                while(enteredLetters.size()>0){
                    CurrrentPlayerhand.add(enteredLetters.get(0));
                    enteredLetters.remove(0);
                }
                PlayLetters(input);
    }
    public void playerOptionTwo(Scanner input,Board cloneBoard){
                if(cloneBoard.equals(gameboard)){
                    System.out.println("No changes to the board have been made can't finish turn");
                    playeroption(input, cloneBoard);
                }
                try{
                    BoardisValid(cloneBoard);
                    gameboard = cloneBoard;
                    while(CurrrentPlayerhand.size()<7){
                        CurrrentPlayerhand.add(letters.Draw1Letter());
                    }
                    NumberofSkips = 0;  
                    playerturn++;
                    PlayGame(input); 
                }
                catch(Exception a){
                    playeroption(input, cloneBoard);
                }
                
    }
    public void playerOptionThree(Scanner input){
               while(enteredLetters.size()>0){
                    CurrrentPlayerhand.add(enteredLetters.get(0));
                    enteredLetters.remove(0);
                }
                NumberofSkips++;
                playerturn++;
                PlayGame(input);
    }

    public String playerOptionFive(Scanner input){
               if(enteredLetters.size()>0){
                    System.out.println("Can't discard if you have played letters, you must restart.");
                   return "9";
                }
                else{
                    String letter = "";
                    while(CurrrentPlayerhand.contains(letter)==false){
                        System.out.println("Enter a letter from you hand.\n"+ CurrrentPlayerhand);
                        letter = input.nextLine();
                    }
                    CurrrentPlayerhand.remove(letter);
                    CurrrentPlayerhand.add(letters.Draw1Letter());
                    letters.add1Letter(letter);
                    System.out.println("Your new hand is\n"+ CurrrentPlayerhand);
                    return "3";
                }
    }
    


    public boolean BoardisValid(Board cloneBoard) throws Exception{
        int check = 0;
        if(enteredLocations.size()>1){
            if(enteredLocations.get(0).substring(0, 1).equals(enteredLocations.get(1).substring(0, 1))){
                check = 1;
            }
            else{
                check = 2;
            }
        }
        if(cloneBoard.checkHorizontalWords()&&cloneBoard.checkVerticalWords() && cloneBoard.BoardHasOneLetter() == false){
            getScore(cloneBoard, check);
            return true;
        }
        System.out.println("The board has an invalid word ");
        throw new Exception();
    }

    public void getScore(Board cloneBoard, int check) throws Exception{
         HashMap<String,Integer> HorizontalWords = cloneBoard.getHorizontalWord();
            HashMap<String,Integer> verticalWords = cloneBoard.getVerticalWord();
            if(checkiftakenoverword(check)){
                System.out.println("you have taken over a word, must restart.");
                throw new Exception();
            }
            for(String key:HorizontalWords.keySet()){
                if(gameboard.getHorizontalWord().get(key) == null){
                    
                    players[playerturn].addscore(HorizontalWords.get(key));
                }
            }
            for(String key:verticalWords.keySet()){
                if(gameboard.getVerticalWord().get(key) == null){
                    
                    players[playerturn].addscore(verticalWords.get(key));
                }
            }
            if(CurrrentPlayerhand.size() == 0){
                players[playerturn].addscore(20);
            }
            System.out.println("Current score is " +players[playerturn].getScore());
    }

    public boolean checkiftakenoverword(int check){
        for(String key:gameboard.getHorizontalWord().keySet()){
            if(check == 1){
                for(int i = 0;i<key.length();i++){
                    if(TakenOverLetters.contains(key.substring(i, i+1))){
                        key = key.substring(i+1);
                    }
                }
                if(key.equals("")){
                    return true;
                }
            }  
        }
        for(String key:gameboard.getVerticalWord().keySet()){
            if(check == 2){
                for(int i = 0;i<key.length();i++){
                    if(TakenOverLetters.contains(key.substring(i, i+1))){
                        key = key.substring(i+1);
                    }
                }
                if(key.equals("")){
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<String> removeletter(){
        for(int i = 0;i<CurrrentPlayerhand.size(); i++){
            if(CurrrentPlayerhand.get(i).equals(currentPlacedLetter)){
                CurrrentPlayerhand.remove(i);
                enteredLetters.add(currentPlacedLetter);
                break;
            }
        }
        return enteredLetters;
    }

    public void LetterinHand()throws Exception{
        int check = 0;
        for(int i = 0;i<CurrrentPlayerhand.size();i++){
            if (CurrrentPlayerhand.get(i).equals(currentPlacedLetter)){
                check = 1;
            }
        } 
        if (check == 0){
            throw new Exception("Letter not in hand");
        }

    }
    public void checkplayedlocations()throws Exception{
        int check = 0;
        if(enteredLocations.size() <1){
            if(currentPlacedLocation.matches("d4|e4|d5|e5") == false && gameboard.BoardisEmpty()){
                throw new Exception("You need to start by placing a tile in the middle 4 spaces.");
            }
            enteredLocations.add(enteredLocations.size(),currentPlacedLocation);
            return;
        }
        String first = currentPlacedLocation;
        for(int i =0;i<enteredLocations.size();i++){
            String second = enteredLocations.get(i);
            if(first.equals(second)){
                System.out.println(first);
                System.out.println(second);
                throw new Exception("Cant play the same letter on the space.");
            }
            if(first.substring(0, 1).equals(second.substring(0,1))){
                if(check == 0){
                    check = 1;
                }
                else if (check == 2){
                    throw new Exception("You have to place a tile in the same row or column");
                }
            }
            else if(first.substring( 1).equals(second.substring(1))){
                if(check == 0){
                    check = 2;
                }
                else if (check ==1){
                    throw new Exception("You have to place a tile in the same row or column");
                }
            }
            else{
                throw new Exception("You have to place a tile in the same row or column");
            }
        }
        enteredLocations.add(enteredLocations.size(),currentPlacedLocation);
    }
    

    public boolean SurroundingisEmpty(Board cloneBoard)throws Exception{
        if(cloneBoard.BoardisEmpty()){
            return true;
        }
        int row = Integer.valueOf(getCurrentPlacedLocation().substring(1));
        int col = Integer.valueOf(cloneBoard.getnumber(getCurrentPlacedLocation().substring(0,1)));
        HashMap<String,Space> boards = cloneBoard.getBoard();
        for(int i = -1; i<2;i++ ){
            for(int a = -1; a<2;a++){        
                    if(i==0 && a!=0 || i!=0 && a==0){
                        if((row+a == 9||row+a == 0||col+i == 9||col+i == 0) == false){ 
                            if((boards.get(Integer.toString(col+i)+Integer.toString(row+a)).getHeight()>0)){
                                return false;
                            }
                        }
                    }
                    
            }
        }
        throw new Exception("Space has no neighboring peices");
    } 
    public void TryPlacingaLetter(Scanner input,Board cloneBoard){  
        String letter = "";
        String location = "";
        while(true){
            System.out.println(cloneBoard+"\n\nYour current hand is\n"+CurrrentPlayerhand);
            System.out.println("Enter the location of the peice.");
            location = input.nextLine();
            System.out.println("Enter the letter you want to put at the location.(Capital letters)");
            letter = input.nextLine();
            currentPlacedLetter = letter;
            currentPlacedLocation = location;
            try{
                LetterinHand();
                SurroundingisEmpty(cloneBoard);
                checkplayedlocations();
                cloneBoard.PlaceALetter(location, letter);
                location = gameboard.getnumber(location.substring(0,1))+location.substring(1);
                TakenOverLetters+= gameboard.getBoard().get(location).getLetter();
                break;
            }
            catch(Exception a){
                System.out.println(a.getMessage());
            }
        }
       
    }
 

    public  boolean CheckifValidLetter(String letter){
        if (letter.equals("Q")){
            return false;
        }
        return letter.matches("[A-Z]|Qu");
    }
    
    public  boolean CheckifValidLocation(String location){
        return location.matches("[a-j][1-9]|[a-j]10");
    }

    public String toString(){
        return gameboard.toString();
    }

    public static void run(){
        Game a = new Game(0); 
        a.printHighscore();
        Scanner input = new Scanner(System.in); 
        String numofplayers = "";
        while(numofplayers.matches("2|3|4") == false){
            System.out.println("Enter the number if players. (2-4)");
            numofplayers = input.nextLine();
        }
        Game game =  new Game(Integer.valueOf(numofplayers));
        game.GetTurnOrder();
        game.PlayGame(input);
    }
    public static void main(String[] args) throws Exception {
        run();   
    }

    public String getCurrentPlacedLocation() {
        return currentPlacedLocation;
    }
    public Board getGameboard() {
        return gameboard;
    }
    public void setGameboard(Board gameboard) {
        this.gameboard = gameboard;
    }
    public Player[] getPlayers() {
        return players;
    }
    public void setPlayers(Player[] players) {
        this.players = players;
    }
    public Bag getLetters() {
        return letters;
    }
    public void setLetters(Bag letters) {
        this.letters = letters;
    }
    public int getPlayerturn() {
        return playerturn;
    }
    public void setPlayerturn(int playerturn) {
        this.playerturn = playerturn;
    }
    public void setCurrentPlacedLocation(String currentPlacedLocation) {
        this.currentPlacedLocation = currentPlacedLocation;
    }
    public String getCurrentPlacedLetter() {
        return currentPlacedLetter;
    }
    public void setCurrentPlacedLetter(String currentPlacedLetter) {
        this.currentPlacedLetter = currentPlacedLetter;
    }
    public ArrayList<String> getCurrrentPlayerhand() {
        return CurrrentPlayerhand;
    }
    public void setCurrrentPlayerhand(ArrayList<String> currrentPlayerhand) {
        this.CurrrentPlayerhand = currrentPlayerhand;
    }
    public ArrayList<String> getEnteredLocations() {
        return enteredLocations;
    }
    public void setEnteredLocations(ArrayList<String> enteredLocations) {
        this.enteredLocations = enteredLocations;
    }
    public int getNumberofSkips() {
        return NumberofSkips;
    }
    public void setNumberofSkips(int numberofSkips) {
        NumberofSkips = numberofSkips;
    }
    public String getTakenOverLetters() {
        return TakenOverLetters;
    }
    public void setTakenOverLetters(String takenOverLetters) {
        TakenOverLetters = takenOverLetters;
    }
    public HashMap<String, Integer> getHighscores() {
        return Highscores;
    }
    public void setHighscores(HashMap<String, Integer> highscores) {
        Highscores = highscores;
    }  
}
