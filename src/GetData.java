import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import javax.swing.text.html.parser.DTD;
import java.io.InputStream;
import java.util.Scanner;

public class GetData {
    public static void main(String[] args) throws Exception {
        Arbitrage btcEth = new Arbitrage("BTC-AUD", "ETH-AUD", "ETH-BTC");
        btcEth.checkArbitrage();
    }
}
