package silverbars.liveorderboard;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import silverbars.liveorderboard.order.Order;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.google.common.base.MoreObjects.toStringHelper;

public class LiveOrderBoardSummaryInformation {
    @Nonnull
    private final Map<Double, Entry> buyEntries;
    @Nonnull
    private final Map<Double, Entry> sellEntries;

    /**
     * Creates a new instance of summary information based on a ordered list of entries
     * @param buyEntries
     * @param sellEntries
     */
    LiveOrderBoardSummaryInformation(@Nonnull Map<Double, Entry> buyEntries, @Nonnull Map<Double, Entry> sellEntries) {
        // making state of the model immutable to avoid confusion and unexpected behaviour
        this.buyEntries = ImmutableMap.copyOf(buyEntries);
        this.sellEntries = ImmutableMap.copyOf(sellEntries);
    }

    /**
     * Get a read-only list of SELL entries.
     */
    @Nonnull
    public Map<Double, Entry> buyEntries() {
        return buyEntries;
    }

    /**
     * Get a read-only list of SELL entries.
     */
    @Nonnull
    public Map<Double, Entry> sellEntries() {
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
     * A simple placeholder for summary information entry
     */
    public static final class Entry {
        private final double quantity;
        private final double price;
        private final Set<Order> orders;

        public Entry(double quantity, double price, Order... orders) {
            this(quantity, price, ImmutableSet.copyOf(orders));
        }

        public Entry(double quantity, double price, Set<Order> orders) {
            this.quantity = quantity;
            this.price = price;
            // ensure it is immutable
            this.orders = ImmutableSet.copyOf(orders);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(quantity);
            sb.append(" kg for ");

            // format price as long if it has no fractional part
            if (price == (long) price) {
                sb.append((long) price);
            } else {
                sb.append(price);
            }

            sb.append(" GBP"); // £

            sb.append(" // ");

            sb.append(Joiner.on(" + ").join(orders));

            return sb.toString();
        }

        public double getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entry entry = (Entry) o;
            return Double.compare(entry.quantity, quantity) == 0 &&
                    Double.compare(entry.price, price) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(quantity, price);
        }
    }
}
