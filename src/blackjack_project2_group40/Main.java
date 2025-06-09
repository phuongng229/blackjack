/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack_project2_group40;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author phuong & jonathan
 */
public class Main {
    
    public static void main(String[] args) {
        try {
            DBManager.getConnection();
            DBManager.createPlayerTable();
            DBManager.insertPlayer("Fuong", 3, 1, 0, 1500.0);
            DBManager.insertPlayer("Fu", 5, 2, 5, 1500.0);
            DBManager.insertPlayer("F", 200, 24, 5, 1500.0);
            DBManager.insertPlayer("Jon", 500, 24, 5, 1500.0);
        } finally {
            DBManager.closeConnection();
        }

        // Shutdown Derby engine properly
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException e) {
            if ("XJ015".equals(e.getSQLState())) {
                System.out.println("Derby shut down normally.");
            } else {
                System.out.println("Error shutting down Derby: " + e.getMessage());
            }
        }

        System.exit(0);
    }
    
}
