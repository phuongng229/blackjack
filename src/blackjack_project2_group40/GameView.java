/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack_project2_group40;

import java.awt.event.ActionListener;
import java.util.List;

/**
 *
 * @author Jonathan
 * 
 * Defines the view. It abstracts all GUI details behind a simple interface.
 * By coding to this interface, the controller remains decoupled from Swing or any specific UI toolkit.
 * 
 */
public interface GameView {
    //void displayState(GameState state);
    void showMessage(String message);
    boolean promptYesNo(String message);
    
    // Input Fields
    String getEnteredPlayerName();
    void clearPlayerNameField();
    double getEnteredBetAmount();
    void clearBetAmountField();

    // Player Setup
    void setPlayerSetupHandler(ActionListener listener);
    void updatePlayerCount(int count);

    // Game Phase
    void setCurrentPersonName(String name);
    void setCurrentPersonBalance(double balance);
    void setCurrentPersonBet(double bet);
    void setActionTitle(String text);
    void setActionButtons(List<PlayerAction> actions, ActionListener listener);
    
    void showBetInput(boolean visible);
    
}
