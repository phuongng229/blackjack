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
 */
public interface GameView {
    //void displayState(GameState state);
    void showMessage(String message);
    boolean promptYesNo(String message);
    
    // Player Setup
    void setPlayerSetupHandler(ActionListener listener);
    void updatePlayerCount(int count);
    void clearPlayerNameField();
    String getEnteredPlayerName();
    
    void setCurrentPlayerName(String name);
    void setActionButtons(List<PlayerAction> actions, ActionListener listener);
}
