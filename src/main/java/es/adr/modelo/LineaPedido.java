package es.adr.modelo;

public class LineaPedido {
    private int id;
    private int pedidoId;
    private int cantidad;
    private Producto producto;

    public LineaPedido(int id, int pedidoId, int cantidad, Producto producto) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.cantidad = cantidad;
        this.producto = producto;
    }

    public LineaPedido(int cantidad, Producto producto) {
        this(0, 0, cantidad, producto);
    }

    public LineaPedido(LineaPedido lp) {
        this(lp.id, lp.pedidoId, lp.cantidad, lp.producto);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void addCantidad(int cantidad) {
        this.cantidad += cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public double getPrecio() {
        return cantidad * producto.getPrecio();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((producto == null) ? 0 : producto.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LineaPedido other = (LineaPedido) obj;
        if (producto == null) {
            if (other.producto != null)
                return false;
        } else if (!producto.equals(other.producto))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("%-3d   %-2d         %-20s   %.2feur%n", id, cantidad, producto, getPrecio());
    }
}
