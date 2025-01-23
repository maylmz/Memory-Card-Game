import javax.swing.*;
import java.awt.*;

public class Instructions extends JFrame {
    public Instructions() {
        setTitle("Instructions");
        setSize(500, 100);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTextArea instructionsText = new JTextArea();
        instructionsText.setText("Instructions:\nThere are 3 levels in game. It gets gradually harder! Match all pairs of cards to win!");
        instructionsText.setEditable(false);

        add(new JScrollPane(instructionsText), BorderLayout.CENTER);
        setVisible(true);
    }
}
