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
import managers.GiveAwayManager;
import managers.StorageManager;
import org.springframework.dao.DataAccessException;

/**
 *
 * @author bigblackbug
 */
public class NewBookDialog extends JDialog {

    private NewBookPanel bookPanel;
    private AdminLibraryManager libraryManager;
    private AdminPanel adminPanel;

    public NewBookDialog(AdminLibraryManager adminLibraryManager, AdminPanel adminPanel) {
        this.libraryManager = adminLibraryManager;
        this.adminPanel = adminPanel;
        bookPanel = new NewBookPanel();
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
                JTextField isbnTF = bookPanel.getIsbnTextField();
                String isbn = isbnTF.getText();
                if (isbn.isEmpty()) {
                    errorLabel.setText("isbn field is empty");
                    return;
                }

                String pub = (String) bookPanel.getPublisherComboBox().getSelectedItem();
                List<String> selectedAuthors = bookPanel.getAuthorList().getSelectedValuesList();
                if (selectedAuthors.isEmpty()) {
                    errorLabel.setText("Please select at least one author");
                    return;
                }
                Integer amount=(Integer) bookPanel.getAmountSpinner().getValue();
                if(amount<=0){
                    errorLabel.setText("The amount should be a positive number");
                    return;
                }
                
                BookDAO bookDAO = libraryManager.getBookDAO();
                try {
                    bookDAO.insert(new Book(isbn, libraryManager.getPublisherDAO().findByName(pub).getPublisherId()));
                    for (String author : selectedAuthors) {
                        bookDAO.addAuthor(isbn, libraryManager.getAuthorDAO().findByName(author).getID());
                    }
                    adminPanel.prepare();

                    NewBookDialog.this.setVisible(false);
                } catch (org.springframework.dao.DuplicateKeyException dex) {
                    errorLabel.setText("Duplicate key");
                } catch (DataAccessException daex) {
                    errorLabel.setText("Unknown error");
                }
                StorageManager gam=libraryManager.getStorageManager();
                //try{
                gam.replenishLibrary(isbn,amount);
                //}



            }
        });
    }
}
