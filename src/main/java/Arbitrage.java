/**
 * The Arbitrage object holds details of the three markets being analysed
 * and can check if there are arbitrage opportunities currently available.
 * <p>
 * Three valid markets are required for triangular arbitrage:
 * 1) X-AUD - where "X" is currency 1
 * 2) Y-AUD - where "Y" is currency 2
 * 3) Y-X - where "Y" is the base
 */
public class Arbitrage {
    Market marketX; // BTC-AUD
    Market marketY; // ETH-AUD
    Market marketXY; // ETH-BTC (Y should be the base and X the quote)

    BidAsk xBidAsk;
    BidAsk yBidAsk;
    BidAsk xyBidAsk;
    BidAsk crossRate;

    Strategy strategy = new Strategy();

    /**
     * @param defaultMarket in the format "X-Y", e.g "BTC-ETH" will connect to: "BTC-AUD", "ETH-AUD", "ETH-BTC" markets
     */
    public Arbitrage(String defaultMarket) {
        setMarkets(defaultMarket);
    }

    /**
     * A bid and ask can be calculated between "Y-X" using the "X-AUD"
     * and "Y-AUD" markets. This indirect rate is known as the cross rate.
     *
     * @param x the prices of the top orders in the "X-AUD" market
     * @param y the prices of the top orders in the "Y-AUD" market
     * @return the current cross rate bid-ask
     */
    private static BidAsk calculateCrossRate(BidAsk x, BidAsk y) {
        double bidPrice = (1 / x.ask.price) / (1 / y.bid.price);
        double askPrice = (1 / x.bid.price) / (1 / y.bid.price);
        Order bid = new Order(bidPrice, 0);
        Order ask = new Order(askPrice, 0);

        return new BidAsk(bid, ask);
    }

    /**
     * @param selectedMarket in the format "X-Y", e.g "BTC-ETH" will connect to: "BTC-AUD", "ETH-AUD", "ETH-BTC" markets
     */
    public void setMarkets(String selectedMarket) {
        String[] markets = selectedMarket.split("-");
        String marketX = markets[0];
        String marketY = markets[1];
        this.marketX = new Market(marketX + "-" + "AUD");
        this.marketY = new Market(marketY + "-" + "AUD");
        this.marketXY = new Market(marketY + "-" + marketX);
        strategy.setMarketStrings(marketX, marketY);
    }

    /**
     * @return a 2D array which can be read by a JTable
     */
    public Object[][] getMarketData() {
        Object[][] data = {
                {marketX.getTicker(), xBidAsk.bid.price, xBidAsk.bid.volume, xBidAsk.ask.price, xBidAsk.ask.volume},
                {marketY.getTicker(), yBidAsk.bid.price, yBidAsk.bid.volume, yBidAsk.ask.price, yBidAsk.ask.volume},
                {marketXY.getTicker(), xyBidAsk.bid.price, xyBidAsk.bid.volume, xyBidAsk.ask.price, xyBidAsk.ask.volume}
        };
        return data;
    }

    /**
     * Gets the top orders from the connected markets and determines whether arbitrage exists.
     *
     * @return A strategy object holding information on whether arbitrage exists, and if so,
     * how it can be achieved and the total profit.
     * @throws Exception
     */
    public Strategy checkArbitrage() throws Exception {
        xBidAsk = marketX.getBidAsk();
        yBidAsk = marketY.getBidAsk();
        xyBidAsk = marketXY.getBidAsk();

        crossRate = calculateCrossRate(xBidAsk, yBidAsk);

        // Compare cross rate with current "X-Y" rates
        // If market spread lower than cross rate spread, X is overvalued, buy X
        if (xyBidAsk.ask.price < crossRate.bid.price) {
            strategy.update(true, xBidAsk.ask, xyBidAsk.ask, yBidAsk.bid);
        }
        // If market spread higher than cross rate spread, Y is overvalued, buy Y
        else if (xyBidAsk.bid.price > crossRate.ask.price) {
            strategy.update(false, yBidAsk.ask, xyBidAsk.bid, xBidAsk.bid);
        } else {
            strategy.setProfitability(false);
        }
        return strategy;
    }

    public BidAsk getCrossSpread() {
        return crossRate;
    }

    public BidAsk getMarketSpread() {
        return xyBidAsk;
    }
}
