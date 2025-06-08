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
 */
public class GameController implements ActionListener {
    private final Game model;
    private final GameView view;

    public GameController(Game model, GameView view) {
        this.model = model;
        this.view  = view;
        init();
    }

    private void init() {
        view.setPlayerSetupHandler(this);
        //view.setActionButtons(model.getAvailableActions(), this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        
        // Player Setup Phase

        if ("Join".equals(cmd)) { // Join button clicked
            String name = view.getEnteredPlayerName();
            if (name.isEmpty()) {
                view.showMessage("Name cannot be empty.");
                return;
            }
            if (model.playerNameTaken(name)) {
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
            model.addPlayer(name,scores);
            view.showMessage("Player \"" + name + "\" has joined.");
            view.clearPlayerNameField();
        }
    }
    private void showNameTakenMessage(String name) {
        view.showMessage("Sorry, " + name + " is already taken. Please enter a different name.");
        view.clearPlayerNameField();
    }
}