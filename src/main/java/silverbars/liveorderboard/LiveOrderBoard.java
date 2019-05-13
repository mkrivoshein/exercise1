package silverbars.liveorderboard;

import com.google.common.annotations.VisibleForTesting;
import silverbars.liveorderboard.order.Order;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * A class for managing live order board state and associated reporting functions.
 */
public class LiveOrderBoard {
    /**
     * a placeholder for live order board state
     */
    @Nonnull
    private final LiveOrderBoardState liveOrderBoardState;

    public LiveOrderBoard() {
        this(new LiveOrderBoardState());
    }

    /**
     * Dependency injection simplifies unit testing
     */
    @VisibleForTesting
    LiveOrderBoard(@Nonnull LiveOrderBoardState liveOrderBoardState) {
        this.liveOrderBoardState = liveOrderBoardState;
    }

    /**
     * Register an order with the live order board
     *
     * @param order an order to be registered
     * @return true if an order was not registered before. This is modeled on <code>Set.add()</code> method
     */
    public boolean registerOrder(@Nonnull Order order) {
        return liveOrderBoardState.registerOrder(order);
    }

    /**
     * Cancel an order and remove it form the live order board
     *
     * @param order an order to be cancelled
     * @return true if an order was removed from the live order board. This is modeled on <code>Set.remove()</code> method
     */
    public boolean cancelOrder(@Nonnull Order order) {
        return liveOrderBoardState.cancelOrder(order);
    }

    /**
     * Summary information generated on the back of the current state of the live order board
     */
    @Nonnull
    public LiveOrderBoardSummaryInformation getSummaryInformation() {
        return liveOrderBoardState.getLiveOrderBoardSummaryInformation();
    }

    @Nonnull
    @Override
    public String toString() {
        return toStringHelper(this)
                .addValue(liveOrderBoardState)
                .toString();
    }
}
