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
    
    @Override
    public void playTurn() {
        
        System.out.println("It's " + getName() + "'s turn!");
        
        //Check for Blackjack
        if (getHand().getTotalValue() == 21 && getHand().getSize() == 2) {
            System.out.println(getName() + " has Blackjack!");
            printHand();
            return;
        }

        //Using while loop to let dealer hit as many times as necessary until the total is 17 or more
        while (getHand().getTotalValue() < 17) {
            hit();
            updateHandResults();
            if (isBust()) {
                System.out.println(getName() + " is bust!");
                return;
            }
        }
        stand();
    }
    
    private void printHand() {
        System.out.println(getName() + "'s hand is " + getHand() +" with a total value of "+ getHand().getTotalValue() + ".");
    }
    
    private void hit() {
        Card drawCard = getDeck().drawCard();
        getHand().addCard(drawCard);
        System.out.println(getName() + " hits and draws [" + drawCard + "]");
        printHand();
    }
    
    private void stand() {
        System.out.println("The dealer stands at "+getHand().getTotalValue()+ ".");
    }
    
}
