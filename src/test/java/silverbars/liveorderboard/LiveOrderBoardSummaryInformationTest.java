package silverbars.liveorderboard;

import org.junit.jupiter.api.Test;
import silverbars.liveorderboard.order.Order;
import silverbars.liveorderboard.order.OrderSide;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * TODO: move to integration tests
 */
class LiveOrderBoardSummaryInformationTest {
    private Order orderA = new Order("order a", "user1", 3.5, 306.0, OrderSide.SELL);
    private Order orderB = new Order("order b", "user1", 1.2, 310.0, OrderSide.SELL);
    private Order orderC = new Order("order c", "user1", 1.5, 307.0, OrderSide.SELL);
    private Order orderD = new Order("order d", "user2", 2.0, 306.0, OrderSide.SELL);
    private Order orderE = new Order("order e", "user3", 1.5, 307.0, OrderSide.BUY);
    private Order orderF = new Order("order f", "user4", 2.0, 310.0, OrderSide.BUY);
    private Order orderG = new Order("order g", "user4", 1.3, 308.0, OrderSide.BUY);
    private Order orderH = new Order("order h", "user5", 0.6, 307.0, OrderSide.BUY);

    private LiveOrderBoard liveOrderBoard = new LiveOrderBoard();

    @Test
    public void emptySummaryInformation() {
        LiveOrderBoardSummaryInformation summaryInformation = liveOrderBoard.getSummaryInformation();

        assertThat(summaryInformation, is(notNullValue()));
        assertThat(summaryInformation.buyOrdersDescriptions(), is(new String[0]));
        assertThat(summaryInformation.sellOrdersDescriptions(), is(new String[0]));
    }

    @Test
    public void sellOrdersSummaryInformation() {
        liveOrderBoard.registerOrder(orderA);
        liveOrderBoard.registerOrder(orderB);
        liveOrderBoard.registerOrder(orderC);
        liveOrderBoard.registerOrder(orderD);

        LiveOrderBoardSummaryInformation summaryInformation = liveOrderBoard.getSummaryInformation();

        assertThat(summaryInformation, is(notNullValue()));
        assertThat(summaryInformation.buyOrdersDescriptions(), is(new String[0]));
        assertThat(summaryInformation.sellOrdersDescriptions(), is(new String[]{
                "5.5 kg for 306 GBP // order a + order d",
                "1.5 kg for 307 GBP // order c",
                "1.2 kg for 310 GBP // order b",
        }));
    }

    @Test
    public void buyOrdersSummaryInformation() {
        liveOrderBoard.registerOrder(orderE);
        liveOrderBoard.registerOrder(orderF);
        liveOrderBoard.registerOrder(orderG);
        liveOrderBoard.registerOrder(orderH);

        LiveOrderBoardSummaryInformation summaryInformation = liveOrderBoard.getSummaryInformation();

        assertThat(summaryInformation, is(notNullValue()));
        assertThat(summaryInformation.buyOrdersDescriptions(), is(new String[]{
                "2.0 kg for 310 GBP // order f",
                "1.3 kg for 308 GBP // order g",
                "2.1 kg for 307 GBP // order e + order h",
        }));
        assertThat(summaryInformation.sellOrdersDescriptions(), is(new String[0]));
    }

    @Test
    public void buyAndSellOrdersSummaryInformation() {
        liveOrderBoard.registerOrder(orderA);
        liveOrderBoard.registerOrder(orderB);
        liveOrderBoard.registerOrder(orderC);
        liveOrderBoard.registerOrder(orderD);
        liveOrderBoard.registerOrder(orderE);
        liveOrderBoard.registerOrder(orderF);
        liveOrderBoard.registerOrder(orderG);
        liveOrderBoard.registerOrder(orderH);

        LiveOrderBoardSummaryInformation summaryInformation = liveOrderBoard.getSummaryInformation();

        assertThat(summaryInformation, is(notNullValue()));
        assertThat(summaryInformation.buyOrdersDescriptions(), is(new String[]{
                "2.0 kg for 310 GBP // order f",
                "1.3 kg for 308 GBP // order g",
                "2.1 kg for 307 GBP // order e + order h",
        }));
        assertThat(summaryInformation.sellOrdersDescriptions(), is(new String[]{
                "5.5 kg for 306 GBP // order a + order d",
                "1.5 kg for 307 GBP // order c",
                "1.2 kg for 310 GBP // order b",
        }));
    }
}