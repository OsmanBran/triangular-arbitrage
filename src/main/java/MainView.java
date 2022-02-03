import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView {
    private JButton check;
    private JTable marketTable;
    private JPanel panel;
    private JList actions;

    private Arbitrage arb;

    public MainView() {
        arb = new Arbitrage("BTC-AUD", "ETH-AUD", "ETH-BTC");

        check.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    arb.checkArbitrage();
                } catch (Exception E) {
                    JOptionPane.showMessageDialog(panel, "The Server is not available now. Please try again!",
                            "System Message", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainView");
        frame.setContentPane(new MainView().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
