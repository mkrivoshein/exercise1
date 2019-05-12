package silverbars.liveorderboard;

import com.google.common.collect.ImmutableMap;
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

        LiveOrderBoardSummaryInformation.Entry entry1 = new LiveOrderBoardSummaryInformation.Entry(3.5, 306, orderA);
        LiveOrderBoardSummaryInformation.Entry entry2 = new LiveOrderBoardSummaryInformation.Entry(1.2, 310, orderB);

        assertThat(summaryInformation.buyEntries(), is(ImmutableMap.of()));
        assertThat(summaryInformation.sellEntries(), is(ImmutableMap.of(306d, entry1, 310d, entry2)));
    }

    @Test
    public void summaryInformationForThreeBuyOrders() {
        LiveOrderBoardState liveOrderBoardState = new LiveOrderBoardState();
        liveOrderBoardState.registerOrder(orderE);
        liveOrderBoardState.registerOrder(orderF);
        liveOrderBoardState.registerOrder(orderG);

        SummaryInformationFunction function = new SummaryInformationFunction();

        LiveOrderBoardSummaryInformation summaryInformation = function.apply(liveOrderBoardState);

        LiveOrderBoardSummaryInformation.Entry entry1 = new LiveOrderBoardSummaryInformation.Entry(1.5, 307, orderE);
        LiveOrderBoardSummaryInformation.Entry entry2 = new LiveOrderBoardSummaryInformation.Entry(1.3, 308, orderG);
        LiveOrderBoardSummaryInformation.Entry entry3 = new LiveOrderBoardSummaryInformation.Entry(2.0, 310, orderF);

        assertThat(summaryInformation.buyEntries(), is(ImmutableMap.of(310d, entry3, 308d, entry2, 307d, entry1)));
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

        LiveOrderBoardSummaryInformation.Entry entry1 = new LiveOrderBoardSummaryInformation.Entry(5.5, 306, orderA, orderD);
        LiveOrderBoardSummaryInformation.Entry entry2 = new LiveOrderBoardSummaryInformation.Entry(1.5, 307, orderC);
        LiveOrderBoardSummaryInformation.Entry entry3 = new LiveOrderBoardSummaryInformation.Entry(1.2, 310, orderB);

        LiveOrderBoardSummaryInformation.Entry entry4 = new LiveOrderBoardSummaryInformation.Entry(2.1, 307, orderE, orderH);
        LiveOrderBoardSummaryInformation.Entry entry5 = new LiveOrderBoardSummaryInformation.Entry(1.3, 308, orderG);
        LiveOrderBoardSummaryInformation.Entry entry6 = new LiveOrderBoardSummaryInformation.Entry(2.0, 310, orderF);

        assertThat(summaryInformation.buyEntries(), is(ImmutableMap.of(310d, entry6, 308d, entry5, 307d, entry4)));
        assertThat(summaryInformation.sellEntries(), is(ImmutableMap.of(306d, entry1, 307d, entry2, 310d, entry3)));
    }

}