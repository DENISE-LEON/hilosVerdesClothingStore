package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.yearup.models.Profile;

import javax.sql.DataSource;
import java.sql.*;

@Repository
public class ProfileDao extends DaoBase implements org.yearup.data.IprofileDao {

    JdbcTemplate template;

    @Autowired
    public ProfileDao(DataSource dataSource, JdbcTemplate template) {
        super(dataSource);
        this.template = template;
    }

    @Override
    public Profile create(Profile profile) {
        String sql = "INSERT INTO profiles (user_id, first_name, last_name, phone, email, address, city, state, zip) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = getConnection())
        {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, profile.getUserId());
            ps.setString(2, profile.getFirstName());
            ps.setString(3, profile.getLastName());
            ps.setString(4, profile.getPhone());
            ps.setString(5, profile.getEmail());
            ps.setString(6, profile.getAddress());
            ps.setString(7, profile.getCity());
            ps.setString(8, profile.getState());
            ps.setString(9, profile.getZip());

            ps.executeUpdate();

            return profile;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Profile getProfileById(int userId) {
        String statement = """
                SELECT *
                FROM profiles
                WHERE user_id = ?;
                """;

        return  template.queryForObject(statement, rowMapper, userId);
    }

    @Override
    public Profile updateProfile(int userId, Profile profile) {
        String statement = """
                UPDATE profiles
                SET first_name = ?,
                last_name = ?,
                phone = ?,
                email = ?,
                address = ?,
                city = ?,
                state = ?,
                zip = ?
                WHERE user_id = ?
                """;
         template.update(statement,
                 profile.getFirstName(),
                 profile.getLastName(),
                 profile.getPhone(),
                 profile.getEmail(),
                 profile.getAddress(),
                 profile.getCity(),
                 profile.getState(),
                 profile.getZip(),
                 userId);
         return profile;
    }

    public RowMapper<Profile> rowMapper = (resultSet, rowNum) -> {
        int userId = resultSet.getInt("user_id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String phone = resultSet.getString("phone");
        String email = resultSet.getString("email");
        String address = resultSet.getString("address");
        String city = resultSet.getString("city");
        String state = resultSet.getString("state");
        String zip = resultSet.getString("zip");

            return new Profile(userId, firstName, lastName, phone, email, address, city,state, zip);
    };
}
