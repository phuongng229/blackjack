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
        view.setPlayerSetupHandler(this);
        //view.setActionButtons(model.getAvailableActions(), this);
    }

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
                ((GameGUI) view).showGameScreen();
                model.setupDealer();
                model.startNewRound();
                refreshView();
                return;
            }
            case "NEXT_ROUND" -> {
                model.advance();
                refreshView();
                return;
            }
            case "HIT", "STAND", "DOUBLE_DOWN", "QUIT" -> {
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
    
    private void handlePlayerJoin() {
        String name = view.getEnteredPlayerName();
        if (name.isEmpty()) {
            view.showMessage("Name cannot be empty.");
            return;
        }
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
    
    private void showNameTakenMessage(String name) {
        view.showMessage("Sorry, " + name + " is already taken. Please enter a different name.");
        view.clearPlayerNameField();
    }
    
    
    private void refreshView() {
        System.out.print("View Refreshed");
        
        Game.Phase phase = model.getCurrentPhase();
        Person person = model.getCurrentPerson();
        //view.showMessage("Phase: " + phase + " — " + person.getName() + "'s turn");
        
        //view.showPhase(phase);
        
        view.setCurrentPlayerName(person.getName()); // Update GUI player name label

        // If it’s the dealer’s turn, run it automatically.
        // This method probably shouldn't be here but it needs to be somewhere it can be triggered on phase change
        if (model.getCurrentPhase() == Game.Phase.DEALER_TURN) {
            String dealerLog = model.performDealerTurn();
            view.showMessage(dealerLog);
            refreshView(); // recursive refresh now in SETTLE
            return;
        }        

        // Otherwise, show the buttons for the current player
        List<PlayerAction> actions = model.getAvailablePlayerActions();
        view.setActionButtons(actions, this);
    }
    
}