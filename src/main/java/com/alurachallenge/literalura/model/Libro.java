package com.alurachallenge.literalura.model;

import jakarta.persistence.*; // Importa las anotaciones de JPA

@Entity // Declara esta clase como una entidad JPA
@Table(name = "libros") // Mapea la clase a una tabla llamada 'libros'
public class Libro {
    @Id // Marca 'id' como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática de ID
    private Long id;

    @Column(unique = true) // Asegura que el título sea único en la BD
    private String titulo;
    private String idioma;
    private Integer numeroDescargas;

    @ManyToOne // Un autor puede tener muchos libros, un libro tiene un autor
    @JoinColumn(name = "autor_id") // Columna de clave foránea en la tabla 'libros'
    private Autor autor;

    @Override
    public String toString() {
        return "------ LIBRO ------\n" +
                "Título: " + titulo + "\n" +
                "Autor: " + (autor != null ? autor.getNombre() : "Desconocido") + "\n" +
                "Idioma: " + idioma + "\n" +
                "Número de Descargas: " + numeroDescargas + "\n" +
                "-------------------";
    }

    // Constructor vacío requerido por JPA
    public Libro() {
    }

    public Libro(String titulo, String idioma, Integer numeroDescargas, Autor autor) {
        this.titulo = titulo;
        this.idioma = idioma;
        this.numeroDescargas = numeroDescargas;
        this.autor = autor;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Integer getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Integer numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }
}