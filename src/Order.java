public class Order {
    float price;
    float volume;

    public Order (float price, float volume) {
        this.price = price;
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "Order{" +
                "price=" + price +
                ", volume=" + volume +
                '}';
    }
}
