package org.yearup.data.mysql;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.yearup.models.Product;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductDao extends DaoBase implements org.yearup.data.ProductDao {
    private JdbcTemplate jdbcTemplate;

    public ProductDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        super(dataSource);
        this.jdbcTemplate = jdbcTemplate;

    }

    @Override
    public List<Product> search(Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, String subCategory) {
        List<Product> products = new ArrayList<>();

        //? = -1:ignore filter(sentinel val)
        //put the ignores first so if val = -1 sql ignores filter completely, prevents invalid expressions from running
        String statement = """
                SELECT *
                FROM products
                WHERE (? = -1 OR category_id = ?)
                AND (? = -1 OR price BETWEEN ? AND ?)
                AND (? = '' OR subcategory = ?);""";

        //here is where setting to the sentinel value is handled, if null sert ? = -1(ignore)
        categoryId = categoryId == null ? -1 : categoryId;
        //change ignore to if null set to 0
        minPrice = minPrice == null ?  BigDecimal.ZERO : minPrice;
        maxPrice = maxPrice == null ? new BigDecimal("-1") : maxPrice;
        subCategory = subCategory == null ? "" : subCategory;


        //need to account for each placeholder(hence the repetiteveness)and make sure the order matches the query
        return jdbcTemplate.query(statement, mapRow, categoryId, categoryId, maxPrice, minPrice, maxPrice, subCategory, subCategory);

    }

    @Override
    public List<Product> listByCategoryId(int categoryId) {

        String statement = """
                SELECT *
                FROM products
                WHERE category_id = ?;
                """;

        return jdbcTemplate.query(statement, mapRow, categoryId);
    }


    @Override
    public Product getById(int productId) {
        String statement = """
                SELECT *
                FROM products
                WHERE product_id = ?;
                """;
        //query returns the list, for object returns 1 object
        return jdbcTemplate.queryForObject(statement, mapRow, productId);
    }

    @Override
    public Product create(Product product) {

        String sql = "INSERT INTO products(name, price, category_id, description, subcategory, image_url, stock, featured) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, product.getName());
            statement.setBigDecimal(2, product.getPrice());
            statement.setInt(3, product.getCategoryId());
            statement.setString(4, product.getDescription());
            statement.setString(5, product.getSubCategory());
            statement.setString(6, product.getImageUrl());
            statement.setInt(7, product.getStock());
            statement.setBoolean(8, product.isFeatured());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the generated keys
                ResultSet generatedKeys = statement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    // Retrieve the auto-incremented ID
                    int orderId = generatedKeys.getInt(1);

                    // get the newly inserted category
                    return getById(orderId);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void update(int productId, Product product) {
        String sql = """
                 UPDATE products
                 SET name = ?,
                 price = ?,
                 category_id = ?,
                 description = ? ,
                 subcategory = ?,
                 image_url = ?,
                 stock = ?,
                 featured = ?
                 WHERE product_id = ?;
                 """;

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, product.getName());
            statement.setBigDecimal(2, product.getPrice());
            statement.setInt(3, product.getCategoryId());
            statement.setString(4, product.getDescription());
            statement.setString(5, product.getSubCategory());
            statement.setString(6, product.getImageUrl());
            statement.setInt(7, product.getStock());
            statement.setBoolean(8, product.isFeatured());
            statement.setInt(9, productId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int productId) {

        String sql = "DELETE FROM products " +
                " WHERE product_id = ?;";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, productId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected RowMapper<Product> mapRow = (row, rowNum) -> {
        int productId = row.getInt("product_id");
        String name = row.getString("name");
        BigDecimal price = row.getBigDecimal("price");
        int categoryId = row.getInt("category_id");
        String description = row.getString("description");
        String subCategory = row.getString("subcategory");
        int stock = row.getInt("stock");
        boolean isFeatured = row.getBoolean("featured");
        String imageUrl = row.getString("image_url");

        return new Product(productId, name, price, categoryId, description, subCategory, stock, isFeatured, imageUrl);
    };
}
