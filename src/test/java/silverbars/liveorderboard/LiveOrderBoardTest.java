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
