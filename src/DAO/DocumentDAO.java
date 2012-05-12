/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import beans.Document;
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
public class DocumentDAO extends SimpleJdbcDaoSupport implements DAO<Document> {

    @Override
    public void insert(Document item) throws DuplicateKeyException {
        getSimpleJdbcTemplate().update("insert into document(name) values(?)", item.name);
    }

    @Override
    public void updateByID(Document item) {
        getSimpleJdbcTemplate().update("update document set name=? where document_id=?",
                item.name, item.documentId);
    }

    @Override
    public void delete(Object id) {
        getSimpleJdbcTemplate().update("delete from document where document_id=?", id);
    }

    @Override
    public Document findById(Object id) {
        try {
            return getSimpleJdbcTemplate().queryForObject(
                    "select * from document where document_id =  ?", new DocumentMapper(), id);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Document> getAll() {
        return getSimpleJdbcTemplate().query("select * from document", new DocumentMapper());
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
        return getSimpleJdbcTemplate().queryForList("select * from document");
    }

    public List<Document> filter(String name) {
        return getSimpleJdbcTemplate().query(String.format("select * from document where char(name) like '%s", name) + "%'",
                new DocumentMapper());
    }

    public Document search(String column, String input) {
        try {
            return getSimpleJdbcTemplate().queryForObject(String.format("select * from document where char(%s) = '%s", 
                    column, input) + "'", new DocumentMapper());
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return new Document();
        }
    }

    private static final class DocumentMapper implements RowMapper<Document> {

        @Override
        public Document mapRow(ResultSet rs, int rowNum) throws SQLException {
            Document a = new Document();
            a.name = (rs.getString("name"));

            a.documentId = (rs.getInt("document_id"));
            return a;
        }
    }
}
