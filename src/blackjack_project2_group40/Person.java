/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack_project2_group40;

/**
 *
 * @author jonathan & phuong
 */
public abstract class Person {

    private final String name;
    private final Hand hand;
    private final Deck deck;
    private HandCheck handResults;
    private PlayerScores scores;
    private Action lastAction;

    public Person(Deck deck, String name) {
        this.deck = deck;
        this.name = name;
        this.hand = new Hand();
        this.scores = new PlayerScores();
    }

    public String getName() {
        return name;
    }

    public Hand getHand() {
        return hand;
    }

    public Deck getDeck() {
        return deck;
    }

    public PlayerScores getScores() {
        return scores;
    }
    
    public void setScores(PlayerScores scores) {
        this.scores = scores;
    }

    public Action getLastAction() {
        return lastAction;
    }
    
    public void setLastAction(Action lastAction) {
        this.lastAction = lastAction;
    }
   
    //Reset memory of what the player did in the last round
    public void resetLastAction() {
        this.lastAction = null;
    }

    public HandCheck getHandResults() {
        return handResults;
    }

    //Check total value of handResults by using class GameRules
    public void updateHandResults() {
        this.handResults = GameRules.handCheck(hand);
    }

    public boolean isBust() {
        return handResults != null && handResults.isBust;
    }

    public boolean isBlackjack() {
        return handResults != null && handResults.isBlackjack;
    }

    public abstract void playTurn();

}
