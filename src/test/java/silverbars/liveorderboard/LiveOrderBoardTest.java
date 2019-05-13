package silverbars.liveorderboard;

import org.junit.jupiter.api.Test;
import silverbars.liveorderboard.order.Order;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class LiveOrderBoardTest {
    // using field initializers to ensure order of initialisation
    private final LiveOrderBoardState liveOrderBoardState = mock(LiveOrderBoardState.class);
    private final LiveOrderBoard liveOrderBoard = new LiveOrderBoard(liveOrderBoardState);

    @Test
    public void registerOrderCallsToLiveOrderBoardState() {
        // prepare
        Order mockOrder1 = mock(Order.class);
        Order mockOrder2 = mock(Order.class);
        Order mockOrder3 = mock(Order.class);

        when(liveOrderBoardState.registerOrder(mockOrder1)).thenReturn(true);
        when(liveOrderBoardState.registerOrder(mockOrder2)).thenReturn(true);
        when(liveOrderBoardState.registerOrder(mockOrder3)).thenReturn(false);

        // execute
        boolean result1 = liveOrderBoard.registerOrder(mockOrder1);
        boolean result2 = liveOrderBoard.registerOrder(mockOrder2);
        boolean result3 = liveOrderBoard.registerOrder(mockOrder3);

        // verify results
        assertThat(result1, is(true));
        assertThat(result2, is(true));
        assertThat(result3, is(false));

        // verify that call was delegated to the mock
        verify(liveOrderBoardState).registerOrder(mockOrder1);
        verify(liveOrderBoardState).registerOrder(mockOrder2);
        verify(liveOrderBoardState).registerOrder(mockOrder3);

        verifyNoMoreInteractions(liveOrderBoardState);
    }

    @Test
    public void cancelOrderCallsLiveOrderBoardState() {
        // prepare
        Order mockOrder1 = mock(Order.class);
        Order mockOrder2 = mock(Order.class);
        Order mockOrder3 = mock(Order.class);

        when(liveOrderBoardState.cancelOrder(mockOrder1)).thenReturn(true);
        when(liveOrderBoardState.cancelOrder(mockOrder2)).thenReturn(false);
        when(liveOrderBoardState.cancelOrder(mockOrder3)).thenReturn(true);

        // execute
        boolean result1 = liveOrderBoard.cancelOrder(mockOrder1);
        boolean result2 = liveOrderBoard.cancelOrder(mockOrder2);
        boolean result3 = liveOrderBoard.cancelOrder(mockOrder3);

        // verify results
        assertThat(result1, is(true));
        assertThat(result2, is(false));
        assertThat(result3, is(true));

        // verify that call was delegated to the mock
        verify(liveOrderBoardState).cancelOrder(mockOrder1);
        verify(liveOrderBoardState).cancelOrder(mockOrder2);
        verify(liveOrderBoardState).cancelOrder(mockOrder3);

        verifyNoMoreInteractions(liveOrderBoardState);
    }

    /**
     * Verify that live order board is using delegating call to live order board state instance to generate summary information
     */
    @Test
    public void summaryInformation() {
        // prepare
        LiveOrderBoardSummaryInformation summaryInformation = mock(LiveOrderBoardSummaryInformation.class);
        when(liveOrderBoardState.getLiveOrderBoardSummaryInformation()).thenReturn(summaryInformation);

        LiveOrderBoard liveOrderBoard = new LiveOrderBoard(liveOrderBoardState);

        // execute
        LiveOrderBoardSummaryInformation result = liveOrderBoard.getSummaryInformation();

        // verify
        assertThat(result, is(sameInstance(summaryInformation)));
    }

    @Test
    public void verifyToString() {
        when(liveOrderBoardState.toString()).thenReturn("DUMMY STATE");

        assertThat(liveOrderBoard.toString(), is("LiveOrderBoard{DUMMY STATE}"));
    }
}
