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

/**
 *
 * @author Jonathan
 */
public class GameGUI implements GameView {
    private final JFrame frame = new JFrame("Blackjack");
    private final JTextArea log = new JTextArea(10,30);
    private final JPanel buttons = new JPanel();
    
    // Setup Panel
    private final JPanel setupPanel  = new JPanel();
    private final JTextField nameField  = new JTextField(15);
    private final JButton joinButton  = new JButton("Join");
    private final JButton startGameButton  = new JButton("Start Game");

    public GameGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        // --- Title Label ---
        JLabel titleLabel = new JLabel("Welcome to Blackjack", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28)); // Make it big and bold
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding
        frame.add(titleLabel, BorderLayout.PAGE_START);
        
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
        rulesArea.setEditable(false);
        rulesArea.setText(rulesText);
        JScrollPane rulesScroll = new JScrollPane(rulesArea);
        rulesScroll.setBorder(BorderFactory.createTitledBorder("Rules"));
        rulesArea.setOpaque(false);
        centerPanel.add(rulesScroll);
        
        // --- Log panel (right) ---
        log.setEditable(false);
        log.setLineWrap(true);
        log.setWrapStyleWord(true);
        centerPanel.add(new JScrollPane(log));
        
        frame.add(centerPanel, BorderLayout.CENTER);
        
        // --- Setup panel (bottom) ---
        setupPanel.add(new JLabel("Enter player name:"));
        setupPanel.add(nameField);
        setupPanel.add(joinButton);
        setupPanel.add(startGameButton);
        frame.add(setupPanel, BorderLayout.SOUTH);
        
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
        // --- Action buttons at bottom (empty for now) ---
        //frame.add(buttons, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    // --- HELPER FUNCTIONS ---

    // --- Messaging ---
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
    
    // --- Player Setup ---
    @Override
    public void setPlayerSetupHandler(ActionListener listener) {
        joinButton.setActionCommand("Join"); // to differentiate it from others
        joinButton.addActionListener(listener); // plain and simple
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
    public void clearPlayerSetup() {
        // Remove that panel once setup is complete
        frame.remove(setupPanel);
        frame.revalidate();
        frame.repaint();
    }
    
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

    private String pretty(PlayerAction act) {
        String s = act.name().toLowerCase().replace('_',' ');
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    } 
    
}