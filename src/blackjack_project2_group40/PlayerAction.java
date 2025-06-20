/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack_project2_group40;

/**
 *
 * @author jonathan & phuong
 */
public enum PlayerAction {
    HIT("Hit"), STAND("Stand"), QUIT("Quit"), DOUBLE_DOWN("Double Down"), PLACE_BET("Place Bet"), NEXT_ROUND("Keep Playing"), DEALER_CONTINUE("Continue");
    private final String label;

    PlayerAction(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }

}
