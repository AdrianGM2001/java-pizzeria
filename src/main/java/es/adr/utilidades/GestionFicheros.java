package es.adr.utilidades;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import es.adr.modelo.Cliente;
import es.adr.modelo.Ingrediente;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class GestionFicheros {
    // RUTAS
    private static final String RUTA_ADMINISTRADORES = "admin.txt";
    private static final String RUTA_CLIENTES = "clientes.xml";
    private static final String RUTA_INGREDIENTES = "ingredientes.csv";

    // Sin librería

    // Sin librería (el fichero no tiene encabezado)
    public static List<Cliente> importarAdministradoresSinLibreria() throws IOException {
        try (Stream<String> lineas = Files.lines(Path.of(RUTA_ADMINISTRADORES))) {
            return lineas.map(GestionFicheros::stringToCliente).toList();
        } catch (IOException e) {
            throw new IOException(e.getMessage() + "\nHa habido un problema con el archivo admin.txt");
        }
    }

    private static Cliente stringToCliente(String linea) throws IllegalArgumentException {
        List<String> elementos = Stream.of(linea.split("[,|;]")).map(String::trim).toList();

        // Validación básica
        if (elementos.size() != 7)
            throw new IllegalArgumentException("\nHa habido un problema con el archivo admin.txt\nNúmero de campos incorrecto o uso de \",\", \"|\" o \";\" en los campos -> " + linea);

        if (elementos.stream().anyMatch(String::isEmpty))
            throw new IllegalArgumentException("\nHa habido un problema con el archivo admin.txt\nCampo/s vacío/s -> " + linea);

        try {
            // id de 0 a 2147483647
            if (Integer.parseInt(elementos.getFirst()) < 0) {
                throw new IllegalArgumentException("\nHa habido un problema con el archivo admin.txt\nID incorrecto (valor mínimo 0) -> " + linea);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e.getMessage() + "\nHa habido un problema con el archivo admin.txt\nID incorrecto o demasiado grande (valor máximo " + Integer.MAX_VALUE +") -> " + linea);
        }

        return new Cliente(Integer.parseInt(elementos.get(0)), elementos.get(1), elementos.get(2), elementos.get(3), elementos.get(4), elementos.get(5), elementos.get(6), true);
    }

    // Sin librería (el fichero no tiene encabezado, tampoco impide exportar con separadores admitidos en la importación, pero esto no había que hacerlo) 
    public static boolean exportarAdministradoresSinLibreria(List<Cliente> clientes, String separador) throws IOException {
        try {
            Files.write(Path.of(RUTA_ADMINISTRADORES), clientes.stream().map(cliente -> GestionFicheros.clienteToString(cliente, separador)).toList());
            return true;
        } catch (IOException e) {
            throw new IOException(e.getMessage() + "\nHa habido un problema al escribir el archivo admin.txt");
        }
    }

    private static String clienteToString(Cliente cliente, String separador) {
        return String.format("%d%s%s%s%s%s%s%s%s%s%s%s%s", cliente.getId(), separador, cliente.getDni(), separador, cliente.getNombre(), separador, cliente.getDireccion(), separador, cliente.getTelefono(), separador, cliente.getEmail(), separador, cliente.getPassword());
    }

    // Librería JAXB

    public static List<Cliente> importarClientesXML() throws JAXBException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ClienteWrapper.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            ClienteWrapper persona = (ClienteWrapper) unmarshaller.unmarshal(new File(RUTA_CLIENTES));
            return persona.getClientes();
        } catch (JAXBException e) {
            throw new JAXBException(e.getMessage() + "\nHay uno o más campos con datos no válidos o vacíos en el archivo clientes.xml");
        }
    }

    public static boolean exportarClientesXML(List<Cliente> clientes) throws JAXBException {
        try {
            ClienteWrapper persona = new ClienteWrapper(clientes);
            JAXBContext jaxbContext = JAXBContext.newInstance(ClienteWrapper.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(persona, new File(RUTA_CLIENTES));
            return true;
        } catch (JAXBException e) {
            throw new JAXBException(e.getMessage() + "\nHay uno o más campos con datos no válidos en la lista de clientes");
        }
    }

    // Librería OpenCSV

    public static List<Ingrediente> importarIngredientesCSV() throws IllegalStateException, IOException {
        List<Ingrediente> ingredientes;
        try (FileReader fileReader = new FileReader(RUTA_INGREDIENTES)) {
            CsvToBean<Ingrediente> csvToBean = new CsvToBeanBuilder<Ingrediente>(fileReader).withType(Ingrediente.class).withSeparator(';').build();
            ingredientes = csvToBean.parse();
            return ingredientes;
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(e.getMessage() + "\nNo se ha encontrado el archivo ingredientes.csv");
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage() + "\nHay uno o más campos con datos no válidos o vacíos en el archivo ingredientes.csv");
        } catch (IOException e) {
            throw new IOException(e.getMessage() + "\nHa habido un problema al leer el archivo ingredientes.csv");
        }
    }

    public static boolean exportarIngredientesCSV(List<Ingrediente> ingredientes) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        try (PrintWriter pw = new PrintWriter(RUTA_INGREDIENTES)) {
            StatefulBeanToCsv<Ingrediente> beanToCsv = new StatefulBeanToCsvBuilder<Ingrediente>(pw).withSeparator(';').build();
            beanToCsv.write(ingredientes);
            return true;
        } catch (IOException e) {
            throw new IOException(e.getMessage() + "\nHa habido un problema al escribir el archivo ingredientes.csv");
        } catch (CsvDataTypeMismatchException  e) {
            throw new CsvDataTypeMismatchException(e.getMessage() + "\nHay uno o más campos con datos no válidos en la lista de ingredientes");
        } catch (CsvRequiredFieldEmptyException e) {
            throw new CsvRequiredFieldEmptyException(e.getMessage() + "\nHay uno o más campos obligatorios vacíos en la lista de ingredientes");
        }
    }
}
