/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import DAO.DocumentDAO;
import beans.Document;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author BigBlackBug
 */
public class SelectUserDialog extends JDialog {

    private SelectUserPanel selectUserPanel;
    private UserPanel userPanel;
    private DefaultListModel<String> listModel;
    private DocumentDAO documentDAO;
    //private int document;
    private boolean userMode;

    private void loadAll() {
        listModel.clear();
        List<Document> all = documentDAO.getAll();
        for (Document d : all) {
            listModel.addElement(d.name);
        }
    }
    /* private void load(int amount){
    listModel.clear();
    }*/

    private void initList() {
        listModel = new DefaultListModel<String>();
        selectUserPanel.getResultsList().setModel(listModel);
        selectUserPanel.getUserInputTF().getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) {
                processChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                processChange();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                processChange();
            }

            private void processChange() {
                String text = selectUserPanel.getUserInputTF().getText();
                if (text.isEmpty() || text.trim().isEmpty()) {
                    loadAll();
                    selectUserPanel.getErrorLabel().setText("");
                    return;
                }
                List<Document> filter = documentDAO.filter(text);
                listModel.clear();
                if (filter.isEmpty()) {

                    selectUserPanel.getErrorLabel().setText("There's no such user");
                } else {
                    selectUserPanel.getErrorLabel().setText("");
                    for (Document d : filter) {
                        listModel.addElement(d.name);
                    }
                }
            }
        });
    }

    private void initSetUserButton() {
        selectUserPanel.getSetUserButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectUserPanel.getResultsList().getSelectedIndex() == -1) {
                    selectUserPanel.getErrorLabel().setText("select a row");
                } else {
                    Document d = documentDAO.search("name",
                            listModel.getElementAt(selectUserPanel.getResultsList().
                            getSelectedIndex()));
                    
                    userPanel.setDocument(d.documentId);
                    userPanel.reload(userMode);
                    SelectUserDialog.this.setVisible(false);
                }
            }
        });
    }

    public SelectUserDialog(DocumentDAO documentDAO, UserPanel userPanel, boolean userMode) {
        this.userMode = userMode;
        this.userPanel = userPanel;
        setModalityType(ModalityType.APPLICATION_MODAL);
        this.documentDAO = documentDAO;
        setLocationRelativeTo(userPanel);
        selectUserPanel = new SelectUserPanel();
        initList();
        loadAll();
        //populatePublisherComboBox();
        getContentPane().add(selectUserPanel);
        initSetUserButton();
        pack();
    }
}
