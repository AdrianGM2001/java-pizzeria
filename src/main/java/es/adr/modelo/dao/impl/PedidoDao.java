package es.adr.modelo.dao.impl;

import es.adr.modelo.*;
import es.adr.modelo.dao.Dao;
import es.adr.utilidades.ConfiguracionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PedidoDao implements Dao<Pedido> {
    private final Connection conn;
    private final Dao<Cliente> clienteDao;
    private final Dao<Producto> productoDao;

    public PedidoDao(Connection conn) {
        this.conn = conn;
        this.clienteDao = new ClienteDao(conn);
        this.productoDao = ProductoDao.getInstance(conn);
    }

    @Override
    public Optional<Pedido> find(int id) throws SQLException {
        Optional<Pedido> pedido;
        List<LineaPedido> lineas = new ArrayList<>();
        Cliente cliente;

        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.FIND_PEDIDO);
             PreparedStatement ps2 = conn.prepareStatement(ConfiguracionBD.FIND_PEDIDO_PRODUCTO_BY_PEDIDO)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (!rs.next())
                return Optional.empty();

            cliente = clienteDao.find(rs.getInt("id")).orElse(null); // Este cliente siempre existe

            ps2.setInt(1, id);

            ResultSet rsLineas = ps2.executeQuery();

            while (rsLineas.next()) {
                Producto producto = productoDao.find(rsLineas.getInt("producto_id")).orElse(null); // Este producto siempre existe
                lineas.add(new LineaPedido(rsLineas.getInt("id"), id, rsLineas.getInt("cantidad"), producto));
            }

            String metodoPago = rs.getString("metodo_pago");
            METODO_PAGO pago = null;

            if (metodoPago != null) {
                pago = switch (metodoPago) {
                    case "tarjeta" -> METODO_PAGO.TARJETA;
                    case "efectivo" -> METODO_PAGO.EFECTIVO;
                    default -> pago;
                };
            }

            pedido = Optional.of(new Pedido(id, rs.getDate("fecha"), ESTADO_PEDIDO.valueOf(rs.getString("estado").toUpperCase()), cliente, lineas, pago));
        }
        return pedido;
    }

    @Override
    public List<Pedido> findAll() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.FIND_PEDIDO_ALL_IDS)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next())
                find(rs.getInt("id")).ifPresent(pedidos::add);
        }

        return pedidos;
    }

    public List<Pedido> findByEstadoPedido(ESTADO_PEDIDO estadoPedido) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.FIND_PEDIDO_IDS_BY_ESTADO_PEDIDO)) {
            ps.setString(1, estadoPedido.name());
            ResultSet rs = ps.executeQuery();

            while (rs.next())
                find(rs.getInt("id")).ifPresent(pedidos::add);
        }
        return pedidos;
    }

    public List<Pedido> findByClienteId(int clienteId) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.FIND_PEDIDO_IDS_BY_CLIENTE)) {
            ps.setInt(1, clienteId);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
                find(rs.getInt("id")).ifPresent(pedidos::add);
        }
        return pedidos;
    }

    public Optional<LineaPedido> findLineaPedido(int pedidoId, int productoId) throws SQLException {
        Optional<LineaPedido> linea = Optional.empty();

        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.FIND_LINEA_PEDIDO)) {
            ps.setInt(1, pedidoId);
            ps.setInt(2, productoId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Producto producto = productoDao.find(rs.getInt("producto_id")).orElse(null); // Este producto siempre existe
                linea = Optional.of(new LineaPedido(rs.getInt("id"), pedidoId, rs.getInt("cantidad"), producto));
            }
        }
        return linea;
    }

    public List<LineaPedido> findLineasPedidoByPedidoId(int pedidoId) throws SQLException {
        List<LineaPedido> lineas = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.FIND_PEDIDO_PRODUCTO_BY_PEDIDO)) {
            ps.setInt(1, pedidoId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int idLineaPedido = rs.getInt("id");
                int cantidad = rs.getInt("cantidad");
                productoDao.find(rs.getInt("producto_id")).ifPresent(producto ->
                        lineas.add(new LineaPedido(idLineaPedido, pedidoId, cantidad, producto)));
            }
        }
        return lineas;
    }

    @Override
    public void save(Pedido pedido) throws SQLException, IllegalArgumentException {
        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.SAVE_PEDIDO, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, pedido.getFecha());
            ps.setString(2, pedido.getEstado().name());
            ps.setInt(3, pedido.getCliente().getId());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next())
                pedido.setId(rs.getInt(1));

            for (LineaPedido linea : pedido.getLineas())
                saveLineaPedido(linea);
        } catch (SQLException e) {
            if (e.getErrorCode() == 1452)
                throw new IllegalArgumentException("El cliente o producto no existe");
            else if (e.getErrorCode() == 1062)
                throw new IllegalArgumentException("El pedido ya existe");
            throw e;
        }
    }

    public void saveLineaPedido(LineaPedido lineaPedido) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.SAVE_LINEA_PEDIDO, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, lineaPedido.getPedidoId());
            ps.setInt(2, lineaPedido.getProducto().getId());
            ps.setInt(3, lineaPedido.getCantidad());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next())
                lineaPedido.setId(rs.getInt(1));
        }
    }

    @Override
    public void update(Pedido pedido) throws SQLException, IllegalArgumentException {
        if (find(pedido.getId()).isEmpty())
            throw new IllegalArgumentException("Pedido no encontrado");

        String pago = pedido.getMetodoPago() == null ? null : pedido.getMetodoPago().name().toLowerCase();

        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.UPDATE_PEDIDO)) {
            ps.setDate(1, pedido.getFecha());
            ps.setString(2, pedido.getEstado().name());
            ps.setString(3, pago);
            ps.setInt(4, pedido.getId());
            ps.executeUpdate();
        }
    }

    public void updateLineaPedido(LineaPedido lineaPedido) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.UPDATE_LINEA_PEDIDO)) {
            ps.setInt(1, lineaPedido.getCantidad());
            ps.setInt(2, lineaPedido.getPedidoId());
            ps.setInt(3, lineaPedido.getProducto().getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Pedido pedido) throws SQLException, IllegalArgumentException {
        if (find(pedido.getId()).isEmpty())
            throw new IllegalArgumentException("Pedido no encontrado");

        if (pedido.getEstado() != ESTADO_PEDIDO.PENDIENTE)
            throw new IllegalArgumentException("Pedido no pendiente");

        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.DELETE_PEDIDO)) {
            ps.setInt(1, pedido.getId());
            ps.executeUpdate();
        }
    }
}
