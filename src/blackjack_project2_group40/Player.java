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
public class Player extends Person {
    private final Scanner scan = new Scanner(System.in);
    private List<Action> availableActions;
    
    public Player(Deck deck, String name, PlayerScores scores) {
        super(deck, name);
        setScores(scores);
    }
    
    @Override
    public void playTurn() {
        resetLastAction(); //Using method from Person
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
        while (getLastAction() != Action.STAND && !isBust() && getLastAction() != Action.QUIT) {
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
        /*if (handResults.canSplit) availableActions.add(Action.SPLIT);
        if (handResults.canDoubleDown) availableActions.add(Action.DOUBLE_DOWN);*/
    }
    
    private Action promptForAction() {
        while (true) {
            //Asking users to choose available actions
            System.out.println("Would you like to: ");
            for (int i = 0; i < availableActions.size(); i++) {
                System.out.printf("(%d) %s%n", i + 1, availableActions.get(i));
            }
            
            //Let them enter their choice or x to exit
            System.out.println("Enter your choice (or enter 'x' to exit): ");
            String input = scan.nextLine().trim();
            if (input.equalsIgnoreCase("x")) {
//                System.out.println("Thanks for playing. See you again soon!");
                return Action.QUIT;
            }
            
            try {
                int inputChoice = Integer.parseInt(input);
                if (inputChoice >= 1 && inputChoice <= availableActions.size()) {
                    return availableActions.get(inputChoice - 1);
                }
            } catch (NumberFormatException e) {
                //Let fall through
            }
            
            System.out.printf("Invalid input. Please enter a number between 1 and %d, or 'x' to quit.%n", availableActions.size());   
            
        }
    }
    
    private void performAction(Action action) {
        if (action == Action.QUIT) {
            System.out.println(getName() + " has left the game.");
            setLastAction(Action.QUIT);
            return;
        }
        
        switch (action) {
            case HIT -> hit();
            case STAND -> stand();
            /*case SPLIT -> split();
            case DOUBLE_DOWN -> doubleDown();*/
        }
        setLastAction(action);   
    }
    
    private void hit() {
        Card drawCard = getDeck().drawCard();
        getHand().addCard(drawCard);
        System.out.println("You hit and draw the [" + drawCard + "].");
        printHandDetails();
        
        updateHandResults(); //Update if isBust()
        
        if (isBust()) {
            System.out.println("You are bust!");
        }
    }
    
    private void stand() {
        System.out.println("You stand at "+getHand().getTotalValue()+".");
    }
}
