import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class PollDetails extends JPanel {
    private JLabel lastPollDetails;
    private JLabel pollDetails;

    public PollDetails(){
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        Border etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        Border space = new EmptyBorder(10,10,10,10);
        Border compound = BorderFactory.createCompoundBorder(etched, space);
        compound = BorderFactory.createCompoundBorder(space, compound);

        JLabel label1 = new JLabel("Polling every 10 seconds...");
        label1.setBorder(space);

        lastPollDetails = new JLabel("");
        lastPollDetails.setBorder(compound);

        pollDetails = new JLabel("");
        pollDetails.setBorder(compound);

        this.add(label1);
        this.add(lastPollDetails);
        this.add(pollDetails);
    }

    public void updateDetails(BidAsk cross, BidAsk market, Strategy result, Poll poll){
        updateLastTrade(cross, market, result);
        updatePollDetails(poll);
    }

    private void updateLastTrade(BidAsk cross, BidAsk market, Strategy result){
        lastPollDetails.setText(
                "<html>Last market spread: " + Utils.spreadString(market) + "<br/>"
                        + "Last cross spread: " + Utils.spreadString(cross) + "<br/>"
                        + "Last profit:  " + Utils.valueString(result.getProfit()) + "</html>"
        );
    }

    private void updatePollDetails(Poll poll){
        pollDetails.setText(
                "<html>Total polls: " + poll.getTotalPolls() + "<br/>"
                        + "Profitable polls: " + poll.getProfitablePolls() + "<br/>"
                        + "Total profit:  " + poll.getTotalProfit() + "</html>"
        );
    }
}
