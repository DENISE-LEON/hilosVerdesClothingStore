package org.yearup.data.mysql;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;

@Repository
public class ShoppingCartDao extends DaoBase implements org.yearup.data.IshoppingCartDao {
    ProductDao productDao;

    private JdbcTemplate template;

    public ShoppingCartDao(DataSource dataSource, JdbcTemplate template) {
        super(dataSource);
        this.template = template;
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        String statement = """
                SELECT *
                FROM Shopping_cart
                WHERE user_id = ?
                """;
        return null;
    }


    @Override
    public ShoppingCartItem addItemToCart(int userId, ShoppingCartItem item) {
        String statement = """
                INSERT INTO Shopping_cart
                VALUES (?, ?, ?);
                """;


        return null;
    }

    @Override
    public ShoppingCartItem deleteItem(int userId, int productId) {
        String statement = """
                DELETE FROM Shopping_cart
                WHERE user_id = ? AND product_id =?;
                """;

        return null;
    }

    @Override
    public void updateQuantity(int userId, int productId, int quantity) {
        String statement = """
                UPDATE shopping_cart
                SET quantity = ?
                WHERE user_id =? AND product_id =?;
                """;
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

        Product product = new Product (productId, name, price, categoryId, description, subCategory, stock, isFeatured, imageUrl);

        ShoppingCartItem item = new ShoppingCartItem();
        item.setProduct(product);
        item.setQuantity(results.getInt("quantity"));
        return item;
    };
}
