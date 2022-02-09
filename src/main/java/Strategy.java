import static java.lang.Math.min;

public class Strategy {
    private String[] values = {"AUD", "", "", "AUD", "AUD"};
    private String[] labels = {"Initial Capital","","","","Profit"};
    private String X;
    private String Y;
    private boolean profitable;

    private void addLabelX(){
        values[1] = X;
        values[2] = Y;
        labels[1] = "buy " + X;
        labels[2] = "buy " + Y;
        labels[3] = "sell " + Y;
    }

    private void addLabelY(){
        values[1] = Y;
        values[2] = X;
        labels[1] = "buy " + Y;
        labels[2] = "buy " + X;
        labels[3] = "sell " + X;
    }

    public void update(boolean xOvervalued, Order trade1, Order trade2, Order trade3){
        profitable = true;
        values = new String[]{"AUD", "", "", "AUD", "AUD"};
        labels = new String[]{"Initial Capital", "", "", "", "Profit"};

        if (xOvervalued){
            addLabelX();
            recordProfitX(trade1, trade2, trade3);
        }
        else {
            addLabelY();
            recordProfitY(trade1, trade2, trade3);
        }
    }

    private void recordProfitX(Order XAsk, Order XYAsk, Order YBid) {
        double capital = calculateCapitalX(XAsk, XYAsk, YBid);
        values[0] = valueString(capital, values[0]);

        double x = capital / XAsk.price;
        values[1] = valueString(x, values[1]);
        appendTradePrice(1, XAsk.price, "AUD");

        double y = x / XYAsk.price;
        values[2] = valueString(y, values[2]);
        appendTradePrice(2, XYAsk.price, Y);

        double aud = y * YBid.price;
        values[3] = valueString(aud, values[3]);
        appendTradePrice(3, YBid.price, "AUD");

        double profit = aud - capital;
        values[4] = valueString(profit, values[4]);
    }

    private void recordProfitY(Order YAsk, Order XYBid, Order XBid) {
        double capital = calculateCapitalY(YAsk, XYBid, XBid);
        values[0] = valueString(capital, values[0]);

        double y = capital / YAsk.price;
        values[1] = valueString(y, values[1]);
        appendTradePrice(1, YAsk.price, "AUD");

        double x = y * XYBid.price;
        values[2] = valueString(x, values[2]);
        appendTradePrice(2, XYBid.price, Y);

        double aud = x * XBid.price;
        values[3] = valueString(aud, values[3]);
        appendTradePrice(3, XBid.price, "AUD");

        double profit = aud - capital;
        values[4] = valueString(profit, values[4]);
    }

    private double calculateCapitalX(Order XAsk, Order XYAsk, Order YBid){
        // Find max amount of Y sold
        double maxYSold = min(YBid.volume, XYAsk.volume);

        // Find max amount of X bought & sold
        double maxXSold = min(maxYSold * XYAsk.price, XAsk.volume);

        return maxXSold * XAsk.price;
    }


    private double calculateCapitalY(Order YAsk, Order XYBid, Order XBid){
        // Find max amount of X Sold
        double maxXSold = min(XBid.volume, XYBid.volume * XYBid.price);

        // Find max amount of Y bought & sold
        double maxYSold = min(maxXSold / XYBid.price, YAsk.volume);

        return maxYSold * YAsk.price;
    }

    private static String valueString(double value, String currency){
        return String.format("%,.2f", value) + currency;
    }

    private void appendTradePrice(int strIndex, double price, String currency){
        labels[strIndex] = labels[strIndex] + " @" + String.format("%,.2f", price) + currency;
    }

    public void setMarketStrings(String X, String Y){
        this.X = X;
        this.Y = Y;
    }

    public String[] getLabels() {
        return labels;
    }

    public String[] getValues() {
        return values;
    }

    public void setProfitability(boolean profitability){
        profitable = profitability;
    }

    public boolean isProfitable(){
        return profitable;
    }

    public String getProfit(){
        if (profitable){
            return values[4];
        }
        else {
            return "0 AUD";
        }
    }
}
