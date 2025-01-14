package es.adr.modelo;

import java.util.List;

public class Pizza extends Producto {
    private final SIZE size;
    private final List<Ingrediente> ingredientes;

    public Pizza(int id, String nombre, double precio, SIZE size, List<Ingrediente> ingredientes) {
        super(id, nombre, precio);
        this.size = size;
        this.ingredientes = ingredientes;
    }

    public Pizza(String nombre, double precio, SIZE size, List<Ingrediente> ingredientes) {
        this(0, nombre, precio, size, ingredientes);
    }

    public SIZE getSize() {
        return size;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public String toStringExamen() {
        return id + ", " + nombre + ", " + precio + ", " + size;
    }

    @Override
    public String toString() {
        return "Pizza [id=" + id + ", nombre=" + nombre + ", precio=" + precio + ", size=" + size + ", ingredientes=" + ingredientes + "]";
    }
}
