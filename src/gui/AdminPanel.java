/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import beans.Author;
import beans.Book;
import beans.Document;
import beans.Publisher;
import dbcoursework.DBTableModel;
import java.awt.Dialog;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import managers.AdminLibraryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

/**
 *
 * @author BigBlackBug
 */
public class AdminPanel extends javax.swing.JPanel {
    
    enum TableType {
        
        AUTHOR, BOOK, DOCUMENT, PUBLISHER, AUTHORSHIP, LIBRARY
    }
    private TableType currentType = TableType.AUTHOR;
    private DBTableModel currentModel;
    private DefaultListModel<String> infoListModel;
    @Autowired
    private AdminLibraryManager adminLibraryManager;
    
    public AdminLibraryManager getAdminLibraryManager() {
        return adminLibraryManager;
    }
    
    public void setAdminLibraryManager(AdminLibraryManager adminLibraryManager) {
        this.adminLibraryManager = adminLibraryManager;
    }

    /**
     * Creates new form AdminPanel
     */
    public final void prepare() {
        // addBookButton.setVisible(false);
        //  removeBookButton.setVisible(false);
        infoListScrollPane.setVisible(false);
        infoLabel.setVisible(false);
        newitemButton.setEnabled(true);
        deleteItemButton.setEnabled(true);
        updateButton.setEnabled(true);
        
        infoListModel.clear();
        if (currentType == TableType.AUTHOR) {
            //    addBookButton.setVisible(true);
            //   removeBookButton.setVisible(true);
            infoListScrollPane.setVisible(true);
            infoLabel.setVisible(true);
            
            infoLabel.setText("Author's books");
            prepareAuthor();
        } else if (currentType == TableType.BOOK) {
            infoListScrollPane.setVisible(true);
            infoLabel.setVisible(true);
            infoLabel.setText("Book's authors");
            prepareBook();
        } else if (currentType == TableType.DOCUMENT) {
            prepareDocument();
        } else if (currentType == TableType.PUBLISHER) {
            preparePublisher();
        } else if (currentType == TableType.AUTHORSHIP) {
            newitemButton.setEnabled(false);
            deleteItemButton.setEnabled(false);
            updateButton.setEnabled(false);
            prepareAuthorship();
        } else if (currentType == TableType.LIBRARY) {
            // newitemButton.setEnabled(false);
            prepareLibrary();
        }
        jTable1.setModel(currentModel);
        //revalidate();
        validate();
        repaint();
        infoList.revalidate();
        jTable1.repaint();
    }
    
    private void prepareDocument() {
        currentModel = new DBTableModel(adminLibraryManager.getDocumentDAO().getAllAsMap(), false, listener);
    }
    
    private void prepareLibrary() {
        currentModel = new DBTableModel(adminLibraryManager.getStorageManager().getAllEntriesAsMap(), false, listener);
    }
    
    private void prepareBook() {
        currentModel = new DBTableModel(adminLibraryManager.getBookDAO().getAllAsMap(), false, listener);

        //  jTable1.getColumnModel().getColumn(1).setCellEditor(new JScrollTableEditor());
        // TableColumn sportColumn = jTable1.getColumnModel().getColumn(jTable1.getColumnCount()-1);
        //sportColumn.setCellEditor(new BookDropDownEditor(adminLibraryManager.getBookDAO()));
        // JComboBox<String> combo=new JComboBox<>(adminLibraryManager.getBookDAO().getAuthorsOf(TOOL_TIP_TEXT_KEY));
        //sportColumn.setCellEditor(new DefaultCellEditor(comboBox));
    }
    
    private void preparePublisher() {
        currentModel = new DBTableModel(adminLibraryManager.getPublisherDAO().getAllAsMap(), false, listener);
    }
    
    private void prepareAuthor() {
        //jTable1.setModel(new DBTableModel(adminLibraryManager.getAuthorDAO().getAllAsMap(), false));
        currentModel = new DBTableModel(adminLibraryManager.getAuthorDAO().getAllAsMap(), false, listener);
    }
    
    private void prepareAuthorship() {
        //jTable1.setModel(new DBTableModel(adminLibraryManager.getAuthorDAO().getAllAsMap(), false));
        currentModel = new DBTableModel(adminLibraryManager.getAuthorship(), false);
    }
    
    private final class MyTableModelListener implements TableModelListener {
        
        @Override
        public void tableChanged(TableModelEvent e) {
            System.out.println("updating");
            e.getSource();
            /*
             * int row = e.getFirstRow(); int lastRow=e.getLastRow(); for (int i
             * = row; i < lastRow; i++) { update(i); }
             */
            for (int i = 0; i < currentModel.getRowCount(); i++) {
                update(i);
            }
            prepare();
            
        }
    }
    private final MyTableModelListener listener = new MyTableModelListener();
    
    private void initComponents2() {
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        infoListModel = new DefaultListModel<String>();
        //infoList.setModel(new DefaultListModel());
        infoList.setModel(infoListModel);
        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            
            @Override
            public void valueChanged(ListSelectionEvent e) {
                
                System.out.println("HERE");
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                
                int firstIndex = e.getFirstIndex();
                int lastIndex = e.getLastIndex();
                boolean isAdjusting = e.getValueIsAdjusting();
//                guiConfig.currentMacroTable.append("Event for indexes "
//                        + firstIndex + " - " + lastIndex
//                        + "; isAdjusting is " + isAdjusting
//                        + "; selected indexes:");

                if (!isAdjusting) {
                    if (lsm.isSelectionEmpty()) {
                        // guiConfig.currentMacroTable.append(" <none>");
                    } else {
                        infoListModel.clear();
                        // Find out which indexes are selected.
                        int minIndex = lsm.getMinSelectionIndex();
                        int maxIndex = lsm.getMaxSelectionIndex();
                        System.out.println(firstIndex + " " + lastIndex);
                        for (int i = minIndex; i <= maxIndex; i++) {
                            //if (lsm.isSelectedIndex(i)) {
                            String elementAt = (String) jTable1.getModel().getValueAt(i, 0);
                            if (currentType == TableType.AUTHOR) {
                                List<Book> books = adminLibraryManager.getAuthorDAO().getBooksOf(Integer.parseInt(elementAt));
                                for (Book b : books) {
                                    infoListModel.addElement(b.getIsbn());
                                }
                            } else if (currentType == TableType.BOOK) {
                                List<Author> authors = adminLibraryManager.getBookDAO().getAuthorsOf(elementAt);
                                for (Author a : authors) {
                                    infoListModel.addElement(a.getName());
                                }
                                
                            }
                            //}
                        }
                    }
                }
                //    guiConfig.currentMacroTable.append("\n");
                //   guiConfig.currentMacroTable.setCaretPosition(guiConfig.currentMacroTable.getDocument().getLength());

            }
        });
        
        
        
    }
    
    private void update(int row) {
        String[] line = currentModel.getLine(row);
        try {
            if (currentType == TableType.AUTHOR) {
                Author a = new Author();
                a.setID(Integer.parseInt(line[0]));
                a.setName(line[1]);
                adminLibraryManager.getAuthorDAO().updateByID(a);
            } else if (currentType == TableType.BOOK) {
                Book b = new Book();
                b.setIsbn(line[0]);
                b.setPublisherId(Integer.parseInt(line[1]));
                adminLibraryManager.getBookDAO().updateByID(b);
            } else if (currentType == TableType.DOCUMENT) {
                Document d = new Document();
                d.documentId = Integer.parseInt(line[0]);
                d.name = line[1];
                //     d.password = line[2];
                //  d.isAdmin = Boolean.parseBoolean(line[3]);
                adminLibraryManager.getDocumentDAO().updateByID(d);
            } else if (currentType == TableType.PUBLISHER) {
                Publisher p = new Publisher();
                p.setPublisherId(Integer.parseInt(line[0]));
                p.setName(line[1]);
                adminLibraryManager.getPublisherDAO().updateByID(p);
            }
        } catch (DataIntegrityViolationException ex) {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(null, "DataIntegrityViolationException");
        }
    }
    
    private void newItemListener() {
        if (currentType == TableType.AUTHOR) {
            new NewAuthorDialog(adminLibraryManager.getAuthorDAO()).setVisible(true);
        } else if (currentType == TableType.BOOK) {
            new NewBookDialog(adminLibraryManager, this).setVisible(true);
            
        } else if (currentType == TableType.DOCUMENT) {
            new NewDocumentDialog(adminLibraryManager.getDocumentDAO()).setVisible(true);
            
        } else if (currentType == TableType.PUBLISHER) {
            new NewPublisherDialog(adminLibraryManager.getPublisherDAO()).setVisible(true);
        } else if (currentType == TableType.LIBRARY) {
            JDialog d = new JDialog();
            prepareAndShowDialog(d, new NewLibraryItemPanel(this, adminLibraryManager, d));
        }
        
        prepare();
    }

    /*
     * private void updateItemListener() { if (currentType == TableType.AUTHOR)
     * { // prepareAuthor(); } else if (currentType == TableType.BOOK) { //new
     * NewBookDialog(adminLibraryManager).setVisible(true); } else if
     * (currentType == TableType.DOCUMENT) { //prepareDocument(); } else if
     * (currentType == TableType.PUBLISHER) { // preparePublisher(); } }
     */
    private void deleteItemListener() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            errorLabel.setText("select a row");
            return;
        } else {
            errorLabel.setText("");
        }
        int realRow = jTable1.convertRowIndexToModel(selectedRow);
        String[] get = currentModel.getData().get(realRow);
        if (currentType == TableType.AUTHOR) {
            try {
                adminLibraryManager.getAuthorDAO().delete(get[0]);
            } catch (DataIntegrityViolationException dex) {
                errorLabel.setText("this author is in use");
            }
        } else if (currentType == TableType.BOOK) {
            try {
                adminLibraryManager.getBookDAO().delete(get[0]);
            } catch (DataIntegrityViolationException dex) {
                errorLabel.setText("this book is in use");
            }
            
        } else if (currentType == TableType.DOCUMENT) {
            try {
                adminLibraryManager.getDocumentDAO().delete(get[0]);
            } catch (DataIntegrityViolationException dex) {
                errorLabel.setText("this doc is in use");
            }
        } else if (currentType == TableType.PUBLISHER) {
            try {
                adminLibraryManager.getPublisherDAO().delete(get[0]);
            } catch (DataIntegrityViolationException dex) {
                errorLabel.setText("this publisher is in use");
            }
        } else if (currentType == TableType.LIBRARY) {
            adminLibraryManager.getStorageManager().withdrawAll(get[0]);
        }
        prepare();
    }
    
    public AdminPanel(AdminLibraryManager adminLibraryManager) {
        this.adminLibraryManager = adminLibraryManager;
        initComponents();
        initComponents2();
        prepare();
    }
    
    private static void prepareAndShowDialog(JDialog d, JPanel panel) {
        
        d.setLocationRelativeTo(null);
        d.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        d.getContentPane().add(panel);
        d.pack();
        d.setVisible(true);
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
        newitemButton = new javax.swing.JButton();
        updateButton = new javax.swing.JButton();
        deleteItemButton = new javax.swing.JButton();
        errorLabel = new javax.swing.JLabel();
        infoListScrollPane = new javax.swing.JScrollPane();
        infoList = new javax.swing.JList();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        infoLabel = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();

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
        jTable1.setFillsViewportHeight(true);
        jScrollPane1.setViewportView(jTable1);

        newitemButton.setText("new");
        newitemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newitemButtonActionPerformed(evt);
            }
        });

        updateButton.setText("update");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

        deleteItemButton.setText("delete");
        deleteItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteItemButtonActionPerformed(evt);
            }
        });

        errorLabel.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        errorLabel.setForeground(new java.awt.Color(0, 51, 204));

        infoList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        infoListScrollPane.setViewportView(infoList);

        jButton4.setText("book");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("author");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("pub");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("doc");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("ship");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        infoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        infoLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jButton10.setText("lib");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(newitemButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(deleteItemButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(updateButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(errorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(infoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(infoListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(infoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(infoListScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(errorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(newitemButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteItemButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(updateButton)))
                .addContainerGap(3, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void newitemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newitemButtonActionPerformed
        newItemListener();
    }//GEN-LAST:event_newitemButtonActionPerformed
    
    private void deleteItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteItemButtonActionPerformed
        deleteItemListener();
    }//GEN-LAST:event_deleteItemButtonActionPerformed
    
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        currentType = TableType.BOOK;
        prepare();
    }//GEN-LAST:event_jButton4ActionPerformed
    
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        currentType = TableType.AUTHOR;
        prepare();
    }//GEN-LAST:event_jButton5ActionPerformed
    
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        currentType = TableType.PUBLISHER;
        prepare();
    }//GEN-LAST:event_jButton6ActionPerformed
    
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        currentType = TableType.DOCUMENT;
        prepare();
    }//GEN-LAST:event_jButton7ActionPerformed
    
    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        currentType = TableType.AUTHORSHIP;
        prepare();
    }//GEN-LAST:event_jButton8ActionPerformed
    
    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            errorLabel.setText("select a row");
            return;
        } else {
            errorLabel.setText("");
        }
        int realRow = jTable1.convertRowIndexToModel(selectedRow);
        String[] get = currentModel.getData().get(realRow);
        
        if (currentType == TableType.BOOK) {
            
            new UpdateBookDialog(adminLibraryManager, this, get[0]).setVisible(true);
        } else if (currentType == TableType.AUTHOR) {
            new UpdateAuthorDialog(adminLibraryManager, this, Integer.parseInt(get[0])).setVisible(true);
        } else if (currentType == TableType.LIBRARY) {
            /*
             * JDialog d = new JDialog(); d.setLocationRelativeTo(null);
             * d.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
             * d.getContentPane().add(new UpdateLibraryPanel(get[0], this,
             * adminLibraryManager, d)); d.pack(); d.setVisible(true);
             */
            JDialog d = new JDialog();
            prepareAndShowDialog(d, new UpdateLibraryPanel(get[0], this, adminLibraryManager, d));
            
        }
        
    }//GEN-LAST:event_updateButtonActionPerformed
    
    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        currentType = TableType.LIBRARY;
        prepare();
    }//GEN-LAST:event_jButton10ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deleteItemButton;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JLabel infoLabel;
    private javax.swing.JList infoList;
    private javax.swing.JScrollPane infoListScrollPane;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton newitemButton;
    private javax.swing.JButton updateButton;
    // End of variables declaration//GEN-END:variables
}
