package org.yearup.data;

import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface IshoppingCartDao {

    ShoppingCart getByUserId(int userId);
    // add additional method signatures here
    //add item
    ShoppingCartItem addItemToCart(int userId,int prodId);

    //delete item
    ShoppingCartItem deleteItem(int userId,int productId);

    //update item quantity
    ShoppingCartItem updateQuantity(int userId,int productId);
}
