package com.snakegame.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;

public class GameFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    private LoginPanel loginPanel;
    private GamePanel gamePanel;
    private HighscorePanel highscorePanel;

    public GameFrame() {
        setTitle("Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        loginPanel = new LoginPanel(this);
        gamePanel = new GamePanel(this);
        highscorePanel = new HighscorePanel(this);

        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(gamePanel, "GAME");
        mainPanel.add(highscorePanel, "HIGHSCORE");

        add(mainPanel);
        
        // Passt die Größe an das bevorzugte Layout an (GamePanel hat 600x600)
        pack();
        setLocationRelativeTo(null);
    }

    public void showLogin() {
        cardLayout.show(mainPanel, "LOGIN");
    }

    public void startGame(String playerName) {
        cardLayout.show(mainPanel, "GAME");
        gamePanel.startGame(playerName);
    }

    public void showHighscores() {
        highscorePanel.loadHighscores();
        cardLayout.show(mainPanel, "HIGHSCORE");
    }
}
