import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Menu
{
    private static JMenuBar menuBar; //This is the menubar object
    private JMenu menu; //This is the menu object
    private JMenuItem changeBoardSize, Saving, Loading ; //This is the option the user can select to change board size
    private int BoardSize; //This is the board size itself

    public Menu()
    {
        menuBar = new JMenuBar();
        menu = new JMenu("Menu");
        changeBoardSize = new JMenuItem("Change Board Size");
        changeBoardSize.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { //This is a listener for the menu option, it will ask the user for a board size and this will be parsed the the reversi class where it will create a new board

                    String boardSize = JOptionPane.showInputDialog("How big do you want your board? Give 1 number");
                    Reversi.setBoardSize(Integer.parseInt(boardSize));
                    Reversi.setTurn(5);
                }

            });
        menu.add(changeBoardSize);
        Saving = new JMenuItem("Save");
        Saving.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { //This is a listener for the menu option, it will save all the details about the game and export them into a text document
                    WriteToFile();
                }
            });
        menu.add(Saving);
        Loading = new JMenuItem("Load");
        Loading.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { //This is a listener for the menu option, it will take the saved data and then place in into the program
                    Reversi.setLoadedDetails(ReadFromFile());
                    Reversi.setTurn(6);
                }
            });
        menu.add(Loading);
        menuBar.add(menu);
    }

    //This will get the interger board size
    public int getBoardSize(){
        return BoardSize;   
    }

    //This will get the menubar object
    public static JMenuBar getMenuBar(){
        return menuBar;
    }

    //This is the method that will write out the important data into a text document
    public void WriteToFile(){
        File file = new File("ReversiOut.txt");
        try(FileWriter writer = new FileWriter(file)){
            String[] Player1 = new String[2];
            Player1[0] = Reversi.getPlayer1Name();
            Player1[1] = Integer.toString(Reversi.getPlayer1Wins());
            for(int i = 0; i < 2; i++) {
                writer.write(Player1[i].toString());
                writer.write('\n');
            }
            String[] Player2 = new String[2];
            Player2[0] = Reversi.getPlayer2Name();
            Player2[1] = Integer.toString(Reversi.getPlayer2Wins());
            for(int i = 0; i < 2; i++) {
                writer.write(Player2[i].toString());
                writer.write('\n');
            }
            writer.write(Integer.toString(Reversi.getTurn()));
            writer.write('\n');
            writer.write(Integer.toString(Reversi.getBoardSize()));
            writer.write('\n');
            String[][] ReversiBoardStates = new String[Reversi.getBoardSize()][Reversi.getBoardSize()];
            for(int i = 0; i < Reversi.getBoardSize(); i++){
                for(int j = 0; j < Reversi.getBoardSize(); j++){
                    ReversiBoardStates[i][j] = Reversi.getBoard().getButton(i,j).getText();
                }
            }
            System.out.println(Reversi.getBoard().getButton(0,0).getText());
            for(int i = 0; i < Reversi.getBoardSize(); i++){
                for(int j = 0; j < Reversi.getBoardSize(); j++){
                    writer.write(ReversiBoardStates[i][j]);
                    writer.write('\n');
                }
            }
        }
        catch(IOException e){
            System.err.println("There was a problem writing to " + file);
        }
    }
    
    //This is the method that will read the important data into a text document and convert it to an arrayList that will be read and then be used to set the corresponding fields
    public ArrayList<String> ReadFromFile(){
     ArrayList<String> readOuts = new ArrayList<String>();
        try {
            int i = 0;
      File myObj = new File("ReversiOut.txt");
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
          String data = myReader.nextLine();
        readOuts.add(data);
        i++;
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    return readOuts;
    }
    
    
}
