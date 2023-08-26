package ar.edu.utn.frbb.tup.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProfesorTest {

    @Test
    void agregarMateriaDictada() {
        Profesor profesor = new Profesor("pepito", "gonzalez", "aaa");
        Materia materia = new Materia("pepe 1", 1,1, profesor);
        profesor.agregarMateriaDictada(materia.getNombre());
    }

    @Test
    void getNombre() {
        Profesor profesor = new Profesor("pepito", "gonzalez", "aaa");
        assertEquals("pepito", profesor.getNombre());
    }

    @Test
    void getApellido() {
        Profesor profesor = new Profesor("pepito", "gonzalez", "aaa");
        assertEquals("gonzalez", profesor.getApellido());
    }

    @Test
    void getTitulo() {
        Profesor profesor = new Profesor("pepito", "gonzalez", "aaa");
        assertEquals("aaa", profesor.getTitulo());
    }

    @Test
    void getMateriasDictadas() {
        Profesor profesor = new Profesor("pepito", "gonzalez", "aaa");
        Materia materia = new Materia("pepe 1", 1,1, profesor);
        profesor.agregarMateriaDictada(materia.getNombre());
        assertTrue(profesor.getMateriasDictadas().contains(materia.getNombre()));
    }

    @Test
    void getprofesorId() {
        Profesor profesor = new Profesor("pepito", "gonzalez", "aaa");
        profesor.setprofesorId(123);
        assertEquals(123, profesor.getprofesorId());
    }

    @Test
    void setprofesorId() {
        Profesor profesor = new Profesor("pepito", "gonzalez", "aaa");
        profesor.setprofesorId(123);
        assertEquals(123, profesor.getprofesorId());
    }

    @Test
    void setMateriasDictadas() {
        Profesor profesor = new Profesor("pepito", "gonzalez", "aaa");
        Materia materia = new Materia("pepe 1", 1,1, profesor);
        ArrayList<String> materiasDictadas = new ArrayList<>();
        materiasDictadas.add(materia.getNombre());
        profesor.setMateriasDictadas(materiasDictadas);
    }
}