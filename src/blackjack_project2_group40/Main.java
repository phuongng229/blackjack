/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack_project2_group40;

import javax.swing.SwingUtilities;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author phuong & jonathan
 */
public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            Game model = new Game(); // Instantiate the game model
            GameGUI view = new GameGUI(); // Instantiate the view; its constructor builds all the JFrames, JPanels, etc.
            new GameController(model, view); // Wire them together via controller

            //Ensure the Derby is shut down when the application closes
            view.getFrame().addWindowListener(new java.awt.event.WindowAdapter() {

                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    shutdownDerby();
                }
            });
        });
    }
    //Shutdown Derby

    private static void shutdownDerby() {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
            System.out.println("Derby shut down successfully");
        } catch (SQLException e) {
            if ("XJ015".equals(e.getSQLState())) {
                System.out.println("Derby shut down normally");
            } else {
                System.out.println("Error shutting down Derby: " + e.getMessage());
            }
        }
        System.exit(0);
    }

}
