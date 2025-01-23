import javax.swing.*;
import java.awt.*;

public class SelectLevel extends JFrame {
    public SelectLevel() {
        setTitle("Select Level");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel levelPanel = new JPanel();
        levelPanel.setLayout(new GridLayout(3, 1));

        JButton level1Button = new JButton("Level 1");
        level1Button.addActionListener(e -> {
            dispose();
            new Game(1, 0);
        });

        JButton level2Button = new JButton("Level 2");
        level2Button.addActionListener(e -> {
            dispose();
            new Game(2, 0);
        });

        JButton level3Button = new JButton("Level 3");
        level3Button.addActionListener(e -> {
            dispose();
            new Game(3, 0);
        });

        levelPanel.add(level1Button);
        levelPanel.add(level2Button);
        levelPanel.add(level3Button);

        add(levelPanel, BorderLayout.CENTER);
        setVisible(true);
    }
}
