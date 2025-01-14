package es.adr.modelo.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    Optional<T> find(int id) throws SQLException;

    List<T> findAll() throws SQLException;

    void save(T t) throws SQLException, IllegalArgumentException;

    void update(T t) throws SQLException, IllegalArgumentException;

    void delete(T t) throws SQLException, IllegalArgumentException;
}
