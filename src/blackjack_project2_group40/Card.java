/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack_project2_group40;

/**
 *
 * @author phuong & jonathan
 */
public class Card {
    private final Face face;
    private final Suit suit;
    
    public Card(Face face, Suit suit) {
        this.face = face;
        this.suit = suit;
    }
    
    public Card(Card card) { //Copy constructor
        this.face = card.getFace();
        this.suit = card.getSuit();
    }
    
    public Face getFace() {
        return this.face;
    }
    
    public Suit getSuit() {
        return this.suit;
    }
    
    public int getValue() {
        return face.getValue();
    }
    
    @Override
    public String toString() {
        return face.toString() + "-" + suit.toString();
//        return face.toString() + " of " + suit.toString();
    }
}
