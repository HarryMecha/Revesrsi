import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Write a description of class Reversi here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Reversi
{
    protected static Board ReversiBoard; //This is the board
    Container contentPane; //This is the Panels containter
    private static int BoardSize; //This is an integer that determine the board size
    private Menu menu; //This hold the dropdown menu
    private JPanel Rboard; //This is the Panel that holds the ReversiBoard board object
    private JFrame frame; //This is the main frame
    private JLabel[] player1Labels; //This is an array of Labels contained the in the player1 panel
    private JLabel[] player2Labels; //This is an array of Labels contained the in the player2 panel
    private JPanel playerPanel; //This is the player panel that holds player details and buttons 
    private JPanel playerPanel1; //This is the panel that holds all of player 1's data
    private JPanel playerPanel2;//This is the panel that holds all of player 2's data
    private JLabel turncounter; //This is a label that shows's who's turn it currently is
    protected static int turn; //This is which turn it is
    private static int player1Wins; //This is the number of wins player 1 has
    private static int player2Wins; //This is the number of wins player 2 has
    private static String player1Name; //This is player 1's inputted name
    private static String player2Name; //This is player 2's inputted name
    private static ArrayList<String> loadedDetails;
    public Reversi()
    {
        frame = new JFrame("Reversi");
        menu = new Menu();
        BoardSize = 8;
        ReversiBoard = new Board(); 
        player1Labels = new JLabel [3];
        player2Labels = new JLabel [3];
        playerPanel = new JPanel();
        Rboard = new JPanel();
        turn = 0;
        player1Wins = 0;
        player2Wins =0;
        makeFrame();
        while (turn > -1){
            textUpdater();
            switch(turn){
                case(1):
                while(getTurn() == 1){
                    textUpdater();
                }
                break;
                case(2):
                while(getTurn() == 2){
                    textUpdater();
                }
                break;
                case(3):
                break;
                case(5):
                newBoard();
                turn = 3;
                break;
                case(6):
                loadDetails(loadedDetails);
                break;
                default:
                if(turn > 2){
                    int white = getWhitePieces();
                    int black = getBlackPieces();
                    if (white>black){
                        JOptionPane.showMessageDialog(null,"White has won!");
                        player1Wins++;
                    }
                    if (white<black){
                        JOptionPane.showMessageDialog(null,"Black has won!");
                        player2Wins++;
                    }
                    turn =3;
                }
                if(turn == 0){
                    player1Name = JOptionPane.showInputDialog("Input Player1 Name");
                    player2Name = JOptionPane.showInputDialog("Input Player2 Name");
                    textUpdater();
                    turn = 3;

                }
                break;
            }
        }
    }
    
    //This sets up all the elements in the frame
    protected void makeFrame(){
        frame.setJMenuBar(menu.getMenuBar());
        frame.setLayout(new BorderLayout());
        contentPane = frame.getContentPane();
        contentPane.add(ReversiBoard.getPanel(),BorderLayout.CENTER);
        contentPane.add(makePlayerPanel(),BorderLayout.EAST);
        frame.setPreferredSize(new Dimension(1000, 1000));
        frame.pack();
        frame.setVisible(true);
    }

    //This sets up a new board once a new board size has been given
    protected void newBoard(){
        contentPane.remove(ReversiBoard.getPanel());
        ReversiBoard = new Board();
        contentPane.add(ReversiBoard.getPanel(),BorderLayout.CENTER);
        turn = 1;
    }

    //This will load details from the saved text document
    public void loadDetails(ArrayList<String> arrayL){
        
    Reversi.setPlayer1Name(arrayL.get(0));
    Reversi.setPlayer1Wins(Integer.parseInt(arrayL.get(1)));
    Reversi.setPlayer2Name(arrayL.get(2));
    Reversi.setPlayer2Wins(Integer.parseInt(arrayL.get(3)));
    Reversi.setBoardSize(Integer.parseInt(arrayL.get(5)));
    newBoard();
    int index = 6;
    for(int i = 0; i < Reversi.getBoardSize(); i++){
                for(int j = 0; j < Reversi.getBoardSize(); j++){
                     Reversi.getBoard().getButton(i,j).setText(arrayL.get(index));
                     index++;
                }
            }
    }
    
    
    //This sets up the player panel
    private JPanel makePlayerPanel(){
        turncounter = new JLabel("");
        JButton playButton = new JButton("Play");
        //This gives an action to the play button
        playButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    if((getTurn() == 3)&&(getPlayer1Name() != "" && getPlayer2Name() != "")){
                        ReversiBoard.boardSetup();
                        turn = 1;
                        textUpdater();
                    }
                }
            }); 

        JButton newGameButton = new JButton("New Game");
        //This gives an action to the new game button
        newGameButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    ReversiBoard.boardSetup();
                    turn = 1;
                    textUpdater();
                }

            }); 
        playerPanel = new JPanel();
        playerPanel.setLayout(new GridLayout(3,1));
        playerPanel1 = new JPanel();
        playerPanel2 = new JPanel();

        playerPanel1.setLayout(new BoxLayout(playerPanel1, BoxLayout.PAGE_AXIS));
        playerPanel1.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        playerPanel2.setLayout(new BoxLayout(playerPanel2, BoxLayout.PAGE_AXIS));
        playerPanel2.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel player1Name = new JLabel();
        JLabel player1Wins = new JLabel();
        JLabel player1Pieces = new JLabel();

        player1Labels[0] = player1Name;
        playerPanel1.add(player1Labels[0]);
        player1Labels[1] = player1Wins;
        playerPanel1.add(player1Labels[1]);
        player1Labels[2] = player1Pieces;
        playerPanel1.add(player1Labels[2]);

        JLabel player2Name = new JLabel();
        JLabel player2Wins = new JLabel();
        JLabel player2Pieces = new JLabel();

        player2Labels[0] = player2Name;
        playerPanel2.add(player2Labels[0]);
        player2Labels[1] = player2Wins;
        playerPanel2.add(player2Labels[1]);
        player2Labels[2] = player2Pieces;
        playerPanel2.add(player2Labels[2]);

        playerPanel.add(playerPanel1);
        
        JPanel buttonPanel= new JPanel();
        playerPanel.add(buttonPanel);
        buttonPanel.add(turncounter);
        buttonPanel.add(playButton);
        buttonPanel.add(newGameButton);
        playerPanel.add(playerPanel2);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        textUpdater();
        return playerPanel;
    }

    //This fetches the amount of white pieces on the board
    public static int getWhitePieces(){
        int pieceNumber = 0;
        for(int i=0; i<BoardSize; i++){
            for(int j=0; j<BoardSize; j++){
                if(ReversiBoard.getButton(i,j).getText() == "W"){
                    pieceNumber++;
                }
            }
        }
        return pieceNumber;
    }

    //This fetches the amount of black pieces on the board
    public static int getBlackPieces(){
        int pieceNumber = 0;
        for(int i=0; i<BoardSize; i++){
            for(int j=0; j<BoardSize; j++){
                if(ReversiBoard.getButton(i,j).getText() == "B"){
                    pieceNumber++;
                }
            }
        }
        return pieceNumber;
    }

    
    //This fetches player 1's name
    public static String getPlayer1Name(){
        return player1Name;
    }
    
    public static void setPlayer1Name(String name){
        player1Name = name;
    }

    //This fetches player 2's name
    public static String getPlayer2Name(){
        return player2Name;
    }
    
    public static void setPlayer2Name(String name){
        player2Name = name;
    }

    //This fetches player 1's wins
    public static int getPlayer1Wins(){
        return player1Wins;
    }
    
    public static void setPlayer1Wins(int wins){
        player1Wins = wins;
    }

    //This fetches player 2's wins
    public static int getPlayer2Wins(){
        return player2Wins;
    }
    
    public static void setPlayer2Wins(int wins){
        player2Wins = wins;
    }

    //This sets the new bard size to the users input
    public static void setBoardSize(int bs){
        BoardSize = bs;   
    }

    //This fetches the boardsize
    public static int getBoardSize(){
        return BoardSize;   
    }

    //This fetches the board object
    public static Board getBoard(){
        return ReversiBoard;
    }

    public static void setLoadedDetails(ArrayList<String> details){
        loadedDetails = details;
    }
    
    //This updates the text in the player panel
    public void textUpdater(){
        if(turn == 1){
            turncounter.setText("White");
        }else
            turncounter.setText("Black");
        player1Labels[0].setText("Name: "+getPlayer1Name());
        player1Labels[1].setText("Wins:"+getPlayer1Wins());
        player1Labels[2].setText("Pieces on board: "+getWhitePieces());
        player2Labels[0].setText("Name: "+getPlayer2Name());
        player2Labels[1].setText("Wins:"+getPlayer2Wins());
        player2Labels[2].setText("Pieces on board: "+getBlackPieces());
    }

    //This fetches the turn
    public static int getTurn(){
        return turn;
    }

    //This is used to change the turn
    public static void setTurn(int t){
        turn = t;
    }   
}
