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
    private ResultsPanel resultsPanel;
    Arbitrage arb = new Arbitrage();

    public MainView() {
        // Initialise frame
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Settings pane
        settingsPanel = new SettingsPanel();
        frame.add(settingsPanel, BorderLayout.PAGE_START);

        // Top pane shows market orders
        JPanel centrePanel = new JPanel();
        centrePanel.setLayout(new BoxLayout(centrePanel, BoxLayout.PAGE_AXIS));
        marketTable = new MarketTable();
        centrePanel.add(marketTable);

        // Lower pane shows actions
        strategyTable = new StrategyTable();
        resultsPanel = new ResultsPanel();
        centrePanel.add(resultsPanel);

        frame.add(centrePanel, BorderLayout.CENTER);

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

            JButton pollButton = new JButton("Poll arbitrage");
            // pollButton.addActionListener();
            this.add(pollButton);

            this.setBorder(new EmptyBorder(10, 10, 10, 10));
        }
    }

    public class ResultsPanel extends JPanel {
        private CardLayout cardModel;
        private FailMessage failMessage;

        public ResultsPanel() {
            // Set this to fail message initially
            this.setPreferredSize(new Dimension(200, 300));
            cardModel = new CardLayout();
            this.setLayout(cardModel);

            JLabel defaultText = new JLabel("Press \"Check\" or \"Poll\" to begin", SwingConstants.CENTER);
            this.add(defaultText, "default");

            failMessage = new FailMessage();
            this.setAlignmentX(failMessage.CENTER_ALIGNMENT);
            this.add(failMessage, "fail");

            StrategyTable checkSuccessTable = strategyTable;
            this.add(checkSuccessTable, "success");

            JPanel pollDetails = new JPanel();
            this.add(pollDetails, "poll");

            cardModel.show(this, "default");
        }

        public void displayCheckResults(Strategy result){
            if (result.isProfitable()){
                strategyTable.setData(result);
                cardModel.show(this,"success");
            }
            else {
                failMessage.setMessage(arb.getCrossSpread(), arb.getMarketSpread());
                cardModel.show(this,"fail");
            }
        }

        public void displayPollResults() {
            cardModel.show(this,"poll");
        }
    }

    public class checkArbitrageListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                arb.setMarkets((String) settingsPanel.marketDropdown.getSelectedItem());
                Strategy result = arb.checkArbitrage();
                resultsPanel.displayCheckResults(result);

                marketTable.setData(arb.getMarketData());
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(),
                        "Message", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public class pollArbitrageListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // initialise tracking variables

                // update GUI to only show stop poll

                // update GUI to show current statistics

                // begin polling every 10 seconds (use new thread?)
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
