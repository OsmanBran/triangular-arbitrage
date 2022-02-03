public class Order {
    double price;
    double volume;

    public Order (double price, double volume) {
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
