/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import beans.Document;
import dbcoursework.DBTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import managers.StorageManager.LibraryEntry;
import managers.UserLibraryManager;

/**
 *
 * @author BigBlackBug
 */
public class UserPanel extends javax.swing.JPanel {

    //@Autowired
    private UserLibraryManager libraryManager;
    private int document = -1;
    private boolean userMode = false;
    private DBTableModel tableModel;
    //private DefaultListModel listModel;

    public UserPanel() {
    }

    /**
     * Creates new form UserPanel
     */
    public UserPanel(UserLibraryManager userLibraryManager) {
        this.libraryManager = userLibraryManager;
        initComponents();
        initList();

//        amountTF.setVisible(false);
        //setAmountButton.setVisible(false);

        showHideButtons();
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableModel = new DBTableModel(libraryManager.getAllStorageEntriesAsMap(), false);
        jTable1.setModel(tableModel);

        // jTable1.setE
    }

    public void setLibraryManager(UserLibraryManager libraryManager) {
        this.libraryManager = libraryManager;
    }

    private void showHideButtons() {
        returnBookButton.setEnabled(userMode);
        giveBookButton.setEnabled(!userMode);
//        libraryModeButton.setEnabled(userMode);
        //       userModeButton.setEnabled(!userMode);
        selectUserButton.setEnabled(userMode);
        modeToggleButton.setSelected(userMode);
        giveBookButton.setVisible(!userMode);
        returnBookButton.setVisible(userMode);
        userInputTF.setVisible(!userMode);
        /*
         * userInputTF.setEnabled(userMode); resultsList.setEnabled(userMode);
         * setUserButton.setEnabled(userMode);
         */
    }

    private void setMode(boolean userMode) {
        this.userMode = userMode;
        showHideButtons();

    }

    public void reload(boolean userMode) {

        setMode(userMode);
        /*
         * listModel.clear(); userInputTF.setText("");
         */
        if (userMode) {
            tableModel.setData(libraryManager.getBooksAsMapInProperFormatOf(document));
        } else {
            tableModel.setData(libraryManager.getAllStorageEntriesAsMap());
        }
    }

    public void setDocument(int document) {
        this.document = document;
    }

    private void initList() {
        // listModel = new DefaultListModel<>();
        // resultsList.setModel(listModel);
        userInputTF.getDocument().addDocumentListener(new DocumentListener() {

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
                String text = userInputTF.getText();
                if (text.isEmpty() || text.trim().isEmpty()) {
                    reload(userMode);
                    errorLabel.setText("");
                    return;
                }
                List<Map<String, Object>> filter = libraryManager.getStorageManager().filterAsMap(text);
                //listModel.clear();
                if (filter.isEmpty()) {

                    errorLabel.setText("There's no such book");
                } else {
                    errorLabel.setText("");
                    tableModel.setData(filter);
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        giveBookButton = new javax.swing.JButton();
        returnBookButton = new javax.swing.JButton();
        errorLabel = new javax.swing.JLabel();
        selectUserButton = new javax.swing.JButton();
        modeToggleButton = new javax.swing.JToggleButton();
        userInputTF = new javax.swing.JTextField();
        infoLabel = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jTable1);

        giveBookButton.setText("give");
        giveBookButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                giveBookButtonActionPerformed(evt);
            }
        });

        returnBookButton.setText("return");
        returnBookButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnBookButtonActionPerformed(evt);
            }
        });

        errorLabel.setBackground(new java.awt.Color(230, 230, 230));
        errorLabel.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        errorLabel.setOpaque(true);

        selectUserButton.setText("select user");
        selectUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectUserButtonActionPerformed(evt);
            }
        });

        modeToggleButton.setText("Library mode");
        modeToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modeToggleButtonActionPerformed(evt);
            }
        });

        infoLabel.setBackground(new java.awt.Color(235, 235, 235));
        infoLabel.setOpaque(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(giveBookButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(infoLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(errorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(modeToggleButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                    .addComponent(returnBookButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userInputTF, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(selectUserButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(modeToggleButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(giveBookButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(returnBookButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(userInputTF, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectUserButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(infoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(errorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(54, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void giveBookButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_giveBookButtonActionPerformed
        int selectedRow = -1;
        if (jTable1.getSelectedRowCount() == 0) {
            errorLabel.setText("select a row");
            return;
        } else {
            selectedRow = jTable1.getSelectedRow();
        }
        new SelectUserDialog(libraryManager.getDocumentDAO(), this, userMode).setVisible(true);
        int amount;
        try {
            amount = Integer.parseInt(JOptionPane.showInputDialog("amount"));
        } catch (NumberFormatException ex) {
            errorLabel.setText("invalid number");
            return;
        }
        /*
         * setAmountButton.addActionListener(new ActionListener() {
         *
         * @Override public void actionPerformed(ActionEvent e) { amount =
         * Integer.parseInt(amountTF.getText()); } });
         */        errorLabel.setText("");
        try {
            String isbn = tableModel.getValueAt(selectedRow, 0).toString();
            libraryManager.giveBook(document, isbn, amount);
            System.out.println("given");
        } catch (RuntimeException ex) {
            errorLabel.setText(ex.getMessage());
        }
        tableModel.setData(libraryManager.getAllStorageEntriesAsMap());
        document = -1;

    }//GEN-LAST:event_giveBookButtonActionPerformed

    private void returnBookButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnBookButtonActionPerformed
        /*
         * int selectedRow = -1; if (jTable1.getSelectedRowCount() == 0) {
         * errorLabel.setText("select a row"); return; } else { selectedRow =
         * jTable1.getSelectedRow(); }
         */
        if (document == -1) {
            errorLabel.setText("select a user");
            return;
        }
        // setMode(!userMode);
        int selectedRow = -1;
        if (jTable1.getSelectedRowCount() == 0) {
            errorLabel.setText("select a row");
            return;
        } else {
            selectedRow = jTable1.getSelectedRow();
        }
        errorLabel.setText("");
        //  new SelectUserDialog(libraryManager.getDocumentDAO(), this, userMode).setVisible(true);
        int amount;
        try {
            amount = Integer.parseInt(JOptionPane.showInputDialog("amount"));
        } catch (NumberFormatException ex) {
            errorLabel.setText("invalid number");
            return;
        }
        try {
            String isbn = tableModel.getValueAt(selectedRow, 0).toString();
            libraryManager.returnBook(document, isbn, amount);
            System.out.println("ret");
        } catch (RuntimeException ex) {
            errorLabel.setText(ex.getMessage());
        }
        // tableModel.setData(libraryManager.getAllStorageEntriesAsMap());
        reload(userMode);
        //  document = -1;
    }//GEN-LAST:event_returnBookButtonActionPerformed

    private void selectUserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectUserButtonActionPerformed
        new SelectUserDialog(libraryManager.getDocumentDAO(), this, userMode).setVisible(true);
        // System.out.println(document);
        //reload(userMode);
    }//GEN-LAST:event_selectUserButtonActionPerformed

    private void modeToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modeToggleButtonActionPerformed

        if (modeToggleButton.isSelected()) {
            //modeToggleButton.setSelected(true);
            modeToggleButton.setText("User mode");
            // giveBookButton.setVisible(false);
            // acceptBookButton.setVisible(false);
            // userInputTF.setVisible(false);
            // jScrollPane2.setVisible(false);
            reload(true);
            // if(document==-1)tableModel.clear();
        } else {
            /*
             * modeToggleButton.setSelected(false);
             * giveBookButton.setVisible(true);
             * acceptBookButton.setVisible(true); userInputTF.setVisible(true);
             */
            //  jScrollPane2.setVisible(true);
            reload(false);
            modeToggleButton.setText("Library mode");
        }
    }//GEN-LAST:event_modeToggleButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel errorLabel;
    private javax.swing.JButton giveBookButton;
    private javax.swing.JLabel infoLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JToggleButton modeToggleButton;
    private javax.swing.JButton returnBookButton;
    private javax.swing.JButton selectUserButton;
    private javax.swing.JTextField userInputTF;
    // End of variables declaration//GEN-END:variables
}
