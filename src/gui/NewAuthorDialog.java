/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import DAO.AuthorDAO;
import DAO.DocumentDAO;
import beans.Author;
import beans.Document;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import org.springframework.dao.DuplicateKeyException;

/**
 *
 * @author BigBlackBug
 */
public class NewAuthorDialog extends JDialog{
    
    private AuthorDAO authorDAO;
    private NewDocumentPanel documentPanel;

    public NewAuthorDialog(AuthorDAO authorDAO) {
        this.authorDAO = authorDAO;
        this.documentPanel = new NewDocumentPanel();
        init();
        setLocationRelativeTo(null);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        getContentPane().add(documentPanel);
        pack();
    }

    private void init() {
        documentPanel.getCommitButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String newName = documentPanel.getNameTF().getText();
                try {
                    authorDAO.insert(new Author(newName));
                } catch (DuplicateKeyException dex) {
                    documentPanel.getjLabel2().setText("There is an author with this name");
                }
                NewAuthorDialog.this.setVisible(false);
            }
        });
    }
}
