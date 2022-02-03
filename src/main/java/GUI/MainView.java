package GUI;

import javax.swing.*;
import java.awt.*;

public class MainView {
    private JFrame frame;

    public MainView() {
        // Initialise frame
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Check button
        JButton checkButton = new JButton("Check arbitrage");
        frame.add(checkButton, BorderLayout.PAGE_START);

        // Middle pane shows market orders


        // Lower pane shows actions


        // Display window
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
}
