package es.adr;

import es.adr.controlador.ClienteControlador;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class LeerFicheros {
    public static void main(String[] args) {
        ClienteControlador cc = ClienteControlador.getInstance();

        try {
            cc.importarAdministradores().forEach(System.out::println);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }

        try {
            cc.importarClientes().forEach(System.out::println);
        } catch (JAXBException e) {
            System.err.println("Ha habido un problema con el archivo clientes.xml");
        }

        try {
            cc.importarIngredientes().forEach(System.out::println);
        } catch (IOException e) {
            System.err.println("Ha habido un problema con el archivo ingredientes.csv");
        }
    }
}
