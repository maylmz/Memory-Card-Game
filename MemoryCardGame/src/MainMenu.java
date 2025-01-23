import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainMenu extends JFrame {
    int boardWidth = 800;
    int boardHeight = 600;
    Image background;

    JFrame mainMenuFrame;

    MainMenu() {
        mainMenuFrame = new JFrame("Memory Card Game");
        mainMenuFrame.setSize(boardWidth, boardHeight);
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenuFrame.setLocationRelativeTo(null);

        ImageIcon icon = new ImageIcon("src/background.jpg");
        background = icon.getImage();

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(null);

        JLabel titleLabel = new JLabel("Memory Card Game");
        titleLabel.setBounds(250, 20, 300, 50);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.CYAN);

        JButton startButton = new JButton("Start Game");
        startButton.setBounds(300, 100, 200, 50);
        startButton.addActionListener(e -> {
            mainMenuFrame.dispose();
            new Game(1, 0);
        });

        JButton levelButton = new JButton("Select Level");
        levelButton.setBounds(300, 200, 200, 50);
        levelButton.addActionListener(e -> {
            mainMenuFrame.dispose();
            new SelectLevel();
        });

        JButton instructionsButton = new JButton("Instructions");
        instructionsButton.setBounds(300, 300, 200, 50);
        instructionsButton.addActionListener(e -> new Instructions());

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(300, 400, 200, 50);
        exitButton.addActionListener(e -> System.exit(0));

        backgroundPanel.add(titleLabel);
        backgroundPanel.add(startButton);
        backgroundPanel.add(levelButton);
        backgroundPanel.add(instructionsButton);
        backgroundPanel.add(exitButton);

        mainMenuFrame.setContentPane(backgroundPanel);
        mainMenuFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainMenu::new);
    }
}
