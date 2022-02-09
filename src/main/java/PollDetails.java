import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class PollDetails extends JPanel {
    private JLabel details;

    public PollDetails(){
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        Border etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        Border space = new EmptyBorder(10,10,10,10);
        Border compound = BorderFactory.createCompoundBorder(etched, space);
        compound = BorderFactory.createCompoundBorder(space, compound);

        JLabel label1 = new JLabel("Polling every 10 seconds...");
        label1.setBorder(space);

        details = new JLabel("");
        details.setBorder(compound);

        this.add(label1);
        this.add(details);
    }

    public void updatePoll(BidAsk cross, BidAsk market, Strategy result){
        details.setText(
                "<html>last market spread: " + MainView.spreadString(market) + "<br/>"
                + "last cross spread: " + MainView.spreadString(cross) + "<br/>"
                + "last profit:  " + result.getProfit() + "</html>"
        );
    }
}
