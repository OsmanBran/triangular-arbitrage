import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ArbitrageTest {
    Arbitrage arbitrage;
    MockMarket marketX;
    MockMarket marketY;
    MockMarket marketXY;

    double volumeX = 0.24;
    double volumeY = 3;

    private void initialiseMockArbitrage(){
        marketX = new MockMarket("BTC-AUD");
        marketY = new MockMarket("ETH-AUD");
        marketXY = new MockMarket("ETH-BTC");

        arbitrage = new Arbitrage("BTC-AUD", "ETH-AUD", "ETH-BTC");
        arbitrage.marketX = marketX;
        arbitrage.marketY = marketY;
        arbitrage.marketXY = marketXY;
    }

    /**
     * Sets the cross rate to: Bid: 0.0699, Ask: 0.0701
     */
    private void setCrossRate(){
        marketX.setBid(54537.40, volumeX).setAsk(54629.76, volumeX);
        marketY.setBid(3820.27, volumeY).setAsk(3827.46, volumeY);
    }

    public ArbitrageTest() {
        initialiseMockArbitrage();
        setCrossRate();
    }

    @Test
    @DisplayName("returns profit when market spread is lower than cross spread")
    void profitWhenMarketLowerThanCross() throws Exception {
        marketXY.setBid(0.067, volumeY).setAsk(0.068, volumeY);
        Strategy result = arbitrage.checkArbitrage();
        String profit = result.getValues()[4];
        assertEquals("316.34AUD", profit);
    }

    @Test
    @DisplayName("returns profit when market spread is higher than cross spread")
    void profitWhenMarketHigherThanCross() throws Exception {
        marketXY.setBid(0.072, volumeY).setAsk(0.073, volumeY);
        Strategy result = arbitrage.checkArbitrage();
        String profit = result.getValues()[4];
        assertEquals("297.70AUD", profit);
    }

    @Test
    @DisplayName("returns no profit when market and cross spread overlap")
    void noProfitWhenSpreadsOverlap() throws Exception {
        marketXY.setBid(0.06, volumeY).setAsk(0.07, volumeY);
        Strategy result = arbitrage.checkArbitrage();
        assertEquals(false, result.isProfitable());
    }
}
