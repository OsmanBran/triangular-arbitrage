public class MockMarket extends Market{
    private Order bid;
    private Order ask;

    public MockMarket(String ticker) {
        super(ticker);
        bid = new Order(0, 0);
        ask = new Order(0, 0);
    }

    @Override
    public BidAsk getBidAsk(){
        return new BidAsk(bid, ask);
    }

    public MockMarket setBid(double price, double volume){
        bid = new Order(price, volume);
        return this;
    }

    public MockMarket setAsk(double price, double volume){
        ask = new Order(price, volume);
        return this;
    }
}
