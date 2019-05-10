package silverbars.liveorderboard.order;

import javax.annotation.Nonnull;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public class Order {
    @Nonnull
    private final String orderId;
    @Nonnull
    private final String userId;
    private final double quantity;
    private final double price;
    @Nonnull
    private final OrderSide orderSide;

    /**
     * For simplicity the constructor is public. At some stage it might be feasible to make it private or protected
     * and offer a set of factory methods instead
     */
    public Order(String orderId, String userId, double quantity, double price, OrderSide orderSide) {
        this.orderId = checkNotNull(orderId, "orderId is null");
        this.userId = checkNotNull(userId, "userId is null");
        this.quantity = quantity;
        this.price = price;
        this.orderSide = checkNotNull(orderSide, "orderSide is null");
    }

    @Nonnull
    public String getOrderId() {
        return orderId;
    }

    @Nonnull
    public String getUserId() {
        return userId;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    @Nonnull
    public OrderSide getOrderSide() {
        return orderSide;
    }

    /**
     * For simplicity each and every field is considered important in establishing equality of two objects
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Double.compare(order.quantity, quantity) == 0 &&
                Double.compare(order.price, price) == 0 &&
                Objects.equals(orderId, order.orderId) &&
                Objects.equals(userId, order.userId) &&
                orderSide == order.orderSide;
    }

    /**
     * It might be feasible to use fewer fields in calculating hash code but that would be a case of premature optimisation
     */
    @Override
    public int hashCode() {
        return Objects.hash(orderId, userId, quantity, price, orderSide);
    }
}
