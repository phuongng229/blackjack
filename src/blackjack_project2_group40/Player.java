/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack_project2_group40;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author jonathan & phuong
 */
public class Player extends Person implements Bettor {
    private final Scanner scan = new Scanner(System.in);
    private List<Action> availableActions;
    private boolean isActive;
    private double balance;
    private static final double INITIAL_BALANCE = 2000;
    private static final double MIN_BET = 50;
    private static final double MAX_BET = 300;
    private double currentBet;
    
    public Player(Deck deck, String name, PlayerScores scores) {
        super(deck, name);
        setScores(scores);
        this.isActive = true;
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
    
    public void quit() {
        this.isActive = false;
    }
    
    @Override
    public void placeBet(double amount) {
        if (amount <= balance) {
            currentBet = amount;
            balance -= amount;
        } else {
            throw new IllegalArgumentException("Insufficient balance for the bet.");
        }
    }

    public void promptForBet() {
        while (true) {
            System.out.println("------------------------------------------------------------------------");
            System.out.println(getName() + ", your current balance is: $" + balance);
            System.out.println("Enter your bet amount (min bet $" + MIN_BET + ", max bet $" + MAX_BET + "):");
            
            String input = scan.nextLine().trim();
            try {
                double bet = Double.parseDouble(input);
                if (bet < MIN_BET || bet > MAX_BET) {
                    System.out.println("Bet must be between $" + MIN_BET + " and $" + MAX_BET);
                } else if (bet > balance) {
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
//        promptForBet();
        
        System.out.println(getName() + ", it's your turn!");
        printHandDetails();
        
        //Check if player got Blackjack
        if (getHand().getTotalValue() == 21 && getHand().getSize() == 2) {
            System.out.println(getName() + " has Blackjack!");
            setLastAction(Action.STAND);
            return;
        } 
        
        updateHandResults(); //Update handResults BEFORE entering the loop
        
        //Loop to let users choose their action and update the Hand Result
        while (getLastAction() != Action.STAND && !isBust()) {
            updateAvailableAction();
            Action action = promptForAction();
            performAction(action);
            updateHandResults();
            
            //If player hits 21 total from multiple Hit, stop automatically
            if (getHand().getTotalValue() == 21) {
                System.out.println("You reached 21!");
                setLastAction(Action.STAND);
                break;
            }
        } 
        
    }
    
    private void printHandDetails() {
        System.out.println("Your hand is now " + getHand() + ", with a total value of " + getHand().getTotalValue());
    }
    
    private void updateAvailableAction() {
        availableActions = new ArrayList<>();
        if (getHandResults().canHit) availableActions.add(Action.HIT);
        if (getHandResults().canStand) availableActions.add(Action.STAND);
        if (getHandResults().canDoubleDown) availableActions.add(Action.DOUBLE_DOWN);
    }
    
    private Action promptForAction() {
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
    
    private void performAction(Action action) {
        
        switch (action) {
            case HIT -> hit();
            case STAND -> stand();
            case DOUBLE_DOWN -> doubleDown();
        }
        setLastAction(action);   
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
    }
    
    private void hit() {
        drawAndProcessCard("You hit and draw the [%s].");
    }
    
    private void doubleDown() {
        double bet = currentBet * 2;
        placeBet(bet);
        System.out.println("You double down and increase your bet to $" + bet + ". Remaining balance: $" + getBalance());
        drawAndProcessCard("You draw the [%s].");
    }
    
    private void stand() {
        System.out.println("You stand at "+getHand().getTotalValue()+".");
    }
}
