package es.adr.modelo;

import java.util.List;

public class Pasta extends Producto {
    private final List<Ingrediente> ingredientes;

    public Pasta(int id, String nombre, double precio, List<Ingrediente> ingredientes) {
        super(id, nombre, precio);
        this.ingredientes = ingredientes;
    }

    public Pasta(String nombre, double precio, List<Ingrediente> ingredientes) {
        this(0, nombre, precio, ingredientes);
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    @Override
    public String toString() {
        return "Pasta [id=" + id + ", nombre=" + nombre + ", precio=" + precio + "ingredientes=" + ingredientes + "]";
    }
}
