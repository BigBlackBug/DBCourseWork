/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import DAO.UserDAO;
import beans.Document;
import beans.User;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Objects;
import javax.swing.JFrame;
import org.springframework.dao.DuplicateKeyException;

/**
 *
 * @author BigBlackBug
 */
public class LoginForm extends javax.swing.JFrame {

    private UserDAO userDAO;
    private UserPanel userPanel;
    private AdminPanel adminPanel;
    private JFrame jFrame;

    /**
     * Creates new form LoginForm
     */
    public LoginForm(UserDAO userDAO, UserPanel userPanel, AdminPanel adminPanel) {
        this.userDAO = userDAO;
        this.userPanel = userPanel;
        this.adminPanel = adminPanel;
        initComponents();
        // userForm = new JFrame("user!");
        // userForm.setContentPane(userPanel);
       /*
         * userForm.add(userPanel); userForm.add(userPanel);
         * userForm.add(userPanel);userForm.add(userPanel);
         */

        //  userForm.pack();
        jPasswordField1.setEchoChar('$');
        //Objects.hashCode(this);
    }

    private static final class MyWindowListener implements WindowListener {

        JFrame loginForm;

        public MyWindowListener(JFrame loginForm) {
            this.loginForm = loginForm;
        }

        @Override
        public void windowOpened(WindowEvent e) {
            loginForm.setVisible(false);

        }

        @Override
        public void windowClosing(WindowEvent e) {
            loginForm.setVisible(true);
            System.out.println("qwe");
        }

        @Override
        public void windowClosed(WindowEvent e) {
        }

        @Override
        public void windowIconified(WindowEvent e) {
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
        }

        @Override
        public void windowActivated(WindowEvent e) {
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        loginButton = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
        registerButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        adminModeCheckBox = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        loginButton.setText("login");
        loginButton.setActionCommand("Login");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        registerButton.setText("register");
        registerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerButtonActionPerformed(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(0, 51, 255));

        adminModeCheckBox.setSelected(true);
        adminModeCheckBox.setText("Admin Mode");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(loginButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(registerButton, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(adminModeCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(adminModeCheckBox, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(loginButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jPasswordField1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(registerButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        String name = jTextField1.getText();

        String password = new String(jPasswordField1.getPassword());
        int hashedPassword = Objects.hash(password);
        //Document foundUser = userDAO.findById(name);
        User foundUser=userDAO.findById(name);
        if (foundUser != null) {
            if (foundUser.password.equals(String.valueOf(hashedPassword))) {
                if (adminModeCheckBox.isSelected()) {
                    jFrame = new JFrame();
                    jFrame.add(adminPanel);
                    jFrame.pack();
                    jFrame.addWindowListener(new MyWindowListener(this));
                    jFrame.setVisible(true);
                } else {
                    //userPanel.setDocument(foundUser.documentId);
                    userPanel.reload(false);
                    jFrame = new JFrame();
                    jFrame.add(userPanel);
                    jFrame.pack();
                    jFrame.addWindowListener(new MyWindowListener(this));
                    jFrame.setVisible(true);
                }
            } else {
                jLabel1.setText("invalid password");
            }
        } else {
            jLabel1.setText("there's no such user");
        }
        //System.out.println(pass);
    }//GEN-LAST:event_loginButtonActionPerformed

    private void registerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerButtonActionPerformed
        String name = jTextField1.getText();
        if (name.isEmpty()) {
            jLabel1.setText("the name is empty!");
            return;

        }

        String password = new String(jPasswordField1.getPassword());
        if (password.isEmpty()) {
            jLabel1.setText("the password is empty!");
            return;
        }

        int hashedPassword = Objects.hash(password);
        try {
            userDAO.insert(new User(name, String.valueOf(hashedPassword)));
        } catch (DuplicateKeyException dex) {
            jLabel1.setText("the user with such name already exists!");
            return;
        }
        jLabel1.setText("success!");
    }//GEN-LAST:event_registerButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox adminModeCheckBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton loginButton;
    private javax.swing.JButton registerButton;
    // End of variables declaration//GEN-END:variables
}