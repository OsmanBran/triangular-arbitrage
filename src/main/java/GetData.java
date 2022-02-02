public class GetData {
    public static void main(String[] args) throws Exception {
        Arbitrage btcEth = new Arbitrage("BTC-AUD", "ETH-AUD", "ETH-BTC");
        btcEth.checkArbitrage();
    }
}
