/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import DAO.AuthorDAO;
import beans.Author;
import beans.Book;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import managers.AdminLibraryManager;
import org.springframework.dao.DuplicateKeyException;

/**
 *
 * @author BigBlackBug
 */
public class UpdateAuthorDialog extends JDialog {

    private UpdateAuthorPanel bookPanel;
    private AdminLibraryManager libraryManager;
    private AdminPanel adminPanel;
    private Integer authorID;

    public UpdateAuthorDialog(AdminLibraryManager libraryManager, AdminPanel adminPanel, Integer authorID) {

        this.libraryManager = libraryManager;
        this.adminPanel = adminPanel;
        this.authorID = authorID;
        bookPanel = new UpdateAuthorPanel();
        setLocationRelativeTo(null);
        init();
        setModalityType(ModalityType.APPLICATION_MODAL);

        getContentPane().add(bookPanel);

        pack();
    }

    private void init() {
        bookPanel.getCommitButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                AuthorDAO authorDAO = libraryManager.getAuthorDAO();
                String text = bookPanel.getNameTF().getText();
                if (text.isEmpty()) {
                    bookPanel.getInfoLabel().setText("empty name");
                    return;
                }
                authorDAO.updateByID(new Author(authorID, text));
                adminPanel.prepare();
                UpdateAuthorDialog.this.setVisible(false);
            }
        });
        bookPanel.getAddBookButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                bookPanel.getInfoLabel().setText("");
                List<Book> all = libraryManager.getBookDAO().getAll(); //.toArray(values);
                String[] values = new String[all.size()];
                for (int i = 0; i < all.size(); i++) {
                    values[i] = all.get(i).getIsbn();
                }
                String result = (String) JOptionPane.showInputDialog(null, "message",
                        "title", JOptionPane.INFORMATION_MESSAGE, null, values, values[0]);
                try {
                    libraryManager.getAuthorDAO().addBook(authorID, result);
                } catch (DuplicateKeyException dex) {
                    bookPanel.getInfoLabel().setText(dex.getMessage());
                }
                adminPanel.prepare();
            }
        });
        bookPanel.getRemoveBookButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                bookPanel.getInfoLabel().setText("");
                List<Book> all = libraryManager.getBookDAO().getAll(); //.toArray(values);
                String[] values = new String[all.size()];
                for (int i = 0; i < all.size(); i++) {
                    values[i] = all.get(i).getIsbn();
                }
                String result = (String) JOptionPane.showInputDialog(null, "message",
                        "title", JOptionPane.INFORMATION_MESSAGE, null, values, values[0]);
                try {
                    libraryManager.getAuthorDAO().removeBook(authorID, result);
                } catch (DuplicateKeyException dex) {
                    bookPanel.getInfoLabel().setText(dex.getMessage());
                }
                adminPanel.prepare();
            }
        });
    }
}
