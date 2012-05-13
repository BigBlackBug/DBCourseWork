/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import beans.Author;
import beans.Book;
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
public class AuthorDAO extends SimpleJdbcDaoSupport implements DAO<Author> {

    @Override
    public void insert(Author item) throws DuplicateKeyException {
        getSimpleJdbcTemplate().update("insert into author(author_name,phone) values(?,?)", item.name,item.phone);
    }

    @Override
    public void updateByID(Author item) {
        getSimpleJdbcTemplate().update("update author set author_name=?, phone=? where author_id=?",
                item.name, item.phone, item.authorId);
    }

    @Override
    public void delete(Object id) {
        getSimpleJdbcTemplate().update("delete from author where author_id=?", id);
    }

    public List<Book> getBooksOf(Integer authorID) {
        return getSimpleJdbcTemplate().query("select book.isbn, book.description, "
                + "book.name, book.pagesAmount, book.publisher_id "
                + "from book join authorship on book.isbn=authorship.isbn where author_id=?",
                new BookDAO.BookMapper(), authorID);
    }
    //

    public void addBook(Integer authorId, String isbn) {
        List<Map<String, Object>> queryForList = getSimpleJdbcTemplate().queryForList(
                "select * from authorship where author_id=? and isbn=?", authorId, isbn);
        if (queryForList.isEmpty()) {
            getSimpleJdbcTemplate().update("insert into authorship(author_id,isbn) values(?,?)", authorId, isbn);
        } else {
            throw new DuplicateKeyException("too bad");
        }
    }

    public void removeBook(Integer authorId, String isbn) {
        getSimpleJdbcTemplate().update("delete from authorship where author_id=? and isbn=?", authorId, isbn);
    }

    public Author findByName(String id) {
        try {
            return getSimpleJdbcTemplate().queryForObject("select * from author where author_name=?", new AuthorMapper(), id);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public Author findById(Object id) {
        try {
            return getSimpleJdbcTemplate().queryForObject("select * from author where author_id=?", new AuthorMapper(), id);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Author> getAll() {
        return getSimpleJdbcTemplate().query("select * from author", new AuthorMapper());
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
        return getSimpleJdbcTemplate().queryForList("select * from author");
    }

    public static final class AuthorMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            Author a = new Author();
            a.name=(rs.getString("author_name"));
            a.authorId=(rs.getInt("author_id"));
            a.phone=rs.getString("phone");
            return a;
        }
    }
}
