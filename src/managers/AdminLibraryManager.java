/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import DAO.AuthorDAO;
import DAO.BookDAO;
import DAO.DocumentDAO;
import DAO.PublisherDAO;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

/**
 *
 * @author BigBlackBug
 */
public class AdminLibraryManager extends SimpleJdbcDaoSupport {

    @Autowired
    private AuthorDAO authorDAO;
    @Autowired
    private BookDAO bookDAO;
    @Autowired
    private DocumentDAO documentDAO;
    @Autowired
    private PublisherDAO publisherDAO;
    @Autowired
    private StorageManager storageManager;
    @Autowired
    private GiveAwayManager giveAwayManager;

    public GiveAwayManager getGiveAwayManager() {
        return giveAwayManager;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public AuthorDAO getAuthorDAO() {
        return authorDAO;
    }

    public BookDAO getBookDAO() {
        return bookDAO;
    }

    public List<Map<String, Object>> getAuthorship() {
        return getSimpleJdbcTemplate().queryForList("select * from authorship");
    }

    public PublisherDAO getPublisherDAO() {
        //sert
        return publisherDAO;
    }

    public DocumentDAO getDocumentDAO() {
        return documentDAO;
    }
}
