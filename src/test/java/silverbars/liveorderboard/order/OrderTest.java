package silverbars.liveorderboard.order;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OrderTest {
    @ParameterizedTest
    @CsvSource(
            {
                    "orderA,user1,3.5,306,SELL",
                    "orderB,user1,1.2,310,SELL",
                    "orderC,user2,1.5,307,SELL",
                    "orderD,user2,2.0,306,SELL",
                    "orderE,user3,1.2,310,BUY",
            }
    )
    public void createTestOrders(String orderId, String userId, double quantity, double price, OrderSide orderSide) {
        Order order = new Order(orderId, userId, quantity, price, orderSide);

        assertAll("order",
                () -> assertThat(order.getOrderId(), is(orderId)),
                () -> assertThat(order.getUserId(), is(userId)),
                () -> assertThat(order.getQuantity(), is(quantity)),
                () -> assertThat(order.getPrice(), is(price)),
                () -> assertThat(order.getOrderSide(), is(orderSide))
        );
    }

    @SuppressWarnings("UnstableApiUsage")
    @Test
    public void testEqualsAndHashCode() {
        Order order1 = new Order("orderA", "user1", 3.5, 306.0, OrderSide.BUY);
        Order order2 = new Order("orderA", "user1", 3.5, 306.0, OrderSide.BUY);
        Order order3 = new Order("orderB", "user1", 3.5, 306.0, OrderSide.BUY);
        Order order4 = new Order("orderB", "user2", 3.5, 306.0, OrderSide.BUY);

        new EqualsTester()
                .addEqualityGroup(order1, order2)
                .addEqualityGroup(order3)
                .addEqualityGroup(order4)
                .testEquals();
    }
}