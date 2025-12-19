package org.yearup.data.mysql;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.yearup.data.IcategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class CategoryDao extends DaoBase implements IcategoryDao {
    private final JdbcTemplate template;

    // @Autowired
    public CategoryDao(JdbcTemplate template, DataSource dataSource) {
        super(dataSource);
        this.template = template;

    }

    //
    @Override
    public List<Category> getAllCategories() {
        String statement = """
                SELECT category_id,
                name,
                description
                FROM Categories
                """;

        return template.query(statement, categoryRowMapper);
    }

    @Override
    public Category getById(int categoryId) {
        String statement = """
                SELECT category_id,
                name,
                description
                FROM Categories
                WHERE category_id = ?;
                """;
        return template.queryForObject(statement, categoryRowMapper, categoryId);
    }

    @Override
    public Category create(Category category) {
        String statement = """
                INSERT INTO Categories (category_id, name, description)
                VALUES (NULL, ?, ?);
                """;

        //keyholder is interface that stores keys, generated keyholder is an implememntation of keyholder interface
        KeyHolder keyHolder = new GeneratedKeyHolder();


        //since the generated key is wanted lambda and prepared statements are needed
        template.update
                (connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            statement,
                            Statement.RETURN_GENERATED_KEYS
                    );
                    ps.setString(1, category.getName());
                    ps.setString(2, category.getDescription());
                    return ps;
                }, keyHolder);

        int genKey = keyHolder.getKey().intValue();


        return new Category(genKey, category.getName(), category.getDescription());
    }

    @Override
    public void update(int categoryId, Category category) {
        String statement = """
                UPDATE Categories
                SET
                name = ?,
                description = ?
                WHERE category_id = ?;
                """;

        template.update(
                statement,
                category.getName(),
                category.getDescription(),
                categoryId
        );
    }

    @Override
    public void delete(int categoryId) {
        // delete category
        String statement = """
                DELETE FROM Categories
                WHERE category_id = ?;
                """;
        template.update(statement, categoryId);
    }

    public RowMapper<Category> categoryRowMapper = (resultSet, rowNum) -> {
        int categoryId = resultSet.getInt("category_id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");

        return new Category(categoryId, name, description);
    };


}
