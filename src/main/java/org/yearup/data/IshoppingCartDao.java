package org.yearup.data;

import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface IshoppingCartDao {

    ShoppingCart getByUserId(int userId);
    // add additional method signatures here
    //add item
    ShoppingCartItem addItemToCart(int userId,ShoppingCartItem item);

    //delete item
    ShoppingCartItem deleteItem(int userId,int productId);

    //update item quantity
    void updateQuantity(int userId,int productId, int quantity);
}
