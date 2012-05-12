/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import DAO.BookDAO;
import beans.Author;
import beans.Book;
import beans.Document;
import beans.Publisher;
import dbcoursework.DBTableModel;
import java.awt.Component;
import java.awt.TrayIcon;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import managers.AdminLibraryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

/**
 *
 * @author BigBlackBug
 */
public class AdminPanel extends javax.swing.JPanel {

    enum TableType {

        AUTHOR, BOOK, DOCUMENT, PUBLISHER, AUTHORSHIP
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
    public void prepare() {
        addBookButton.setVisible(false);
        removeBookButton.setVisible(false);
        infoListScrollPane.setVisible(false);
        // System.g

        infoListModel.clear();
        if (currentType == TableType.AUTHOR) {
            addBookButton.setVisible(true);
            removeBookButton.setVisible(true);
            infoListScrollPane.setVisible(true);
            prepareAuthor();
        } else if (currentType == TableType.BOOK) {
            infoListScrollPane.setVisible(true);
            prepareBook();
        } else if (currentType == TableType.DOCUMENT) {
            prepareDocument();
        } else if (currentType == TableType.PUBLISHER) {
            preparePublisher();
        } else if (currentType == TableType.AUTHORSHIP) {
            prepareAuthorship();
        }
        jTable1.setModel(currentModel);
        revalidate();
        repaint();
        jTable1.repaint();
    }

    private void prepareDocument() {
        currentModel = new DBTableModel(adminLibraryManager.getDocumentDAO().getAllAsMap(), true, listener);
    }

    /*
     * public class BookDropDownEditor extends AbstractCellEditor implements
     * TableCellEditor {
     *
     * private BookDAO bookDAO; private ArrayList<String> books; private
     * JScrollPane component; private JList list;
     *
     * public BookDropDownEditor(BookDAO bookDAO) { this.bookDAO = bookDAO; list
     * = new JList(new DefaultListModel()); component = new JScrollPane(list);
     *
     * }
     *
     * //Implement the one CellEditor method that AbstractCellEditor doesn't.
     * @Override public Object getCellEditorValue() { if (!books.isEmpty()) {
     * return books.get(0); } else { return ""; } }
     *
     * //Implement the one method defined by TableCellEditor. @Override public
     * Component getTableCellEditorComponent(JTable table, Object value, boolean
     * isSelected, int row, int column) { DefaultListModel model =
     * (DefaultListModel) list.getModel(); model.clear();
     * System.out.println("log"); List<Author> authors =
     * bookDAO.getAuthorsOf((String) table.getModel().getValueAt(row, 0)); for
     * (Author a : authors) { model.addElement(a.getName()); }
     *
     * return component; } }
     */
    private void prepareBook() {
        currentModel = new DBTableModel(adminLibraryManager.getBookDAO().getAllAsMap(), true, listener);

        //  jTable1.getColumnModel().getColumn(1).setCellEditor(new JScrollTableEditor());
        // TableColumn sportColumn = jTable1.getColumnModel().getColumn(jTable1.getColumnCount()-1);
        //sportColumn.setCellEditor(new BookDropDownEditor(adminLibraryManager.getBookDAO()));
        // JComboBox<String> combo=new JComboBox<>(adminLibraryManager.getBookDAO().getAuthorsOf(TOOL_TIP_TEXT_KEY));
        //sportColumn.setCellEditor(new DefaultCellEditor(comboBox));
    }

    private void preparePublisher() {
        currentModel = new DBTableModel(adminLibraryManager.getPublisherDAO().getAllAsMap(), true, listener);
    }

    private void prepareAuthor() {
        //jTable1.setModel(new DBTableModel(adminLibraryManager.getAuthorDAO().getAllAsMap(), false));
        currentModel = new DBTableModel(adminLibraryManager.getAuthorDAO().getAllAsMap(), true, listener);
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
        tableComboBox.setModel(new DefaultComboBoxModel((TableType.values())));

        tableComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // String selectedItem = e.getItem().toString();
                    //System.out.println(selectedItem);
                    TableType type = (TableType) e.getItem();
                    currentType = type;
                    prepare();
                }
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
        }
        prepare();
    }

    public AdminPanel() {
        initComponents();
        initComponents2();
        //  prepare();


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
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        tableComboBox = new javax.swing.JComboBox();
        errorLabel = new javax.swing.JLabel();
        addBookButton = new javax.swing.JButton();
        removeBookButton = new javax.swing.JButton();
        infoListScrollPane = new javax.swing.JScrollPane();
        infoList = new javax.swing.JList();

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
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("new");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("update");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("delete");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        tableComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        errorLabel.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        errorLabel.setForeground(new java.awt.Color(0, 51, 204));

        addBookButton.setText("add book");
        addBookButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBookButtonActionPerformed(evt);
            }
        });

        removeBookButton.setText("remove book");
        removeBookButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeBookButtonActionPerformed(evt);
            }
        });

        infoList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        infoListScrollPane.setViewportView(infoList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(addBookButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(removeBookButton, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(errorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 210, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tableComboBox, 0, 393, Short.MAX_VALUE))))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 644, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(infoListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                    .addComponent(infoListScrollPane))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(tableComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(errorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(addBookButton)
                            .addComponent(removeBookButton))
                        .addGap(0, 65, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        newItemListener();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        deleteItemListener();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void addBookButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBookButtonActionPerformed
        int selectedRow = -1;
        if (jTable1.getSelectedRowCount() == 0) {
            errorLabel.setText("select a row");
            return;
        } else {
            selectedRow = jTable1.getSelectedRow();
        }

        List<Book> all = adminLibraryManager.getBookDAO().getAll(); //.toArray(values);
        String[] values = new String[all.size()];
        for (int i = 0; i < all.size(); i++) {
            values[i] = all.get(i).getIsbn();
        }
        String result = (String) JOptionPane.showInputDialog(jTable1, "message",
                "title", JOptionPane.INFORMATION_MESSAGE, null, values, values[0]);
        adminLibraryManager.getAuthorDAO().addBook(Integer.parseInt(currentModel.getLine(selectedRow)[0]), result);
        prepare();

    }//GEN-LAST:event_addBookButtonActionPerformed

    private void removeBookButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeBookButtonActionPerformed
        int selectedRow = -1;
        if (jTable1.getSelectedRowCount() == 0) {
            errorLabel.setText("select a row");
            return;
        } else {
            selectedRow = jTable1.getSelectedRow();
        }

        List<Book> all = adminLibraryManager.getBookDAO().getAll(); //.toArray(values);
        String[] values = new String[all.size()];
        for (int i = 0; i < all.size(); i++) {
            values[i] = all.get(i).getIsbn();
        }
        String result = (String) JOptionPane.showInputDialog(jTable1, "Select a book",
                "", JOptionPane.INFORMATION_MESSAGE, null, values, values[0]);
        adminLibraryManager.getAuthorDAO().removeBook(Integer.parseInt(currentModel.getLine(selectedRow)[0]), result);
        prepare();

    }//GEN-LAST:event_removeBookButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBookButton;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JList infoList;
    private javax.swing.JScrollPane infoListScrollPane;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton removeBookButton;
    private javax.swing.JComboBox tableComboBox;
    // End of variables declaration//GEN-END:variables
}
