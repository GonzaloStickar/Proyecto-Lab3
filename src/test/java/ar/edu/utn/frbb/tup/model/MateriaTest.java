package ar.edu.utn.frbb.tup.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MateriaTest {

    @Test
    void setNombre() {
        Profesor profesor = new Profesor("pepito", "gonzalez", "aaa");
        Materia materia = new Materia("pepe 1", 1,1, profesor);
        materia.setNombre("pepe 2");
        assertEquals("pepe 2", materia.getNombre());
    }

    @Test
    void getAnio() {
        Profesor profesor = new Profesor("pepito", "gonzalez", "aaa");
        Materia materia = new Materia("pepe 1", 1,1, profesor);
        assertEquals(1, materia.getAnio());
    }

    @Test
    void setAnio() {
        Profesor profesor = new Profesor("pepito", "gonzalez", "aaa");
        Materia materia = new Materia("pepe 1", 1,1, profesor);
        materia.setAnio(1);
        assertEquals(1, materia.getAnio());
    }

    @Test
    void getCuatrimestre() {
        Profesor profesor = new Profesor("pepito", "gonzalez", "aaa");
        Materia materia = new Materia("pepe 1", 1,1, profesor);
        assertEquals(1, materia.getCuatrimestre());
    }

    @Test
    void setCuatrimestre() {
        Profesor profesor = new Profesor("pepito", "gonzalez", "aaa");
        Materia materia = new Materia("pepe 1", 1,1, profesor);
        materia.setCuatrimestre(1);
        assertEquals(1, materia.getCuatrimestre());
    }

    @Test
    void getProfesor() {
        Profesor profesor = new Profesor("pepito", "gonzalez", "aaa");
        Materia materia = new Materia("pepe 1", 1,1, profesor);
        assertEquals(profesor, materia.getProfesor());
    }

    @Test
    void setProfesor() {
        Profesor profesor = new Profesor("pepito", "gonzalez", "aaa");
        Materia materia = new Materia("pepe 1", 1,1, profesor);
        materia.setProfesor(new Profesor("pepe", "gonza", "bbb"));
        assertEquals("pepe", materia.getProfesor().getNombre());
    }

    @Test
    void agregarCorrelatividad() {
        Profesor profesor = new Profesor("pepito", "gonzalez", "aaa");
        Materia materia1 = new Materia();
        materia1.setNombre("pepe 1");
        Materia materia2 = new Materia("pepe 1", 1,1, profesor);
        materia2.agregarCorrelatividad(materia1.getNombre());
        assertEquals(materia1.getNombre(), materia2.getCorrelatividades().get(0));
    }

    @Test
    void getCorrelatividades() {
        Profesor profesor = new Profesor("pepito", "gonzalez", "aaa");
        Materia materia1 = new Materia("pepe 1", 1,1, profesor);
        Materia materia2 = new Materia("pepe 1", 1,1, profesor);
        materia2.getCorrelatividades().add("pepe 1");
        assertEquals(materia1.getNombre(), materia2.getCorrelatividades().get(0));
    }

    @Test
    void setCorrelatividades() {
        Profesor profesor = new Profesor("pepito", "gonzalez", "aaa");
        Materia materia1 = new Materia("pepe 1", 1,1, profesor);
        Materia materia2 = new Materia("pepe 1", 1,1, profesor);
        List<String> correlativas = new ArrayList<>();
        correlativas.add(materia1.getNombre());
        materia2.setCorrelatividades(correlativas);
        assertEquals(materia1.getNombre(), materia2.getCorrelatividades().get(0));
    }

    @Test
    void getNombre() {
        Profesor profesor = new Profesor("pepito", "gonzalez", "aaa");
        Materia materia = new Materia("pepe 1", 1,1, profesor);
        materia.setNombre("pepe 2");
        assertEquals("pepe 2", materia.getNombre());
    }

    @Test
    void getMateriaId() {
        Profesor profesor = new Profesor("pepito", "gonzalez", "aaa");
        Materia materia = new Materia("pepe 1", 1,1, profesor);
        materia.setMateriaId(123);
        assertEquals(123, materia.getMateriaId());
    }

    @Test
    void setMateriaId() {
        Profesor profesor = new Profesor("pepito", "gonzalez", "aaa");
        Materia materia = new Materia("pepe 1", 1,1, profesor);
        materia.setMateriaId(123);
        assertEquals(123, materia.getMateriaId());
    }
}