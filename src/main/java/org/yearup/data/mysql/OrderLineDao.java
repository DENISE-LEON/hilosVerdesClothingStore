package org.yearup.data.mysql;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.yearup.models.OrderLine;
import org.yearup.models.ShoppingCartItem;
import java.sql.PreparedStatement;
import java.sql.Statement;



@Repository
public class OrderLineDao {
    JdbcTemplate template;
    ShoppingCartDao shoppingCartDao;



    public OrderLineDao(JdbcTemplate template, ShoppingCartDao shoppingCartDao) {
        this.template = template;
        this.shoppingCartDao = shoppingCartDao;

    }

    public OrderLine add(int orderId, ShoppingCartItem item) {

        String statement = """
                INSERT INTO order_line_items
                (order_line_item_id, order_id, product_id, sales_price, quantity, discount)
                VALUES (NULL, ?, ?, ?, ?, 0);
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder() ;
         template.update(connection -> {
        PreparedStatement preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, orderId);
        preparedStatement.setInt(2, item.getProductId());
        preparedStatement.setBigDecimal(3, item.getLineTotal());
        preparedStatement.setInt(4, item.getQuantity());
        return preparedStatement;
    }, keyHolder);

        int genKey = keyHolder.getKey().intValue();

        OrderLine orderLine = new OrderLine();
        orderLine.setLineItemId(genKey);
        orderLine.setOrderId(orderId);
        orderLine.setProductId(item.getProductId());
        orderLine.setSalesPrice(item.getLineTotal());
        orderLine.setQuantity(item.getQuantity());

        return orderLine;
    }


}
