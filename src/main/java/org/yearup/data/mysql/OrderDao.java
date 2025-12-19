package org.yearup.data.mysql;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.yearup.models.Order;
import org.yearup.models.Profile;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class OrderDao extends DaoBase implements IorderDao {

    OrderLineDao lineDao;
    ShoppingCartDao shoppingCartDao;
    JdbcTemplate template;
    ProfileDao profileDao;

    public OrderDao(DataSource dataSource, JdbcTemplate template, OrderLineDao lineDao, ShoppingCartDao shoppingCartDao, ProfileDao profileDao) {
        super(dataSource);
        this.template = template;
        this.lineDao = lineDao;
        this.shoppingCartDao = shoppingCartDao;
        this.profileDao = profileDao;
    }

    @Override
    public Order checkOut(int userId) {
        Profile profile = profileDao.getProfileById(userId);
        ShoppingCart cart = shoppingCartDao.getByUserId(userId);
        String statement = """
                INSERT INTO orders (order_id, user_id, date, address, city, state, zip, shipping_amount)
                VALUES
                (NULL, ?, NOW(), ?, ?, ?, ?, ?)
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, profile.getAddress()); // or user profile address
            preparedStatement.setString(3, profile.getCity());
            preparedStatement.setString(4, profile.getState());
            preparedStatement.setString(5, profile.getZip());
            preparedStatement.setBigDecimal(6, shoppingCartDao.getByUserId(userId).getTotal());
            return preparedStatement;
        }, keyHolder);

        int genKey = keyHolder.getKey().intValue();

        Order order = new Order(genKey, userId, profile.getAddress(), profile.getCity(), profile.getState(), profile.getZip(), cart.getTotal());
        for (ShoppingCartItem item : cart.getItems().values()) {
            lineDao.add(genKey, item);
        }

        shoppingCartDao.deleteAllItems(userId);
        return order;
    }


}
