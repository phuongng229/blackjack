/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack_project2_group40;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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
        
        System.out.println("[DEBUG] Action performed | Command: " + command); // DEBUG ONLY
        
        switch (command) {
            case "JOIN" -> {
                handlePlayerJoin();
            }
            case "START" -> {
                handleGameStart();
            }
            case "HOME" -> {
                handleGameQuit();
            }
            case "NEXT_ROUND" -> { // The continue to next round player action button was clicked
                handleNextRound();
            }
            case "PLACE_BET" -> {
                handleBetPlaced();
            }
            case "DEALER_CONTINUE" -> {
                model.advance();
                model.settleBets();
                refreshView();
            }
            case  "QUIT" -> {
                if(model.getPlayerCount() < 2) { // Exits to menu if last player chooses to quit
                    handleGameQuit();
                } else {
                    handlePlayerStandardAction(command);
                }
            }
            case "HIT", "DOUBLE_DOWN", "STAND" -> {
                handlePlayerStandardAction(command);
            }
            default -> {
                view.showMessage("Unrecognized command: " + command);
            }
        }
    }
    public void handlePlayerStandardAction(String command) {
        try {
            PlayerAction action = PlayerAction.valueOf(command);
            List<String> logs = model.performPlayerAction(action);
            logs.forEach(log -> view.showMessage(log));
            model.advance();
            refreshView();
        } catch (IllegalArgumentException ex) {
            view.showMessage("Invalid action: " + command);
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
    }
    
    private void handleNextRound() {
        // It should only continue to next round once all players have chosen to continue or quit
        int oldRound = model.getCurrentRound();
        model.advance();
        int newRound = model.getCurrentRound();
        if (newRound > oldRound) {
            view.showMessage("Starting Round " + newRound + "...");
        }
        refreshView();
    }
    
    private void handleGameStart() {
        model.startNewGame();
        view.showMessage("Game Started");
        view.showGameScreen();
        refreshView();
    }
    
    private void handleGameQuit() {
        boolean ok = view.promptYesNo("Are you sure? This will end the current game.");
        if (!ok) {
            return;
        }
        model.clearGame();
        view.updatePlayerCount(model.getPlayerCount());
        view.clearLog();
        view.showStartScreen();
    }
    
    private void handleBetPlaced() {
        double bet = view.getEnteredBetAmount();
        GameData data = model.getCurrentGameData();
        
        if (model.betExceedsBalance(bet)) {
            view.showMessage(data.currentPersonName + ": Your bet exceeds your current balance.");
            return;
        }
        if (model.betExceedsRange(bet)) {
            view.showMessage(data.currentPersonName + ": Your bet does not fall within the allowed amount.");
            return;
        }
        model.placeBet(bet);
        view.clearBetAmountField();
        model.advance();
        refreshView();
    }
    
    private void refreshView() {
        GameData data = model.getCurrentGameData();
        if (data.currentPhase == Game.Phase.DEALER_TURN) {
            // If it's the dealer's turn, play dealer's turn and reload data
            data = handleDealerPhase();
        }
        updateUI(data);
    }
    
    private void updateUI(GameData data) { // Update GUI with GameData       
        view.setPersonBalanceLabel(data.currentPlayerBalance);
        view.setPersonBetLabel(data.currentPlayerBet);
        view.setActionButtons(data.currentPersonAvailableActions, this); // update action buttons
        //view.setDealerBust(data.dealerIsBust);
        //view.setCurrentPersonBust(data.currentPersonIsBust);
        view.setDealerHandTitle(data.dealerName + "'s Hand Value: " + data.dealerHandValue, data.dealerIsBust);
        view.setPlayerHandTitle(data.currentPersonName + "'s Hand Value: " + data.currentPersonHandValue, data.currentPersonIsBust);
        view.setPlayerHand(data.currentPersonHand.getCards());
        view.setDealerHand(data.dealerHand.getCards());
        
        switch (data.currentPhase) {
            case BETTING -> {
                view.setRoundStatusLabel(data.currentPersonName + "'s Betting");
                view.setActionTitle(data.currentPersonName + ", how much would you like to bet? $50 - $300 Max");
                view.showDealerHand(false);
                view.showPlayerHand(false);
                view.showBetInput(true);
            }
            case PLAYER_TURN -> {
                view.setRoundStatusLabel(data.currentPersonName + "'s Turn");
                view.setActionTitle(data.currentPersonName + ", would you like to?");
                if (data.currentPersonIsBust) view.setActionTitle(data.currentPersonName + ", you went bust!");
                if (data.currentPersonLastAction == PlayerAction.DOUBLE_DOWN) view.setActionTitle(data.currentPersonName + ", you doubled your bet to $" + data.currentPlayerBet + "!");
                // Additional cases can be added here
                view.showDealerHand(true);
                view.showPlayerHand(true);
                view.showBetInput(false);
            }
            case DEALER_TURN -> {
                view.setRoundStatusLabel(data.currentPersonName + "'s Turn");
                view.setActionTitle(data.dealerName + " stands at " + data.dealerHandValue + ".");
                if (data.currentPersonIsBust) view.setActionTitle(data.dealerName + " went bust!");
                // Additional cases can be added here
                view.showDealerHand(false);
                view.showPlayerHand(true);
            }
            case SETTLE -> {
                view.setRoundStatusLabel(data.currentPersonName);
                view.showDealerHand(true);
                view.showPlayerHand(true);
                switch (data.currentPlayerResult) {
                    case WIN -> {
                        view.setActionTitle(data.currentPersonName + ", you won $" + data.currentPlayerBet + "! Would you like to?");
                        view.showMessage(data.currentPersonName + ": Wins $" + data.currentPlayerBet);
                    }
                    case LOSE -> {
                        view.setActionTitle(data.currentPersonName + ", you lost $" + data.currentPlayerBet + "! Would you like to?");
                        view.showMessage(data.currentPersonName + ": Loses $" + data.currentPlayerBet);
                    }
                    case PUSH -> {
                        view.setActionTitle(data.currentPersonName + ", you pushed $" + data.currentPlayerBet + "! Would you like to?");
                        view.showMessage(data.currentPersonName + ": Pushes $" + data.currentPlayerBet);
                    }
                }
            }
        }
    }
    
    private GameData handleDealerPhase() {
        List<String> logs = model.performDealerTurn();
        logs.forEach(log -> view.showMessage(log));
        return model.getCurrentGameData(); // always return fresh data
    }
    
}