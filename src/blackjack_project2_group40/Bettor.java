/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package blackjack_project2_group40;

/**
 *
 * @author phuong
 */
public interface Bettor {
    void placeBet(double amount); //Handle placing a bet
    double getBalance(); //To retrieve the current balance
    double getCurrentBet();
    
    void winBet(); 
    void loseBet();
    void pushBet();
}
