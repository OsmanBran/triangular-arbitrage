import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView {
    private JFrame frame;
    private MarketTable marketTable;
    private StrategyTable strategyTable;
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
        strategyTable = new StrategyTable();
        frame.add(strategyTable, BorderLayout.PAGE_END);

        // Display window
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    public class checkArbitrageListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Strategy result = arb.checkArbitrage();
                if (result.isProfitable()){
                    strategyTable.clearFailMessage();
                    strategyTable.setData(result);
                }
                else {
                    strategyTable.displayFailure(result.getFailMessage());
                }

                marketTable.setData(arb.getMarketData());
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(),
                        "Message", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new MainView();
    }
}
