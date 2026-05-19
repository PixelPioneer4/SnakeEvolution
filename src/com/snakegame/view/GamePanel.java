package com.snakegame.view;

import com.snakegame.model.Food;
import com.snakegame.model.Player;
import com.snakegame.model.Snake;
import com.snakegame.service.HighscoreManager;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    private static final int TILE_SIZE = 20;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int DELAY = 130;

    private Snake snake;
    private Food food;
    private Player player;
    private Timer timer;
    private boolean running = false;
    private GameFrame frame;
    private HighscoreManager highscoreManager;

    public GamePanel(GameFrame frame) {
        this.frame = frame;
        this.highscoreManager = new HighscoreManager();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());
    }

    public void startGame(String playerName) {
        player = new Player(playerName);
        snake = new Snake(WIDTH / TILE_SIZE / 2, HEIGHT / TILE_SIZE / 2, 3);
        spawnFood();
        running = true;
        if (timer != null) timer.stop();
        timer = new Timer(DELAY, this);
        timer.start();
        requestFocusInWindow();
    }

    private void spawnFood() {
        Random random = new Random();
        int x = random.nextInt(WIDTH / TILE_SIZE);
        int y = random.nextInt(HEIGHT / TILE_SIZE);
        food = new Food(x, y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (running) {
            // Essen zeichnen
            g.setColor(Color.RED);
            g.fillRect(food.getX() * TILE_SIZE, food.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);

            // Schlange zeichnen
            g.setColor(Color.GREEN);
            for (Point p : snake.getBody()) {
                g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }

            // Score zeichnen
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("Score: " + player.getScore(), 10, 20);
        } else if (player != null) {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        String msg = "Game Over";
        Font font = new Font("Arial", Font.BOLD, 40);
        FontMetrics metrics = getFontMetrics(font);
        
        g.setColor(Color.RED);
        g.setFont(font);
        g.drawString(msg, (WIDTH - metrics.stringWidth(msg)) / 2, HEIGHT / 2);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String scoreMsg = "Final Score: " + player.getScore();
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString(scoreMsg, (WIDTH - metrics2.stringWidth(scoreMsg)) / 2, HEIGHT / 2 + 40);
        
        String continueMsg = "Press ENTER to view Highscores";
        g.drawString(continueMsg, (WIDTH - metrics2.stringWidth(continueMsg)) / 2, HEIGHT / 2 + 80);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            checkCollisions();
            if (running) {
                if (checkFood()) {
                    snake.grow();
                    spawnFood();
                } else {
                    snake.move();
                }
            }
        }
        repaint();
    }

    private boolean checkFood() {
        Point head = snake.getHead();
        int nextX = head.x + snake.getDx();
        int nextY = head.y + snake.getDy();
        if (nextX == food.getX() && nextY == food.getY()) {
            player.addScore(10);
            return true;
        }
        return false;
    }

    private void checkCollisions() {
        Point head = snake.getHead();
        int nextX = head.x + snake.getDx();
        int nextY = head.y + snake.getDy();

        // Wandkollision
        if (nextX < 0 || nextX >= WIDTH / TILE_SIZE || nextY < 0 || nextY >= HEIGHT / TILE_SIZE) {
            running = false;
        }

        // Selbstkollision
        for (Point p : snake.getBody()) {
            if (p.x == nextX && p.y == nextY) {
                running = false;
                break;
            }
        }

        if (!running) {
            timer.stop();
            highscoreManager.saveScore(player.getName(), player.getScore());
        }
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (!running && key == KeyEvent.VK_ENTER) {
                frame.showHighscores();
                return;
            }
            if (!running) return;

            int dx = snake.getDx();
            int dy = snake.getDy();

            // Verhindern, dass die Schlange direkt in die entgegengesetzte Richtung läuft
            if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && dx != 1) {
                snake.setDirection(-1, 0);
            } else if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && dx != -1) {
                snake.setDirection(1, 0);
            } else if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && dy != 1) {
                snake.setDirection(0, -1);
            } else if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && dy != -1) {
                snake.setDirection(0, 1);
            }
        }
    }
}
