/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import DAO.DocumentDAO;
import beans.Document;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import managers.GiveAwayManager.GiveAwayEntry;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author BigBlackBug
 */
public class UserLibraryManager {

    @Autowired
    private StorageManager storageManager;
    @Autowired
    private GiveAwayManager giveAwayManager;
    @Autowired
    private DocumentDAO documentDAO;
    
    public DocumentDAO getDocumentDAO(){
        return documentDAO;
    }

    public List<StorageManager.LibraryEntry> getAllStorageEntries() {
        return storageManager.getAllEntries();
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }
    

    public List<Map<String, Object>> getAllStorageEntriesAsMap() {
        return storageManager.getAllEntriesAsMap();
    }

    public List<Map<String, Object>> getAllGiveAwayEntriesAsMap() {
        return giveAwayManager.getAllEntriesAsMap();
    }

    public List<GiveAwayEntry> getBooksOf(int document) {
        return giveAwayManager.getBooksGivenTo(document);//Collections.emptyList();
    }

    public List<Map<String, Object>> getBooksAsMapOf(int document) {
        return giveAwayManager.getBooksGivenAsMapOf(document);//Collections.emptyList();
    }
    public List<Map<String, Object>> getBooksAsMapInProperFormatOf(int document) {
        return giveAwayManager.getBooksGivenAsMapInProperFormatOf(document);//Collections.emptyList();
    }

    public void giveBook(Integer document, String isbn, int amount) throws RuntimeException {
        giveAwayManager.giveBook(document, isbn, amount);
    }

    public void returnBook(Integer document, String isbn, int amount) throws RuntimeException {
        giveAwayManager.returnBook(document, isbn, amount);
    }
}
