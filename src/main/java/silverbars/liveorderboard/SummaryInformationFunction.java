package silverbars.liveorderboard;

import silverbars.liveorderboard.order.Order;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

/**
 * A factory that translates a summary order board state into a summary information
 */
class SummaryInformationFunction implements Function<LiveOrderBoardState, LiveOrderBoardSummaryInformation> {
    @Override
    public LiveOrderBoardSummaryInformation apply(@Nonnull LiveOrderBoardState state) {
        // split the stream into buy and sell and group data by price
        Map<Double, LiveOrderBoardSummaryInformation.Entry> buyEntries = buyEntries(state);
        Map<Double, LiveOrderBoardSummaryInformation.Entry> sellEntries = sellEntries(state);


        return new LiveOrderBoardSummaryInformation(buyEntries, sellEntries);
    }

    private Map<Double, LiveOrderBoardSummaryInformation.Entry> buyEntries(LiveOrderBoardState state) {
        return generateEntries(state, Order::isBuy, Comparator.reverseOrder());
    }

    private Map<Double, LiveOrderBoardSummaryInformation.Entry> sellEntries(LiveOrderBoardState state) {
        return generateEntries(state, Order::isSell, Comparator.naturalOrder());
    }

    private Map<Double, LiveOrderBoardSummaryInformation.Entry> generateEntries(LiveOrderBoardState state,
                                                                                Predicate<Order> orderFilter,
                                                                                Comparator<Double> priceSortingOrder) {
        Stream<Order> orderStream = state.stream().filter(orderFilter);

        return summarize(groupByPrice(orderStream), priceSortingOrder);
    }


    private Map<Double, List<Order>> groupByPrice(@Nonnull Stream<Order> orders) {
        return orders.collect(groupingBy(Order::getPrice));
    }

    private SortedMap<Double, LiveOrderBoardSummaryInformation.Entry> summarize(Map<Double, List<Order>> orderData,
                                                                                Comparator<Double> keyOrder) {
        SortedMap<Double, LiveOrderBoardSummaryInformation.Entry> result = new TreeMap<>(keyOrder);

        orderData.forEach((price, orders) -> {
            // DoubleStream.sum() attempts to calculate a sum of doubles and retain precision
            double sum = orders.stream().mapToDouble(Order::getQuantity).sum();
            result.put(price, new LiveOrderBoardSummaryInformation.Entry(sum, price));
        });

        return result;
    }
}
