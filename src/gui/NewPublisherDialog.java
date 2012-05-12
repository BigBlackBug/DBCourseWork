/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import DAO.DocumentDAO;
import DAO.PublisherDAO;
import beans.Document;
import beans.Publisher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import org.springframework.dao.DuplicateKeyException;

/**
 *
 * @author BigBlackBug
 */
public class NewPublisherDialog extends JDialog{
    
    private PublisherDAO publisherDAO;
    private NewDocumentPanel documentPanel;

    public NewPublisherDialog(PublisherDAO publisherDAO) {
        this.publisherDAO = publisherDAO;
        this.documentPanel = new NewDocumentPanel();
        init();
        setLocationRelativeTo(null);
        setModalityType(ModalityType.APPLICATION_MODAL);
        getContentPane().add(documentPanel);
        pack();
    }

    private void init() {
        documentPanel.getCommitButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String newName = documentPanel.getNameTF().getText();
                try {
                    publisherDAO.insert(new Publisher(newName));
                } catch (DuplicateKeyException dex) {
                    documentPanel.getjLabel2().setText("There is a document with this name");
                }
                NewPublisherDialog.this.setVisible(false);
            }
        });
    }
}
