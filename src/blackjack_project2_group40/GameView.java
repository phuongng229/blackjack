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
    void showStartScreen();
    void showGameScreen();
            
    void showMessage(String message);
    boolean promptYesNo(String message);
    void setGameHandler(ActionListener listener);
    
    // Input Fields
    String getEnteredPlayerName();
    void clearPlayerNameField();
    double getEnteredBetAmount();
    void clearBetAmountField();
    void clearLog();

    // Player Setup
    void updatePlayerCount(int count);

    // Game Phase
    void setRoundStatusLabel(String text);
    void setPersonBalanceLabel(String text);
    void setPersonBetLabel(String text);
    void setActionTitle(String text);
    void setActionButtons(List<PlayerAction> actions, ActionListener listener);
    void setPlayerHandTitle(String text, boolean isBust);
    void setDealerHandTitle(String text, boolean isBust);
    void setPlayerHand(List<Card> cards);
    void setDealerHand(List<Card> cards);
    
    void showPlayerHand(boolean visible);
    void showDealerHand(boolean visible);
    void showBetInput(boolean visible);
    
}
