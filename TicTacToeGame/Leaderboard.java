/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication12;

import java.util.ArrayList;
import javax.swing.JFrame;
import ttt.james.server.TTTWebService;
import ttt.james.server.TTTWebService_Service;

/**
 *
 * @author Stephen
 */
public class Leaderboard extends javax.swing.JFrame {
    private int uid;
    private int gid;
    private int port;
    String address;
    String username;
    private TTTWebService proxy;
    private TTTWebService_Service link;
     String[][] leaderboardTable;
     String result;
    
    /**
     * Creates new form Leaderboard
     */
    public Leaderboard() {
        initComponents();
    }

    Leaderboard(int uid, int gid, int port, String address, String username) {
       initComponents();
        this.uid = uid;
        this.address = address;
        this.port = port;
        this.gid = gid;
        this.username = username;
        leaderboardTable = getTable();
        for(int i = 0;i<leaderboardTable.length;i++)
        {
           for(int j = 0; j<leaderboardTable.length; j++)
           {
             result += leaderboardTable[i][j];
           }
        }
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
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        MainMenu = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel1.setText("Leaderboard:");

        MainMenu.setText("Main Menu");
        MainMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MainMenuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 197, Short.MAX_VALUE)
                .addComponent(MainMenu)
                .addGap(45, 45, 45))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(MainMenu))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void MainMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MainMenuActionPerformed
            MainScreen ms = new MainScreen(uid,address, port, gid, username);
            ms.setVisible(true);
            ms.pack();
            ms.setLocationRelativeTo(null);
            ms.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.dispose();
    }//GEN-LAST:event_MainMenuActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Leaderboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Leaderboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Leaderboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Leaderboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Leaderboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton MainMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
