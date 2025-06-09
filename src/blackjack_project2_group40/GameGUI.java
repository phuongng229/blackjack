/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack_project2_group40;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.text.NumberFormatter;

/**
 *
 * @author Jonathan
 * 
 * GameGUI class is a Swing implementation of GameView that:
 * - Builds the JFrame, panels, labels, text fields, buttons, etc.
 * - Uses CardLayout to swap UI regions for different game states (startPanel, gamePanel)
 * - Renders log messages to the player log JTextArea.
 * - Manages enabling/disabling buttons based on input.
 * - Forwards user actions (button presses) to the controller via ActionListeners.
 * - etc.
 * 
 * UI wiring and layout logic goes here - no game rules or flow control are implemented in this class.
 * 
 */
public class GameGUI implements GameView {
    private final JFrame frame = new JFrame("Blackjack");
    // Using card layouts to allow switching between start and game screens without reloading GUI every time.
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);
    
    // Panels
    private final JPanel startPanel = new JPanel();
    private final JPanel gamePanel = new JPanel(new BorderLayout());
    
    //Components for startPanel
    private final JTextArea log = new JTextArea(10,30);
    private final JLabel playerCountLabel = new JLabel("Players: 0/"+GameRules.MAX_PLAYERS);
    private final JTextField nameField  = new JTextField(15);
    private final JButton joinButton  = new JButton("Join");
    private final JButton startGameButton  = new JButton("Start Game");
    private final JButton homeButton  = new JButton("Home");
    
    // Components for gamePanel
    private final JFormattedTextField betField;
    private final JLabel betFieldLabel = new JLabel("Amount: $");
    private final JPanel buttons = new JPanel();
    private final JLabel currentPersonBalance = new JLabel();
    private final JLabel currentPersonBet = new JLabel();
    private final JLabel actionTitle = new JLabel("Would you like to?");
    
    private final Map<String, ImageIcon> cardIconMap = new HashMap<>();
    private final JPanel playerHandPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
    private final JPanel dealerHandPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
    
    public GameGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);      
        
        // Uses formatter to create betField in currency format, suggested by ChatGPT
        // Needs to be in constructor as betField is final
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setGroupingUsed(false);
        NumberFormatter formatter = new NumberFormatter(decimalFormat);
        formatter.setValueClass(Double.class);
        formatter.setAllowsInvalid(false); // prevents letters or symbols
        formatter.setCommitsOnValidEdit(true);
        betField = new JFormattedTextField(formatter);
        betField.setColumns(6);
        
        loadCardIcons();
        setupStartPanel();
        setupGamePanel();
        
        mainPanel.add(startPanel, "Start");
        mainPanel.add(gamePanel, "Game");

        frame.setContentPane(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private void loadCardIcons() {
        for (Suit suit : Suit.values()) {
            for (Face face : Face.values()) {
                String code = face.getCode() +"-"+ suit.getCode();  // e.g. "A-S"
                String path = "/images/cards/" + code + ".png";
                java.net.URL imgURL = getClass().getResource(path);
                if (imgURL != null) {
                    cardIconMap.put(code, new ImageIcon(imgURL));
                } else {
                    System.err.println("Missing image: " + path);
                }
            }
        }
    }
    
    private void setupStartPanel() {
        startPanel.setLayout(new BorderLayout());
        
        // --- Title Label ---
        JLabel titleLabel = new JLabel("Welcome to Blackjack", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // adds padding
        startPanel.add(titleLabel, BorderLayout.NORTH);
        
        // --- Center panel (Rules + Log) ---
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        
        // --- Game rules panel (left) ---
        String rulesText = ("- Maximum of " + GameRules.MAX_PLAYERS + " players\n"
            + "- Try to get 21 without going over\n"
            + "- Dealer hits until 17+\n"
            + "- Aces can be 1 or 11\n"
            + "- Face cards are worth 10\n"
        );
        JTextArea rulesArea = new JTextArea();
        rulesArea.setText(rulesText);
        rulesArea.setEditable(false);
        JScrollPane rulesScroll = new JScrollPane(rulesArea);
        rulesScroll.setBorder(BorderFactory.createTitledBorder("Rules"));
        rulesArea.setOpaque(false);
        centerPanel.add(rulesScroll);
        
        // --- Log panel (right) ---
        log.setEditable(false);
        log.setLineWrap(true);
        log.setWrapStyleWord(true);
        centerPanel.add(new JScrollPane(log));
        
        startPanel.add(centerPanel, BorderLayout.CENTER);
        
        // --- Input panel (bottom) ---
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Enter player name:"));
        inputPanel.add(nameField);
        inputPanel.add(joinButton);
        inputPanel.add(startGameButton);
        inputPanel.add(playerCountLabel);
        startGameButton.setEnabled(false);
        joinButton.setEnabled(false); // initially false until text is entered into name field
        nameField.getDocument().addDocumentListener(new DocumentListener() { // Event listener on nameField to toggle joinButton enabled/disabled
            @Override
            public void insertUpdate(DocumentEvent e) { update(); }
            @Override
            public void removeUpdate(DocumentEvent e) { update(); }
            @Override
            public void changedUpdate(DocumentEvent e) { update(); }
            private void update() {
                String text = nameField.getText().trim();
                joinButton.setEnabled(!text.isEmpty());
            }
        });

        startPanel.add(inputPanel, BorderLayout.SOUTH);
    }

    private void setupGamePanel() {
        JPanel detailsPanel = new JPanel();
        detailsPanel.add(currentPersonBalance);
        detailsPanel.add(currentPersonBet);
        
        
        // Load the background image for the handsWrapper
        ImageIcon handsBgIcon = new ImageIcon(getClass().getResource("/images/background.jpg"));
        Image handsBg = handsBgIcon.getImage();

        // Create the background panel with no forced layout
        JPanel handsWrapper = new ImageBackgroundPanel(handsBg, null);

        // Set the layout you want to use (preserving original layout)
        handsWrapper.setLayout(new BoxLayout(handsWrapper, BoxLayout.Y_AXIS));
        handsWrapper.setOpaque(false);
        dealerHandPanel.setOpaque(false);
        playerHandPanel.setOpaque(false);

        
        // Dealer’s hand at top
        JLabel dealerLabel = new JLabel("Dealer’s Hand");
        handsWrapper.add(dealerLabel);
        handsWrapper.add(dealerHandPanel);
        
        // Player’s hand below
        JLabel playerLabel = new JLabel("Your Hand");
        handsWrapper.add(playerLabel);
        handsWrapper.add(playerHandPanel);
        
        gamePanel.add(handsWrapper, BorderLayout.CENTER);
        
        // --- Input panel (bottom) ---
        JPanel southPanel = new JPanel(new BorderLayout(0, 8)); // hgap=0px, vgap=8px (between NORTH and CENTER)
        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        actionTitle.setHorizontalAlignment(SwingConstants.CENTER);
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 0));
        
        // --- Input Panel ---
        //JPanel inputPanel = new JPanel(new BorderLayout());
        JPanel inputPanelLeft = new JPanel();
        JPanel inputPanelCenter = new JPanel();
        JPanel inputPanelRight = new JPanel();
        
        inputPanelLeft.add(homeButton);
        inputPanelCenter.add(betFieldLabel);
        inputPanelCenter.add(betField);
        inputPanelCenter.add(buttons);
        
        JPanel inputPanel = buildInputPanel(inputPanelLeft, inputPanelCenter, inputPanelRight);
        
        dealerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dealerHandPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerHandPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        southPanel.add(detailsPanel, BorderLayout.NORTH);
        southPanel.add(actionTitle, BorderLayout.CENTER);
        southPanel.add(inputPanel, BorderLayout.SOUTH);
        
        gamePanel.add(southPanel, BorderLayout.SOUTH);
        
        
        
        
        
        
        
    }
    
    private JPanel buildInputPanel(JComponent leftComponent, JComponent centerComponent, JComponent rightComponent) {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // LEFT WRAPPER
        JPanel leftWrapper = new JPanel(new BorderLayout());
        leftWrapper.add(leftComponent, BorderLayout.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(leftWrapper, gbc);

        // CENTER
        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(centerComponent, gbc);

        // RIGHT WRAPPER
        JPanel rightWrapper = new JPanel(new BorderLayout());
        rightWrapper.add(rightComponent, BorderLayout.CENTER);
        gbc.gridx = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(rightWrapper, gbc);

        return inputPanel;
    }
    
    
    
    // ---- View Implementations ----
    
    @Override
    public void showStartScreen() {
        cardLayout.show(mainPanel, "Start");
    }
    @Override
    public void showGameScreen() {
        cardLayout.show(mainPanel, "Game");
    }

    @Override
    public void showMessage(String message) {
        log.append(message + "\n");
    }
    @Override
    public boolean promptYesNo(String message) {
        int result = JOptionPane.showConfirmDialog(
            frame,
            message,
            "Please Confirm",
            JOptionPane.YES_NO_OPTION
        );
        return result == JOptionPane.YES_OPTION;
    }
    
    // --- Start Panel ---
    @Override
    public void setGameHandler(ActionListener listener) {
        joinButton.setActionCommand("Join");
        joinButton.addActionListener(listener);
        startGameButton.setActionCommand("Start");
        startGameButton.addActionListener(listener);
        homeButton.setActionCommand("Home");
        homeButton.addActionListener(listener);
    }
    @Override
    public String getEnteredPlayerName() {
        return nameField.getText().trim();
    }
    @Override
    public void clearPlayerNameField() {
        nameField.setText("");
    }
    @Override
    public void updatePlayerCount(int count) {
        playerCountLabel.setText("Players: " + count+"/"+GameRules.MAX_PLAYERS);
        startGameButton.setEnabled(count > 0);
    }

    // --- Game Panel ---
    @Override
    public void setActionButtons(List<PlayerAction> actions, ActionListener listener) {
        buttons.removeAll();
        for (PlayerAction action : actions) {
            JButton btn = new JButton(pretty(action));
            btn.setActionCommand(action.name());
            btn.addActionListener(listener);
            buttons.add(btn);
        }
        buttons.revalidate();
        buttons.repaint();
    }
    @Override
    public void showBetInput(boolean visible) {
        betFieldLabel.setVisible(visible);
        betField.setVisible(visible);
    }
    @Override
    public double getEnteredBetAmount() {
        Object value = betField.getValue();
        if (value instanceof Number n) {
            return n.doubleValue();
        }
        return 0.0; 
    }
    @Override
    public void clearBetAmountField() {
        betField.setValue(null);
    }
    @Override
    public void setCurrentPersonBalance(double balance) {
        currentPersonBalance.setText("Balance: $" + balance);
    }
    @Override
    public void setCurrentPersonBet(double bet) {
        currentPersonBet.setText("Current Bet: $" + bet);
    }
    @Override
    public void setActionTitle(String text) {
        actionTitle.setText(text);
    }
    @Override
    public void displayPlayerHand(List<Card> cards) {
        renderCardsInPanel(cards, playerHandPanel);
    }
    @Override
    public void displayDealerHand(List<Card> cards) {
        renderCardsInPanel(cards, dealerHandPanel);
    }
    // Shared helper
    private void renderCardsInPanel(List<Card> cards, JPanel panel) {
        panel.removeAll();
        
        int height = (panel == playerHandPanel) ? 120 : 80; 
        /*
        for (Card card : cards) {
            String code = card.getFace().getCode() +"-"+ card.getSuit().getCode();
            ImageIcon icon = cardIconMap.get(code);
            Image scaled = icon.getImage().getScaledInstance(80, -1, Image.SCALE_SMOOTH);
            JLabel lbl = new JLabel(new ImageIcon(scaled));
            panel.add(lbl);
        }
        */
        for (Card card : cards) {
            String code = card.getFace().getCode() + "-" + card.getSuit().getCode();
            ImageIcon icon = cardIconMap.get(code);
            Image img = icon.getImage().getScaledInstance(-1, height, Image.SCALE_SMOOTH);
            panel.add(new JLabel(new ImageIcon(img)));
        }
        panel.revalidate();
        panel.repaint();
    }
    
    private String pretty(PlayerAction act) {
        String s = act.name().toLowerCase().replace('_',' ');
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    } 
    
}