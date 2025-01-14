package es.adr.modelo.dao.impl;

import es.adr.modelo.Cliente;
import es.adr.modelo.dao.Dao;
import es.adr.utilidades.ConfiguracionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ClienteDao implements Dao<Cliente> {

    private final Connection conn;

    public ClienteDao(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Optional<Cliente> find(int id) throws SQLException {
        Optional<Cliente> cliente = Optional.empty();

        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.FIND_CLIENTE)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                cliente = Optional.of(new Cliente(
                        rs.getInt("id"),
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getBoolean("administrador"))
                );
        }
        return cliente;
    }

    public Optional<Cliente> findByEmail(String email) throws SQLException {
        Optional<Cliente> cliente = Optional.empty();

        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.FIND_CLIENTE_BY_EMAIL)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                cliente = Optional.of(new Cliente(
                        rs.getInt("id"),
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getBoolean("administrador"))
                );
        }
        return cliente;
    }

    @Override
    public List<Cliente> findAll() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.FIND_ALL_CLIENTE)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next())
                clientes.add(new Cliente(
                        rs.getInt("id"),
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getBoolean("administrador"))
                );
        }
        clientes.sort(Comparator.comparingInt(Cliente::getId));
        return clientes;
    }

    @Override
    public void save(Cliente cliente) throws SQLException, IllegalArgumentException {
        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.SAVE_CLIENTE, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cliente.getDni());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getDireccion());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getEmail());
            ps.setString(6, cliente.getPassword());
            ps.setBoolean(7, cliente.isAdmin());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next())
                cliente.setId(rs.getInt(1));
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062)
                throw new IllegalArgumentException("Usuario ya registrado");
            throw e;
        }
    }

    @Override
    public void update(Cliente cliente) throws SQLException, IllegalArgumentException {
        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.UPDATE_CLIENTE)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDireccion());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getEmail());
            ps.setString(5, cliente.getPassword());
            ps.setBoolean(6, cliente.isAdmin());
            ps.setInt(7, cliente.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062)
                throw new IllegalArgumentException("Usuario ya registrado");
            throw e;
        }
    }

    @Override
    public void delete(Cliente cliente) throws SQLException, IllegalArgumentException {
        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.DELETE_CLIENTE)) {
            ps.setInt(1, cliente.getId());
            ps.executeUpdate();

            if (ps.getUpdateCount() == 0)
                throw new IllegalArgumentException("Cliente no encontrado");
        }
    }
}
