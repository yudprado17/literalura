package com.alurachallenge.literalura.repository;

import com.alurachallenge.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // Declara esta interfaz como un componente de repositorio de Spring
public interface LibroRepository extends JpaRepository<Libro, Long> {
    // Derived Query para buscar un libro por título (insensible a mayúsculas/minúsculas)
    Optional<Libro> findByTituloContainsIgnoreCase(String titulo);

    // Derived Query para listar libros por idioma
    List<Libro> findByIdioma(String idioma);

    // Derived Query para contar libros por idioma
    long countByIdioma(String idioma);

    // Listar todos los libros ordenados por título
    List<Libro> findAllByOrderByTituloAsc();
}