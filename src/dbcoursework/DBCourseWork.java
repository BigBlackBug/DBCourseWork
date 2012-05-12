/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbcoursework;

import DAO.UserDAO;
import gui.AdminPanel;
import gui.LoginForm;
import gui.UserPanel;
import javax.swing.*;
import managers.AdminLibraryManager;
import managers.UserLibraryManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author BigBlackBug
 */
public class DBCourseWork {

    public static final ApplicationContext ctx;

    static {
        ctx = new ClassPathXmlApplicationContext("/springConfig.xml");

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            System.out.println("GG");

        }

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                // UserPanel panel = (UserPanel) ctx.getBean("UserPanel");
                UserPanel panel = new UserPanel((UserLibraryManager) ctx.getBean("UserLibraryManager"));
                AdminPanel adminPanel = new AdminPanel((AdminLibraryManager) ctx.getBean("AdminLibraryManager"));
                LoginForm form = new LoginForm((UserDAO) ctx.getBean("UserDAO"), panel, adminPanel);
                form.setVisible(true);
                form.setLocationRelativeTo(null);
            }
        });

    }
}
