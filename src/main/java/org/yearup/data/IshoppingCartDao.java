package org.yearup.data;

import org.yearup.models.ShoppingCart;


public interface IshoppingCartDao {

    ShoppingCart getByUserId(int userId);
    // add additional method signatures here
    //add item
    ShoppingCart addItemToCart(int userId,int prodId);

    //delete item
    ShoppingCart deleteItem(int userId,int productId);

    //delete all items
    ShoppingCart deleteAllItems(int userId);

    //update item quantity
    ShoppingCart updateQuantity(int userId,int productId, int quantity);
}
