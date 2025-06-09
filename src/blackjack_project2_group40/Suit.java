/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package blackjack_project2_group40;

/**
 *
 * @author jonathan & phuong
 */
public enum Suit {
    CLUBS   ("Clubs", "C"),
    DIAMONDS("Diamonds", "D"),
    HEARTS  ("Hearts", "H"),
    SPADES  ("Spades", "S");
    
    private final String name;
    private final String code;

    Suit(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getCode() {
        return code;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
}
