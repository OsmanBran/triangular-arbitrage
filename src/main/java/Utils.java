public class Utils {
    public static String spreadString(BidAsk spread){
        return String.format("%,.5f", spread.bid.price) + " - " + String.format("%,.5f", spread.ask.price);
    }

    public static String valueString(double value){
        return String.format("%,.2f", value) + "AUD";
    }

    public static String valueString(double value, String currency){
        return String.format("%,.2f", value) + currency;
    }
}
