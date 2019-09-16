package javaapplication12;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.*;
import javax.swing.*;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import java.io.*;
import java.net.*;
import java.util.*;
import ttt.james.server.TTTWebService;
import ttt.james.server.TTTWebService_Service;

public class Player implements ActionListener, Runnable{
    String username;
    boolean myTurn;
    boolean startCheck = false;
    String address;
    int port;
    String playerType = "";
    int saveIndex = 0;
    /**
     * @param args the command line arguments
     */
    public int uid;
    public int gid;
    private TTTWebService proxy;
    boolean finished = false;
    private TTTWebService_Service link;
    
    public Player(int gid, int uid , boolean b, String player, String username, int port, String address)
    {
        this.username = username;
        this.playerType = player;
        this.gid = gid;
        this.uid = uid;
        this.myTurn = b;
        this.address = address;
        this.port = port;
    }
    public Player()
    {
        
    }
    public void run(){
        //getScoreboard();
        while(!finished)
        {
            new TicTacToe(uid,address, port, gid, username);
        }
       
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
  
    

  