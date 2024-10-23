package es.adr;

import es.adr.controlador.ClienteControlador;
import es.adr.modelo.Cliente;
import es.adr.modelo.Ingrediente;

import java.util.List;

public class EscribirFicheros {
    public static void main(String[] args) {
        List<Cliente> clientes = List.of(
            new Cliente(1, "57675445X", "Luis", "Calle Falsa 123", "564635466", "rtyger54@ex.es", "ertyhertyg"),
            new Cliente(2, "43565767Y", "Alberto", "Calle Falsa 123", "2345546543", "sfdthg456@ex.es", "ertyaqwerq354"),
            new Cliente(3, "34568769Z", "Paco", "Calle Falsa 123", "456745543", "gfyhfrsdthge@ex.es", "ertyer346")
        );

        List<Cliente> administradores = List.of(
            new Cliente(1, "12345678A", "Pepe", "Calle Falsa 123", "6567365877", "dfsf32@ex.es", "b36454563ccwer4vcwert23v453624", true),
            new Cliente(2, "14563378V", "Juan", "Calle Falsa 123", "3245345466", "345123sqads@ex.es", "23141234321werfwefrwe3241", true),
            new Cliente(3, "56765746S", "Carlos", "Calle Falsa 123", "4564543345", "sdfasd324@ex.es", "qwerwqr1324r3dx1123", true)
        );

        List<Ingrediente> ingredientes = List.of(
            new Ingrediente(1, "Tomate", List.of("gluten", "lactosa")),
            new Ingrediente(2, "Lechuga", List.of("gluten", "lactosa")),
            new Ingrediente(3, "Cebolla", List.of("gluten", "lactosa"))
        );

        ClienteControlador cc = ClienteControlador.getInstance();

        try {
            cc.exportarAdministradores(administradores);
            System.out.println("Se ha creado el archivo admin.txt");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        try {
            cc.exportarClientes(clientes);
            System.out.println("Se ha creado el archivo clientes.xml");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        try {
            cc.exportarIngredientes(ingredientes);
            System.out.println("Se ha creado el archivo ingredientes.csv");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
