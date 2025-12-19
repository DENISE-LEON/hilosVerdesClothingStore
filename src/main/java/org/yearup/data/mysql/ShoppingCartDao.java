package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;

@Repository
public class ShoppingCartDao extends DaoBase implements org.yearup.data.IshoppingCartDao {

    private final IProductDao productDao;
    private JdbcTemplate template;

    @Autowired
    public ShoppingCartDao(DataSource dataSource, JdbcTemplate template, IProductDao productDao) {
        super(dataSource);
        this.template = template;
        this.productDao = productDao;
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        //to get the product info a join is needed
        String statement = """
                SELECT p.product_id, p.name, p.price, p.category_id, p.description, p.subcategory, p.stock, p.featured, p.image_url,
                sc.user_id, sc.quantity
                FROM Shopping_cart sc
                JOIN Products p ON p.product_id = sc.product_id
                WHERE user_id = ?;
                """;

        //get the list of items
        //object is collected from the rowmapper, all else is ignored by the list
        List<ShoppingCartItem> items = template.query(statement, rowMapper, userId);
        ShoppingCart cart = new ShoppingCart();

        //manual looping is required to add the items in the list to the map bc rowmapper returns simple list
        for (ShoppingCartItem item : items) {
            cart.add(item);
        }

        //can condense line 37-43(getting list and looping) with streams:
        //ShoppingCart cart = new ShoppingCart();
        //template.query(statement, rowMapper, userId)
        //  .forEach(cart::add);
        return cart;
    }

    @Override
    public ShoppingCart addItemToCart(int userId, int prodId) {
         if (getByUserId(userId).contains(prodId)) {
           return addProductToCartAgain(userId, prodId);

        } else {
           return addItemToCartFirstTime(userId, prodId);
        }
    }

    public ShoppingCart addItemToCartFirstTime(int userId, int productId) {
        String statement = """
                INSERT INTO Shopping_cart (user_id, product_id, quantity)
                VALUES (?, ?, 1);
                """;

      template.update(statement, userId, productId);
      ShoppingCartItem item = new ShoppingCartItem();
        item.setProduct(productDao.getById(productId));

        return getByUserId(userId);
    }

    public ShoppingCart addProductToCartAgain(int userId, int productId) {
        String statement = """
                UPDATE shopping_cart
                SET quantity = quantity + 1
                WHERE user_id = ? AND product_id = ?;
                """;
        template.update(statement, userId, productId);
        // ShoppingCart cart = new
        return getByUserId(userId);
    }


    @Override
    public ShoppingCart deleteItem(int userId, int productId) {
        String statement = """
                DELETE FROM Shopping_cart
                WHERE user_id = ? AND product_id =?;
                """;

        template.update(statement, userId, productId);
        return getByUserId(userId);
    }

    @Override
    public ShoppingCart deleteAllItems(int userId) {
        String statement = """
                DELETE FROM Shopping_cart
                WHERE user_id = ?
                """;

        template.update(statement, userId);
        return getByUserId(userId);
    }

    @Override
    public ShoppingCart updateQuantity(int userId, int productId, int quantity) {
        String statement = """
                UPDATE shopping_cart
                SET quantity = ?
                WHERE user_id = ? AND product_id = ?;
                """;
        template.update(statement, userId, productId, quantity);

        return getByUserId(userId);

    }

    //one row in shopping cart = one shopping cart item
    public RowMapper<ShoppingCartItem> rowMapper = (results, rowNum) -> {

        int productId = results.getInt("product_id");
        String name = results.getString("name");
        BigDecimal price = results.getBigDecimal("price");
        int categoryId = results.getInt("category_id");
        String description = results.getString("description");
        String subCategory = results.getString("subcategory");
        int stock = results.getInt("stock");
        boolean isFeatured = results.getBoolean("featured");
        String imageUrl = results.getString("image_url");

        Product product = new Product(productId, name, price, categoryId, description, subCategory, stock, isFeatured, imageUrl);

        ShoppingCartItem item = new ShoppingCartItem();
        item.setProduct(product);
        item.setQuantity(results.getInt("quantity"));
        return item;
    };
}
