import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class Board implements ActionListener
{
    private JPanel boardPanel; //This is the JPanel containing the buttons
    protected JButton[][] reversiBoard; //This is an array of buttons that act as a board
    private int maxBoardSize; //This is the maximumBoadSize
    private int column; //This is the column of the board used for finding elements in the array
    private int row; //This is the row of the board used for finding elements in the array

    //This is the constructor for the board
    public Board()
    {
        boardPanel = new JPanel();
        maxBoardSize = Reversi.getBoardSize();
        reversiBoard = new JButton[maxBoardSize][maxBoardSize];
        boardPanel.setLayout(new GridLayout(maxBoardSize,maxBoardSize));
        //This adds the buttons to each element of the array
        for(int i=0; i<maxBoardSize; i++){
            for(int j=0; j<maxBoardSize; j++){
                reversiBoard[i][j] = new JButton();
                reversiBoard[i][j].addActionListener(this);
            }
        }
        panelSetup(reversiBoard);
    }

    //This sets up the starting squares on the board
    public void boardSetup(){
        for(int i=0; i<maxBoardSize; i++){
            for(int j=0; j<maxBoardSize; j++){

                reversiBoard[i][j].setText(" ");

                if((i==(maxBoardSize/2 -1) || i ==maxBoardSize/2)&&(j==i)){
                    reversiBoard[i][j].setText("W");
                }
                if((i==(maxBoardSize/2 -1) && j ==maxBoardSize/2)||(i==maxBoardSize/2 && j==(maxBoardSize/2 -1))){
                    reversiBoard[i][j].setText("B");
                }
            }
        }
    }

    //This is used to check if the button can be pressed and handle what happens when is it or isn't an available button
    public void actionPerformed(ActionEvent e){  
        Object source = e.getSource();
        switch(Reversi.turn){
            //White's turn
            case(1):
            if (source instanceof JButton) {
                JButton button = (JButton) source;
                if(button.getText()==" "){
                    for(int i=0; i<maxBoardSize; i++){
                        for(int j=0; j<maxBoardSize; j++){
                            if(reversiBoard[i][j].equals(button)){
                                row = i;
                                column =j;
                            }    
                        }
                    }
                    if(FlipCheck(column, row, "W")){ //Checks if it can be flipped
                        button.setText("W");
                        Reversi.setTurn(2);
                    }
                    Boolean legalMove = canMakeMove(); //This will check if on the next turn the either player can make any valid moves
                    if(legalMove == null){
                        Reversi.setTurn(4); //This means that one of the players has one as no moves are valid and will call the method in the switch case where turn is 4
                    }else{
                        if(legalMove == false){ //This is means that black can't move so the turn is passed
                            JOptionPane.showMessageDialog(null,"Black cannot move");
                            Reversi.setTurn(1);
                        }
                    }

                }

            }
            break;
            //Black's turn
            case(2):
            if (source instanceof JButton) {
                JButton button = (JButton) source;
                if(button.getText()==" "){
                    for(int i=0; i<maxBoardSize; i++){
                        for(int j=0; j<maxBoardSize; j++){
                            if(reversiBoard[i][j].equals(button)){
                                row = i;
                                column =j;
                            }    
                        }
                    }
                    if(FlipCheck(column, row, "B")){
                        button.setText("B");
                        Reversi.setTurn(1);
                    }
                    Boolean legalMove = canMakeMove();
                    if(legalMove == null){
                        Reversi.setTurn(4);
                    }else{
                        if(legalMove == false){ //This is means that white can't move so the turn is passed
                            JOptionPane.showMessageDialog(null,"White cannot move");
                            Reversi.setTurn(2);
                        }
                    }

                }

                break;
            }
        }
    }

    //This will put the buttons into the array
    public void panelSetup(JButton[][] bs){
        for(int i=0; i<maxBoardSize; i++){
            for(int j=0; j<maxBoardSize; j++){
                boardPanel.add(reversiBoard[i][j]);
            }
        }
    }

    //This will return the JPanel
    public JPanel getPanel()
    {
        return boardPanel;
    }

    //This will return a specific button
    public JButton getButton(int i, int j){
        return reversiBoard[i][j];
    }

    //This is a function that makes sure that a player is able to make a move
    public  Boolean canMakeMove(){
        boolean isWLegal = false;
        boolean isBLegal = false;
        int turn = Reversi.turn;
        if(isBoardFull()) //Checks if board is full
            turn = 4;
        for(int i=0; i<maxBoardSize; i++){
            for(int j=0; j<maxBoardSize; j++){
                if(reversiBoard[i][j].getText()==" "){
                    row = i;
                    column =j;
                    if(moveCheck(row,column,"W")){
                        isWLegal = true;
                    }
                    if(moveCheck(row,column,"B")){
                        isBLegal = true;
                    }
                }    
            }
        }

        switch(turn){
            case(1):
            if(isWLegal){
                return true;
            }
            turn = 3;
            break;
            case(2):
            if(isBLegal){
                return true;
            }
            turn = 3;
            break;
            case(3):
            if(!(isWLegal)&&!(isBLegal)){
                Reversi.turn = 3;
                turn=0;
                return null;
            }
            if((Reversi.turn==1)&&(!isWLegal)){
                return false;
            }
            if((Reversi.turn==2)&&(!isBLegal)){
                return false;
            }
            break;
        }

        return null;
    }

    //This is a function used to check if the board is full of pieces
    public boolean isBoardFull(){
        for(int i=0; i<maxBoardSize; i++){
            for(int j=0; j<maxBoardSize; j++){
                if(reversiBoard[i][j].getText()==" "){
                    return false;
                }    
            }
        }
        return true;
    }

    //This is the main checking function that checks if either player has a valid move to make
    public boolean moveCheck(int c, int r, String colour){
        String opposite = " ";
        boolean changed = false;
        int difference = r - c;
        if(colour == "W")
            opposite = "B";
        if(colour == "B")
            opposite = "W";
        //This is for checking you can match left
        if(c!=0){
            if(reversiBoard[r][c-1].getText() == opposite){
                for(int i = (c-1); i > (-1); i--){
                    if(reversiBoard[r][i].getText() == colour){
                        int found = i;
                        for(int k =c; k >= found; k--){
                            changed = true;
                        }
                    }
                    if(reversiBoard[r][i].getText() == " "){
                        i = -1;
                    }
                }
            }
        }
        //This is for checking you can match right
        if(c!= (maxBoardSize -1)){
            if(reversiBoard[r][c+1].getText() == opposite){
                for(int i = (c+1); i < (maxBoardSize); i++){

                    if(reversiBoard[r][i].getText() == colour){
                        int found = i;
                        for(int k = c; k <= found; k++){
                            changed = true;
                        }
                    } if(reversiBoard[r][i].getText() == " "){
                        i = maxBoardSize;
                    }
                }
            }
        }
        //This is for checking you can match down
        if(r!=(maxBoardSize -1)){
            if(reversiBoard[r+1][c].getText() == opposite){
                for(int i = (r+1); i < (maxBoardSize); i++){

                    if(reversiBoard[i][c].getText() == colour){
                        int found = i;
                        for(int k = r; k <= found; k++){
                            changed = true;
                        }
                    }if(reversiBoard[i][c].getText() == " "){
                        i = maxBoardSize;
                    }
                }
            }
        }
        //This is for checking you can match up
        if(r!=0){
            if(reversiBoard[r-1][c].getText() == opposite){
                for(int i = (r-1); i > (-1); i--){

                    if(reversiBoard[i][c].getText() == colour){
                        int found = i;
                        for(int k = r; k >= found; k--){
                            changed = true;
                        }
                    }if(reversiBoard[i][c].getText() == " "){
                        i = -1;
                    }
                }
            }
        }
        //This is for checking you can match diagonally up left
        if(r==0||c==(maxBoardSize -1)){}
        else{
            if(reversiBoard[r-1][c+1].getText() == opposite){
                for(int i = (r-1); i > (-1); i--){
                    for(int j = (c+1); j < (maxBoardSize); j++){

                        if(reversiBoard[i][j].getText() == colour){
                            int foundRow = i;
                            int foundColumn =j;
                            for(int k = r-1; k >= foundRow; k--){
                                for(int l = c+1; l <= foundColumn; l++){
                                    return true; 
                                }
                            }
                        }else{
                            if(i-1 != -1)
                                i=i-1;
                        }
                    }
                }
            }
        }
        //This is for checking you can match diagonally up right
        if(r==(maxBoardSize -1)||c==(maxBoardSize -1)){}
        else{
            if(reversiBoard[r+1][c+1].getText() == opposite){
                for(int i = (r+1); i < (maxBoardSize); i++){
                    for(int j = (c+1); j < (maxBoardSize); j++){

                        if(reversiBoard[i][j].getText() == colour){
                            int foundRow = i;
                            int foundColumn =j;
                            for(int k = r+1; k <= foundRow; k++){
                                for(int l = c+1; l <= foundColumn; l++){
                                    return true; 
                                }
                            }
                        }
                        else{
                            if(i+1 != maxBoardSize)
                                i=i+1;
                        }
                    }
                }
            }

        }
        //This is for checking you can match diagonally down right
        if(r==(maxBoardSize -1)||c==0){}
        else{
            if(reversiBoard[r+1][c-1].getText() == opposite){
                for(int i = (r+1); i < (maxBoardSize); i++){
                    for(int j = (c-1); j > (-1); j--){

                        if(reversiBoard[i][j].getText() == colour){
                            int foundRow = i;
                            int foundColumn =j;
                            for(int k = r+1; k <= foundRow; k++){
                                for(int l = c-1; l >= foundColumn; l--){
                                    return true; 
                                }
                            }
                        }
                        else{
                            if(i+1 != maxBoardSize)
                                i=i+1;
                        }     
                    }
                }
            }
        }
        //This is for checking you can match diagonally down left
        if(r==0||c==0){}
        else{
            if(reversiBoard[r-1][c-1].getText() == opposite){
                for(int i = (r-1); i > (-1); i--){
                    for(int j = (c-1); j > (-1); j--){

                        if(reversiBoard[i][j].getText() == colour){
                            int foundRow = i;
                            int foundColumn =j;
                            for(int k = r-1; k >= foundRow; k--){
                                for(int l = c-1; l >= foundColumn; l--){
                                    return true; 
                                }
                            }
                        }else{
                            if(i-1 != -1)
                                i=i-1;
                        }
                    }
                }
            }
        }
        if(changed == true){
            return true;   
        }

        return false;
    }

    //This is the main funtion for flipping the pieces given it's a valid move
    public boolean FlipCheck(int c, int r, String colour){
        String opposite = " ";
        boolean changed = false;
        int difference = r - c;
        if(colour == "W")
            opposite = "B";
        if(colour == "B")
            opposite = "W";
        //This is for checking you can match left
        if(c!=0){
            if(reversiBoard[r][c-1].getText() == opposite){
                for(int i = (c-1); i > (-1); i--){
                    if(reversiBoard[r][i].getText() == colour){
                        int found = i;
                        for(int k =c; k >= found; k--){
                            reversiBoard[r][k].setText(colour);
                            changed = true;
                        }
                    }
                    if(reversiBoard[r][i].getText() == " "){
                        i = -1;
                    }
                }
            }
        }
        //This is for checking you can match right
        if(c!= (maxBoardSize -1)){
            if(reversiBoard[r][c+1].getText() == opposite){
                for(int i = (c+1); i < (maxBoardSize); i++){
                    if(reversiBoard[r][i].getText() == colour){
                        int found = i;
                        for(int k = c; k <= found; k++){
                            reversiBoard[r][k].setText(colour);
                            changed = true;
                        }
                    }
                    if(reversiBoard[r][i].getText() == " "){
                        i = maxBoardSize;
                    }
                }
            }
        }
        //This is for checking you can match down
        if(r!=(maxBoardSize -1)){
            if(reversiBoard[r+1][c].getText() == opposite){
                for(int i = (r+1); i < (maxBoardSize); i++){
                    if(reversiBoard[i][c].getText() == colour){
                        int found = i;
                        for(int k = r; k <= found; k++){
                            reversiBoard[k][c].setText(colour);
                            changed = true;
                        }
                    }
                    if(reversiBoard[i][c].getText() == " "){
                        i = maxBoardSize;
                    }
                }
            }
        }
        //This is for checking you can match up
        if(r!=0){
            if(reversiBoard[r-1][c].getText() == opposite){
                for(int i = (r-1); i > (-1); i--){
                    if(reversiBoard[i][c].getText() == colour){
                        int found = i;
                        for(int k = r; k >= found; k--){
                            reversiBoard[k][c].setText(colour);
                            changed = true;
                        }
                    }
                    if(reversiBoard[i][c].getText() == " "){
                        i = -1;
                    }
                }
            }
        }
        //This is for checking you can match diagonally up left
        if(r==0||c==(maxBoardSize -1)){}
        else{
            if(reversiBoard[r-1][c+1].getText() == opposite){
                for(int i = (r-1); i > (-1); i--){
                    for(int j = (c+1); j < (maxBoardSize); j++){

                        if(reversiBoard[i][j].getText() == colour){
                            int foundRow = i;
                            int foundColumn =j;
                            for(int k = r-1; k >= foundRow; k--){
                                for(int l = c+1; l <= foundColumn; l++){
                                    reversiBoard[k][l].setText(colour);
                                    return true; 
                                }
                            }
                        }else{
                            if(i-1 != -1)
                                i=i-1;
                        }
                    }
                }
            }
        }
        //This is for checking you can match diagonally up right
        if(r==(maxBoardSize -1)||c==(maxBoardSize -1)){}
        else{
            if(reversiBoard[r+1][c+1].getText() == opposite){
                for(int i = (r+1); i < (maxBoardSize); i++){
                    for(int j = (c+1); j < (maxBoardSize); j++){
                        if(reversiBoard[i][j].getText() == colour){
                            int foundRow = i;
                            int foundColumn =j;
                            for(int k = r+1; k <= foundRow; k++){
                                for(int l = c+1; l <= foundColumn; l++){
                                    reversiBoard[k][l].setText(colour);
                                    return true; 
                                }
                            }
                        }
                        else{
                            if(i+1 != maxBoardSize)
                                i=i+1;
                        }
                    }
                }
            }

        }
        //This is for checking you can match diagonally down right
        if(r==(maxBoardSize -1)||c==0){}
        else{
            if(reversiBoard[r+1][c-1].getText() == opposite){
                for(int i = (r+1); i < (maxBoardSize); i++){
                    for(int j = (c-1); j > (-1); j--){
                        if(reversiBoard[i][j].getText() == colour){
                            int foundRow = i;
                            int foundColumn =j;
                            for(int k = r+1; k <= foundRow; k++){
                                for(int l = c-1; l >= foundColumn; l--){
                                    reversiBoard[k][l].setText(colour);
                                    return true; 
                                }
                            }
                        }
                        else{
                            if(i+1 != maxBoardSize)
                                i=i+1;
                        }
                    }
                }
            }
        }
        //This is for checking you can match diagonally down left
        if(r==0||c==0){}
        else{
            if(reversiBoard[r-1][c-1].getText() == opposite){
                for(int i = (r-1); i > (-1); i--){
                    for(int j = (c-1); j > (-1); j--){

                        if(reversiBoard[i][j].getText() == colour){
                            int foundRow = i;
                            int foundColumn =j;
                            for(int k = r-1; k >= foundRow; k--){
                                for(int l = c-1; l >= foundColumn; l--){
                                    reversiBoard[k][l].setText(colour);
                                    return true; 
                                }
                            }
                        }else{
                            if(i-1 != -1)
                                i=i-1;
                        }
                    }
                }
            }
        }
        if(changed == true){
            return true;   
        }

        return false;
    }
}