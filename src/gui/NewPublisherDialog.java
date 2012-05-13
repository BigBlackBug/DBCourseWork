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
public class NewPublisherDialog extends JDialog {

    private PublisherDAO publisherDAO;
    private NewPublisherPanel publisherPanel;

    public NewPublisherDialog(PublisherDAO publisherDAO) {
        this.publisherDAO = publisherDAO;
        this.publisherPanel = new NewPublisherPanel();
        init();
        setLocationRelativeTo(null);
        setModalityType(ModalityType.APPLICATION_MODAL);
        getContentPane().add(publisherPanel);
        pack();
    }

    private void init() {
        publisherPanel.getCommitButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String newName = publisherPanel.getNameTF().getText();
                String newAddress = publisherPanel.getAddressTF().getText();
                String newPhone = publisherPanel.getPhoneTF().getText();
                if (!newPhone.matches("[1-9]+")) {
                    publisherPanel.getInfoLabel().setText("Phone number can only "
                            + "consist of numbers");
                    return;
                }
                try {
                    publisherDAO.insert(new Publisher(newName, newAddress, newPhone));
                } catch (DuplicateKeyException dex) {
                    publisherPanel.getInfoLabel().setText("There is a document with this name");
                }
                NewPublisherDialog.this.setVisible(false);
            }
        });
    }
}
