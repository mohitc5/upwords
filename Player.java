import java.util.Arrays;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;

public class Player {
    public int numberOfPlayers;
    public Bag letters;
    private ArrayList<String> hand;
    private int score;

    public Player(Bag bag) {
        numberOfPlayers = 0;
        letters = bag;
        this.hand = letters.randomHand();
        this.score = 0;
    }
    public void addscore(int a){
        this.score +=a;
    }
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public Bag getLetters() {
        return letters;
    }

    public void setLetters(Bag letters) {
        this.letters = letters;
    }



    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ArrayList<String> getHand() {
        return hand;
    }

    public void setHand(ArrayList<String> hand) {
        this.hand = hand;
    }
    public static void main(String[] args) {
        Bag bag = new Bag();
        Player a = new Player(bag);
        System.out.println(a.getHand());

        
    }
    
    




}
