package es.adr.controlador;

import es.adr.modelo.ESTADO_PEDIDO;
import es.adr.modelo.LineaPedido;
import es.adr.modelo.Pagable;
import es.adr.modelo.Pedido;

import java.util.ArrayList;
import java.util.List;

public class PedidoControlador {
    private static PedidoControlador pedidoControlador;

    private List<Pedido> pedidos;
    private Pedido pedidoActual;

    private PedidoControlador() {
        pedidos = new ArrayList<>();
        pedidoActual = null;
    }

    public static PedidoControlador getInstance() {
        if (pedidoControlador != null)
            return pedidoControlador;
        
        return new PedidoControlador();
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public Pedido getPedidoActual() {
        return pedidoActual;
    }

    public void setPedidoActual(Pedido pedidoActual) {
        this.pedidoActual = pedidoActual;
    }

    public boolean anyadirLinea(LineaPedido lp) {
        pedidoActual.addLinea(lp);
        return true;
    }

    public boolean finalizarPedido(Pagable pago) {
        if (pedidoActual.getLineas().isEmpty())
            throw new IllegalStateException("ERROR: No hay ninguna línea en el pedido");

        pago.pagar(pedidoActual.getPrecioTotal());
        pedidoActual.setEstado(ESTADO_PEDIDO.FINALIZADO);

        return true;
    }

    public Pedido cancelarPedido() {
        switch (pedidoActual.getEstado()) {
            case PENDIENTE, FINALIZADO:
                System.out.println("CANCELAR_PEDIDO: Pedido cancelado");
                pedidoActual.setEstado(ESTADO_PEDIDO.CANCELADO);
                return pedidoActual;
            case ENTREGADO:
                throw new IllegalStateException("ERROR: No se puede cancelar un pedido que ya haya sido entregado");
            case CANCELADO:
                throw new IllegalStateException("ERROR: El pedido ya ha sido cancelado");
            default:
                throw new IllegalStateException("ERROR: No hay ningún pedido actualmente");
        }
    }

    public Pedido entregarPedido() {
        if (pedidoActual.getEstado() != ESTADO_PEDIDO.FINALIZADO)
            throw new IllegalStateException("ERROR: No hay ningún pedido finalizado");
        
        pedidoActual.setEstado(ESTADO_PEDIDO.ENTREGADO);
        System.out.println("ENTREGAR_PEDIDO: Pedido entregado");
        return pedidoActual;
    }
}
