import GUI.MainView;

import javax.swing.*;

public class AppLauncher {
    public static void main(String[] args) throws Exception {
        Arbitrage arb = new Arbitrage("BTC-AUD", "ETH-AUD", "ETH-BTC");
        new MainView();
    }
}
