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
public class Player extends Person implements Bettor {    
    private double balance;
    private double currentBet;
    private boolean isActive;

    private static final double INITIAL_BALANCE = 2000;
    private static final double MIN_BET = 50;
    private static final double MAX_BET = 300;
    
    public Player(Deck deck, String name, PlayerScores scores) {
        super(deck, name);
        setScores(scores);
        this.balance = INITIAL_BALANCE;
        this.currentBet = 0;
    }
    
    @Override
    public double getBalance() {
        return balance;
    }
    @Override
    public double getCurrentBet() {
        return currentBet;
    }
    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean active) {
        this.isActive = active;
    }
    
    public void clearBet() {
        this.currentBet = 0;
    }
    
    public boolean canPlaceBet(double amount) {
        return !betExceedsRange(amount) && !betExceedsBalance(amount);
    }
    
    public boolean betExceedsRange(double amount) {
        return amount < MIN_BET || amount > MAX_BET;
    }
    
    public boolean betExceedsBalance(double amount) {
        return amount > balance;
    }
    
    @Override
    public void placeBet(double amount) {
        if (!canPlaceBet(amount)) {
            throw new IllegalArgumentException("Invalid bet amount.");
        }
        currentBet = amount;
        balance -= amount;
    }
    
    @Override
    public void winBet() {
        balance += currentBet * 2;
    }
    @Override
    public void pushBet() {
        balance += currentBet;
    }
    @Override
    public void loseBet() {
        // Bet is lost, do nothing
        // currentBet is cleared on round start
    }
    
    
    public List<PlayerAction> getAvailableActions() {
        List<PlayerAction> availableActions = new ArrayList<>();
        HandCheck handResults = getHandResults();
        if (handResults.canHit) availableActions.add(PlayerAction.HIT);
        if (handResults.canStand) availableActions.add(PlayerAction.STAND);
        if (handResults.canDoubleDown && canPlaceBet(currentBet*2)) availableActions.add(PlayerAction.DOUBLE_DOWN); //checks if the hand can double down and whether doubling down would exceed max/min betting range or player balance

        return availableActions;
    }
    
    public Card doubleDown() {
        placeBet(currentBet * 2);
        Card drawCard = getDeck().drawCard();
        getHand().addCard(drawCard);
        return drawCard;
    }
    
    /*public void promptForBet() { 
        while (true) {
            System.out.println("------------------------------------------------------------------------");
            System.out.println(getName() + ", your current balance is: $" + balance);
            System.out.println("Enter your bet amount (min bet $" + MIN_BET + ", max bet $" + MAX_BET + "):");
            
            String input = scan.nextLine().trim();
            try {
                double bet = Double.parseDouble(input);
                if (betExceedsRange(bet)) {
                    System.out.println("Bet must be between $" + MIN_BET + " and $" + MAX_BET);
                } else if (betExceedsBalance(bet)) {
                    System.out.println("Insufficient balance. Enter a lower amount.");
                } else {
                    placeBet(bet);
                    System.out.println("You placed a bet of $" + bet + ". Remaining balance: $" + getBalance());
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            }
        }
    }
    
    
    
    @Override
    public void winBet() {
        double wins = currentBet;
        double returnAmount = currentBet * 2;
        System.out.println(getName() + " wins $" + wins);
        balance += returnAmount;
        System.out.println("Your balance is $" + balance);
        currentBet = 0;
    }
    
    @Override
    public void pushBet() {
        System.out.println(getName() + " pushes. Bet of $" + currentBet + " returned.");
        balance += currentBet;
        System.out.println("Your balance is $" + balance);
        currentBet = 0;
    }
    
    @Override
    public void loseBet() {
        System.out.println(getName() + " loses the bet of $" + currentBet);
        System.out.println("Your balance is $" + balance);
        currentBet = 0;
    }
    
    @Override
    public void playTurn() {
        if (!isActive) {
            return; //Skip if the player is not active
        }
        
        resetLastAction(); //Using method from Person
        //promptForBet();
        
        System.out.println(getName() + ", it's your turn!");
        printHandDetails();
        
        //Check if player got Blackjack
        if (getHand().getTotalValue() == 21 && getHand().getSize() == 2) {
            System.out.println(getName() + " has Blackjack!");
            setLastAction(PlayerAction.STAND);
            return;
        } 
        
        updateHandResults(); //Update handResults BEFORE entering the loop
        
        //Loop to let users choose their action and update the Hand Result
        while (getLastAction() != PlayerAction.STAND && !isBust()) {
            updateAvailableAction();
            PlayerAction action = promptForAction();
            performAction(action);
            updateHandResults();
            
            //If player hits 21 total from multiple Hit, stop automatically
            if (getHand().getTotalValue() == 21) {
                System.out.println("You reached 21!");
                setLastAction(PlayerAction.STAND);
                break;
            }
        } 
        
    }
    
    private void printHandDetails() {
        System.out.println("Your hand is now " + getHand() + ", with a total value of " + getHand().getTotalValue());
    }
    
    private void updateAvailableAction() {
        availableActions = new ArrayList<>();
        if (getHandResults().canHit) availableActions.add(PlayerAction.HIT);
        if (getHandResults().canStand) availableActions.add(PlayerAction.STAND);
        //checks if the hand can double down and whether doubling down would exceed max/min betting range or player balance
        if (getHandResults().canDoubleDown && isValidBet(currentBet*2)) availableActions.add(PlayerAction.DOUBLE_DOWN);
    }
    
    private PlayerAction promptForAction() {
        while (true) {
            //Asking users to choose available actions
            System.out.println("Would you like to: ");
            for (int i = 0; i < availableActions.size(); i++) {
                System.out.printf("(%d) %s%n", i + 1, availableActions.get(i));
            }
            
            //Let them enter their choice
            System.out.println("Enter your choice: ");
            String input = scan.nextLine().trim();
            
            try {
                int inputChoice = Integer.parseInt(input);
                if (inputChoice >= 1 && inputChoice <= availableActions.size()) {
                    return availableActions.get(inputChoice - 1);
                }
            } catch (NumberFormatException e) {
                //Let fall through
            }
            
            System.out.printf("Invalid input. Please enter a number between 1 and %d.%n", availableActions.size());   
            
        }
    }
    
    // Used by hit and double down methods to draw a card, print hand details, update hand results and let the user know if they're bust. 
    private void drawAndProcessCard(String format) {
        Card drawCard = getDeck().drawCard();
        getHand().addCard(drawCard);
        
        System.out.println(String.format(format, drawCard));
        printHandDetails();
        updateHandResults();

        if (isBust()) {
            System.out.println("You are bust!");
        }
    }*/
    
}
