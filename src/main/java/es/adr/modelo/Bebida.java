package es.adr.modelo;

public class Bebida extends Producto {
    private final SIZE size;


    public Bebida(int id, String nombre, double precio, SIZE size) {
        super(id, nombre, precio);
        this.size = size;
    }

    public Bebida(String nombre, double precio, SIZE size) {
        this(0, nombre, precio, size);
    }

    public SIZE getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "Bebida [id=" + id + ", nombre=" + nombre + ", precio=" + precio + ", size=" + size + "]";
    }
}
