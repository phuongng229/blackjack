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
    
    // Components for gamePanel
    private final JFormattedTextField betField;
    private final JLabel betFieldLabel = new JLabel("Amount: $");
    private final JPanel buttons = new JPanel();
    private final JLabel currentPersonName = new JLabel("");
    private final JLabel currentPersonBalance = new JLabel();
    private final JLabel currentPersonBet = new JLabel();
    private final JLabel actionTitle = new JLabel("Would you like to?");
    
    public GameGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);        
        
        // Uses formatter to create betField in currency format
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setGroupingUsed(false);
        NumberFormatter formatter = new NumberFormatter(decimalFormat);
        formatter.setValueClass(Double.class);
        formatter.setAllowsInvalid(false); // prevents letters or symbols
        formatter.setCommitsOnValidEdit(true);
        betField = new JFormattedTextField(formatter);
        betField.setColumns(6);
        
        setupStartPanel();
        setupGamePanel();
        
        mainPanel.add(startPanel, "Start");
        mainPanel.add(gamePanel, "Game");

        frame.setContentPane(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
        
        
    private void setupStartPanel() {
        startPanel.setLayout(new BorderLayout());
        
        // --- Title Label ---
        JLabel titleLabel = new JLabel("Welcome to Blackjack", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding
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
        detailsPanel.add(currentPersonName);
        detailsPanel.add(currentPersonBalance);
        detailsPanel.add(currentPersonBet);
        gamePanel.add(detailsPanel, BorderLayout.NORTH);
        
        // --- Input panel (bottom) ---
        JPanel southPanel = new JPanel(new BorderLayout(0, 8)); // hgap=0px, vgap=8px (between NORTH and CENTER)
        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        actionTitle.setHorizontalAlignment(SwingConstants.CENTER);
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 0));
        
        JPanel inputPanel = new JPanel();
        inputPanel.add(betFieldLabel);
        inputPanel.add(betField);
        inputPanel.add(buttons);
        
        southPanel.add(actionTitle, BorderLayout.NORTH);
        southPanel.add(inputPanel, BorderLayout.CENTER);
        gamePanel.add(southPanel, BorderLayout.SOUTH);
    }
    
    
    
    // ---- HELPER FUNCTIONS ----
    
    
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
    public void setPlayerSetupHandler(ActionListener listener) {
        joinButton.setActionCommand("Join");
        joinButton.addActionListener(listener);
        startGameButton.setActionCommand("Start");
        startGameButton.addActionListener(listener);
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
    public void setCurrentPersonName(String name) {
        currentPersonName.setText(name);
    }
    @Override
    public void setCurrentPersonBalance(double balance) {
        currentPersonBalance.setText("Balance: " + balance);
    }
    @Override
    public void setCurrentPersonBet(double bet) {
        currentPersonBet.setText("Current Bet: " + bet);
    }
    @Override
    public void setActionTitle(String text) {
        actionTitle.setText(text);
    }
    
    private String pretty(PlayerAction act) {
        String s = act.name().toLowerCase().replace('_',' ');
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    } 
    
}