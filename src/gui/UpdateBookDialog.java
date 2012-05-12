/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import DAO.BookDAO;
import beans.Author;
import beans.Book;
import beans.Publisher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import managers.AdminLibraryManager;
import org.springframework.dao.DataAccessException;

/**
 *
 * @author BigBlackBug
 */
public class UpdateBookDialog extends JDialog {
    
    private UpdateBookPanel bookPanel;
    private AdminLibraryManager libraryManager;
    private AdminPanel adminPanel;
    private String isbn;
    
    public UpdateBookDialog(AdminLibraryManager libraryManager, AdminPanel adminPanel, String isbn) {
        this.libraryManager = libraryManager;
        this.adminPanel = adminPanel;
        this.isbn = isbn;
        this.bookPanel = new UpdateBookPanel();
        bookPanel.getjLabel1().setText("UPDATING ISBN = "+isbn);
        populateAuthorList();
        setLocationRelativeTo(null);
        
        setModalityType(ModalityType.APPLICATION_MODAL);
        populatePublisherComboBox();
        getContentPane().add(bookPanel);
        initCommitButton();
        pack();
        
    }
    
    private void populateAuthorList() {
        JList authorList = bookPanel.getAuthorList();
        DefaultListModel listModel = new DefaultListModel();
        List<Author> authors = libraryManager.getAuthorDAO().getAll();
        for (Author a : authors) {
            listModel.addElement(a.getName());
        }
        
        authorList.setModel(listModel);
        authorList.repaint();
    }
    
    private void populatePublisherComboBox() {
        JComboBox publisherComboBox = bookPanel.getPublisherComboBox();
        //ComboBoxModel model = publisherComboBox.getModel();
        List<Publisher> publishers = libraryManager.getPublisherDAO().getAll();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
        for (Publisher p : publishers) {
            model.addElement(p.getName());
        }
        publisherComboBox.setModel(model);
        publisherComboBox.repaint();
        
    }
    
    private void initCommitButton() {
        bookPanel.getCommitButton().addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                JLabel errorLabel = bookPanel.getErrorLabel();
                if (isbn.isEmpty()) {
                    errorLabel.setText("isbn field is empty");
                    return;
                }
                
                String pub = (String) bookPanel.getPublisherComboBox().getSelectedItem();
                
                BookDAO bookDAO = libraryManager.getBookDAO();
                try {
                    bookDAO.updateByID(new Book(isbn, libraryManager.getPublisherDAO().findByName(pub).getPublisherId()));
                } catch (DataAccessException daex) {
                    errorLabel.setText("Unknown error");
                }
                List<String> selectedAuthors = bookPanel.getAuthorList().getSelectedValuesList();
                if (!selectedAuthors.isEmpty()) {
                    bookDAO.deleteAllAuthors(isbn);
                    for (String author : selectedAuthors) {
                        bookDAO.addAuthor(isbn, libraryManager.getAuthorDAO().findByName(author).getID());
                    }
                }
                adminPanel.prepare();
                UpdateBookDialog.this.setVisible(false);
            }
        });
    }
}
