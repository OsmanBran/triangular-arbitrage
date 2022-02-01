import static java.lang.Math.min;

public class Arbitrage {
    Market marketX; // BTC-AUD
    Market marketY; // ETH-AUD
    Market marketXY; // ETH-BTC

    public Arbitrage (String marketX, String marketY, String marketXY){
        this.marketX = new Market(marketX);
        this.marketY = new Market(marketY);
        this.marketXY = new Market(marketXY);
    }

    /**
     * Calls the latest asks and bids from each market and checks if arbitrage is available
     */
    public void checkArbitrage () throws Exception {
        BidAsk xBidAsk = marketX.getBidAsk();
        BidAsk yBidAsk = marketY.getBidAsk();
        BidAsk xyBidAsk = marketXY.getBidAsk();

        BidAsk crossRate = getCrossRate(xBidAsk, yBidAsk);

        int result = compareCrossRate(crossRate, xyBidAsk);
        if (result == 1){
            System.out.println(getProfitX(xBidAsk.ask, xyBidAsk.ask, yBidAsk.bid));
        }
        else if (result == -1){
            System.out.println(getProfitY(yBidAsk.ask, xyBidAsk.bid, xBidAsk.bid));
        }
        else {
            System.out.println("No arbitrage available");
        }
    }

    /**
     * Returns the cross rate of two quotes
     * @param x e.g BTC/AUD
     * @param y e.g ETH/AUD
     * @return cross rate for BTC/ETH
     */
    private static BidAsk getCrossRate (BidAsk x, BidAsk y) {
        float bidPrice = (1 / x.ask.price) / (1 / y.bid.price);
        float askPrice = (1 / x.bid.price) / (1 / y.bid.price);
        Order bid = new Order(bidPrice, 0);
        Order ask = new Order(askPrice, 0);

        return new BidAsk(bid, ask);
    }

    private static int compareCrossRate (BidAsk cross, BidAsk current) {
        // Current ask is higher than cross bid -> buy X with AUD
        if (current.ask.price > cross.bid.price){
            return 1;
        }
        // Current bid is higher than cross ask -> buy Y with AUD
        else if (current.bid.price > cross.ask.price){
            return -1;
        }
        // No arbitrage available
        else {
            return 0;
        }
    }

    /**
     * Returns the possible profit by purchasing X and converting via the XY market
     * @param XAsk
     * @param XYAsk
     * @param YBid
     * @return
     */
    private static float getProfitX(Order XAsk, Order XYAsk, Order YBid) {
        float capital = 10000; //TODO: replace with volume

        float x = capital / XAsk.price;
        float y = x / XYAsk.price;
        float aud = y * YBid.price;
        return aud - capital;
    }

    /**
     * Returns the possible profit by purchasing Y and converting via the XY market
     * @param YAsk
     * @param XYBid
     * @param XBid
     * @return
     */
    private static float getProfitY(Order YAsk, Order XYBid, Order XBid) {
        float capital = 10000;

        float y = capital / YAsk.price;
        float x = y * XYBid.price;
        float aud = x * XBid.price;
        return aud - capital;
    }
}
