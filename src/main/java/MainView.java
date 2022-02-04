import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView {
    private JFrame frame;
    private MarketTable marketTable;
    Arbitrage arb;

    private void initialiseModel(){
        arb = new Arbitrage("BTC-AUD", "ETH-AUD", "ETH-BTC");
    }

    public MainView() {
        initialiseModel();

        // Initialise frame
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Check button
        JButton checkButton = new JButton("Check arbitrage");
        checkButton.addActionListener(new checkArbitrageListener());
        frame.add(checkButton, BorderLayout.PAGE_START);

        // Middle pane shows market orders
        marketTable = new MarketTable();
        frame.add(marketTable, BorderLayout.CENTER);

        // Lower pane shows actions

        // Display window
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    public class checkArbitrageListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                arb.checkArbitrage();
                marketTable.setData(arb.getMarketData());
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Something went wrong",
                        "Message", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
