import static java.lang.Math.min;

public class Arbitrage {
    Market marketX; // BTC-AUD
    Market marketY; // ETH-AUD
    Market marketXY; // ETH-BTC (Y should be the base and X the quote)

    BidAsk xBidAsk;
    BidAsk yBidAsk;
    BidAsk xyBidAsk;
    BidAsk crossRate;

    Strategy strategy = new Strategy();

    public void setMarkets(String selectedMarket){
        // string in format X-Y
        String[] markets = selectedMarket.split("-");
        String marketX = markets[0];
        String marketY = markets[1];
        this.marketX = new Market(marketX + "-" + "AUD");
        this.marketY = new Market(marketY + "-" + "AUD");
        this.marketXY = new Market(marketY + "-" + marketX);
        strategy.setMarketStrings(marketX, marketY);
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

        crossRate = calculateCrossRate(xBidAsk, yBidAsk);

        // Compare cross rate with current XY rates
        // If market spread lower than cross rate spread, X is overvalued, buy X
        if (xyBidAsk.ask.price < crossRate.bid.price){
            strategy.update(true, xBidAsk.ask, xyBidAsk.ask, yBidAsk.bid);
        }
        // If market spread higher than cross rate spread, Y is overvalued, buy Y
        else if (xyBidAsk.bid.price > crossRate.ask.price){
            strategy.update(false, yBidAsk.ask, xyBidAsk.bid, xBidAsk.bid);
        }
        else {
            strategy.setProfitability(false);
        }
        return strategy;
    }

    public BidAsk getCrossSpread(){
        return crossRate;
    }

    public BidAsk getMarketSpread(){
        return xyBidAsk;
    }

    /**
     * Returns the cross rate of two quotes
     * @param x e.g BTC/AUD
     * @param y e.g ETH/AUD
     * @return cross rate for BTC/ETH
     */
    private static BidAsk calculateCrossRate (BidAsk x, BidAsk y) {
        double bidPrice = (1 / x.ask.price) / (1 / y.bid.price);
        double askPrice = (1 / x.bid.price) / (1 / y.bid.price);
        Order bid = new Order(bidPrice, 0);
        Order ask = new Order(askPrice, 0);

        return new BidAsk(bid, ask);
    }
}
