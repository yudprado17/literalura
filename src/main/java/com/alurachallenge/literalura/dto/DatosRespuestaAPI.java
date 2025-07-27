package com.alurachallenge.literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosRespuestaAPI(
        @JsonAlias("results") List<DatosLibro> resultados
) {
}