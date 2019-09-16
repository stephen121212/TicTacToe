/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication12;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import ttt.james.server.TTTWebService;
import ttt.james.server.TTTWebService_Service;

/**
 *
 * @author Stephen
 */
public class TicTacToe extends JFrame implements ActionListener, Runnable{
    private TTTWebService proxy;
    private TTTWebService_Service link;
    private JLabel text, clicked;
    private JButton button;
    private JPanel panel;
    private JTextField textField;
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
     private JPanel gameBoard;
    private JPanel menu;
    private JLabel menuText, curPlayerID;
    private JButton[] squares;
    private JButton reset;
    private Thread thread;
    private int[][] board;
    private Color[] playerBadge;
    private int player;
    private int numSides;
    private int gameState;  /* -2: new game, -1: draw, 0: player1 win, 1: player2 win */
    private int numSquares;
     private int uid;
    private String address;
    boolean finished = false;
    boolean gameOver = false;
    String username;
    boolean myTurn;
    boolean startCheck = false;
    String playerType = "";
    int saveIndex = 0;
    private int port;
    private int gid;
    /**
     * @param args the command line arguments
     */
    

    public TicTacToe(int gid, int uid, String address, int port, String username) {
       
        this.uid = uid;
        this.address = address;
        this.port = port;
        this.gid = gid;
        this.username = username;
    }
    public TicTacToe()
    {
        
    }
    public void run(){
        start();
        //getScoreboard();
        while(!finished)
        {
            updateUI();
            checkTurn();
            if(startCheck == true)
                checkGame();
            /*
            try{
                wait(500);
            }
            catch(Exception e)
            {
                e.getMessage();
            }*/
           // checkGame();
        }
       
    }
  
    
    
    public void gameOptions()
    {
        TicTacToe frame = new TicTacToe();
        //(new Thread(new Player())).start();
        
        
	frame.setTitle("TicTacToe");
        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };
        
        JButton b=new JButton("Create Game");  
        b.setBounds(50,100,95,30);
        frame.add(b);
     
         b.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){  
            createGame();
        }  
       });
         
        JButton c=new JButton("Join Game");  
        c.setBounds(50,180,95,30);  
        frame.add(c);  
     
         c.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){  
            //showGames();
        }  
       });
        
        frame.setSize(800,400);
        frame.setBounds(500,200,00,00);
        frame.setPreferredSize(new Dimension(200,400));
        frame.setLayout(null);  
        frame.setVisible(true);
        frame.addWindowListener(l);
        frame.pack();
        frame.setVisible(true);
       
    }
    
    
     
    public int loginPlayer(String username, String password)
    {        
        int result = proxy.login(username,password);
        
        if(result != 0 && result != -1)
        {
            this.username = username;
            //successful login
            uid = result;
            //gameOptions();
            return 1;
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Error: Login details are incorrect!");
            return 0;
        }
            
    }
   
    public int registerPlayer(String name, String surname, String username, String password)
    {       
        String result = proxy.register(username,password,name,surname);
        
        if(!(result.equalsIgnoreCase("ERROR-REPEAT") || result.equalsIgnoreCase("ERROR-INSERT") || result.equalsIgnoreCase("ERROR-DB") || result.equalsIgnoreCase("ERROR-RETRIEVE")))
        {
            //succeeded
            this.username = username;
            uid = Integer.parseInt(result);
            //gameOptions();
            return 1;
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Error: registration failed");
            return 0;
        }
            
          
    }
    
    public void createGame()
    {
        //create a new game
        
        String result = proxy.newGame(uid);
        if(!(result.equalsIgnoreCase("ERROR-NOTFOUND") || result.equalsIgnoreCase("ERROR-INSERT") || result.equalsIgnoreCase("ERROR-DB") || result.equalsIgnoreCase("ERROR-RETRIEVE")))
        {
            gid = Integer.parseInt(result);
            myTurn = true;
            playerType = "Player 1";
            Runnable p = new TicTacToe(uid,address, port, gid, username);
            new Thread(p).start();
            
        }
    }
    
    public void joinGame(int index)
    {
        //create a new game
        saveIndex = index;
        String result = proxy.showOpenGames();
        
        String arr[] = result.split("\n");
        boolean found = false;
        String join = "0";
        
        String temp [] = arr[index].split(",");
        if((!(temp[1].equalsIgnoreCase(username))) && !found) // check available games, player cant join a game they started themself
        {
            found = true;

            gid = Integer.parseInt(temp[0]);

            join = proxy.joinGame(uid,gid);
            myTurn = false;
            playerType = "Player 2";
            Runnable p = new TicTacToe(uid,address, port, gid, username);
            new Thread(p).start();
            //start();
        }
        if(join == "0" || found == false)
            JOptionPane.showMessageDialog(null, "Error: could not join any game");
    }
    
    public String[] getOpenGames()
    {
        String games = proxy.showOpenGames();   
        if(!(games.equalsIgnoreCase("ERROR-NOGAMES")))
        {
            String arr[] = games.split("\n");
            String result[] = new String[arr.length];
            
                for(int i = 0; i < arr.length; i++)
                {
                    String temp [] = arr[i].split(",");
                    if(!(temp[1].equalsIgnoreCase(username)))
                    {
                         result[i] = temp[1];
                    }
                }
            
            /*
            //fill with your games that have been played but arent finished
            String results = showAllMyGames(String.valueOf(uid)); 
            String split []= results.split("\n");
            ArrayList<String> playedOpenGames = new ArrayList<String>();
            
            if(!results.equalsIgnoreCase("ERROR-NOGAMES"))
            {
                for(int i = 0; i < split.length; i++)
                {

                    String temp [] = split[i].split(",");
                    String gameID = temp[0];
                    //check to see if that game is still running
                    String gameState = getGameState(Integer.parseInt(gameID));
                    if(Integer.parseInt(gameState) == 0)
                    {
                        playedOpenGames.add(split[2]);
                    }
                }
            }
            
            
            String finalArray[] = new String [playedOpenGames.size() + result.length];
            for(int j = 0; j < result.length ; j ++)
            {
                finalArray[j] = result[j];
            }
            for(int k = 0; k < playedOpenGames.size(); k++)
            {
                finalArray[result.length + k] = playedOpenGames.get(k);
            }
            */
            return result;
        }
        
        
        
        
        String [] empty = new String[1];
        return empty;
    }
    
    public void start()
    {
        gameState = -2;
        player = 0;
        numSides = 3;
        numSquares = 9;

        board = new int[numSides][numSides];
        playerBadge = new Color[2];
        playerBadge[0] = Color.RED;
        playerBadge[1] = Color.GREEN;

        this.setTitle("Tic Tac Toe: " + playerType + ": " + username);
        this.setBounds(100,100,500,300);
        this.setPreferredSize(new Dimension(500,300));
        this.setLayout(new GridLayout(1,2));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        gameBoard = new JPanel();
        gameBoard.setBounds(new Rectangle(300,300));
        gameBoard.setLayout(new GridLayout(3,3));
        squares = new JButton[9];
        for(int i=0;i<9;i++) {
            squares[i] = new JButton(" ");
            squares[i].addActionListener(this);
            gameBoard.add(squares[i]);
        }

        for(int i=0;i<numSides;i++) {
            for(int j=0;j<numSides;j++) {
                board[i][j] = -1;
                int pos = ((i*numSides) + j);
                squares[pos].setBackground(Color.WHITE);
            }
        }

        menu = new JPanel();
        menu.setLayout(new GridLayout(3,1));

        menuText = new JLabel("Click on a square to start.", SwingConstants.CENTER);
        menuText.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        //curPlayerID = new JLabel("It's player " + (player + 1) + "'s move", SwingConstants.CENTER);
        if(playerType.equalsIgnoreCase("Player 1"))
        {
            
            curPlayerID = new JLabel("Its your move", SwingConstants.CENTER);
            curPlayerID.setBackground(Color.GREEN);
        }
        else
        {
            
            curPlayerID = new JLabel("Waiting for opponent to make a move", SwingConstants.CENTER);
            curPlayerID.setBackground(Color.RED);
        }
        curPlayerID.setForeground(Color.WHITE);
        curPlayerID.setOpaque(true);
        curPlayerID.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        reset = new JButton("Start New Game");
        //reset.addActionListener(this);
       reset.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){  
            //tf.setText("Welcome to Javatpoint.");
            reset();
            
        } 
             });

        menu.add(menuText);
        menu.add(curPlayerID);
        menu.add(reset);

        this.add(gameBoard);
        this.add(menu);
        this.pack();
        this.setVisible(true);
        
    }
    public void actionPerformed(ActionEvent ae) {
        JButton source = (JButton) ae.getSource();
        
        if(source.equals(reset)) {
            reset();
        }
        
         if(source.equals(squares[0]) && myTurn) {
            play(0);
            checkGame();
            myTurn = false;
            startCheck = true;
        }
        
        if(source.equals(squares[1])&& myTurn) {
            play(1);
            checkGame();
            myTurn = false;
            startCheck = true;
        }

        if(source.equals(squares[2])&& myTurn) {
            play(2);
            checkGame();
            myTurn = false;
            startCheck = true;
        }

        if(source.equals(squares[3])&& myTurn) {
            play(3);
            checkGame();
            myTurn = false;
            startCheck = true;
        }

        if(source.equals(squares[4])&& myTurn) {
            play(4);
            checkGame();
            myTurn = false;
            startCheck = true;
        }

        if(source.equals(squares[5])&& myTurn) {
            play(5);
            checkGame();
            myTurn = false;
            startCheck = true;
        }

        if(source.equals(squares[6])&& myTurn) {
            play(6);
            checkGame();
            myTurn = false;
            startCheck = true;
        }

        if(source.equals(squares[7])&& myTurn) {
            play(7);
            checkGame();
            myTurn = false;
            startCheck = true;
        }

        if(source.equals(squares[8])&& myTurn) {
            play(8);
            checkGame();
            myTurn = false;
            startCheck = true;
        }
    }
     
     public void play(int n) {
        if(gameState == -2) {
            if(!taken(n) && numSquares > 0) {
                squares[n].setBackground(Color.GREEN);
                int y = n%numSides;
                int x = (int) n/numSides;
                takeSquare1(x, y, player);
                
                gameState = getGameState2(player);
                switch(gameState) {
                    case -2:
                        player = getPlayer();
                        numSquares = numSquares - 1;
                        
                    break;
                    
                    
                    default:
                }
            } else {
                JOptionPane.showMessageDialog(null, "That square is already taken.\n Please try again.", "Square taken!", ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Game Over.\n Please press reset to restart.", "Game Over", ERROR_MESSAGE);    
        }
    }
    
    public void takeSquare1(int x, int y, int p) {
       String result = proxy.takeSquare(x,y,gid,uid);
       if(Integer.parseInt(result) != 1)
           JOptionPane.showMessageDialog(null, "That square is already taken!here");
    }
     
    public int getPlayer() {
       player = player + 1;
       player = player % 2;
       return player;
    }
     
    public int getGameState2(int p) {
        int state = -2;

        for(int i=0;i<numSides;i++) {
            if(board[i][0] == p && board[i][1] == p && board[i][2] == p) {
                state = p;
            }
            if(board[0][i] == p && board[1][i] == p && board[2][i] == p) {
                state = p;
            }
        }

        if(board[0][0] == p && board[1][1] == p && board[2][2] == p) {
            state = p;
        }
        if(board[0][2] == p && board[1][1] == p && board[2][0] == p) {
            state = p;
        }

        if(numSquares == 1) {
            state = -1;
        }
    return state;
    }
    
    public String[][] getTable()
    {
        
        String getTable = proxy.leagueTable();
        if(!(getTable.equalsIgnoreCase("ERROR-NOGAMES")))
        {
            String result[] = getTable.split("\n");
            ArrayList<String> names = new ArrayList<String>();
            for(int i = 0; i < result.length; i++)
            {
                String temp[] = result[i].split(",");
                if(!(names.contains(temp[1])))
                {
                    names.add(temp[1]);
                }
                if(!(names.contains(temp[2])))
                {
                    names.add(temp[2]);
                }
            }
            String results [][] = new String[names.size()][4];
            String test [] = getTable.split("\n");
            for(int j = 0; j < names.size();j++)
            {
                results[j][0] = names.get(j);
                for(int k = 0; k < test.length; k++)
                {
                    String temp[] = test[k].split(",");
                    if(temp[1].equalsIgnoreCase(names.get(j)))
                    {
                        if(Integer.parseInt(temp[3]) == 1)
                        {
                            if(results[j][1] != null)
                            {
                                int wins = Integer.parseInt(results[j][1]);
                                wins++;
                                results[j][1] = String.valueOf(wins);
                            }
                            else
                                results[j][1] = "1";
                            
                        }
                        else if(Integer.parseInt(temp[3]) == 2)
                        {
                            if(results[j][2] != null)
                            {
                                int losses = Integer.parseInt(results[j][2]);
                                losses++;
                                results[j][2] = String.valueOf(losses);
                            }
                            else
                                results[j][2] = "1";
                        }
                        else if(Integer.parseInt(temp[3]) == 3)
                        {
                            if(results[j][3] != null)
                            {    
                                int draws = Integer.parseInt(results[j][3]);
                                draws++;
                                results[j][3] = String.valueOf(draws);
                            }
                            else
                                results[j][3] = "1";
                        }
                    }
                    if(temp[2].equalsIgnoreCase(names.get(j)))
                    {
                        if(Integer.parseInt(temp[3]) == 1)
                        {
                            if(results[j][2] != null)
                            {
                                int losses = Integer.parseInt(results[j][2]);
                                losses++;
                                results[j][2] = String.valueOf(losses);
                            }
                            else
                                results[j][2] = "1";
                        }
                        else if(Integer.parseInt(temp[3]) == 2)
                        {
                            if(results[j][1] != null)
                            { 
                                int wins = Integer.parseInt(results[j][1]);
                                wins++;
                                results[j][1] = String.valueOf(wins);
                            }
                            else
                                results[j][1] = "1";
                        }
                        else if(Integer.parseInt(temp[3]) == 3)
                        {
                            if(results[j][3] != null)
                            {
                                int draws = Integer.parseInt(results[j][3]);
                                draws++;
                                results[j][3] = String.valueOf(draws);
                            }
                            else
                                results[j][3] = "1";
                        }
                    }
                }
            }
            
            return results;
            
            
        }
       return new String [4][4];
    }
    
    public void getScoreboard()
    {
        String result = proxy.showAllMyGames(uid);
        String arr []= result.split("\n");
        String otherUser = "";
        int wins = 0;
        int losses = 0;
        int draws = 0;
        if(!(result.equalsIgnoreCase("ERROR-NOGAMES")))
        {
            for(int i = 0; i < arr.length; i++)
            {
                String temp [] = arr[i].split(",");

                if(Integer.parseInt(temp[0]) == gid)
                {
                    if(username.equalsIgnoreCase(temp[1]))
                    {
                        otherUser = temp[2];
                    }
                    else
                        otherUser = temp[1];
                }
            }
        
        String table = proxy.leagueTable();
        arr = table.split("\n");
        
            for(int j = 0; j < arr.length; j++)
            {
                String temp [] = arr[j].split(",");
                if(temp[1].equalsIgnoreCase(username) && temp[2].equalsIgnoreCase(otherUser))
                {
                    if(Integer.parseInt(temp[3]) == 1)
                    {
                        wins ++;
                    }
                    else if(Integer.parseInt(temp[3]) == 2)
                    {
                        losses ++;
                    }
                    else if(Integer.parseInt(temp[3]) == 3)
                    {
                        draws ++;
                    }
                }
                  if(temp[1].equalsIgnoreCase(otherUser) && temp[2].equalsIgnoreCase(username))
                {
                    if(Integer.parseInt(temp[3]) == 1)
                    {
                        losses ++;
                    }
                    else if(Integer.parseInt(temp[3]) == 2)
                    {
                        wins ++;
                    }
                    else if(Integer.parseInt(temp[3]) == 3)
                    {
                        draws ++;
                    }
                }
            
            }
        }
        
        menuText.setText("History: Win: " + wins + " / Loss: " + losses + " / Draw: " + draws);
        
    }
    
    
    
    
     public boolean taken(int n) {
        //check if a square is taken.
        boolean found = false;
        String result = proxy.getBoard(gid);
        //pid,x,y
        if(!(result.equalsIgnoreCase("ERROR-DB") || result.equalsIgnoreCase("ERROR-NOMOVES")))
        {
            String arr[] =  result.split("\n");
            for(int i = 0; i < arr.length; i++)
            {
                String temp [] = arr[i].split(",");
                int x = Integer.parseInt(temp[1]);
                int y = Integer.parseInt(temp[2]);
                if(((x * numSides) + y) == n)
                {
                    found = true;
                }
            }
        }
        if(found == true)
            return true;
        else
            return false; //first move is free
    }
    
    public void reset() {
        /*
        gameState = -2;
        player = 0; 
        numSquares = 9;
        
        for(int i=0;i<numSides;i++) {
            for(int j=0;j<numSides;j++) {
                board[i][j] = -1;
                squares[((i*numSides) + j)].setBackground(Color.WHITE);
            }
        }
        
        curPlayerID.setText("It's player " + (player + 1) + "'s move");
        curPlayerID.setBackground(playerBadge[player]);*/
       

    }

    public void updateUI()
    {
        
        String result = proxy.getBoard(gid);
        //pid,x,y
        if(!(result.equalsIgnoreCase("ERROR-DB") || result.equalsIgnoreCase("ERROR-NOMOVES")))
        {
            String arr[] =  result.split("\n");
            for(int i = 0; i < arr.length; i++)
            {
                String temp [] = arr[i].split(",");
                if(Integer.parseInt(temp[0]) != uid) //other players move
                {
                    int x = Integer.parseInt(temp[1]);
                    int y = Integer.parseInt(temp[2]);
                    int t = ((x*3) + y);
                    squares[t].setBackground(Color.RED); //set other player square to red
                }
            }
            if(myTurn == true)
            {
                curPlayerID.setText("It's your turn");
                curPlayerID.setBackground(Color.GREEN);
            }
            else
            {
                curPlayerID.setText("Waiting for opponent to make a move");
                curPlayerID.setBackground(Color.RED);
            }
        }
        getScoreboard();
    }
    
     public void checkTurn()
    {
        String result = proxy.getBoard(gid);
        String arr[] = result.split("\n");
        int length = arr.length;
        String arr2[] = arr[length - 1].split(",");
        if(!(arr2[0].equalsIgnoreCase("ERROR-NOMOVES")))
        {
            if(Integer.parseInt(arr2[0]) != uid)
            {
                myTurn = true;
            }
        }
    }
    
     public boolean checkGame()
    {
        boolean finished = false;
        String result = proxy.checkWin(gid);
        
       // if(result.equalsIgnoreCase("0") ||result.equalsIgnoreCase("1") || result.equalsIgnoreCase("2") || result.equalsIgnoreCase("3"))
       
            if(gameOver == false)
            {
                if(!(result.equalsIgnoreCase("ERROR-NOMOVES") || result.equalsIgnoreCase("ERROR-RETRIEVE") || result.equalsIgnoreCase("ERROR-DB")))
                {
                    if(result.equals("1"))
                    {
                        finished = true;
                        gameOver = true;
                        this.setVisible(false);
                        proxy.setGameState(gid, 1);
                        JOptionPane.showMessageDialog(null, "Player 1 has won the game");
                        
                    }
                    if(result.equals("2"))
                    {
                        finished = true;
                        gameOver = true;
                        this.setVisible(false);
                        proxy.setGameState(gid, 2);
                        JOptionPane.showMessageDialog(null, "Player 2 has won the game");
                    }
                    if(result.equals("3"))
                    {
                        finished = true;
                        gameOver = true;
                        this.setVisible(false);
                        proxy.setGameState(gid, 3);
                        JOptionPane.showMessageDialog(null, "The game has ended in a draw");
                    }
                }
            }
        
        return finished;
    }   
}

