package org.yearup.data.mysql;

import org.yearup.models.OrderLine;
import org.yearup.models.ShoppingCartItem;

public interface IorderLineDao {

    public OrderLine add(int orderId, ShoppingCartItem item);
}
