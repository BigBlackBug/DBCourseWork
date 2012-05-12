/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import beans.Publisher;
import beans.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

/**
 *
 * @author BigBlackBug
 */
public class UserDAO extends SimpleJdbcDaoSupport implements DAO<User> {

    @Override
    public void insert(User item) throws DuplicateKeyException {
        getSimpleJdbcTemplate().update("insert into users values(?,?)", item.username, item.password);
    }

    @Override
    public void updateByID(User item) {
        getSimpleJdbcTemplate().update("update users set password=? and username=? where username=?",
                item.password, item.username, item.username);
    }

    @Override
    public void delete(Object id) {
        getSimpleJdbcTemplate().update("delete from users where username=?", id);
    }

    @Override
    public User findById(Object id) {
        try {
            return getSimpleJdbcTemplate().queryForObject(
                    "select * from users where username =  ?", new UserMapper(), id);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<User> getAll() {
        return getSimpleJdbcTemplate().query("select * from users", new UserMapper());
    }

    @Override
    public List<Map<String, Object>> getAllAsMap() {
        return getSimpleJdbcTemplate().queryForList("select * from users");
    }

    @Override
    public List<Map<String, Object>> executeQuery(String query) {
        String queryUpperCase = query.toUpperCase();
        if (queryUpperCase.startsWith("INSERT") || queryUpperCase.startsWith("UPDATE") || queryUpperCase.startsWith("DELETE")) {
            getSimpleJdbcTemplate().getJdbcOperations().update(query);
            return Collections.emptyList();
        }
        return getSimpleJdbcTemplate().getJdbcOperations().queryForList(query);
    }

    private static final class UserMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User a = new User();

            a.username = (rs.getString("username"));
            a.password = rs.getString("password");
            //a.setPublisherId(rs.getInt("publisher_id"));
            return a;
        }
    }
}
