/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack_project2_group40;

/**
 *
 * @author jonathan & phuong
 */
public enum Action {
    HIT("Hit"), STAND("Stand"), QUIT("Quit"); // Could also SPLIT("Split") or DOUBLE_DOWN("Double Down")
    private final String label;

    Action(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }

}
