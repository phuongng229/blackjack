/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack_project2_group40;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jonathan & phuong
 */
public class Hand {

    private final List<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }

    //Get a specific card in cards array
    public Card getCard(int index) {
        return cards.get(index);
    }

    //Discard hand to Deck
    public void discardHandToDeck(Deck discardCard) {
        discardCard.addAllCards(cards);
        cards.clear();
    }

    //Add a card to the hand
    public void addCard(Card card) {
        cards.add(card);
    }

    //Calculates the value
    public int getTotalValue() {
        int totalValue = 0;
        int aceCount = 0;
        
        for (Card card : cards) { //calculate total value
            totalValue += card.getValue();
            if (card.getValue() == 11) { //check if any card is Ace
                aceCount++;
            }
        }
        
        if (totalValue > 21 && aceCount > 0) {
            while (totalValue > 21 && aceCount > 0) {
                aceCount--;
                totalValue -= 10;
            }
        }
        return totalValue;
    }

    //Return number of cards in the hand
    public int getSize() {
        return cards.size();
    }
    
    public void clear() {
        cards.clear();
    }

    @Override
    public String toString() { //returns the card list line by line
        StringBuilder outputCard = new StringBuilder();
        for (int i = 0; i < cards.size(); i++) {
            outputCard.append(cards.get(i)); //Add card to the text
            if (i < cards.size() - 1) { //If it's not the last card, we add comma
                outputCard.append(", ");
            }
        }
        return outputCard.toString();
    }

}
