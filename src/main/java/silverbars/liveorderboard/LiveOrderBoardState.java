package silverbars.liveorderboard;

import silverbars.liveorderboard.order.Order;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * A simple mutable container for live order board state.
 * This class is not thread-safe.
 */
public class LiveOrderBoardState {
    /** internal state can be modelled with a simple HashSet */
    private final Set<Order> liveOrders = new HashSet<>();

    /**
     * Register an order with the live order board
     * @param order an order to be registered
     * @return true if an order was not registered before. This is modeled on <code>Set.add()</code> method
     */
    public boolean registerOrder(@Nonnull Order order) {
        return liveOrders.add(order);
    }

    /**
     * Cancel an order and remove it form the live order board
     * @param order an order to be cancelled
     * @return true if an order was removed from the live order board. This is modeled on <code>Set.remove()</code> method
     */
    public boolean cancelOrder(@Nonnull Order order) {
        return liveOrders.remove(order);
    }

    public Stream<Order> stream() {
        return liveOrders.stream();
    }

    @Override
    public String toString() {
        return liveOrders.toString();
    }
}
