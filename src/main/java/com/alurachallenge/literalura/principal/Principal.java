package com.alurachallenge.literalura.principal;

import com.alurachallenge.literalura.dto.DatosAutor;
import com.alurachallenge.literalura.dto.DatosLibro;
import com.alurachallenge.literalura.dto.DatosRespuestaAPI;
import com.alurachallenge.literalura.model.Autor;
import com.alurachallenge.literalura.model.Libro;
import com.alurachallenge.literalura.repository.AutorRepository;
import com.alurachallenge.literalura.repository.LibroRepository;
import com.alurachallenge.literalura.service.ConsumoAPI;
import com.alurachallenge.literalura.service.ConvierteDatos;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private final String URL_BASE = "https://gutendex.com/books/";

    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void exhibirMenu() {
        int opcion = -1;
        while (opcion != 0) {
            String menu = """
                    ----------------------------------------------------
                    Bienvenido a LiterAlura - Tu catálogo de libros
                    ----------------------------------------------------
                    1. Buscar libro por título
                    2. Listar libros registrados
                    3. Listar autores registrados
                    4. Listar autores vivos en un determinado año
                    5. Mostrar cantidad de libros por idioma
                    0. Salir
                    ----------------------------------------------------
                    Ingrese una opción:
                    """;
            System.out.println(menu);
            try {
                opcion = teclado.nextInt();
                teclado.nextLine(); // Consumir el salto de línea

                switch (opcion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarLibrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivosEnAnio();
                        break;
                    case 5:
                        mostrarCantidadLibrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Saliendo de LiterAlura. ¡Hasta pronto!");
                        break;
                    default:
                        System.out.println("Opción inválida. Por favor, intente de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
                teclado.nextLine(); // Limpiar el buffer del scanner
                opcion = -1; // Para que el bucle continúe
            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Ingrese el título del libro que desea buscar:");
        String tituloBusqueda = teclado.nextLine();
        String json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloBusqueda.replace(" ", "%20"));

        DatosRespuestaAPI datosRespuesta = conversor.obtenerDatos(json, DatosRespuestaAPI.class);

        if (datosRespuesta != null && !datosRespuesta.resultados().isEmpty()) {
            DatosLibro datosPrimerLibro = datosRespuesta.resultados().get(0);

            // Verificar si el libro ya existe en la base de datos
            Optional<Libro> libroExistente = libroRepository.findByTituloContainsIgnoreCase(datosPrimerLibro.titulo());
            if (libroExistente.isPresent()) {
                System.out.println("\nEste libro ya está registrado en la base de datos:");
                System.out.println(libroExistente.get());
                return;
            }

            // Obtener el autor (primer autor si hay varios)
            Autor autor = null;
            if (datosPrimerLibro.autores() != null && !datosPrimerLibro.autores().isEmpty()) {
                DatosAutor datosPrimerAutor = datosPrimerLibro.autores().get(0);

                // Buscar el autor en la base de datos para evitar duplicados
                Optional<Autor> autorExistente = autorRepository.findByNombreContainsIgnoreCase(datosPrimerAutor.nombre());
                if (autorExistente.isPresent()) {
                    autor = autorExistente.get();
                } else {
                    // Si el autor no existe, crearlo y guardarlo
                    autor = new Autor(
                            datosPrimerAutor.nombre(),
                            datosPrimerAutor.anioNacimiento(),
                            datosPrimerAutor.anioFallecimiento()
                    );
                    autorRepository.save(autor);
                    System.out.println("Autor nuevo guardado: " + autor.getNombre());
                }
            }

            // Crear y guardar el libro
            Libro libro = new Libro(
                    datosPrimerLibro.titulo(),
                    datosPrimerLibro.idiomas().get(0), // Tomar solo el primer idioma
                    datosPrimerLibro.numeroDescargas(),
                    autor
            );
            libroRepository.save(libro);
            System.out.println("\nLibro encontrado y guardado exitosamente:");
            System.out.println(libro);

        } else {
            System.out.println("\nNo se encontró ningún libro con ese título.");
        }
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAllByOrderByTituloAsc();
        if (libros.isEmpty()) {
            System.out.println("\nNo hay libros registrados aún.");
        } else {
            System.out.println("\n--- LIBROS REGISTRADOS ---");
            libros.forEach(System.out::println);
        }
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAllByOrderByNombreAsc();
        if (autores.isEmpty()) {
            System.out.println("\nNo hay autores registrados aún.");
        } else {
            System.out.println("\n--- AUTORES REGISTRADOS ---");
            autores.forEach(a -> {
                // Esta es la línea problemática, asegúrate que se vea exactamente así:
                System.out.println("Autor: " + a.getNombre() +
                        " (Nacimiento: " + (a.getAnioNacimiento() != null ? a.getAnioNacimiento() : "Desconocido") +
                        ", Fallecimiento: " + (a.getAnioFallecimiento() != null ? a.getAnioFallecimiento() : "N/A") + ")");
            });
        }
    }

    private void listarAutoresVivosEnAnio() {
        System.out.println("Ingrese el año para buscar autores vivos:");
        try {
            int anio = teclado.nextInt();
            teclado.nextLine(); // Consumir el salto de línea

            List<Autor> autoresVivos = autorRepository.findByAnioNacimientoLessThanEqualAndAnioFallecimientoGreaterThanEqualOrAnioFallecimientoIsNull(anio, anio);

            if (autoresVivos.isEmpty()) {
                System.out.println("\nNo se encontraron autores vivos en el año " + anio + " en la base de datos.");
            } else {
                System.out.println("\n--- AUTORES VIVOS EN EL AÑO " + anio + " ---");
                // Esta línea es CRÍTICA, debe ser EXACTAMENTE así:
                autoresVivos.forEach(a -> {
                    System.out.println("Autor: " + a.getNombre() +
                            " (Nacimiento: " + (a.getAnioNacimiento() != null ? a.getAnioNacimiento() : "¿?") +
                            " - Fallecimiento: " + (a.getAnioFallecimiento() != null ? a.getAnioFallecimiento() : "¿?") + ")");
                });

            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, ingrese un año válido (número entero).");
            teclado.nextLine(); // Limpiar el buffer del scanner
        }
    }

    private void mostrarCantidadLibrosPorIdioma() {
        System.out.println("Ingrese el idioma (ej. es para español, en para inglés):");
        String idioma = teclado.nextLine().toLowerCase(); // Convertir a minúsculas para consistencia

        // Opciones de idiomas para el usuario
        String idiomaCompleto;
        switch (idioma) {
            case "es":
                idiomaCompleto = "Español";
                break;
            case "en":
                idiomaCompleto = "Inglés";
                break;
            case "fr":
                idiomaCompleto = "Francés";
                break;
            case "pt":
                idiomaCompleto = "Portugués";
                break;
            default:
                idiomaCompleto = "Desconocido"; // Para idiomas no mapeados directamente
        }


        long cantidad = libroRepository.countByIdioma(idioma);
        if (cantidad > 0) {
            System.out.println("\nCantidad de libros en " + idiomaCompleto + " (" + idioma.toUpperCase() + "): " + cantidad);
        } else {
            System.out.println("\nNo se encontraron libros en " + idiomaCompleto + " (" + idioma.toUpperCase() + ") en la base de datos.");
        }
    }
}