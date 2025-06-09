/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack_project2_group40;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Jonathan
 * 
 * GameController class listens for user-driven events from the GameGUI (e.g. button clicks),
 * calls relevant methods in the model (trying to speak only to the game class to keep the model and view decoupled),
 * and then updates the GameGUI (view) to reflect the model’s new state
 * (refreshing buttons, log messages, etc.)
 * 
 */
public class GameController implements ActionListener {
    private final Game model;
    private final GameView view;

    public GameController(Game model, GameView view) {
        this.model = model;
        this.view = view;
        init();
    }

    private void init() {
        view.setGameHandler(this);
        //view.setActionButtons(model.getAvailableActions(), this);
    }

    // Recieves ActionListener events and executes functions depending on the command recieved.
    // Will be triggered if a button is clicked for example, can then call methods within the model to handle game logic.
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand().toUpperCase().replace(" ", "_");
        
        System.out.println("Action performed! | Command: "+command); // DEBUG ONLY
        
        switch (command) {
            case "JOIN" -> {
                handlePlayerJoin();
                return;
            }
            case "START" -> {
                view.showMessage("Game starting!");
                view.showGameScreen();
                model.setupDealer();
                model.startNewRound();
                refreshView();
                return;
            }
            case "HOME" -> {
                view.showStartScreen();
                return;
            }
            case "NEXT_ROUND" -> {
                System.out.println("[DEBUG] NEXT_ROUND clicked");

                // BEFORE advance
                int oldRound = model.getCurrentRound();

                model.advance();

                int newRound = model.getCurrentRound();
                System.out.println("[DEBUG] Old round: " + oldRound + ", New round: " + newRound);

                if (newRound > oldRound) {
                    System.out.println("[DEBUG] Detected new round");
                    view.showMessage("=== Starting Round " + newRound + " ===");
                }

                refreshView();
                return;
            }
            case "PLACE_BET" -> {
                handleBetPlaced();
                return;
            }
            case "DEALER_CONTINUE" -> {
                model.advance();
                refreshView();
                return;
            }
            case "HIT", "DOUBLE_DOWN", "STAND", "QUIT" -> {
                try {
                    PlayerAction action = PlayerAction.valueOf(command);
                    String result = model.performPlayerAction(action);
                    System.out.println(result); // TO REMOVE
                    view.showMessage(result);
                    model.advance();
                    refreshView();
                } catch (IllegalArgumentException ex) {
                    view.showMessage("Invalid action: " + command);
                }
                return;
            }
            default -> {
                view.showMessage("Unrecognized command: " + command);
            }
        }
    }
    
    // Used by the "Join" command to handle players joining
    private void handlePlayerJoin() {
        String name = view.getEnteredPlayerName();
        if (model.playerNameIsTaken(name)) {
            showNameTakenMessage(name);
            return;
        }
        PlayerScores scores = model.getPlayerScores(name);
        if (model.playerHasExistingScores(name)) {
            boolean isCorrectPlayer = view.promptYesNo("A player with that name already exists with the following scores. Is this you?\n" + scores);
            if (!isCorrectPlayer) {
                showNameTakenMessage(name);
                return;
            }
        }
        model.addPlayer(name, scores);
        view.showMessage("Player \"" + name + "\" has joined.");
        view.clearPlayerNameField();
        view.updatePlayerCount(model.getPlayerCount());
    }
    
    private void showNameTakenMessage(String name) { // Used by the handlePlayerJoin() method
        view.showMessage("Sorry, " + name + " is already taken. Please enter a different name.");
        view.clearPlayerNameField();
    }
    
    private void handleBetPlaced() {
        double bet = view.getEnteredBetAmount();
        GameData data = model.getCurrentGameData();
        
        if (model.betExceedsBalance(bet)) {
            view.showMessage(data.currentPersonName + ": Your bet exceeds your current balance.");
            return;
        }
        if (model.betExceedsRange(bet)) {
            System.out.println("Bet EXCEEDS!");
            view.showMessage(data.currentPersonName + ": Your bet does not fall within the allowed amount.");
            return;
        }
        model.placeBet(bet);
        view.clearBetAmountField();
        model.advance();
        refreshView();
    }
    
    
    // Refreshes aspects of the GUI when the game state changes. Called multiple times within GameControlelr
    private void refreshView() {
        System.out.println("View Refreshed");
        
        GameData data = model.getCurrentGameData();
        
        // Update GUI with GameData
        view.setPersonBalanceLabel(data.currentPersonBalance);
        view.setPersonBetLabel(data.currentPersonBet);
        view.setActionButtons(data.currentPersonAvailableActions, this); // update action buttons
        view.displayPlayerHand(data.currentPersonHand.getCards());
        view.displayDealerHand(data.dealerHand.getCards());
        
        switch (data.currentPhase) {
            case BETTING -> {
                view.setRoundStatusLabel(data.currentPersonName + "'s Betting");
                view.setActionTitle(data.currentPersonName + ", how much would you like to bet? ($50 - $300 Max)");
                view.showBetInput(true);
            }
            case PLAYER_TURN -> {
                view.setRoundStatusLabel(data.currentPersonName + "'s Turn");
                view.setActionTitle(data.currentPersonName + ", would you like to?");
                view.showBetInput(false);
            }
            case DEALER_TURN -> {
                System.out.println("New Dealers turn!");
                view.setRoundStatusLabel(data.currentPersonName + "'s Turn");
                view.setActionTitle("It's the dealer's turn");
                String result = model.performDealerTurn();
                view.showMessage(result);
            }
            case SETTLE -> {
                view.setRoundStatusLabel(data.currentPersonName);
                view.setActionTitle(data.currentPersonName + ", you won/lost X amount. Would you like to?");
            }
            default -> {
                view.setActionTitle("");
            }
        }
        
    }
    
}