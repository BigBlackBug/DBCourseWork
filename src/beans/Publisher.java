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
public class Publisher implements Serializable {

    private static final long serialVersionUID = 1L;
    public Integer publisherId;
    public String name;
    public String address;
    public String phone;

    public Publisher(String name, String address, String phone) {

        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public Publisher() {
    }
 

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (publisherId != null ? publisherId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Publisher)) {
            return false;
        }
        Publisher other = (Publisher) object;
        if ((this.publisherId == null && other.publisherId != null) || (this.publisherId != null && !this.publisherId.equals(other.publisherId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beans.Publisher[ publisherId=" + publisherId + " ]";
    }
}
