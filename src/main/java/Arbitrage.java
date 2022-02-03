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
    public double checkArbitrage () throws Exception {
        BidAsk xBidAsk = marketX.getBidAsk();
        BidAsk yBidAsk = marketY.getBidAsk();
        BidAsk xyBidAsk = marketXY.getBidAsk();

        BidAsk crossRate = getCrossRate(xBidAsk, yBidAsk);

        // Compare cross rate with current XY rates
        // Current ask is higher than cross bid -> buy X with AUD
        double profit = 0;
        if (xyBidAsk.ask.price > crossRate.bid.price){
            profit = getProfitX(xBidAsk.ask, xyBidAsk.ask, yBidAsk.bid);
        }
        // Current bid is higher than cross ask -> buy Y with AUD
        else if (xyBidAsk.bid.price > crossRate.ask.price){
            profit = getProfitY(yBidAsk.ask, xyBidAsk.bid, xBidAsk.bid);
        }
        System.out.println(profit);
        return profit;
    }

    /**
     * Returns the cross rate of two quotes
     * @param x e.g BTC/AUD
     * @param y e.g ETH/AUD
     * @return cross rate for BTC/ETH
     */
    private static BidAsk getCrossRate (BidAsk x, BidAsk y) {
        double bidPrice = (1 / x.ask.price) / (1 / y.bid.price);
        double askPrice = (1 / x.bid.price) / (1 / y.bid.price);
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
    private static double getProfitX(Order XAsk, Order XYAsk, Order YBid) {
        double capital = calculateCapitalX(XAsk, XYAsk, YBid);

        double x = capital / XAsk.price;
        double y = x / XYAsk.price;
        double aud = y * YBid.price;
        return aud - capital;
    }

    /**
     * Returns the possible profit by purchasing Y, converting to X on XY market, and selling X back to AUD
     * @param YAsk
     * @param XYBid
     * @param XBid
     * @return
     */
    private static double getProfitY(Order YAsk, Order XYBid, Order XBid) {
        double capital = calculateCapitalY(YAsk, XYBid, XBid);

        double y = capital / YAsk.price;
        double x = y * XYBid.price;
        double aud = x * XBid.price;
        return aud - capital;
    }

    /**
     * Returns the amount of AUD that can fill the maximum amount of volume for the X-buying strategy
     * @param XAsk
     * @param XYAsk
     * @param YBid
     * @return
     */
    private static double calculateCapitalX(Order XAsk, Order XYAsk, Order YBid){
        // Find max amount of Y sold
        double maxYSold = min(YBid.volume, XYAsk.volume);

        // Find max amount of X bought & sold
        double maxXSold = min(maxYSold * XYAsk.price, XAsk.volume);

        return maxXSold * XAsk.price;
    }


    private static double calculateCapitalY(Order YAsk, Order XYBid, Order XBid){
        // Find max amount of X Sold
        double maxXSold = min(XBid.volume, XYBid.volume * XYBid.price);

        // Find max amount of Y bought & sold
        double maxYSold = min(maxXSold / XYBid.price, YAsk.volume);

        return maxYSold * YAsk.price;
    }
}
