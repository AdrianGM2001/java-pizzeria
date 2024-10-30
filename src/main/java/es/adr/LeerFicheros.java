package es.adr;

import es.adr.controlador.ClienteControlador;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class LeerFicheros {
    public static void main(String[] args) {
        ClienteControlador cc = ClienteControlador.getInstance();

        try {
            cc.importarAdministradores().forEach(System.out::println);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        try {
            cc.importarClientes().forEach(System.out::println);
        } catch (JAXBException e) {
            System.err.println(e.getMessage());
        }

        try {
            cc.importarIngredientes().forEach(System.out::println);
        } catch (IllegalStateException | IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
