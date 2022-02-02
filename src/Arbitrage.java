import static java.lang.Math.min;

public class Arbitrage {
    Market marketX; // BTC-AUD
    Market marketY; // ETH-AUD
    Market marketXY; // ETH-BTC (Y should be the base and X the quote)

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

        // Compare cross rate with current XY rates
        // Current ask is higher than cross bid -> buy X with AUD
        if (xyBidAsk.ask.price > crossRate.bid.price){
            System.out.println(getProfitX(xBidAsk.ask, xyBidAsk.ask, yBidAsk.bid));
        }
        // Current bid is higher than cross ask -> buy Y with AUD
        else if (xyBidAsk.bid.price > crossRate.ask.price){
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

    /**
     * Returns the possible profit by purchasing X, converting to Y on XY market, and selling Y back to AUD
     * @param XAsk
     * @param XYAsk
     * @param YBid
     * @return
     */
    private static float getProfitX(Order XAsk, Order XYAsk, Order YBid) {
        float capital = calculateCapitalX(XAsk, XYAsk, YBid);

        float x = capital / XAsk.price;
        float y = x / XYAsk.price;
        float aud = y * YBid.price;
        return aud - capital;
    }

    /**
     * Returns the possible profit by purchasing Y, converting to X on XY market, and selling X back to AUD
     * @param YAsk
     * @param XYBid
     * @param XBid
     * @return
     */
    private static float getProfitY(Order YAsk, Order XYBid, Order XBid) {
        float capital = calculateCapitalY(YAsk, XYBid, XBid);

        float y = capital / YAsk.price;
        float x = y * XYBid.price;
        float aud = x * XBid.price;
        return aud - capital;
    }

    /**
     * Returns the amount of AUD that can fill the maximum amount of volume for the X-buying strategy
     * @param XAsk
     * @param XYAsk
     * @param YBid
     * @return
     */
    private static float calculateCapitalX(Order XAsk, Order XYAsk, Order YBid){
        // Find max amount of Y sold
        float maxYSold = min(YBid.volume, XYAsk.volume);

        // Find max amount of X bought & sold
        float maxXSold = min(maxYSold * XYAsk.price, XAsk.volume);

        return maxXSold * XAsk.price;
    }


    private static float calculateCapitalY(Order YAsk, Order XYBid, Order XBid){
        // Find max amount of X Sold
        float maxXSold = min(XBid.volume, XYBid.volume * XYBid.price);

        // Find max amount of Y bought & sold
        float maxYSold = min(maxXSold / XYBid.price, YAsk.volume);

        return maxYSold * YAsk.price;
    }
}
