package org.yearup.data.mysql;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.yearup.models.Product;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductDao extends DaoBase implements org.yearup.data.ProductDao {
    private JdbcTemplate template;

    public ProductDao(JdbcTemplate template, DataSource dataSource) {
        super(dataSource);
        this.template = template;

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
        minPrice = minPrice == null ? BigDecimal.ZERO : minPrice;
        maxPrice = maxPrice == null ? new BigDecimal("-1") : maxPrice;
        subCategory = subCategory == null ? "" : subCategory;


        //need to account for each placeholder(hence the repetiteveness)and make sure the order matches the query
        return template.query(statement, mapRow, categoryId, categoryId, maxPrice, minPrice, maxPrice, subCategory, subCategory);

    }

    @Override
    public List<Product> listByCategoryId(int categoryId) {

        String statement = """
                SELECT *
                FROM products
                WHERE category_id = ?;
                """;

        return template.query(statement, mapRow, categoryId);
    }


    @Override
    public Product getById(int productId) {
        String statement = """
                SELECT *
                FROM products
                WHERE product_id = ?;
                """;
        //query returns the list, for object returns 1 object
        return template.queryForObject(statement, mapRow, productId);
    }

    @Override
    public Product create(Product product) {

        String statement = """
                INSERT INTO products (name, price, category_id, description, subcategory, image_url, stock, featured)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?);
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();


        template.update
                (connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            statement,
                            Statement.RETURN_GENERATED_KEYS
                    );
                    ps.setString(1, product.getName());
                    ps.setBigDecimal(2, product.getPrice());
                    ps.setInt(3, product.getCategoryId());
                    ps.setString(4, product.getDescription());
                    ps.setString(5, product.getSubCategory());
                    ps.setString(6, product.getImageUrl());
                    ps.setInt(7, product.getStock());
                    ps.setBoolean(8, product.isFeatured());
                    return ps;
                }, keyHolder);

        int genkey = keyHolder.getKey().intValue();

        return new Product(genkey, product.getName(), product.getPrice(), product.getCategoryId(),
                product.getDescription(), product.getSubCategory(), product.getStock(), product.isFeatured(), product.getImageUrl());

    }

    @Override
    public void update(int productId, Product product) {
        String statement = """
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

        template.update(statement,
                product.getName(),
                product.getPrice(),
                product.getCategoryId(),
                product.getDescription(),
                product.getSubCategory(),
                product.getImageUrl(),
                product.getStock(),
                product.isFeatured(),
                productId
        );
    }

    @Override
    public void delete(int productId) {

        String statement = """
                DELETE FROM products
                WHERE product_id = ?;
                """;

        template.update(statement, productId);
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
