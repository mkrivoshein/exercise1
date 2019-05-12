package silverbars.liveorderboard;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import silverbars.liveorderboard.order.Order;
import silverbars.liveorderboard.order.OrderSide;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assume.assumeThat;
import static org.mockito.Mockito.mock;

public class LiveOrderBoardStateTest {
    private LiveOrderBoardState liveOrderBoardState = new LiveOrderBoardState();

    @Test
    public void registerNewOrder() {
        // prepare
        Order mockOrder = mock(Order.class);

        // execute
        boolean result = liveOrderBoardState.registerOrder(mockOrder);

        // verify
        assertThat(result, is(true));
    }

    @Test
    public void registerDuplicateOrder() {
        // prepare
        Order order1 = new Order("orderA", "user1", 3.5, 306.0, OrderSide.BUY);
        Order order1Duplicate = new Order("orderA", "user1", 3.5, 306.0, OrderSide.BUY);

        boolean order1Result = liveOrderBoardState.registerOrder(order1);
        assumeThat(order1Result, is(true));

        // execute
        boolean result = liveOrderBoardState.registerOrder(order1Duplicate);

        // verify
        assertThat(result, is(false));
    }

    @Test
    public void cancelExistingOrder() {
        // prepare
        Order mockOrder = mock(Order.class);
        liveOrderBoardState.registerOrder(mockOrder);

        // execute
        boolean result = liveOrderBoardState.cancelOrder(mockOrder);

        // verify
        assertThat(result, is(true));
    }

    @Test
    public void cancelNonExistingOrder() {
        // prepare
        Order mockOrder = mock(Order.class);

        // execute
        boolean result = liveOrderBoardState.cancelOrder(mockOrder);

        // verify
        assertThat(result, is(false));
    }

    @Test
    public void cancelExistingOrderUsingDuplicateObject() {
        // prepare
        Order order1 = new Order("orderA", "user1", 3.5, 306.0, OrderSide.BUY);
        Order order1Duplicate = new Order("orderA", "user1", 3.5, 306.0, OrderSide.BUY);
        liveOrderBoardState.registerOrder(order1);

        // execute
        boolean result = liveOrderBoardState.cancelOrder(order1Duplicate);

        // verify
        assertThat("If two order objects are equal, any of them can be used to cancel the order", result, is(true));
    }

    @Test
    public void cancelExistingOrderTwice() {
        // prepare
        Order mockOrder = mock(Order.class);
        liveOrderBoardState.registerOrder(mockOrder);
        boolean firstCancel = liveOrderBoardState.cancelOrder(mockOrder);

        assumeThat(firstCancel, is(true));

        // execute
        boolean result = liveOrderBoardState.cancelOrder(mockOrder);

        // verify
        assertThat("Second attempt to cancel an order should yield false", result, is(false));
    }

    @Test
    public void streamEmptyLiveOrderBoardState() {
        List<Order> streamContents = liveOrderBoardState.stream().collect(Collectors.toList());

        assertThat(streamContents, is(ImmutableList.of()));
    }

}