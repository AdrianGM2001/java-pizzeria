package es.adr.modelo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Pedido {
    private final int id;
    private final Date fecha;
    private double precioTotal;
    private ESTADO_PEDIDO estado;
    private final Cliente cliente;
    private List<LineaPedido> lineas;

    public Pedido(int id, Cliente cliente) {
        this.id = id;
        fecha = new Date();
        precioTotal = 0;
        estado = ESTADO_PEDIDO.PENDIENTE;
        this.cliente = cliente;
        lineas = new ArrayList<>();
    }

    public Pedido(Pedido pedido) {
        this(pedido.id, pedido.cliente);
    }

    public int getId() {
        return id;
    }

    public Date getFecha() {
        return fecha;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
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
            
        precioTotal += linea.getPrecio();
        
        lineas.sort(Comparator.comparingInt(LineaPedido::getId));
    }

    @Override
    public String toString() {
        StringBuilder ticket = new StringBuilder(String.format("%s%nID PEDIDO: %d%nFECHA: %s%nESTADO: %s%n%nID    CANTIDAD   NOMBRE PRODUCTO        PRECIO%n", "-".repeat(60), id, fecha.toString(), estado));

        for (LineaPedido linea : lineas)
            ticket.append(linea);
        
        ticket.append(String.format("%n%-32s TOTAL  %.2feur", " ", precioTotal));

        return String.format("%s%n%s%n", ticket.toString(), "-".repeat(60));
    }
}
