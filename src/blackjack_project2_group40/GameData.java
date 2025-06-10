/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack_project2_group40;

import java.util.List;

/**
 *
 * @author Jonathan
 
 GameData structure used for passing info about the game state to the GUI
 */
public class GameData {
    // Current Round Data
    public int currentRound;
    public Game.Phase currentPhase;
    public int playerCount;
    
    // Current Person Data
    public String currentPersonName;
    public Hand currentPersonHand;
    public int currentPersonHandValue;
    public List<PlayerAction> currentPersonAvailableActions;
    public PlayerAction currentPersonLastAction;
    public boolean currentPersonIsBust;
    
    // Dealer Data
    public String dealerName;
    public Hand dealerHand;
    public int dealerHandValue;
    public boolean dealerIsBust;
    
    // Player Data
    public double currentPlayerBalance;
    public double currentPlayerBet;
    public GameRules.Result currentPlayerResult;
    
}