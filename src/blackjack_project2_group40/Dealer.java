/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack_project2_group40;

/**
 *
 * @author jonathan & phuong
 */
public class Dealer extends Person {
    
    public Dealer(Deck deck, String name) {
        super(deck, name);
    }
    
    public boolean shouldHit() {
        return getHand().getTotalValue() < 17;
    }
    
}
