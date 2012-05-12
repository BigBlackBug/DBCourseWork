/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author BigBlackBug
 */
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
  
    private String isbn;
  
    private Integer publisherId;

    public Book() {
    }

    public Book(String isbn) {
        this.isbn = isbn;
    }

    public Book(String isbn,Integer publisherId){
        this(isbn);
        this.publisherId=publisherId;
    }
    public void setPublisherId(java.lang.Integer publisherId) {
        this.publisherId = publisherId;
    }
    
    

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getPublisherId() {
        return publisherId;
    }

    public void Integer(Integer publisherId) {
        this.publisherId = publisherId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (isbn != null ? isbn.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Book)) {
            return false;
        }
        Book other = (Book) object;
        if ((this.isbn == null && other.isbn != null) || (this.isbn != null && !this.isbn.equals(other.isbn))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beans.Book[ isbn=" + isbn + " ]";
    }
    
}
