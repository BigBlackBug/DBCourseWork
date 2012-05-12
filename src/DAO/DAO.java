/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.util.List;
import java.util.Map;
import org.springframework.dao.DuplicateKeyException;

/**
 *
 * @author BigBlackBug
 */
public interface DAO<T> {

    void insert(T item) throws DuplicateKeyException;

    void updateByID(T item);

    void delete(Object id);

    T findById(Object id);

    List<T> getAll();

    List<Map<String, Object>> getAllAsMap();

    List<Map<String, Object>> executeQuery(String query);
}
