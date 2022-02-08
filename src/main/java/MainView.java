import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView {
    private JFrame frame;
    private MarketTable marketTable;
    private StrategyTable strategyTable;
    private SettingsPanel settingsPanel;
    Arbitrage arb = new Arbitrage();

    public MainView() {
        // Initialise frame
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Settings pane
        settingsPanel = new SettingsPanel();
        frame.add(settingsPanel, BorderLayout.PAGE_START);

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

    public class SettingsPanel extends JPanel {
        JComboBox marketDropdown;

        SettingsPanel(){
            this.setLayout(new GridLayout(1,0));
            String[] marketStrings = {"BTC-ETH", "BTC-LTC", "BTC-XRP"};
            marketDropdown = new JComboBox(marketStrings);
            this.add(marketDropdown);

            JButton checkButton = new JButton("Check arbitrage");
            checkButton.addActionListener(new checkArbitrageListener());
            this.add(checkButton);
            this.setBorder(new EmptyBorder(10, 10, 10, 10));
        }
    }

    public class checkArbitrageListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                arb.setMarkets((String) settingsPanel.marketDropdown.getSelectedItem());
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
