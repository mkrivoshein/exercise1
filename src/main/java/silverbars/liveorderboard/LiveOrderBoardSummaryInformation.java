package silverbars.liveorderboard;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import silverbars.liveorderboard.order.Order;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.MoreObjects.toStringHelper;

public class LiveOrderBoardSummaryInformation {
    // assuming that there is no goal of crossing buy and sell orders, it is reasonable to collect them
    // into two separate buckets as requirements clearly focus on BUY and SELL orders as being grouped separately
    @Nonnull
    private final Map<Double, Set<Order>> buyEntries;
    @Nonnull
    private final Map<Double, Set<Order>> sellEntries;

    /**
     * Creates a new instance of summary information based on a ordered list of entries
     */
    LiveOrderBoardSummaryInformation(@Nonnull Map<Double, Set<Order>> buyEntries, @Nonnull Map<Double, Set<Order>> sellEntries) {
        // making state of the model immutable to avoid confusion and unexpected behaviour
        this.buyEntries = ImmutableMap.copyOf(buyEntries);
        this.sellEntries = ImmutableMap.copyOf(sellEntries);
    }

    /**
     * Get a read-only list of BUY entries.
     */
    @Nonnull
    public Map<Double, Set<Order>> buyEntries() {
        return buyEntries;
    }

    /**
     * Get a read-only list of SELL entries.
     */
    @Nonnull
    public Map<Double, Set<Order>> sellEntries() {
        return sellEntries;
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("BUY", buyEntries().entrySet())
                .add("SELL", sellEntries().entrySet())
                .toString();
    }

    /**
     * returns an ordered array of buy order descriptions; one description per each price entry
     */
    @Nonnull
    public String[] buyOrdersDescriptions() {
        return descriptions(buyEntries);
    }

    /**
     * returns an ordered array of sell order descriptions; one description per each price entry
     */
    @Nonnull
    public String[] sellOrdersDescriptions() {
        return descriptions(sellEntries);
    }

    private String[] descriptions(Map<Double, Set<Order>> entries) {
        return entries.entrySet().stream()
                .map((entry) -> description(entry.getKey(), entry.getValue()))
                .toArray(String[]::new);

    }

    @Nonnull
    private String description(double price, @Nonnull Set<Order> orders) {
        StringBuilder sb = new StringBuilder();
        sb.append(getQuantity(orders));
        sb.append(" kg for ");

        // format price as long if it has no fractional part
        if (price == (long) price) {
            sb.append((long) price);
        } else {
            sb.append(price);
        }

        sb.append(" GBP"); // £

        sb.append(" // ");

        sb.append(Joiner.on(" + ").join(orders.stream().map(Order::getOrderId).sorted().collect(Collectors.toList())));

        return sb.toString();
    }

    private double getQuantity(@Nonnull Set<Order> orders) {
        // DoubleStream.sum() attempts to calculate a sum of doubles and retain precision
        return orders.stream().mapToDouble(Order::getQuantity).sum();
    }
}
