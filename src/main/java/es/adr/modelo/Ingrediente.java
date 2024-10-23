package es.adr.modelo;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;

import java.util.List;


public class Ingrediente {
    @CsvBindByName(column = "id")
    private int id;
    @CsvBindByName(column = "nombre")
    private String nombre;
    @CsvBindAndSplitByName(column = "alergenos", writeDelimiter = ",", elementType = String.class)
    private List<String> alergenos;

    public Ingrediente() {}

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

    @Override
    public String toString() {
        return "Ingrediente [alergenos=" + alergenos + ", id=" + id + ", nombre=" + nombre + "]";
    }
}
