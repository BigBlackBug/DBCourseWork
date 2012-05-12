/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbcoursework;

import gui.LoginForm;
import gui.UserPanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 *
 * @author BigBlackBug
 */
public class DBCourseWork {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final ApplicationContext ctx = new ClassPathXmlApplicationContext("/springConfig.xml");


        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                UserPanel panel = (UserPanel) ctx.getBean("UserPanel");
                LoginForm form = (LoginForm) ctx.getBean("LoginForm");
                form.setVisible(true);
                form.setLocationRelativeTo(null);
                //panel.setDocument(2);
                JFrame jFrame = new JFrame();
                jFrame.add(panel);

                jFrame.setSize(500, 700);
                //jFrame.setVisible(true);
                //  panel.setVisible(true);
            }
        });

    }
}
