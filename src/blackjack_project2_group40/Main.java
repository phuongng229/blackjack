/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack_project2_group40;

import javax.swing.SwingUtilities;

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
        });
    }
}