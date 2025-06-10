/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack_project2_group40;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jonathan & phuong
 * 
 * Game class acts as the primary model the GameController interacts with,
 * it acts as a bridge between GameController and all the other models (e.g. Player), and contains methods for general game logic.
 */
public class Game {
    public enum Phase { BETTING, PLAYER_TURN, DEALER_TURN, SETTLE };
    private final Deck mainDeck = Deck.createFullDeck();
    private final Deck discardDeck = new Deck();
    private final List<Person> personList;
    private int currentPersonIndex = 0;
    private int playerCount = 0;
    private int currentRound = 0;
    
    private Phase phase = Phase.BETTING;

    public Game() {
        personList = new ArrayList<>();
    }
    
    public int getCurrentRound() {
        return currentRound;
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
    
    private void setupDealer() {
        Dealer dealer = new Dealer(mainDeck, "Dealer");
        personList.add(dealer);
    }
    
    // -- Player Turns --
    
    public Person getCurrentPerson() {
        return personList.get(currentPersonIndex);
    }
    
    public boolean betExceedsRange(double amount) {
        Person currentPerson = getCurrentPerson();
        if (currentPerson instanceof Player player) {
            return player.betExceedsRange(amount);
        } else {
            throw new IllegalStateException("Current person is not a Player");
        }
    }
    
    public boolean betExceedsBalance(double amount) {
        Person currentPerson = getCurrentPerson();
        if (currentPerson instanceof Player player) {
            return player.betExceedsBalance(amount);
        } else {
            throw new IllegalStateException("Current person is not a Player");
        }
    }
    
    public void placeBet(double amount) {
        Person person = getCurrentPerson();
        if (person instanceof Player player) {
            player.placeBet(amount);
        }
    }
    
    public GameData getCurrentGameData() {
        Person dealer = personList.get(playerCount); // Dealer always last in list
        Person person = getCurrentPerson();
        GameData gameData = new GameData();
        
        // Current Round Data
        gameData.currentRound = currentRound;
        gameData.currentPhase = phase;
        gameData.playerCount = playerCount;
        
        // Current Person Data
        gameData.currentPersonHand = person.getHand();
        gameData.currentPersonHandValue = person.getHand().getTotalValue();
        gameData.currentPersonName = person.getName();
        gameData.currentPersonAvailableActions = getAvailablePlayerActions();
        gameData.currentPersonLastAction = person.getLastAction();
        gameData.currentPersonIsBust = person.isBust();
        
        // Dealer Data
        gameData.dealerName = dealer.getName();
        gameData.dealerHand = dealer.getHand();
        gameData.dealerHandValue = dealer.getHand().getTotalValue();
        gameData.dealerIsBust = dealer.isBust();
        
        // Player Data
        if (person instanceof Player player) {
            gameData.currentPlayerBalance = player.getBalance();
            gameData.currentPlayerBet = player.getCurrentBet();
            gameData.currentPlayerResult = GameRules.getPlayerResult(player, dealer);
        }
        return gameData;
    }
    
    // Determines PlayerAction enum values that are currently avalilable to the player
    private List<PlayerAction> getAvailablePlayerActions() {
        Person currentPerson = getCurrentPerson();
        switch (phase) {
            case BETTING -> {
                return List.of(PlayerAction.PLACE_BET);
            }
            case PLAYER_TURN -> {
                if (currentPerson instanceof Player player) return player.getAvailableActions();
            }
            case DEALER_TURN -> {
                if (currentPerson instanceof Dealer) return List.of(PlayerAction.DEALER_CONTINUE);
            }
            case SETTLE -> {
                return List.of(PlayerAction.NEXT_ROUND, PlayerAction.QUIT);
            }
            default -> {
                return List.of();
            }
        }
        return List.of();
    }
    
    // Runs a single player action depending on input from GameController
    public List<String> performPlayerAction(PlayerAction action) {
        List<String> log = new ArrayList<>();
        Person person = getCurrentPerson();
        if (person instanceof Player player) {
            switch (action) {
                case HIT -> {
                    Card drawCard = player.hit();
                    log.add(player.getName() + ": Hits and draws the " + drawCard + ".");
                    if (player.isBust()) {
                        log.add(player.getName() + ": Busts with " + player.getHand().getTotalValue() + ".");
                    }
                }
                case DOUBLE_DOWN -> {
                    Card drawCard = player.doubleDown();
                    log.add(player.getName() + ": Doubles down, increasing their bet to $" + player.getCurrentBet());
                    log.add(player.getName() + ": Draws the " + drawCard);
                    if (player.isBust()) {
                        log.add(player.getName() + ": Busts with " + player.getHand().getTotalValue() + ".");
                    }
                }
                case STAND -> {
                    player.stand();
                    log.add(player.getName() + ": Stands at " + player.getHand().getTotalValue() + ".");
                }
                case QUIT -> {
                    // removes player from PersonList and reduces playerCount
                    personList.remove(player);
                    currentPersonIndex--; // Required to not skip a player when a player is deleted and iterating
                    playerCount--;
                    log.add(player.getName() + ": Left the game.");
                }
            }
            player.setLastAction(action);
        }
        return log;
    }
    
    // Runs the dealer's turn (different logic from players)
    public List<String> performDealerTurn() {
        List<String> log = new ArrayList<>();
        Person person = getCurrentPerson();
        if (person instanceof Dealer dealer) {
            while (dealer.shouldHit()) {
                Card drawCard = dealer.hit();
                log.add(dealer.getName() + ": Hits and draws the " + drawCard);
                if (dealer.isBust()) {
                    log.add(dealer.getName() + ": Busts with " + dealer.getHand().getTotalValue() + ".");
                }
            }
            if (!dealer.isBust()) {
                log.add(dealer.getName() + ": Stands at " + dealer.getHand().getTotalValue() + ".");
            }

        } else {
            throw new IllegalStateException("Current person is not the Dealer");
        }
        return log;
    }
    
    // Moves the game forward to the next step. If all players haven't finished their turn,
    // it continues to the next player. Otherwise, continue to the next game phase (e.g. SETTLE, BETTING, etc.)
    public void advance() {
        switch (phase) {
            case BETTING -> {
                // Move to next person in betting phase
                currentPersonIndex++;
                if (currentPersonIndex >= playerCount) {
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
                phase = Phase.SETTLE;
                currentPersonIndex = 0;
                if (currentPersonIndex > playerCount) {
                     // Move to settling phase (settling bets and determining winners)
                     // start settling from first player
                }
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
    
    public void startNewGame() {
        mainDeck.shuffleDeck();
        setupDealer();
        startNewRound();
    }
    
    public void clearGame() {
        personList.clear();
        playerCount = 0;
    }
    
    // Reset for the next round
    private void startNewRound() {
        phase = Phase.BETTING;
        currentPersonIndex = 0;
        currentRound++;
        for (Person p : personList) {
            p.clearLastAction();
            if (p instanceof Player player) {
                player.clearBet();
            }
        }
        checkDeck();
        dealHands();
    }
    
    private void checkDeck() {
        //If deck runs low on cards. Lower than 5 cards, reload from discard pile
        if (mainDeck.getCards().size() < 24) {
            mainDeck.reloadDeck(discardDeck);
        }
    }
    
    
    private void dealHands() {
        for (Person person : personList) {
            // Moves cards to discard deck before dealing new ones.
            person.getHand().discardHandToDeck(discardDeck); 
            person.getHand().clear();
            // Deals two cards to each Person's hand
            for (int i = 0; i < 2; i++) {
                person.getHand().addCard(mainDeck.drawCard());
            }
        }
    }
    
    public void settleBets() {
        Dealer dealer = ((Dealer) personList.get(playerCount));
        for (Person person : personList) {
            if (person instanceof Player player) {
                GameRules.Result result = GameRules.getPlayerResult(player, dealer);
                switch (result) {
                    case WIN -> {
                        player.getScores().incrementWins();
                        player.winBet();
                    }
                    case LOSE -> {
                        player.getScores().incrementLosses();
                        player.loseBet();
                    }
                    case PUSH -> {
                        player.getScores().incrementPushes();
                        player.pushBet();
                    }
                }
                ScoreStore.updatePlayerScore(player.getName(), player.getScores());
            }
        }
    }
}
    
    
    /*
    OLD CODE FOR REFERENCE - TO DELETE

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
