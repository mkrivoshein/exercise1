package silverbars.liveorderboard;

import silverbars.liveorderboard.order.Order;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

/**
 * A factory that translates a summary order board state into a summary information
 */
class SummaryInformationFunction implements Function<LiveOrderBoardState, LiveOrderBoardSummaryInformation> {
    @Override
    public LiveOrderBoardSummaryInformation apply(@Nonnull LiveOrderBoardState state) {
        // split the stream into buy and sell and group data by price
        Map<Double, Set<Order>> buyEntries = buyEntries(state);
        Map<Double, Set<Order>> sellEntries = sellEntries(state);


        return new LiveOrderBoardSummaryInformation(buyEntries, sellEntries);
    }

    private Map<Double, Set<Order>> buyEntries(LiveOrderBoardState state) {
        return generateEntries(state, Order::isBuy, Comparator.reverseOrder());
    }

    private Map<Double, Set<Order>> sellEntries(LiveOrderBoardState state) {
        return generateEntries(state, Order::isSell, Comparator.naturalOrder());
    }

    private Map<Double, Set<Order>> generateEntries(LiveOrderBoardState state,
                                                    Predicate<Order> orderFilter,
                                                    Comparator<Double> priceSortingOrder) {
        SortedMap<Double, Set<Order>> result = new TreeMap<>(priceSortingOrder);

        Stream<Order> filteredOrderStream = state.liveOrders().stream().filter(orderFilter);

        result.putAll(groupByPrice(filteredOrderStream));

        return result;
    }

    private Map<Double, Set<Order>> groupByPrice(@Nonnull Stream<Order> orders) {
        return orders.collect(groupingBy(Order::getPrice, toSet()));
    }
}
