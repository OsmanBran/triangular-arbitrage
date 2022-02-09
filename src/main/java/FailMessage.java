import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class FailMessage extends JPanel {
    private JLabel crossSpread;
    private JLabel marketSpread;

    public FailMessage() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JLabel label1 = new JLabel("<html>No arbitrage opportunity available<br/>Cross rate and market spreads overlap</html>");
        marketSpread = new JLabel("");
        crossSpread = new JLabel("");

        label1.setBorder(new EmptyBorder(10,10,0,0));
        this.add(label1);
        marketSpread.setBorder(new EmptyBorder(0,10,0,0));
        this.add(marketSpread);
        crossSpread.setBorder(new EmptyBorder(0,10,0,0));
        this.add(crossSpread);
    }

    public void setMessage(BidAsk cross, BidAsk market){
        marketSpread.setText("Market Spread: " + MainView.spreadString(market));
        crossSpread.setText("Cross Spread:  " + MainView.spreadString(cross));
    }
}
