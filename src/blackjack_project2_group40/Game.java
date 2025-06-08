/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack_project2_group40;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author jonathan & phuong
 */
public class Game {
    public enum Phase { BETTING, PLAYER_TURN, DEALER_TURN, SETTLE };
    private final Deck mainDeck = Deck.createFullDeck();
    private final Deck discardDeck = new Deck();
    private final List<Person> personList;
    private int currentPersonIndex = 0;
    private int playerCount = 0;
    private int currentRound = 0;
    
    private Phase phase = Phase.PLAYER_TURN;

    public Game() {
        personList = new ArrayList<>();
    }
    
    // --- Player Setup ---
    
    public PlayerScores getPlayerScores(String name) {
        return ScoreStore.getPlayerScores(name);
    }
    
    public int getPlayerCount() {
        return playerCount;
    }
    
    public void addPlayer(String name, PlayerScores scores) {
        personList.add(new Player(mainDeck, name, scores));
        playerCount++;
    }
    
    public boolean playerNameIsTaken(String name) {
        for (Person p : personList) {
            if (p.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean playerHasExistingScores(String name) {
        return ScoreStore.playerHasExistingScores(name);
    }
    
    public void setupDealer() {
        Dealer dealer = new Dealer(mainDeck, "The dealer");
        personList.add(dealer);
    }
    
    // -- Player Turns --
    
    public int getCurrentRound() {
        return currentRound;
    }
    
    public Phase getCurrentPhase() {
        return phase;
    }
    
    public Person getCurrentPerson() {
        return personList.get(currentPersonIndex);
    }
    
    // Determines PlayerAction enum values that are currently avalilable to the player
    public List<PlayerAction> getAvailablePlayerActions() {
        Person currentPerson = getCurrentPerson();
        if (currentPerson instanceof Player player) {
            switch (phase) {
                case BETTING -> {
                    return List.of(PlayerAction.PLACE_BET);
                }
                case PLAYER_TURN -> {
                    return player.getAvailableActions();
                }
                case SETTLE -> {
                    return List.of(PlayerAction.NEXT_ROUND, PlayerAction.QUIT);
                }
                default -> {
                    return List.of();
                }
            }
        }
        // DEALER_TURN so no player actions
        return List.of();
    }
    
    public String performPlayerAction(PlayerAction action) {
        Person person = getCurrentPerson();
        if (person instanceof Player player) {
            StringBuilder log = new StringBuilder();
            switch (action) {
                case HIT -> {
                    Card drawCard = player.hit();
                    log.append(player.getName()).append(" hits and draws the ").append(drawCard).append("\n");
                    if (player.isBust()) {
                        log.append(player.getName()).append(" busts with ").append(player.getHand().getTotalValue()).append(".");
                    }
                }
                case DOUBLE_DOWN -> {
                    Card drawCard = player.doubleDown();
                    log.append(player.getName()).append(" doubles down, increasing their bet to $").append(player.getCurrentBet()).append("\n");
                    log.append(player.getName()).append(" draws the ").append(drawCard).append("\n");
                    if (player.isBust()) {
                        log.append(player.getName()).append(" busts with ").append(player.getHand().getTotalValue()).append(".");
                    }
                }
                case STAND -> {
                    player.stand();
                    log.append(player.getName()).append(" stands at ").append(player.getHand().getTotalValue()).append(".");
                }
                case QUIT -> {
                    // removes player from PersonList and reduces playerCount
                    personList.remove(player);
                    currentPersonIndex--; // Required to not skip a player when a player is deleted and iterating
                    playerCount--;
                    log.append(player.getName()).append(" left the game.");
                }
            }
            player.setLastAction(action);
            return log.toString().trim();
        }
        return "";
    }
    
    public String performDealerTurn() {
         System.out.println("Dealer turn!");
        Dealer dealer = (Dealer)getCurrentPerson();
        StringBuilder log = new StringBuilder();
        while (dealer.shouldHit()) {
            Card drawCard = dealer.hit();
            log.append(dealer.getName()).append(" hits and draws the ").append(drawCard).append("\n");
            if (dealer.isBust()) {
                log.append(dealer.getName()).append(" busts with ").append(dealer.getHand().getTotalValue()).append(".");
            }
        }
        if (!dealer.isBust()) {
            log.append(dealer.getName()).append(" stands at ").append(dealer.getHand().getTotalValue()).append(".");
        }
        advance(); // Move into SETTLE phase
        return log.toString().trim(); // Returns log to GameController for GUI
    }
    
    public void advance() {
        switch (phase) {
            case BETTING -> {
                // Move to next person in betting phase
                currentPersonIndex++;
                if (currentPersonIndex >= playerCount-1) {
                    phase = Phase.PLAYER_TURN; // All players have bet so starts player turns
                    currentPersonIndex = 0;
                }
            }
            case PLAYER_TURN -> {
                if (getCurrentPerson().getLastAction()==PlayerAction.STAND) {
                    // has stood so move to next person’s turn
                    currentPersonIndex++;
                    if (currentPersonIndex >= playerCount) {
                        phase = Phase.DEALER_TURN; // All players have done their turn so it's the dealer's turn
                    }
                }
            }
            case DEALER_TURN -> {
                // Move to settling phase (settling bets and determining winners)
                phase = Phase.SETTLE;
                currentPersonIndex = 0; // start settling from first player
            }
            case SETTLE -> {
                // Move through each player to settle bets
                currentPersonIndex++;
                if (currentPersonIndex >= playerCount) {
                    startNewRound(); // Finished settling all players so start a fresh round
                }
            }
        }
    }
    
    public void startNewRound() {
        phase = Phase.PLAYER_TURN; // CHANGE TO BETTING LATER
        currentPersonIndex = 0;
        currentRound++;
        //checkDeck();
        dealHands();
        System.out.println(getAvailablePlayerActions());
        System.out.println(getCurrentPerson().getHand());
        //playRound();
        //endRound();
    }
    
    private void dealHands() {
        for (Person person : personList) {
            person.getHand().clear();
            for (int i = 0; i < 2; i++) { // Adds two cards to each person's deck
                person.getHand().addCard(mainDeck.drawCard());
            }
        }
    }
    
    
    
    
    
    /*
    // Advance from “betting” phase to “deal” phase. 
    public void dealInitialHands() { ... }

    // Play a single round of player turns. 
    public void playPlayers() { ... }

    // Let the dealer play. 
    public void playDealer() { ... }

    // Compute wins/losses, update scores, discard hands. 
    public void settleBets() { ... }

    // Check deck size and reshuffle from discard if needed. 
    public void refillDeckIfNeeded() { ... }

    // Snapshot of everything the view needs to render right now. 
    public GameState getState() { … }

    // What UI buttons/actions are valid right now. 
    public List<PlayerAction> getAvailableActions() { … }

    // Perform the given action (HIT, STAND, NEXT_ROUND, QUIT). 
    public String performAction(PlayerAction action) { … }
    */
    
    /*
    private final Scanner scan;
    private static int currentRound;
    private Dealer dealer;
    private final Deck mainDeck;
    private final Deck discardDeck;
    private final List<Person> personList;
    private List<Person> remainingPlayers;

    public Game() {
        scan = new Scanner(System.in);
        currentRound = 0;
        mainDeck = Deck.createFullDeck();
        mainDeck.shuffleDeck();
        discardDeck = new Deck();
        personList = new ArrayList<>();
    }

    private void printGameTitle() {
        System.out.println("  ____  _            _     _            _    _ ");
        System.out.println(" | __ )| | __ _  ___| | __(_) __ _  ___| | _| |       ");
        System.out.println(" |  _ \\| |/ _` |/ __| |/ /| |/ _` |/ __| |/ / |       ");
        System.out.println(" | |_) | | (_| | (__|   < | | (_| | (__|   <|_|       ");
        System.out.println(" |____/|_|\\__,_|\\___|_|\\_\\/ |\\__,_|\\___|_|\\_(_)       ");
        System.out.println("                        |__/                          ");
        System.out.println("By Jonathan & Phuong");
        System.out.println("------------------------------------------------------------------------");
    }

    public void play() {
        printGameTitle();
        setupPlayers(); //Adds new Player objects to personList
        dealer = new Dealer(mainDeck, "The dealer");
        personList.add(dealer);
        doGameLoop();
    }

    private void setupPlayers() {
        int playerCount = requestPlayerCount();
        List<String> playerNames = requestPlayerNames(playerCount);

        //Load existing scores from file
        Map<String, PlayerScores> loadScores = ScoreStore.loadScores();

        for (String name : playerNames) {
            PlayerScores scores;

            //Confirm if the player already exists by using confirmExistingPlayer in ScoreStore class
            scores = ScoreStore.confirmExistingPlayer(name, scan);
            while (scores == null) { //not their record, new player
                System.out.println("Looks like someone else is using that name. Please enter a different name for yourself!");
                name = scan.nextLine().trim();
                scores = ScoreStore.confirmExistingPlayer(name, scan);
            }

            //Add the player to the game
            Player player = new Player(mainDeck, name, scores);
            personList.add(player);
        }
        for (Person person : personList) {
            System.out.println(person.getName() + ": " + person.getScores());
        }
    }

    //Count how many players
    private int requestPlayerCount() {
        int countPlayer;
        while (true) {
            System.out.println("Enter the number of players (1-7): ");
            if (scan.hasNextInt()) {
                countPlayer = scan.nextInt();
                scan.nextLine();
                if (countPlayer >= 1 && countPlayer <= 7) {
                    return countPlayer;
                } else {
                    System.out.println("Please enter a number of player between 1 and 7");
                }
            } else {
                System.out.println("Invalid input. Please enter a number: ");
                scan.nextLine();
            }
        }
    }

    //Prompts user for names of each player
    private List<String> requestPlayerNames(int playerCount) {
        List<String> names = new ArrayList<>();
        System.out.println("Please enter player " + (playerCount == 1 ? "name: " : "names: "));

        for (int i = 0; i < playerCount; i++) {
            String playerName;
            boolean isValid = false; //Clearer than using do while loop, suggested by chatGPT

            while (!isValid) {
                System.out.println("Player " + (i + 1) + ": ");
                playerName = scan.nextLine().trim();

                //Check if it is an empty name
                if (playerName.isEmpty()) {
                    System.out.println("Name can't be empty. Please enter your name.");
                    continue;
                }

                //Check if names only numbers
                if (playerName.matches("\\d+")) {
                    System.out.println("Names cannot be only numbers. Please enter a valid name.");
                    continue;
                }

                //Check if it is a duplicate name
                if (names.contains(playerName)) {
                    System.out.println("Looks like someone else is using that name. Please enter a different name for yourself!");
                    continue;
                }

                //If name is valid
                names.add(playerName);
                isValid = true;
            }
        }
        return names;
    }

    //Game loop
    private void doGameLoop() {
        for (Person person : personList) {
            if (person instanceof Player player && player.isActive()) {
                player.promptForBet();
            }
        }
        
        while (true) {
            currentRound++;
            System.out.println("------------------------------------------------------------------------");
            System.out.println("Round " + currentRound + "...");
            checkDeck();
            dealHands();
            playRound();
            endRound();
            
            //After each round, asking if the player want to either continue or quit the game
            if (!askContinueGame()) { 
                break; //Ending the game completely
            }
        }
    }
    
    private boolean askContinueGame() {
        boolean anyPlayerWantToContinue = false;
        boolean allPlayerQuit = true;
        
        for (Person person : personList)
            if (person instanceof Player player) {
                if (!player.isActive()) {
                    continue;
                }
                //Asking if player wants to continue or quit
                System.out.println("------------------------------------------------------------------------");
                System.out.println(person.getName() + ", would you like to play another round? (y/n or 'x' to quit the game)");
                
                String answer = scan.nextLine().trim().toLowerCase();
                
                if (answer.equalsIgnoreCase("x") || answer.equalsIgnoreCase("n")) {
                    System.out.println(person.getName() + " has chosen to quit the game.\nThanks for playing. See you again soon!");
                    ((Player) person).quit();
                } else if (answer.equalsIgnoreCase("y")) {
                    System.out.println(person.getName() + " continues to play the next round.");
                    anyPlayerWantToContinue = true;
                    allPlayerQuit = false;
                } else {
                    System.out.println("Invalid input. Please enter 'y' to continue, 'n' or 'x' to quit the game.");
                    return askContinueGame();
                }
            }
        
        if (allPlayerQuit) {
            return false;
        }
        
        return anyPlayerWantToContinue;
    }

    //Deal cards to players and dealer
    private void dealHands() {
        for (Person person : personList) {
            person.getHand().clear();
            for (int i = 0; i < 2; i++) {
                person.getHand().addCard(mainDeck.drawCard());
            }
            person.updateHandResults();
        }
    }

    //Play round method
    private void playRound() {
        remainingPlayers = new ArrayList<>(personList); //copy from personList so can remove freely if people get busted or quit the game

        // iterator allows removing from from list while iterating through it.
        Iterator<Person> iterator = remainingPlayers.iterator();
        while (iterator.hasNext()) {
            Person person = iterator.next();

            //Skip dealer, doing players first
            if (person instanceof Dealer || !((Player) person).isActive()) {
                continue;
            }
            System.out.println("------------------------------------------------------------------------");

            //Player takes turn
            person.playTurn();

            //Remove if they chose to stand, quite or are bust
            if (person.isBust() || person.getLastAction() == PlayerAction.STAND || person.getLastAction() == PlayerAction.QUIT) {
                iterator.remove();
            }
        }

        //Let dealer to playturn
        for (Person person : personList) {
            if (person instanceof Dealer dealerTurn) {
                System.out.println("------------------------------------------------------------------------");
                dealerTurn.playTurn();
            }
        }
    }

    private void endRound() {
        //Print dealer's final hand
        System.out.println("------------------------------------------------------------------------");
        System.out.println("Dealer's final hand:");
        System.out.println(dealer.getHand() + ". Total value: " + dealer.getHand().getTotalValue());
        
        for (Person person : personList) {
            if (person instanceof Dealer || person.getLastAction() == PlayerAction.QUIT || !((Player) person).isActive()) {
                continue;
            }

            GameRules.Result result = GameRules.getPlayerResult(person, dealer);

            switch (result) {
                case WIN -> {
                    System.out.println(person.getName() + ", you win!");
                    person.getScores().incrementWins();
                    ((Player) person).winBet();
                }
                case LOSE -> {
                    System.out.println(person.getName() + ", you lose!");
                    person.getScores().incrementLosses();
                    ((Player) person).loseBet();
                }
                case PUSH -> {
                    System.out.println(person.getName() + ", you push!");
                    person.getScores().incrementPushes();
                    ((Player) person).pushBet();
                }
            }
            System.out.println("You now have a total of " + person.getScores());
            ScoreStore.updatePlayerScore(person.getName(), person.getScores());

            //Move cards to discard deck
            person.getHand().discardHandToDeck(discardDeck);
            person.getHand().clear();
        }

        dealer.getHand().discardHandToDeck(discardDeck);
        dealer.getHand().clear();
        remainingPlayers.clear();
    }

    //Check if deck is nearly empty
    private void checkDeck() {
        //If deck runs low on cards. Lower than 5 cards, reload from discard pile
        if (mainDeck.getCards().size() < 24) {
            System.out.println("Main deck is low on cards...");
            mainDeck.reloadDeck(discardDeck);
        }
    }
    */

}
