/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

/**
 *
 * @author BigBlackBug
 */
public class User {

    public String username;
    public String password;

    public User() {
    }

    
    public User(String username,String password) {
        this.username = username;
        this.password=password;
    }
}
