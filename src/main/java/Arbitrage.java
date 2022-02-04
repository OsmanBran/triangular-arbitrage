import static java.lang.Math.min;

public class Arbitrage {
    Market marketX; // BTC-AUD
    Market marketY; // ETH-AUD
    Market marketXY; // ETH-BTC (Y should be the base and X the quote)

    BidAsk xBidAsk;
    BidAsk yBidAsk;
    BidAsk xyBidAsk;

    Strategy strategy;

    public Arbitrage (String marketX, String marketY, String marketXY){
        this.marketX = new Market(marketX);
        this.marketY = new Market(marketY);
        this.marketXY = new Market(marketXY);
        this.strategy = new Strategy(this.marketX.getBase(), this.marketY.getBase());
    }

    /**
     * Gets the top bid and asks for each market in the form of JTable data
     * @return
     */
    public Object[][] getMarketData(){
        Object[][] data = {
                {marketX.getTicker(), xBidAsk.bid.price, xBidAsk.bid.volume, xBidAsk.ask.price, xBidAsk.ask.volume},
                {marketY.getTicker(), yBidAsk.bid.price, yBidAsk.bid.volume, yBidAsk.ask.price, yBidAsk.ask.volume},
                {marketXY.getTicker(), xyBidAsk.bid.price, xyBidAsk.bid.volume, xyBidAsk.ask.price, xyBidAsk.ask.volume}
        };
        return data;
    }

    /**
     * Calls the latest asks and bids from each market and checks if arbitrage is available
     */
    public Strategy checkArbitrage () throws Exception {
        xBidAsk = marketX.getBidAsk();
        yBidAsk = marketY.getBidAsk();
        xyBidAsk = marketXY.getBidAsk();

        BidAsk crossRate = getCrossRate(xBidAsk, yBidAsk);

        // Compare cross rate with current XY rates
        // If market spread lower than cross rate spread, X is overvalued, buy X
        if (xyBidAsk.ask.price < crossRate.bid.price){
            strategy.update(true, xBidAsk.ask, xyBidAsk.ask, yBidAsk.bid);
        }
        // If market spread higher than cross rate spread, Y is overvalued, buy Y
        else if (xyBidAsk.bid.price > crossRate.ask.price){
            strategy.update(false, yBidAsk.ask, xyBidAsk.bid, xBidAsk.bid);
        }
        return strategy;
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
}
