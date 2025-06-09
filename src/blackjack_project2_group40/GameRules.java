/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack_project2_group40;

/**
 *
 * @author jonathan & phuong
 */
public class GameRules {
    
    //Checks properties of the player's hand, returning the results
    public static HandCheck handCheck(Hand hand) {
        HandCheck handResults = new HandCheck();
        int totalValue = hand.getTotalValue();
        
        handResults.isBlackjack = totalValue == 21;
        handResults.isBust = totalValue > 21;
        handResults.canHit = !handResults.isBlackjack; //if hand isn't a blackjack
        handResults.canStand = true;
        handResults.canDoubleDown = hand.getSize() == 2; //if player is on their first 2 cards
        
        return handResults;
    }
    
    /*
        Win: the player's hand beats the dealers hand without going bust (or the dealer goes bust)
        Lose: the player's hand doesn't beat the dealer's hand (or the player goes bust)
        Push: the player's hand is equal (tied) to the dealer's hand (and neither goes bust)
    */
    
    public enum Result {
        WIN, LOSE, PUSH
    }
    public static Result getPlayerResult(Person player, Person dealer) {
        if (player.isBust()) return Result.LOSE;
        if (dealer.isBust()) return Result.WIN;
        
        int playerTotal = player.getHand().getTotalValue();
        int dealerTotal = dealer.getHand().getTotalValue();
        
        //Using compare method to make it more compact
        return Integer.compare(playerTotal, dealerTotal) > 0 ? Result.WIN 
               : Integer.compare(playerTotal, dealerTotal) < 0 ? Result.LOSE
               : Result.PUSH;
    }
    
}
