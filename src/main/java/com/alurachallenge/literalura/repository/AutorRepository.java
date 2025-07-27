package com.alurachallenge.literalura.repository;

import com.alurachallenge.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // Declara esta interfaz como un componente de repositorio de Spring
public interface AutorRepository extends JpaRepository<Autor, Long> {
    // Derived Query para buscar un autor por nombre (insensible a mayúsculas/minúsculas)
    Optional<Autor> findByNombreContainsIgnoreCase(String nombre);

    // Derived Query para listar autores vivos en un año dado
    // Busca autores cuyo año de nacimiento sea menor o igual al año dado
    // Y cuyo año de fallecimiento sea mayor o igual al año dado O su año de fallecimiento sea nulo
    List<Autor> findByAnioNacimientoLessThanEqualAndAnioFallecimientoGreaterThanEqualOrAnioFallecimientoIsNull(Integer anio, Integer anioParaNulos);

    // Otra opción más limpia (requiere que null se maneje en la lógica de negocio si el campo no es null)
    // List<Autor> findByAnioNacimientoLessThanEqualAndAnioFallecimientoGreaterThanEqual(Integer anioActual, Integer anioActual2);

    // Listar todos los autores ordenados por nombre
    List<Autor> findAllByOrderByNombreAsc();
}