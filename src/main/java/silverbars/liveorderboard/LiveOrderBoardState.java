package silverbars.liveorderboard;

import com.google.common.collect.ImmutableSet;
import silverbars.liveorderboard.order.Order;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

/**
 * A simple mutable container for live order board state.
 * This class is not thread-safe.
 */
public class LiveOrderBoardState {
    /**
     * Internal state can be modelled with a simple HashSet. More complex state model would simplify access logic but
     * given that access logic is not clearly defined this simple version should work reasonably well
     */
    private final Set<Order> liveOrders = new HashSet<>();

    /**
     * Register an order with the live order board
     *
     * @param order an order to be registered
     * @return true if an order was not registered before. This is modeled on <code>Set.add()</code> method
     */
    public boolean registerOrder(@Nonnull Order order) {
        return liveOrders.add(order);
    }

    /**
     * Cancel an order and remove it form the live order board
     *
     * @param order an order to be cancelled
     * @return true if an order was removed from the live order board. This is modeled on <code>Set.remove()</code> method
     */
    public boolean cancelOrder(@Nonnull Order order) {
        return liveOrders.remove(order);
    }

    /**
     * Returns an immutable copy of the current set of live orders. Can be implemented differently if performance
     * is a concern or it is assured that users of this method will not attempt to modify data.
     */
    @Nonnull
    public Set<Order> liveOrders() {
        return ImmutableSet.copyOf(liveOrders);
    }

    @Nonnull
    public LiveOrderBoardSummaryInformation getLiveOrderBoardSummaryInformation() {
        return new LiveOrderBoardSummaryInformation(buyOrderSummaryInformation(), sellOrderSummaryInformation());
    }

    private Map<Double, Set<Order>> buyOrderSummaryInformation() {
        return summaryInformation(liveOrders, Order::isBuy, Comparator.reverseOrder());
    }

    private Map<Double, Set<Order>> sellOrderSummaryInformation() {
        return summaryInformation(liveOrders, Order::isSell, Comparator.naturalOrder());
    }

    /**
     * Takes a set of orders, filters them with a predicate and then groups into a map with price as a key
     *
     * @param orders            a set of orders
     * @param orderFilter       a filter that can be used to single out buy and sell orders
     * @param priceSortingOrder sorting order for price keys
     * @return a sorted multimap where orders are grouped by order price
     */
    private static Map<Double, Set<Order>> summaryInformation(Set<Order> orders,
                                                              Predicate<Order> orderFilter,
                                                              Comparator<Double> priceSortingOrder) {
        SortedMap<Double, Set<Order>> result = new TreeMap<>(priceSortingOrder);

        Stream<Order> filteredOrderStream = orders.stream().filter(orderFilter);

        result.putAll(groupOrdersByPrice(filteredOrderStream));

        return result;
    }

    private static Map<Double, Set<Order>> groupOrdersByPrice(@Nonnull Stream<Order> orders) {
        return orders.collect(groupingBy(Order::getPrice, toSet()));
    }

    @Override
    public String toString() {
        return liveOrders.toString();
    }
}
