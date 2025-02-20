import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TicTacToeFrame extends JFrame {
    JPanel mainPnl;
    JScrollPane outerScroller;
    JPanel topPnl;
    JPanel cmdPnl;


    // tic-tac-toe gameplay
    String player = "X";
    int moveCnt = 0;
    boolean invalidMoveFLag = false;
    private static final int ROW = 3;
    private static final int COL = 3;
    TicTacToeTile[][] board = new TicTacToeTile[ROW][COL];
    JPanel boardPnl;


    public TicTacToeFrame(){
        mainPnl = new JPanel();
        mainPnl.setLayout(new BorderLayout());
        /*createPlayPanel();
        createStatsPanel();
        createOutputPanel();


        mainPnl.add(playPnl, BorderLayout.NORTH);
        mainPnl.add(statsPnl, BorderLayout.CENTER);
        mainPnl.add(outputPnl, BorderLayout.SOUTH);*/

        createTopPnl();
        createTTTBoard();
        createCmdPnl();
        //btn = new JButton(Arrays.toString(board));
        mainPnl.add(topPnl, BorderLayout.NORTH);
        mainPnl.add(boardPnl, BorderLayout.CENTER);
        mainPnl.add(cmdPnl, BorderLayout.SOUTH);

        outerScroller = new JScrollPane(mainPnl);

        setSize(800, 550);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);

        setTitle("Sage Bushstone Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(outerScroller);
        setVisible(true);
    }

    public void createTopPnl(){
        topPnl = new JPanel();
        JLabel titleLbl = new JLabel("Tic Tac Toe");
        titleLbl.setFont(new Font("Serif", Font.PLAIN, 30));
        titleLbl.setBorder(new EmptyBorder(0, 0, 20, 0));
        topPnl.add(titleLbl);
    }

    public void createCmdPnl(){
        cmdPnl = new JPanel();
        cmdPnl.setLayout(new BorderLayout());
        cmdPnl.setBorder(new EmptyBorder(10, 0, 10, 20));

        JButton quitBtn = new JButton("Quit");
        quitBtn.addActionListener((ActionEvent ae) -> {
            int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Quit?", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, "Quitting on OK...");
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(null, "Press OK to keep playing.");
            }
        });

        cmdPnl.add(quitBtn, BorderLayout.EAST);
    }

    public void createTTTBoard(){
        boardPnl = new JPanel();
        boardPnl.setLayout(new GridLayout(3,3));

        for( int row = 0; row < 3; row++)
            for(int col= 0; col < 3; col++)
            {
                board[row][col] = new TicTacToeTile(row, col);
                board[row][col].setText(" ");
                board[row][col].addActionListener((ActionEvent ae) -> playTicTacToe(ae));
                boardPnl.add(board[row][col]);
            }
    }

    /**
     * plays tic-tac-toe. mimics the TicTacToe class's main method.
     * @param ae - used to find out which button in the array was clicked.
     */
    public void playTicTacToe(ActionEvent ae) {
        final int MOVES_FOR_WIN = 5;
        final int MOVES_FOR_TIE = 7;

            // figures out which of the buttons on the grid the player picks
            JButton playChoice = (JButton) ae.getSource();


            if(isValidChoice(playChoice)) {
                playChoice.setText(player);
                invalidMoveFLag = false;
            }
            else {
                // if the user clicks a button that's already played, show popup to pick a different one
                JOptionPane.showMessageDialog(null, "Invalid Move", "Please pick another spot to play", JOptionPane.INFORMATION_MESSAGE);
                invalidMoveFLag = true;
            }
            moveCnt++;
            boolean isWinOrTie = false;

            if (moveCnt >= MOVES_FOR_WIN) {
                if (isWin()) {
                    isWinOrTie = true;
                    int reply = JOptionPane.showConfirmDialog(null, "Player " + player + " won, do you want to play again?", "End Game", JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        JOptionPane.showMessageDialog(null, "Starting the next game on OK");

                    } else {
                        JOptionPane.showMessageDialog(null, "Quitting on OK...");
                        System.exit(0);
                    }
                    clearBoard();
                }
            }
            if (moveCnt >= MOVES_FOR_TIE) {
                if (isTie()) {
                    isWinOrTie = true;
                    int reply = JOptionPane.showConfirmDialog(null, "It's a tie, do you want to play again?", "End Game", JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        JOptionPane.showMessageDialog(null, "Starting the next game on OK");

                    } else {
                        JOptionPane.showMessageDialog(null, "Quitting on OK...");
                        System.exit(0);
                    }
                    clearBoard();
                }
            }

            // only changes the player symbol if they didn't have an invalid move on their turn
            // this way, we don't get duplicate symbols
            if(!isWinOrTie) {
                if(!invalidMoveFLag) {
                    if (player.equals("X")) {
                        player = "O";
                    } else {
                        player = "X";
                    }
                }
            }
            else {
                player = "X";
            }

    }

    private void clearBoard()
    {
        // sets all the board elements to a space
        for(int row=0; row < ROW; row++)
        {
            for(int col=0; col < COL; col++)
            {
                board[row][col].setText(" ");
            }
        }
    }

    public boolean isValidChoice(JButton btn){
        return btn.getText().equals(" ");
    }

    /**
     * @return true or false if there's a win for a player
     */
    private boolean isWin()
    {
        return isColWin() || isRowWin() || isDiagonalWin();
    }

    /**
     * checks for if there's a column in which a player wins
     * @return true or false if there's a win
     */
    private boolean isColWin()
    {
        // checks for a col win for specified player
        for(int col=0; col < COL; col++)
        {
            if(board[0][col].getText().equals(player) &&
                    board[1][col].getText().equals(player) &&
                    board[2][col].getText().equals(player))
            {
                return true;
            }
        }
        return false; // no col win
    }

    /**
     * checks for if there's a row in which a player wins
     * @return true or false if there's a win
     */
    private boolean isRowWin()
    {
        // checks for a row win for the specified player
        for(int row=0; row < ROW; row++)
        {
            if(board[row][0].getText().equals(player) &&
                    board[row][1].getText().equals(player) &&
                    board[row][2].getText().equals(player))
            {
                return true;
            }
        }
        return false; // no row win
    }

    /**
     * checks for if there's a diagonal row in which a player wins
     * @return true or false if there's a win
     */
    private boolean isDiagonalWin()
    {
        // checks for a diagonal win for the specified player

        if(board[0][0].getText().equals(player) &&
                board[1][1].getText().equals(player) &&
                board[2][2].getText().equals(player) )
        {
            return true;
        }
        if(board[0][2].getText().equals(player) &&
                board[1][1].getText().equals(player) &&
                board[2][0].getText().equals(player) )
        {
            return true;
        }
        return false;
    }

    /**
     * @return true or false if there is a tie or not
     */
    private boolean isTie()
    {
        boolean xFlag = false;
        boolean oFlag = false;
        // Check all 8 win vectors for an X and O so
        // no win is possible
        // Check for row ties
        for(int row=0; row < ROW; row++)
        {
            if(board[row][0].getText().equals("X") ||
                    board[row][1].getText().equals("X") ||
                    board[row][2].getText().equals("X"))
            {
                xFlag = true; // there is an X in this row
            }
            if(board[row][0].getText().equals("O") ||
                    board[row][1].getText().equals("O") ||
                    board[row][2].getText().equals("O"))
            {
                oFlag = true; // there is an O in this row
            }

            if(! (xFlag && oFlag) )
            {
                return false; // No tie can still have a row win
            }

            xFlag = oFlag = false;

        }
        // Now scan the columns
        for(int col=0; col < COL; col++)
        {
            if(board[0][col].getText().equals("X") ||
                    board[1][col].getText().equals("X") ||
                    board[2][col].getText().equals("X"))
            {
                xFlag = true; // there is an X in this col
            }
            if(board[0][col].getText().equals("O") ||
                    board[1][col].getText().equals("O") ||
                    board[2][col].getText().equals("O"))
            {
                oFlag = true; // there is an O in this col
            }

            if(! (xFlag && oFlag) )
            {
                return false; // No tie can still have a col win
            }
        }
        // Now check for the diagonals
        xFlag = oFlag = false;

        if(board[0][0].getText().equals("X") ||
                board[1][1].getText().equals("X") ||
                board[2][2].getText().equals("X") )
        {
            xFlag = true;
        }
        if(board[0][0].getText().equals("O") ||
                board[1][1].getText().equals("O") ||
                board[2][2].getText().equals("O") )
        {
            oFlag = true;
        }
        if(! (xFlag && oFlag) )
        {
            return false; // No tie can still have a diag win
        }
        xFlag = oFlag = false;

        if(board[0][2].getText().equals("X") ||
                board[1][1].getText().equals("X") ||
                board[2][0].getText().equals("X") )
        {
            xFlag =  true;
        }
        if(board[0][2].getText().equals("O") ||
                board[1][1].getText().equals("O") ||
                board[2][0].getText().equals("O") )
        {
            oFlag =  true;
        }
        if(! (xFlag && oFlag) )
        {
            return false; // No tie can still have a diag win
        }

        // Checked every vector so I know I have a tie
        return true;
    }
}
