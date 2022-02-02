import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Market {
    private String orderbookURL;

    public Market (String ticker) {
        orderbookURL = "https://api.btcmarkets.net/v3/markets/" + ticker + "/orderbook";
    }

    public BidAsk getBidAsk () throws Exception {
        String orderBookString = requestOrderBook();
        return parseAsBidAsk(orderBookString);
    }

    private String requestOrderBook () throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(orderbookURL);
        CloseableHttpResponse httpResponse = null;

        try {
            // execute http request
            httpResponse = httpClient.execute(httpGet);

            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException(httpResponse.getStatusLine().getReasonPhrase());
            }
            // return JSON results as String
            HttpEntity entity = httpResponse.getEntity();

            String content = EntityUtils.toString(entity);
            return content;
        }catch (Exception e) {
            throw new RuntimeException("unable to execute json call:" + e);
        } finally {
            // close http connection
            if (httpResponse != null) {
                HttpEntity entity = httpResponse.getEntity();
                if (entity != null) {
                    EntityUtils.consume(entity);
                }
            }
            if (httpClient != null) {
                httpClient.close();
            }
        }
    }

    private BidAsk parseAsBidAsk (String orderBook) throws Exception {
        JSONObject orderBookObj = new JSONObject(orderBook);
        JSONArray asks = orderBookObj.getJSONArray("asks");
        JSONArray bids = orderBookObj.getJSONArray("bids");
        if (asks.length() == 0 || bids.length() == 0){
            throw new Exception("No asks or bids");
        }
        Order topAsk = parseAsOrder(asks.getJSONArray(0));
        Order topBid = parseAsOrder(bids.getJSONArray(0));

        return new BidAsk(topAsk, topBid);
    }

    /**
     * Parses an order in JSON format to an order object
     * @param orderArr
     * @return
     */
    private Order parseAsOrder (JSONArray orderArr) {
        Float price = orderArr.getFloat(0);
        Float volume = orderArr.getFloat(1);

        return new Order(price, volume);
    }
}
