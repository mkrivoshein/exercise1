package silverbars.liveorderboard;

import org.junit.jupiter.api.Test;
import silverbars.liveorderboard.order.Order;
import silverbars.liveorderboard.order.OrderSide;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assume.assumeThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LiveOrderBoardTest {
    private final LiveOrderBoard liveOrderBoard = new LiveOrderBoard();

    @Test
    public void registerNewOrder() {
        // prepare
        Order mockOrder = mock(Order.class);

        // execute
        boolean result = liveOrderBoard.registerOrder(mockOrder);

        // verify
        assertThat(result, is(true));
    }

    @Test
    public void registerDuplicateOrder() {
        // prepare
        Order order1 = new Order("orderA", "user1", 3.5, 306.0, OrderSide.BUY);
        Order order1Duplicate = new Order("orderA", "user1", 3.5, 306.0, OrderSide.BUY);

        boolean order1Result = liveOrderBoard.registerOrder(order1);
        assumeThat(order1Result, is(true));

        // execute
        boolean result = liveOrderBoard.registerOrder(order1Duplicate);

        // verify
        assertThat(result, is(false));
    }

    @Test
    public void cancelExistingOrder() {
        // prepare
        Order mockOrder = mock(Order.class);
        liveOrderBoard.registerOrder(mockOrder);

        // execute
        boolean result = liveOrderBoard.cancelOrder(mockOrder);

        // verify
        assertThat(result, is(true));
    }

    @Test
    public void cancelNonExistingOrder() {
        // prepare
        Order mockOrder = mock(Order.class);

        // execute
        boolean result = liveOrderBoard.cancelOrder(mockOrder);

        // verify
        assertThat(result, is(false));
    }

    @Test
    public void cancelExistingOrderUsingDuplicateObject() {
        // prepare
        Order order1 = new Order("orderA", "user1", 3.5, 306.0, OrderSide.BUY);
        Order order1Duplicate = new Order("orderA", "user1", 3.5, 306.0, OrderSide.BUY);
        liveOrderBoard.registerOrder(order1);

        // execute
        boolean result = liveOrderBoard.cancelOrder(order1Duplicate);

        // verify
        assertThat("If two order objects are equal, any of them can be used to cancel the order", result, is(true));
    }

    @Test
    public void cancelExistingOrderTwice() {
        // prepare
        Order mockOrder = mock(Order.class);
        liveOrderBoard.registerOrder(mockOrder);
        boolean firstCancel = liveOrderBoard.cancelOrder(mockOrder);

        assumeThat(firstCancel, is(true));

        // execute
        boolean result = liveOrderBoard.cancelOrder(mockOrder);

        // verify
        assertThat("Second attempt to cancel an order should yield false", result, is(false));
    }

    /**
     * Verify that live order board is using summary information function to generate summary information
     */
    @Test
    public void summaryInformation() {
        // prepare
        LiveOrderBoardState liveOrderBoardState = mock(LiveOrderBoardState.class);
        LiveOrderBoardSummaryInformation summaryInformation = mock(LiveOrderBoardSummaryInformation.class);

        SummaryInformationFunction summaryInformationFunction = mock(SummaryInformationFunction.class);
        when(summaryInformationFunction.apply(liveOrderBoardState)).thenReturn(summaryInformation);

        LiveOrderBoard liveOrderBoard = new LiveOrderBoard(summaryInformationFunction, liveOrderBoardState);

        // execute
        LiveOrderBoardSummaryInformation result = liveOrderBoard.getSummaryInformation();

        // verify
        assertThat(result, is(sameInstance(summaryInformation)));
    }
}
