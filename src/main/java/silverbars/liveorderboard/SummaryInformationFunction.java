package silverbars.liveorderboard;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.function.Function;

/**
 * A factory that translates a summary order board state into a summary information
 */
class SummaryInformationFunction implements Function<LiveOrderBoardState, LiveOrderBoardSummaryInformation> {
    @Override
    public LiveOrderBoardSummaryInformation apply(@Nonnull LiveOrderBoardState state) {
        return new LiveOrderBoardSummaryInformation(Collections.emptyList());
    }
}
