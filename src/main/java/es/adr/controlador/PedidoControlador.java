package es.adr.controlador;

import es.adr.modelo.*;
import es.adr.modelo.dao.Dao;
import es.adr.modelo.dao.impl.PedidoDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class PedidoControlador {
    private static PedidoControlador instance;
    private static Dao<Pedido> pedidoDao;

    private PedidoControlador(Connection conn) {
        pedidoDao = new PedidoDao(conn);
    }

    public static PedidoControlador getInstance(Connection conn) {
        if (instance == null)
            instance = new PedidoControlador(conn);
        return instance;
    }

    public void agregarPedido(Pedido pedido) throws SQLException, IllegalArgumentException {
        pedidoDao.save(pedido);
    }

    public void agregarLineaPedido(LineaPedido linea) throws SQLException, IllegalArgumentException {
        List<Pedido> pedidosPendientes = ((PedidoDao) pedidoDao).findByEstadoPedido(ESTADO_PEDIDO.PENDIENTE);
        if (pedidosPendientes.isEmpty())
            throw new IllegalArgumentException("ERROR: No hay ningún pedido pendiente");

        Pedido pedidoActual = pedidosPendientes.getFirst();
        Optional<LineaPedido> lineaActual = ((PedidoDao) pedidoDao).findLineaPedido(pedidoActual.getId(), linea.getProducto().getId());
        linea.setPedidoId(pedidoActual.getId());

        if (lineaActual.isEmpty()) {
            ((PedidoDao) pedidoDao).saveLineaPedido(linea);
        } else {
            linea.addCantidad(lineaActual.get().getCantidad());
            ((PedidoDao) pedidoDao).updateLineaPedido(linea);
        }
    }

    public void finalizarPedido(Pagable pago, Cliente cliente) throws SQLException, IllegalArgumentException {
        List<Pedido> pedidos = ((PedidoDao)pedidoDao).findByEstadoPedido(ESTADO_PEDIDO.PENDIENTE);

        Optional<Pedido> pedidoActual = pedidos.stream().filter(p -> p.getCliente().getId() == cliente.getId()).findFirst();

        if (pedidoActual.isEmpty())
            throw new IllegalArgumentException("ERROR: No hay ningún pedido pendiente para este cliente");

        pago.pagar(pedidoActual.get().getPrecioTotal());
        pedidoActual.get().setEstado(ESTADO_PEDIDO.FINALIZADO);
        pedidoActual.get().setMetodoPago(pago);
        pedidoDao.update(pedidoActual.get());
    }

    public void cancelarPedido(Cliente cliente) throws SQLException, IllegalArgumentException {
        List<Pedido> pedidos = ((PedidoDao)pedidoDao).findByEstadoPedido(ESTADO_PEDIDO.PENDIENTE);

        Optional<Pedido> pedidoActual = pedidos.stream().filter(p -> p.getCliente().getId() == cliente.getId()).findFirst();

        if (pedidoActual.isEmpty())
            throw new IllegalArgumentException("ERROR: No hay ningún pedido pendiente para este cliente");

        pedidoActual.get().setEstado(ESTADO_PEDIDO.CANCELADO);
        pedidoDao.update(pedidoActual.get());
    }

    public void entregarPedido(int idPedido) throws SQLException, IllegalArgumentException {
        Optional<Pedido> pedido = pedidoDao.find(idPedido);
        if (pedido.isEmpty())
            throw new IllegalArgumentException("ERROR: No existe ningún pedido con ese ID");
        else if (pedido.get().getEstado() != ESTADO_PEDIDO.FINALIZADO)
            throw new IllegalArgumentException("ERROR: No se puede entregar un pedido que no esté finalizado");

        pedido.get().setEstado(ESTADO_PEDIDO.ENTREGADO);
        pedidoDao.update(pedido.get());
    }

    public Optional<Pedido> getPedidoActual() throws SQLException {
        List<Pedido> pedidos = pedidoDao.findAll();
        pedidos.sort(Comparator.comparingInt(Pedido::getId).reversed()); // Ordenamos por ID descendente para obtener el último pedido
        return pedidos.stream().findFirst();
    }
}
