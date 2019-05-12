package silverbars.liveorderboard;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SummaryInformationFunctionTest {
    @Test
    public void emptySummaryInformation() {
        SummaryInformationFunction function = new SummaryInformationFunction();

        LiveOrderBoardSummaryInformation summaryInformation = function.apply(new LiveOrderBoardState());

        assertThat(summaryInformation.entries(), is(ImmutableList.of()));
    }

}