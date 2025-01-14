package es.adr.modelo;

import java.util.ArrayList;
import java.util.Comparator;
import java.sql.Date;
import java.util.List;

public class Pedido {
    private int id;
    private Date fecha;
    private ESTADO_PEDIDO estado;
    private Cliente cliente;
    private List<LineaPedido> lineas;
    private METODO_PAGO metodoPago;

    public Pedido(int id, Date fecha, ESTADO_PEDIDO estado, Cliente cliente, List<LineaPedido> lineas, METODO_PAGO metodoPago) {
        this.id = id;
        this.fecha = fecha;
        this.estado = estado;
        this.cliente = cliente;
        this.lineas = lineas;
        this.metodoPago = metodoPago;
    }

    public Pedido(int id, Date fecha, ESTADO_PEDIDO estado, Cliente cliente, List<LineaPedido> lineas) {
        this(id, fecha, estado, cliente, lineas, null);
    }

    public Pedido(Cliente cliente) {
        this(0, new Date(System.currentTimeMillis()), ESTADO_PEDIDO.PENDIENTE, cliente, new ArrayList<>(), null);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public double getPrecioTotal() {
        return lineas.stream().mapToDouble(LineaPedido::getPrecio).sum();
    }

    public ESTADO_PEDIDO getEstado() {
        return estado;
    }

    public void setEstado(ESTADO_PEDIDO estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public METODO_PAGO getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(Pagable metodoPago) {
        this.metodoPago = switch (metodoPago) {
            case PagarEfectivo ignored-> METODO_PAGO.EFECTIVO;
            case PagarTarjeta ignored -> METODO_PAGO.TARJETA;
            default -> null;
        };
    }

    public List<LineaPedido> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaPedido> lineas) {
        this.lineas = lineas;
    }

    public void addLinea(LineaPedido linea) {
        int indice = lineas.indexOf(linea);
        
        if (indice == -1)
            lineas.add(new LineaPedido(linea));
        else
            lineas.get(indice).addCantidad(linea.getCantidad());
        
        lineas.sort(Comparator.comparingInt(LineaPedido::getId));
    }

    public void pagar(Pagable metodoPago) {
        metodoPago.pagar(getPrecioTotal());
        this.metodoPago = switch (metodoPago) {
            case PagarEfectivo ignored-> METODO_PAGO.EFECTIVO;
            case PagarTarjeta ignored -> METODO_PAGO.TARJETA;
            default -> null;
        };
        estado = ESTADO_PEDIDO.FINALIZADO;
    }

    @Override
    public String toString() {
        StringBuilder ticket = new StringBuilder(String.format("%s%nID PEDIDO: %d%nFECHA: %s%nESTADO: %s%n%nID    CANTIDAD   NOMBRE PRODUCTO        PRECIO%n", "-".repeat(60), id, fecha.toString(), estado));

        for (LineaPedido linea : lineas)
            ticket.append(linea);
        
        ticket.append(String.format("%n%-32s TOTAL  %.2feur", " ", getPrecioTotal()));

        return String.format("%s%n%s%n", ticket.toString(), "-".repeat(60));
    }
}
