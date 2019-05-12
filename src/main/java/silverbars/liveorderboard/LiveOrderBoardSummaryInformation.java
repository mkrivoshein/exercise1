package silverbars.liveorderboard;

import javax.annotation.Nonnull;
import java.util.List;

public class LiveOrderBoardSummaryInformation {
    @Nonnull
    private final List<Entry> entries;

    /**
     * Creates a new instance of summary information based on a ordered list of entries
     */
    LiveOrderBoardSummaryInformation(@Nonnull List<Entry> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return entries.toString();
    }

    /**
     * A simple placeholder for summary information entry
     *
     * Note: equals() and hashCode() are inherited from Object class
     */
    public static final class Entry {
        private final double quantity;
        private final double price;

        public Entry(double quantity, double price) {
            this.quantity = quantity;
            this.price = price;
        }

        @Override
        public String toString() {
            return String.format("%f kg for £%f", quantity, price);
        }

        public double getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }
    }
}
