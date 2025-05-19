/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack_project2_group40;

/**
 *
 * @author jonathan & phuong
 */
// Data Structure class.
// Using a PlayerScores object makes the scores easier to work with than using an int array.
// The PlayerScores object is passed through the constructor when Players are created and is stored in each Person object.
// E.g. you can access a player's totalWins using person.scores.getTotalWins. You can increase totalWins using person.scores.incrementWins()
public class PlayerScores {
    private int totalWins;
    private int totalLosses;
    private int totalPushes;
    
    public PlayerScores() {
        this.totalWins = 0;
        this.totalLosses = 0;
        this.totalPushes = 0;
    }
    
    //Get methods
    public int getTotalWins() {
        return totalWins;
    }
    
    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }
    
    public int getTotalLosses() {
        return totalLosses;
    }
    
    public void setTotalLosses(int totalLosses) {
        this.totalLosses = totalLosses;
    }
    
    public int getTotalPushes() {
        return totalPushes;
    }
    
    public void setTotalPushes(int totalPushes) {
        this.totalPushes = totalPushes;
    }
    
    //Increment methods
    public void incrementWins() {
        totalWins++;
    }
    
    public void incrementLosses() {
        totalLosses++;
    }
    
    public void incrementPushes() {
        totalPushes++;
    }
    
    @Override
    public String toString() {
        return (
            "Wins: "+ totalWins +", "+
            "Losses: "+ totalLosses +", "+
            "Pushes: "+ totalPushes                
        );
    }
}
