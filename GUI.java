import java.awt.*;
import javax.swing.*;
import javax.swing.text.html.Option;

import java.util.ArrayList;
import java.util.Scanner;

class GUI extends JFrame{

    public GUI(){
        
        super("Upwords");
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);
                Game game = new Game(2);
         JLabel Uppertext = new JLabel("Player 1s turn      Score: "+game.getPlayers()[0].getScore(), SwingConstants.LEFT);
            JLabel Bottomtext = new JLabel("                ", SwingConstants.LEFT);
        Grid grid = new Grid(Uppertext,Bottomtext,game);
        mainPanel.add(Uppertext,BorderLayout.PAGE_START);
        mainPanel.add(grid, BorderLayout.CENTER);
        mainPanel.add(Bottomtext, BorderLayout.PAGE_END);

        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new GUI();
    }
   
   
}



class Grid extends JPanel {
    Game game;
    JButton[][] squares = new JButton[8][8];
    JButton[] pieces = new JButton[7];
    JButton[] options = new JButton[4];
    JLabel upperText;
    JLabel bottomText;
    Board board;
    Board cloneBoard;
    ArrayList<String> CurrrentPlayerhand;
    int turn;
    String CurrentLetter;
    String CurrentLocation;
    int CurrentHeight;


    public Grid(JLabel upperText, JLabel bottomText,Game games) {
        this.game = games;
         CurrrentPlayerhand = game.getPlayers()[turn].getHand();
        this.game.setCurrrentPlayerhand(CurrrentPlayerhand);
        this.upperText = upperText;
        this.bottomText = bottomText;
        setLayout(new BorderLayout());
        board = game.getGameboard();
        cloneBoard = new Board();
        turn = game.getPlayerturn();
        CurrentLetter ="";
        CurrentLocation = "";
        CurrentHeight = 0;
        initializeBoard();
        initializeBottomrow(); 
    }
    
    public void initializeBoard(){
        System.out.println(board);
        JPanel gridPanel = new JPanel(new GridLayout(8, 8));
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squares[i][j] = new JButton();
                String letter = (((board.getBoard()).get(Integer.toString(j+1)+Integer.toString(i+1)))).getLetter();
                if(board.getBoard().get(Integer.toString(j+1)+Integer.toString(i+1)).getHeight()>0){
                    squares[i][j].setText(letter+board.getBoard().get(Integer.toString(j+1)+Integer.toString(i+1)).getHeight());
                }
                else{
                    squares[i][j].setText("");
                }
                gridPanel.add(squares[i][j]);
                addActionListenerBoard(i,j);
            }
        }
        add(gridPanel, BorderLayout.CENTER);
    }
    
    public void initializeBottomrow(){
        JPanel Bottomrow = new JPanel();
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        for (int j = 0; j < 7; j++) {
            pieces[j] = new JButton();
            pieces[j].setPreferredSize(new Dimension(70, 50)); 
            if(j<CurrrentPlayerhand.size()){
                pieces[j].setText(CurrrentPlayerhand.get(j)); 
                System.out.println(pieces[j].getText());
                pieces[j].repaint();
            }
            else{
                   pieces[j].setText("?"); 
            }
            rowPanel.add(pieces[j]);
            addActionListenerHand(j);   
        }
        Bottomrow.add(rowPanel);
        JPanel bowPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        String[] option = {"Skip","End Turn","Restart","Exchange a peice"};
        for (int j = 0; j < 4; j++) {
            options[j] = new JButton();
            options[j].setPreferredSize(new Dimension(150, 50)); 
            options[j].setText(option[j]);
            bowPanel.add(options[j]);
            addActionListenerOption(j);
        }
            Bottomrow.repaint();
        Bottomrow.add(bowPanel);
        add(Bottomrow, BorderLayout.PAGE_END);
    }

    public void addActionListenerBoard(int i, int j){
        squares[i][j].addActionListener(e -> actionPerformedBoard(i, j));
    }

  
    public void actionPerformedBoard(int i, int j){
        if(CurrentLetter.equals("")){
            System.out.println(squares[i][j].getText());
            bottomText.setText("Must click a letter before clicking a space");
        }
        else{
            String location = numberToLetter(j+1)+Integer.toString(i+1);
            game.setCurrentPlacedLetter(CurrentLetter);
            game.setCurrentPlacedLocation(location);
        try{
                game.LetterinHand();
                game.SurroundingisEmpty(cloneBoard);
                game.checkplayedlocations();
                cloneBoard.PlaceALetter(location, CurrentLetter);
                location = game.gameboard.getnumber(location.substring(0,1))+location.substring(1);
                game.TakenOverLetters+= game.gameboard.getBoard().get(location).getLetter();
                System.out.println(cloneBoard);
            }
            catch(Exception a){
                bottomText.setText(a.getMessage());
                return;
            }  
            CurrentHeight = cloneBoard.getBoard().get(location).getHeight();
            squares[i][j].setText(CurrentLetter+CurrentHeight);
            game.removeletter();
            CurrentLetter = "";
            CurrentLocation = "";
            for(int a = 0;i<CurrrentPlayerhand.size();i++){
                if(CurrrentPlayerhand.get(a).equals(CurrentLetter)){
                    CurrrentPlayerhand.remove(a);
                    break;
                }
            }
            updateHand();
            bottomText.setText("  ");
        }

    }
    public void addActionListenerHand(int i){
        pieces[i].addActionListener(e -> actionPerformedHand(i));
    }

  
    public void actionPerformedHand(int i){
        CurrentLetter = pieces[i].getText();
        bottomText.setText("Current letter is "+CurrentLetter);  
    }

    public void addActionListenerOption(int i){
        options[i].addActionListener(e -> actionPerformedOption(i));
    }

  
    public void actionPerformedOption(int i){
       if(options[i].getText().equals("Restart")) {
            playerOptionOne();
       } 
       if(options[i].getText().equals("End Turn")) {
            playerOptionTwo();
       }
       
    }
    public void playerOptionOne() {
    while (game.enteredLetters.size() > 0) {
        game.CurrrentPlayerhand.add(game.enteredLetters.get(0));
        game.enteredLetters.remove(0);
        System.out.println(game.enteredLetters);
        System.out.println(game.CurrrentPlayerhand);
    }
    resetvariables();
    initializeBottomrow();
    initializeBoard(); 
    bottomText.setText("Restarted Board");
}

public void resetvariables(){
    game.enteredLocations = new ArrayList<String>();
    game.currentPlacedLetter ="";
    game.currentPlacedLocation = "";
    CurrrentPlayerhand = game.CurrrentPlayerhand;
    game.enteredLetters = new ArrayList<String>();
    cloneBoard = game.gameboard.clone();
    board = game.getGameboard();
}


private void updateHand() {
    for (int i = 0; i < 7; i++) {
        pieces[i].setText("");
        if (i < CurrrentPlayerhand.size()) {
            pieces[i].setText(CurrrentPlayerhand.get(i));
        }
    }
}


    public void playerOptionTwo(){
                if(cloneBoard.equals(game.gameboard)){
                    bottomText.setText("No changes to the board have been made can't finish turn");
                }
                try{
                    game.BoardisValid(cloneBoard);
                    game.gameboard = cloneBoard.clone();
                    System.out.println(game.gameboard);
                    System.out.println(cloneBoard);
                    while(game.CurrrentPlayerhand.size()<7){
                        game.CurrrentPlayerhand.add(game.letters.Draw1Letter());
                    }
                    game.NumberofSkips = 0;  
                    if(game.getPlayerturn() == 1){
                        game.setPlayerturn(0);
                    }
                    else if(game.getPlayerturn() == 0){
                        game.setPlayerturn(1);
                    }
                    turn = game.getPlayerturn();
                        game.CurrrentPlayerhand = game.players[turn].getHand(); 
                        resetvariables();
                        System.out.println(game.CurrrentPlayerhand);
                        System.out.println(CurrrentPlayerhand);    
                        upperText.setText("Player "+(turn+1)+"s turn      Score: "+game.getPlayers()[turn].getScore());
                        updateHand();
                }
                catch(Exception a){
                    bottomText.setText(a.getMessage());
                }
                
    }
    

    public String numberToLetter(int num){
        if(num == 1){
            return "a";
        }if(num == 2){
            return "b";
        }if(num == 3){
            return "c";
        }if(num == 4){
            return "d";
        }if(num == 5){
            return "e";
        }if(num == 6){
            return "f";
        }if(num == 7){
            return "g";
        }
            return "h";
        
    }
}

