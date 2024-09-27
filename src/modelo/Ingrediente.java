package modelo;

import java.util.List;

public class Ingrediente {
    private final int id;
    private final String nombre;
    private final List<String> alergenos;

    public Ingrediente(int id, String nombre, List<String> alergenos) {
        this.id = id;
        this.nombre = nombre;
        this.alergenos = alergenos;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public List<String> getAlergenos() {
        return alergenos;
    }
}
