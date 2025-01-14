package es.adr.modelo;

import java.util.List;


public class Ingrediente {
    private int id;
    private String nombre;
    private List<String> alergenos;

    public Ingrediente(int id, String nombre, List<String> alergenos) {
        this.id = id;
        this.nombre = nombre;
        this.alergenos = alergenos;
    }

    public Ingrediente(String nombre, List<String> alergenos) {
        this(0, nombre, alergenos);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public List<String> getAlergenos() {
        return alergenos;
    }

    @Override
    public String toString() {
        return "Ingrediente [alergenos=" + alergenos + ", id=" + id + ", nombre=" + nombre + "]";
    }
}
