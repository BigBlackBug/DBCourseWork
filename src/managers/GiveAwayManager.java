/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

/**
 *
 * @author BigBlackBug
 */
public class GiveAwayManager extends SimpleJdbcDaoSupport {

    public void giveBook(Integer documentID, String isbn, int amount) throws RuntimeException {
        int existingAmount = getSimpleJdbcTemplate().queryForInt("select amount from library where isbn=?", isbn);
        if (existingAmount > amount) {
            getSimpleJdbcTemplate().update("update library set amount=amount-? where isbn=?", amount, isbn);
        } else if (existingAmount == amount) {
            getSimpleJdbcTemplate().update("delete from library where isbn=?", isbn);
        } else {
            throw new RuntimeException("not enough books available");
        }
        if (getSimpleJdbcTemplate().query("select * from giveaway where document_id=? and isbn=?",
                new GiveAwayEntryMapper(), documentID, isbn).isEmpty()) {
            getSimpleJdbcTemplate().update("insert into giveaway values(?,?,?)", documentID, isbn, amount);
        } else {
            getSimpleJdbcTemplate().update("update giveaway set amount_given=amount_given+? "
                    + "where document_id=? and isbn=?", amount, documentID, isbn);
        }

    }

    public List<Map<String, Object>> getAllEntriesAsMap() {
        return getSimpleJdbcTemplate().queryForList("select * from giveaway");
    }

    public List<GiveAwayEntry> getBooksGivenTo(Integer document) {
        return getSimpleJdbcTemplate().query("select * from giveaway where document_id=?",
                new GiveAwayEntryMapper(), document);
    }

    public List<Map<String, Object>> getBooksGivenAsMapOf(Integer document) {
        return getSimpleJdbcTemplate().queryForList("select * from giveaway where document_id=?",
                document);
    }
    public List<Map<String, Object>> getBooksGivenAsMapInProperFormatOf(Integer document) {
        return getSimpleJdbcTemplate().queryForList("select isbn,amount_given from giveaway where document_id=?",
                document);
    }

    public void returnBook(Integer documentID, String isbn, int amount) throws RuntimeException{
        int existingAmount = getSimpleJdbcTemplate().
                queryForInt("select amount_given from giveaway where isbn=? and document_id=?",
                isbn, documentID);
        if (amount > existingAmount) {
            throw new RuntimeException("i don't have that much");
        } else if (amount == existingAmount) {
            getSimpleJdbcTemplate().
                    update("delete from giveaway where isbn=? and document_id=?", isbn, documentID);
        }
        getSimpleJdbcTemplate().
                update("update library set amount=amount+? where isbn=?", amount, isbn);
        getSimpleJdbcTemplate().
                update("update giveaway set amount_given=amount_given-? where isbn=?", amount, isbn);
    }

    private static final class GiveAwayEntryMapper implements RowMapper<GiveAwayEntry> {

        @Override
        public GiveAwayEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new GiveAwayEntry(rs.getString("isbn"),
                    rs.getInt("document_id"),
                    rs.getInt("amount_given"));
        }
    }

    public static final class GiveAwayEntry {

        private String isbn;
        private int document;
        private int amountGiven;

        public int getAmountGiven() {
            return amountGiven;
        }

        public int getDocument() {
            return document;
        }

        public String getIsbn() {
            return isbn;
        }

        public GiveAwayEntry(String isbn, int document, int amountGiven) {
            this.isbn = isbn;
            this.document = document;
            this.amountGiven = amountGiven;
        }

        @Override
        public String toString() {
            return "User #" + document + " possesses " + amountGiven + " items of " + isbn;

        }
    }
}
