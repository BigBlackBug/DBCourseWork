/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import beans.Publisher;
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
public class PublisherDAO extends SimpleJdbcDaoSupport implements DAO<Publisher> {

    @Override
    public void insert(Publisher item) throws DuplicateKeyException {
        getSimpleJdbcTemplate().update("insert into publisher(publisher_name,address,phone) "
                + "values(?,?,?)", item.name, item.address, item.phone);
    }

    @Override
    public void updateByID(Publisher item) {
        getSimpleJdbcTemplate().update("update publisher set publisher_name=?,"
                + "address=?, phone=? where publisher_id=?",
                item.name, item.address, item.phone, item.publisherId);
    }

    @Override
    public void delete(Object id) {
        getSimpleJdbcTemplate().update("delete from publisher where publisher_id=?", id);
    }

    public Publisher findByName(String id) {
        try {
            return getSimpleJdbcTemplate().queryForObject(
                    "select * from publisher where publisher_name =  ?", new PublisherMapper(), id);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public Publisher findById(Object id) {
        try {
            return getSimpleJdbcTemplate().queryForObject(
                    "select * from publisher where publisher_id =  ?", new PublisherMapper(), id);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Publisher> getAll() {
        return getSimpleJdbcTemplate().query("select * from publisher", new PublisherMapper());
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

    @Override
    public List<Map<String, Object>> getAllAsMap() {
        return getSimpleJdbcTemplate().queryForList("select * from publisher");
    }

    private static final class PublisherMapper implements RowMapper<Publisher> {

        @Override
        public Publisher mapRow(ResultSet rs, int rowNum) throws SQLException {
            Publisher a = new Publisher();
            a.name=(rs.getString("publisher_name"));
            a.publisherId=(rs.getInt("publisher_id"));
            a.address=(rs.getString("address"));
            a.phone=(rs.getString("phone"));
            return a;
        }
    }
}
