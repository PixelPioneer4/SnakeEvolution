package com.snakegame.view;

import com.snakegame.service.HighscoreManager;
import com.snakegame.service.HighscoreManager.ScoreEntry;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.util.List;

public class HighscorePanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private HighscoreManager highscoreManager;

    public HighscorePanel(GameFrame frame) {
        this.highscoreManager = new HighscoreManager();
        setLayout(new BorderLayout());

        String[] columnNames = {"Name", "Score"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton backButton = new JButton("Zurück zum Login");
        backButton.addActionListener(e -> frame.showLogin());
        add(backButton, BorderLayout.SOUTH);
    }

    public void loadHighscores() {
        tableModel.setRowCount(0);
        List<ScoreEntry> scores = highscoreManager.loadScores();
        for (ScoreEntry entry : scores) {
            tableModel.addRow(new Object[]{entry.getName(), entry.getScore()});
        }
    }
}
