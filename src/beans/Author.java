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
public class Author {

    private static final long serialVersionUID = 1L;
    public Integer authorId;
    public String name;
    public String phone;

    public Author() {
    }

    public Author(Integer authorId, String name) {
        this.authorId = authorId;
        this.name = name;
    }

    public Author(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (authorId != null ? authorId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Author)) {
            return false;
        }
        Author other = (Author) object;
        if ((this.authorId == null && other.authorId != null) || (this.authorId != null && !this.authorId.equals(other.authorId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beans.Author[ authorId=" + authorId + " ]";
    }

}
