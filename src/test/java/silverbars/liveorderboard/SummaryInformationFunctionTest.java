package silverbars.liveorderboard;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.Test;
import silverbars.liveorderboard.order.Order;
import silverbars.liveorderboard.order.OrderSide;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SummaryInformationFunctionTest {
    private Order orderA = new Order("orderA", "user1", 3.5, 306.0, OrderSide.SELL);
    private Order orderB = new Order("orderB", "user1", 1.2, 310.0, OrderSide.SELL);
    private Order orderC = new Order("orderC", "user1", 1.5, 307.0, OrderSide.SELL);
    private Order orderD = new Order("orderD", "user2", 2.0, 306.0, OrderSide.SELL);
    private Order orderE = new Order("orderE", "user3", 1.5, 307.0, OrderSide.BUY);
    private Order orderF = new Order("orderF", "user4", 2.0, 310.0, OrderSide.BUY);
    private Order orderG = new Order("orderG", "user4", 1.3, 308.0, OrderSide.BUY);
    private Order orderH = new Order("orderH", "user5", 0.6, 307.0, OrderSide.BUY);

    @Test
    public void emptySummaryInformation() {
        SummaryInformationFunction function = new SummaryInformationFunction();

        LiveOrderBoardSummaryInformation summaryInformation = function.apply(new LiveOrderBoardState());

        assertThat(summaryInformation.buyEntries(), is(ImmutableMap.of()));
        assertThat(summaryInformation.sellEntries(), is(ImmutableMap.of()));
    }

    @Test
    public void summaryInformationForTwoSellOrders() {
        LiveOrderBoardState liveOrderBoardState = new LiveOrderBoardState();
        liveOrderBoardState.registerOrder(orderA);
        liveOrderBoardState.registerOrder(orderB);

        SummaryInformationFunction function = new SummaryInformationFunction();

        LiveOrderBoardSummaryInformation summaryInformation = function.apply(liveOrderBoardState);

        assertThat(summaryInformation.buyEntries(), is(ImmutableMap.of()));
        assertThat(summaryInformation.sellEntries(), is(ImmutableMap.of(306d, ImmutableSet.of(orderA), 310d, ImmutableSet.of(orderB))));
    }

    @Test
    public void summaryInformationForThreeBuyOrders() {
        LiveOrderBoardState liveOrderBoardState = new LiveOrderBoardState();
        liveOrderBoardState.registerOrder(orderE);
        liveOrderBoardState.registerOrder(orderF);
        liveOrderBoardState.registerOrder(orderG);

        SummaryInformationFunction function = new SummaryInformationFunction();

        LiveOrderBoardSummaryInformation summaryInformation = function.apply(liveOrderBoardState);

        assertThat(summaryInformation.buyEntries(), is(ImmutableMap.of(310d, ImmutableSet.of(orderF), 308d, ImmutableSet.of(orderG), 307d, ImmutableSet.of(orderE))));
        assertThat(summaryInformation.sellEntries(), is(ImmutableMap.of()));
    }

    @Test
    public void summaryInformationForMixOfOverlappingOrders() {
        LiveOrderBoardState liveOrderBoardState = new LiveOrderBoardState();
        liveOrderBoardState.registerOrder(orderA);
        liveOrderBoardState.registerOrder(orderB);
        liveOrderBoardState.registerOrder(orderC);
        liveOrderBoardState.registerOrder(orderD);
        liveOrderBoardState.registerOrder(orderE);
        liveOrderBoardState.registerOrder(orderF);
        liveOrderBoardState.registerOrder(orderG);
        liveOrderBoardState.registerOrder(orderH);

        SummaryInformationFunction function = new SummaryInformationFunction();

        LiveOrderBoardSummaryInformation summaryInformation = function.apply(liveOrderBoardState);

        assertThat(summaryInformation.buyEntries(), is(ImmutableMap.of(310d, ImmutableSet.of(orderF), 308d, ImmutableSet.of(orderG), 307d, ImmutableSet.of(orderE, orderH))));
        assertThat(summaryInformation.sellEntries(), is(ImmutableMap.of(306d, ImmutableSet.of(orderA, orderD), 307d, ImmutableSet.of(orderC), 310d, ImmutableSet.of(orderB))));
    }

}