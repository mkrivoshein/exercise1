package silverbars.liveorderboard;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class LiveOrderBoardSummaryInformationTest {
    private LiveOrderBoard liveOrderBoard = new LiveOrderBoard();
    @Test
    public void emptySummaryInformation() {
        LiveOrderBoardSummaryInformation summaryInformation = liveOrderBoard.getSummaryInformation();

        assertThat(summaryInformation, is(notNullValue()));
    }
}