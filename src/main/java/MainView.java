import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

public class MainView {
    private JFrame frame;
    private MarketTable marketTable;
    private StrategyTable strategyTable;
    private SettingsPanel settingsPanel;
    private ResultsPanel resultsPanel;

    private Arbitrage arb;
    private boolean isPolling = false;
    private Poll poll = new Poll();

    public MainView() {
        arb = new Arbitrage("BTC-ETH");

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
        private CardLayout cardModel;
        JComboBox marketDropdown;

        SettingsPanel(){
            cardModel = new CardLayout();
            this.setLayout(cardModel);

            JPanel defaultPanel = initDefaultView();
            this.add(defaultPanel);

            JPanel pollPanel = initPollView();
            this.add(pollPanel);
        }

        private JPanel initDefaultView(){
            JPanel panel = new JPanel();

            panel.setLayout(new GridLayout(1,0));
            String[] marketStrings = {"BTC-ETH", "BTC-LTC", "BTC-XRP"};
            marketDropdown = new JComboBox(marketStrings);
            marketDropdown.addActionListener(new getMarketListener());
            panel.add(marketDropdown);

            JButton checkButton = new JButton("Check arbitrage");
            checkButton.addActionListener(new checkArbitrageListener());
            panel.add(checkButton);

            JButton pollButton = new JButton("Poll arbitrage");
            pollButton.addActionListener(new pollArbitrageListener());
            panel.add(pollButton);

            panel.setBorder(new EmptyBorder(10, 10, 10, 10));

            return panel;
        }

        private JPanel initPollView(){
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());

            JButton stopPoll = new JButton("Stop polling");
            stopPoll.addActionListener(new stopPollListener());
            panel.add(stopPoll, BorderLayout.CENTER);

            return panel;
        }

        public void togglePoll(){
            cardModel.previous(this);
        }
    }

    public class ResultsPanel extends JPanel {
        private CardLayout cardModel;
        private FailMessage failMessage;
        private PollDetails pollDetails;

        public ResultsPanel() {
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

            pollDetails = new PollDetails();
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

        public void displayPollResults(Strategy result) {
            pollDetails.updateDetails(arb.getCrossSpread(), arb.getMarketSpread(), result, poll);
            cardModel.show(this,"poll");
        }
    }

    public class getMarketListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            arb.setMarkets((String) settingsPanel.marketDropdown.getSelectedItem());
        }
    }

    public class checkArbitrageListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
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
                settingsPanel.togglePoll();

                // begin polling every 10 seconds until poll stopped (use new thread?)
                Thread pollThread = new Thread(new pollArbitrage());
                pollThread.start();
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(),
                        "Message", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public class stopPollListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // stop the thread
            isPolling = false;

            // change setting display
            settingsPanel.togglePoll();
        }
    }

    public class pollArbitrage implements Runnable {
        public void run(){
            isPolling = true;
            poll.clearPoll();

            while(isPolling){
                Strategy result = null;
                try {
                    result = arb.checkArbitrage();
                    poll.updatePoll(result);

                    resultsPanel.displayPollResults(result);

                    marketTable.setData(arb.getMarketData());
                    TimeUnit.SECONDS.sleep(10);
                }
                catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(),
                            "Message", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new MainView();
    }
}
