/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import DAO.DocumentDAO;
import beans.Document;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import org.springframework.dao.DuplicateKeyException;

/**
 *
 * @author BigBlackBug
 */
public class NewDocumentDialog extends JDialog {

    private DocumentDAO documentDAO;
    private NewDocumentPanel documentPanel;

    public NewDocumentDialog(DocumentDAO documentDAO) {
        this.documentDAO = documentDAO;
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
                    documentDAO.insert(new Document(newName));
                } catch (DuplicateKeyException dex) {
                    documentPanel.getjLabel2().setText("There is a document with this name");
                }
                NewDocumentDialog.this.setVisible(false);
            }
        });
    }
}
