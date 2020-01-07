

	import java.util.*;
	import java.awt.*;
	import java.awt.event.*;
	import javax.swing.*;

	/**
	 * A class modelling a tic-tac-toe (noughts and crosses, Xs and Os) game.
	 * 
	 * @author Lynn Marshall
	 * @version November 8, 2012
	 * 
	 * @author ShashaankSrivastava  
	 * @version November 25, 2019
	 */



	public class TicTacToe implements ActionListener{
	   public static final String PLAYER_X = "X"; // player using "X"
	   public static final String PLAYER_O = "O"; // player using "O"
	   public static final String EMPTY = " ";  // empty cell
	   public static final String TIE = "T"; // game ended in a tie
	 
	   private String player;   // current player (PLAYER_X or PLAYER_O)
	   private String winner; // stores the winner
	   
	   private String defPlayer; //sets if it should start with player x or o every game
	   
	   private int[] gameWins = new int[3]; // tracks wins O is [0] X is [1] Ties are [2]

	   private int numFreeSquares; // number of squares still free
	   
	   private JButton buttons[][];// 3x3 array representing the buttons
	   
	   ImageIcon X,O; //stores image icons for x and o
	   
	   JLabel gStat,pTurn; //initializes
	   
	   private JMenuItem quitItem,xStartItem,oStartItem,statResetItem; //menu items for each action
	   private JMenuItem resetItem;
	   
	   /** 
	    * Constructs a new Tic-Tac-Toe game with a fancy GUI initializes many variables
	    */
	   public TicTacToe()
	   {      
	       // Initialize and create array / variables...
	       JFrame window = new JFrame("Tic Tac Toe");
	       JPanel bPanel = new JPanel(new GridLayout(3,3));
	       Container con = window.getContentPane();
	       buttons = new JButton[3][3];
	       numFreeSquares = 9; //starting game with 9 squares
	       player = PLAYER_X;
	       defPlayer = PLAYER_X;
	       
	       gStat = new JLabel("No stats to report yet");
	       pTurn = new JLabel("Player X starts"); // creating labels to store game stats and player turn info
	       
	       con.add(gStat,BorderLayout.NORTH);
	       con.add(pTurn,BorderLayout.SOUTH);
	              
	       // Assign Icons
	       X = new ImageIcon(getClass().getResource("X.png"));
	       O = new ImageIcon(getClass().getResource("O.png"));
	       
	       JMenuBar menubar = new JMenuBar();
	       window.setJMenuBar(menubar); // add menu bar to our frame
	       
	       JMenu fileMenu = new JMenu("Options"); // create a menu
	       menubar.add(fileMenu); // and add to our menu bar
	       
	       resetItem = new JMenuItem("New Game"); // create a menu item called "new game"
	       fileMenu.add(resetItem); // and add to our menu
	       resetItem.addActionListener(this);
	       
	       xStartItem = new JMenuItem("Start as X"); 
	       fileMenu.add(xStartItem); // and add to our menu
	       xStartItem.addActionListener(this);
	       
	       oStartItem = new JMenuItem("Start as O"); 
	       fileMenu.add(oStartItem); // and add to our menu
	       oStartItem.addActionListener(this);
	       
	       statResetItem = new JMenuItem("Reset win stats"); 
	       fileMenu.add(statResetItem); // and add to our menu
	       statResetItem.addActionListener(this);
	       
	       quitItem = new JMenuItem("Quit"); // create a menu item called "Quit"
	       fileMenu.add(quitItem); // and add to our menu
	       quitItem.addActionListener(this);
	       
	       //creating shortcut keys for jmenu such as quit and restart
	       final int SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(); // to save typing
	       resetItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, SHORTCUT_MASK));
	       quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));
	       
	       //Add Buttons and action listener as well as enabling all buttons
	       for(int i = 0; i < 3; i++){
	           for(int j = 0; j < 3; j++){
	               buttons[i][j] = new JButton();
	               buttons[i][j].addActionListener(this);
	               buttons[i][j].setEnabled(true);
	               bPanel.add(buttons[i][j]);
	           }
	       }
	       
	       con.add(bPanel,BorderLayout.CENTER);
	       
	       //Create Windows
	       window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	       window.setResizable(false);
	       window.pack();
	       window.setSize(500,500);
	                     
	       window.setVisible(true);
	   }
	   
	   /**
	    * ActionPerformed gets action from actionlistener interface
	    * 
	    * @param the action event, used to interact with GUI
	    */
	   public void actionPerformed(ActionEvent e){
	       Object o = e.getSource();
	       
	       if(o instanceof JButton){
	           JButton button = (JButton) o;
	           if(button.getIcon() != X && button.getIcon() != O)
	               buttonPress(button);
	               
	           
	        }
	       else{
	           JMenuItem item = (JMenuItem)o;
	           
	           if(item == quitItem) { // quit
	               System.exit(0); 
	           }
	           else if(item == resetItem){//resets game
	               resetGame(defPlayer);
	            }
	           else if(item == xStartItem){//resets game with X as default start
	               resetGame(PLAYER_X);
	               defPlayer = PLAYER_X;
	            }
	           else if(item == oStartItem){//resets game with O as default start
	               resetGame(PLAYER_O);
	               defPlayer = PLAYER_O;
	            }
	           else if(item == statResetItem){//resets game with all game stats set to 0
	               resetStats();
	            }
	           
	       }
	   }
	   
	   /**
	    * will do all checking if the game is won, changes button icon to player(X/O)
	    * updates game stats depending on wins and outputs updated Jlabels for turns
	    */
	   public void buttonPress(JButton b){
	       winner = EMPTY;   // winner: PLAYER_X, PLAYER_O, TIE, EMPTY = in progress  
	       numFreeSquares--; //if button click is successful one less square
	       
	       if(player.equals(PLAYER_X))b.setIcon(X);
	       else b.setIcon(O); //sets icon depending on player turn
	       
	       for(int i = 0; i < 3; i++){
	           for(int j = 0; j < 3; j++){
	               if(b == buttons[i][j]){ // identifies correct button from array
	                   if(haveWinner(i,j)){//checks if winner then procedes accordingly depending on win or tie
	                       
	                      winner = player;
	                      if(player.equals(PLAYER_X)) gameWins[1]++;
	                      else gameWins[0]++;
	                    
	                      endGame();      
	                      return;
	                    }
	                    else if(numFreeSquares == 0){//if a tie
	                           winner = TIE;
	                           gameWins[2]++;
	                           endGame();
	                           return;
	                   }
	                }
	           }   
	        }
	        
	       if(player.equals(PLAYER_X)){
	           player = PLAYER_O;
	           pTurn.setText("Player O's turn, game in progress");
	        }
	       else{
	           player = PLAYER_X;
	           pTurn.setText("Player X's turn, game in progress");
	        }
	    }
	    
	    /**
	     * Ends the game disabling all buttons and changes JLabels to have proper information
	     */
	    public void endGame(){
	        for(int i = 0; i < 3; i++){
	           for(int j = 0; j < 3; j++){
	               buttons[i][j].setIcon(null);
	               buttons[i][j].setEnabled(false);
	            }
	        }
	        pTurn.setText("Game has ended... it was a " + winner);
	        gStat.setText("Game stats so far: X Wins: " + gameWins[1] + " || O Wins: " + gameWins[0]
	        + " || Ties: " + gameWins[2]);
	    }
	    
	    /**
	     * resets to a new game by re enabling icons 
	     * 
	     * @param the player that will start the round
	     */
	    public void resetGame(String player){
	        for(int i = 0; i < 3; i++){
	           for(int j = 0; j < 3; j++){
	               buttons[i][j].setIcon(null);
	               buttons[i][j].setEnabled(true);
	            }
	        }
	        numFreeSquares = 9;
	        this.player = player;
	        winner = EMPTY;
	        pTurn.setText("New game player: " + defPlayer + " will start");
	    }
	    
	    public void resetStats(){
	       for(int i = 0; i < 3; i++){
	        gameWins[i] = 0;
	        }
	       resetGame(defPlayer);
	       gStat.setText("No stats, they were reset just wait...");
	       pTurn.setText("No previous winner stats reset");
	    }
	    
	      /**
	    * Returns true if filling the given square gives us a winner, and false
	    * otherwise.
	    *
	    * @param int row of square just set
	    * @param int col of square just set
	    * 
	    * @return true if we have a winner, false otherwise
	    */
	   private boolean haveWinner(int row, int col) 
	   {
	       // unless at least 5 squares have been filled, we don't need to go any further
	       // (the earliest we can have a winner is after player X's 3rd move).

	       if (numFreeSquares>4) return false;

	       // Note: We don't need to check all rows, columns, and diagonals, only those
	       // that contain the latest filled square.  We know that we have a winner 
	       // if all 3 squares are the same, as they can't all be blank (as the latest
	       // filled square is one of them).

	       // check row "row"
	       if(buttons[row][0].getIcon() != null && buttons[row][1].getIcon()!=null &&
	            buttons[row][2].getIcon() != null ){
	            if ( buttons[row][0].getIcon().equals(buttons[row][1].getIcon()) &&
	            buttons[row][0].getIcon().equals(buttons[row][2].getIcon()) ) return true;
	        }
	       
	       // check column "col"
	       if (buttons[0][col].getIcon() != null && buttons[1][col].getIcon() != null &&
	           buttons[2][col].getIcon() != null)
	           if ( buttons[0][col].getIcon().equals(buttons[1][col].getIcon()) &&
	                buttons[0][col].getIcon().equals(buttons[2][col].getIcon()) ) return true;

	       // if row=col check one diagonal
	       if (row==col){
	           if (buttons[0][0].getIcon() != null && buttons[1][1].getIcon() != null &&
	                   buttons[2][2].getIcon() != null){       
	              if ( buttons[0][0].getIcon().equals(buttons[1][1].getIcon()) &&
	                   buttons[0][0].getIcon().equals(buttons[2][2].getIcon()) ) return true;
	                }
	            }

	       // if row=2-col check other diagonal
	       if (row==2-col){
	           if(buttons[0][2].getIcon() != null && buttons[1][1].getIcon() != null &&
	                   buttons[2][0].getIcon() != null)           
	              if ( buttons[0][2].getIcon().equals(buttons[1][1].getIcon()) &&
	                   buttons[0][2].getIcon().equals(buttons[2][0].getIcon()) ) return true;
	            }

	       // no winner yet
	       return false;
	   }
	   public static void main(String[] args){
		   new TicTacToe();   
	   }
	   
	
}
