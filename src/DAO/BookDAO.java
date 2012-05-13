/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import beans.Author;
import beans.Book;
import beans.Document;
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
public class BookDAO extends SimpleJdbcDaoSupport implements DAO<Book> {

    @Override
    public void insert(Book item) throws DuplicateKeyException {
        getSimpleJdbcTemplate().update("insert into book(isbn,publisher_id,"
                + "description, name, pagesAmount) values(?,?,?,?,?)", item.isbn,
                item.publisherId, item.description, item.name, item.pagesAmount);
    }

    @Override
    public void updateByID(Book item) {
        getSimpleJdbcTemplate().update("update book set isbn=?,publisher_id=?, "
                + "description=?, name=?, pagesAmount=? where isbn=?",
                item.isbn, item.publisherId, item.description,
                item.name, item.pagesAmount, item.isbn);
    }

    public void addAuthor(String isbn, Integer authorID) {
        //TODO если такой автор уже есть
        // if(findById(isbn)!=null){
        getSimpleJdbcTemplate().update("insert into authorship values(?,?)", authorID, isbn);
        //}
        //getSimpleJdbcTemplate().update("update book set isbn=?", args);
    }

    public void deleteAuthor(String isbn, Integer authorID) {
        getSimpleJdbcTemplate().update("delete from authorship where isbn=? and author_id=?", isbn, authorID);
    }

    public void deleteAllAuthors(String isbn) {
        List<Author> authorsOf = getAuthorsOf(isbn);
        for (Author a : authorsOf) {
            deleteAuthor(isbn, a.authorId);
        }
    }

    public List<Book> filter(String name) {
        return getSimpleJdbcTemplate().query(String.format("select * from book where char(isbn) like '%s", name) + "%'",
                new BookMapper());
    }

    public List<Author> getAuthorsOf(String isbn) {
        return getSimpleJdbcTemplate().query("select author.author_id,author.author_name,author.phone "
                + "from author join authorship on authorship.author_id=author.author_id "
                + "where isbn=?",
                new AuthorDAO.AuthorMapper(), isbn);
    }

    @Override
    public void delete(Object id) {
        getSimpleJdbcTemplate().update("delete from book where isbn=?", id);
    }

    @Override
    public Book findById(Object id) {
        try {
            return getSimpleJdbcTemplate().queryForObject(
                    "select * from book where isbn =  ?", new BookMapper(), id);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Book> getAll() {
        return getSimpleJdbcTemplate().query("select * from book", new BookMapper());
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
        return getSimpleJdbcTemplate().queryForList("select * from book");
    }

    public static final class BookMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Book a = new Book();
            a.isbn = (rs.getString("isbn"));
            a.publisherId = rs.getInt("publisher_id");
            a.description = rs.getString("description");
            a.name = rs.getString("name");
            a.pagesAmount = rs.getInt("pagesAmount");
            return a;
        }
    }
}
