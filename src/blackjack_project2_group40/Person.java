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
    //private HandCheck handResults;
    private PlayerScores scores;
    private PlayerAction lastAction;

    public Person(Deck deck, String name) {
        this.deck = deck;
        this.name = name;
        this.hand = new Hand();
        this.scores = new PlayerScores();
    }
    // Getters
    public String getName() { return name; }
    public Hand getHand() { return hand; }
    public Deck getDeck() { return deck; }
    public PlayerScores getScores() { return scores; }
    public PlayerAction getLastAction() { return lastAction; }
    
    //Check total value of handResults by using GameRules class
    public HandCheck getHandResults() {
        return GameRules.handCheck(hand);
    }
    
    // Setters
    public void setScores(PlayerScores scores) { this.scores = scores; }
    public void setLastAction(PlayerAction lastAction) { this.lastAction = lastAction; }
    
    public void resetLastAction() {
        this.lastAction = null;
    }
    
    public Card hit() {
        Card drawCard = getDeck().drawCard();
        getHand().addCard(drawCard);
        return drawCard;
    }
    
    public void stand() {
        
    }

    public boolean isBust() {
        HandCheck handResults = getHandResults();
        return handResults.isBust;
    }

    public boolean isBlackjack() {
        HandCheck handResults = getHandResults();
        return handResults.isBlackjack;
    }

}
