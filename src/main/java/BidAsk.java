public class BidAsk {
    Order bid;
    Order ask;

    public BidAsk(Order bid, Order ask) {
        this.bid = bid;
        this.ask = ask;
    }

    @Override
    public String toString() {
        return "BidAsk{" +
                "bid=" + bid +
                ", ask=" + ask +
                '}';
    }
}
