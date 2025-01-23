import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import javax.swing.SwingWorker;

public class Game extends JFrame {
    private List<JButton> cards;
    private int pairsFound;
    private int triesLeft;
    private int level;
    private int score;
    private int currentScore;
    private JLabel triesLeftLabel;
    private JLabel scoreLabel;
    private List<ImageIcon> cardImages;
    private ImageIcon cardBackImage;
    private JButton firstSelectedCard;
    private JButton secondSelectedCard;
    private Timer timer;
    private static final String HIGH_SCORES_FILE = "high_scores.txt";
    private static final int MAX_HIGH_SCORES = 10;

    public Game(int level, int currentScore) {
        this.level = level;
        this.pairsFound = 0;
        this.triesLeft = getInitialTries(level);
        this.score = 0;
        this.currentScore = currentScore;
        this.cardImages = loadCardImages(level);
        this.cardBackImage = loadCardBackImage(level);

        if (cardImages.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No images found for level " + level, "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setTitle("Memory Card Game - Level " + level);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        createMenuBar();

        JPanel gamePanel = new JPanel(new GridLayout(4, 4));

        triesLeftLabel = new JLabel("Tries Left: " + triesLeft);
        scoreLabel = new JLabel("Score: " + currentScore);

        Font customFont = new Font("Arial", Font.BOLD, 18);
        triesLeftLabel.setFont(customFont);
        scoreLabel.setFont(customFont);
             
        triesLeftLabel.setOpaque(false);
        scoreLabel.setOpaque(false);
             
        triesLeftLabel.setForeground(Color.white);
        scoreLabel.setForeground(Color.white);

        JPanel statusPanel = new JPanel();
        statusPanel.add(triesLeftLabel);
        statusPanel.add(scoreLabel);
        statusPanel.setBackground(Color.MAGENTA);
             
        add(statusPanel, BorderLayout.NORTH);
        add(gamePanel, BorderLayout.CENTER);

        cards = createCards(level);
        Collections.shuffle(cards);
        for (JButton card : cards) {
            gamePanel.add(card);
        }

        setVisible(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Game");
        JMenuItem restartItem = new JMenuItem("Restart");
        restartItem.addActionListener(e -> restartGame());
        JMenuItem highScoresItem = new JMenuItem("High Scores");
        highScoresItem.addActionListener(e -> showHighScores());
        gameMenu.add(restartItem);
        gameMenu.add(highScoresItem);

        JMenu aboutMenu = new JMenu("About");
        JMenuItem aboutGameItem = new JMenuItem("About the Game");
        aboutGameItem.addActionListener(e -> showAboutGame());
        JMenuItem aboutDeveloperItem = new JMenuItem("About the Developer");
        aboutDeveloperItem.addActionListener(e -> showAboutDeveloper());
        aboutMenu.add(aboutGameItem);
        aboutMenu.add(aboutDeveloperItem);

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        menuBar.add(gameMenu);
        menuBar.add(aboutMenu);
        menuBar.add(exitItem);

        setJMenuBar(menuBar);
    }

    private void restartGame() {
        dispose();
        new Game(level, currentScore);
    }

    private void showHighScores() {
        List<String> highScores = readHighScores();
        StringBuilder message = new StringBuilder("High Scores:\n");
        for (String score : highScores) {
            message.append(score).append("\n");
        }
        JOptionPane.showMessageDialog(this, message.toString());
    }

    private void showAboutGame() {
        JOptionPane.showMessageDialog(this, "This is a memory card game. Find all the matching pairs to win.");
    }

    private void showAboutDeveloper() {
        JOptionPane.showMessageDialog(this, "Developed by: Mustafa Alp Yılmaz\nStudent Number: 20220702080");
    }

    private int getInitialTries(int level) {
        switch (level) {
            case 1: return 18;
            case 2: return 15;
            case 3: return 12;
            default: return 18;
        }
    }

    private List<ImageIcon> loadCardImages(int level) {
        List<ImageIcon> images = new ArrayList<>();
        String directory = "src/Level" + level;
        File dir = new File(directory);
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                try {
                    images.add(new ImageIcon(ImageIO.read(file)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return images;
    }

    private ImageIcon loadCardBackImage(int level) {
        try {
            return new ImageIcon(ImageIO.read(new File("src/Level" + level + "/no_image.png")));
        } catch (IOException e) {
            e.printStackTrace();
            return new ImageIcon(new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB));
        }
    }

    private List<JButton> createCards(int level) {
        List<JButton> buttons = new ArrayList<>();
        int pairCount = 8;
        for (int i = 0; i < pairCount; i++) {
            ImageIcon image = cardImages.get(i % cardImages.size());
            JButton button1 = createCardButton(image);
            JButton button2 = createCardButton(image);
            buttons.add(button1);
            buttons.add(button2);
        }
        return buttons;
    }

    private JButton createCardButton(ImageIcon image) {
        JButton button = new JButton();
        button.setIcon(cardBackImage);
        button.putClientProperty("image", image);
        button.putClientProperty("flipped", false);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCardClick(button);
            }
        });
        return button;
    }

    private void handleCardClick(JButton card) {
        if ((boolean) card.getClientProperty("flipped")) {
            return;
        }
        
        card.setIcon((ImageIcon) card.getClientProperty("image"));
        card.putClientProperty("flipped", true);

        if (firstSelectedCard == null) {
            firstSelectedCard = card;
        } else if (secondSelectedCard == null && card != firstSelectedCard) {
            secondSelectedCard = card;
            checkForMatch();
        }
    }

    private void checkForMatch() {
        removeCardListeners();
        if (firstSelectedCard.getClientProperty("image").equals(secondSelectedCard.getClientProperty("image"))) {
            pairsFound++;
            score += getMatchPoints(level);
            currentScore += getMatchPoints(level);
            firstSelectedCard.setEnabled(false);
            secondSelectedCard.setEnabled(false);
            firstSelectedCard = null;
            secondSelectedCard = null;
            updateLabels();
            if (pairsFound == 8) {
                showVictoryMessage();
            } else {
                addCardListeners();
            }
        } else {
            triesLeft--;
            score -= getMismatchPenalty(level);
            currentScore -= getMismatchPenalty(level);
            updateLabels();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    resetSelectedCards();
                    if (triesLeft <= 0) {
                        showDefeatMessage();
                    } else {
                        addCardListeners();
                    }
                }
            }, 1000);
            if (level == 3) {
                shuffleCards();
            }
        }
    }

    private void removeCardListeners() {
        for (JButton card : cards) {
            for (ActionListener al : card.getActionListeners()) {
                card.removeActionListener(al);
            }
        }
    }

    private void addCardListeners() {
        for (JButton card : cards) {
            if (!((boolean) card.getClientProperty("flipped"))) {
                card.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleCardClick(card);
                    }
                });
            }
        }
    }

    private void shuffleCards() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Thread.sleep(1000);
                Collections.shuffle(cards);
                return null;
            }

            @Override
            protected void done() {
                JPanel gamePanel = (JPanel) getContentPane().getComponent(1);
                gamePanel.removeAll();
                for (JButton card : cards) {
                    card.setIcon(cardBackImage);
                    card.putClientProperty("flipped", false);
                    gamePanel.add(card);
                }
                gamePanel.revalidate();
                gamePanel.repaint();
            }
        };
        worker.execute();
    }

    private void resetSelectedCards() {
        if (firstSelectedCard != null) {
            firstSelectedCard.setIcon(cardBackImage);
            firstSelectedCard.putClientProperty("flipped", false);
            firstSelectedCard = null;
        }
        if (secondSelectedCard != null) {
            secondSelectedCard.setIcon(cardBackImage);
            secondSelectedCard.putClientProperty("flipped", false);
            secondSelectedCard = null;
        }
    }

    private void updateLabels() {
        triesLeftLabel.setText("Tries Left: " + triesLeft);
        scoreLabel.setText("Score: " + currentScore);
    }

    private void showVictoryMessage() {
        if (level < 3) {
            JOptionPane.showMessageDialog(this, "You won this level! Moving to the next level.");
            new Game(level + 1, currentScore);
        } else {
            String playerName = JOptionPane.showInputDialog(this, "You won the game! Your total score is " + currentScore + ". Enter your name:");
            if (playerName != null && !playerName.trim().isEmpty()) {
                saveHighScore(playerName, currentScore);
            }
            new Game(1, 0);
        }
        dispose();
    }    

    private void showDefeatMessage() {
        String playerName = JOptionPane.showInputDialog(this, "You lost! Your total score is " + currentScore + ". Enter your name:");
        if (playerName != null && !playerName.trim().isEmpty()) {
            saveHighScore(playerName, currentScore);
        }
        new Game(1, 0);
        dispose();
    }

    private int getMatchPoints(int level) {
        switch (level) {
            case 1: return 5;
            case 2: return 4;
            case 3: return 3;
            default: return 5;
        }
    }

    private int getMismatchPenalty(int level) {
        switch (level) {
            case 1: return 1;
            case 2: return 2;
            case 3: return 3;
            default: return 1;
        }
    }
    
    private void saveHighScore(String playerName, int score) {
        try {
            List<String> highScores = readHighScores();
            highScores.add(playerName + ":" + score);
            highScores = highScores.stream()
                                   .sorted(Comparator.comparingInt(this::extractScore).reversed())
                                   .limit(MAX_HIGH_SCORES)
                                   .collect(Collectors.toList());
            try (FileWriter writer = new FileWriter(HIGH_SCORES_FILE)) {
                for (String highScore : highScores) {
                    writer.write(highScore + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private List<String> readHighScores() {
        List<String> highScores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(HIGH_SCORES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                highScores.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return highScores;
    }
    
    private int extractScore(String highScore) {
        String[] parts = highScore.split(":");
        return Integer.parseInt(parts[1]);
    }
}
