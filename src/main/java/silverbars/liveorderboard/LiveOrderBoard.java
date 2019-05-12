package silverbars.liveorderboard;

import com.google.common.annotations.VisibleForTesting;
import silverbars.liveorderboard.order.Order;

import javax.annotation.Nonnull;

public class LiveOrderBoard {
    /** a function that translates live order board state into summary information */
    @Nonnull
    private final SummaryInformationFunction summaryInformationFunction;

    private final LiveOrderBoardState liveOrderBoardState;

    public LiveOrderBoard() {
        this(new SummaryInformationFunction(), new LiveOrderBoardState());
    }

    /**
     * Allow a summary information function to be replaced with a mock during testing
     */
    @VisibleForTesting
    LiveOrderBoard(@Nonnull SummaryInformationFunction summaryInformationFunction, LiveOrderBoardState liveOrderBoardState) {
        this.summaryInformationFunction = summaryInformationFunction;
        this.liveOrderBoardState = liveOrderBoardState;
    }

    /**
     * Register an order with the live order board
     * @param order an order to be registered
     * @return true if an order was not registered before. This is modeled on <code>Set.add()</code> method
     */
    public boolean registerOrder(@Nonnull Order order) {
        return liveOrderBoardState.registerOrder(order);
    }

    /**
     * Cancel an order and remove it form the live order board
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
        return summaryInformationFunction.apply(liveOrderBoardState);
    }
}
