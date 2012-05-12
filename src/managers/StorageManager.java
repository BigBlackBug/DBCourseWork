/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import DAO.BookDAO;
import beans.Book;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

/**
 *
 * @author BigBlackBug
 */
public class StorageManager extends SimpleJdbcDaoSupport {

    public List<LibraryEntry> getAllEntries() {
        return getSimpleJdbcTemplate().query("select * from library", new LibraryEntryMapper());
    }
    public List<Map<String,Object>> getAllEntriesAsMap() {
        return getSimpleJdbcTemplate().queryForList("select * from library");
    }

    public void replenishLibrary(String isbn, int amount) {
        try {
            getSimpleJdbcTemplate().queryForMap("select amount from library where isbn=?", isbn);
            getSimpleJdbcTemplate().update("update library set amount=amount+? where isbn=?", amount, isbn);
        } catch (EmptyResultDataAccessException ex) {
            getSimpleJdbcTemplate().update("insert into library values(?,?)", isbn, amount);
        }
    }
     public List<LibraryEntry> filter(String isbn) {
        return getSimpleJdbcTemplate().query(String.format("select * from library where char(isbn) like '%s", isbn) + "%'",
                new LibraryEntryMapper());
    }
     public List<Map<String,Object>> filterAsMap(String isbn) {
        return getSimpleJdbcTemplate().queryForList(String.format("select * from library where char(isbn) like '%s", isbn) + "%'");
    }

    public void replenishLibrary(Map<String, Integer> libraryEntries) {
        for (String key : libraryEntries.keySet()) {
            replenishLibrary(key, libraryEntries.get(key));
        }
    }

    public void withdraw(String isbn, int amount) {
        int existingAmount = getSimpleJdbcTemplate().queryForInt("select amount from library where isbn=?", isbn);
        if (existingAmount > amount) {
            getSimpleJdbcTemplate().update("update library set amount=amount-? where isbn=?", amount, isbn);
        } else if (existingAmount == amount) {
            getSimpleJdbcTemplate().update("delete from library where isbn=?", isbn);
        } else {
            throw new RuntimeException("not enough books available");
        }

    }

    private static final class LibraryEntryMapper implements RowMapper<LibraryEntry> {

        @Override
        public LibraryEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new LibraryEntry(rs.getString("isbn"), rs.getInt("amount"));
        }
    }

    public static final class LibraryEntry {

        private String isbn;
        private int amount;

        public LibraryEntry(String isbn, int amount) {
            this.isbn = isbn;
            this.amount = amount;
        }

        public int getAmount() {
            return amount;
        }

        public String getIsbn() {
            return isbn;
        }
    }
}
