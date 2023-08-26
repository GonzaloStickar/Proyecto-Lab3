package ar.edu.utn.frbb.tup.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarreraTest {

    @Test
    void getCodigoCarrera() {
        Carrera carrera = new Carrera();
        carrera.setCodigoCarrera(123);
        assertEquals(123, carrera.getCodigoCarrera());
    }

    @Test
    void setCodigoCarrera() {
        Carrera carrera = new Carrera();
        carrera.setCodigoCarrera(123);
        assertEquals(123, carrera.getCodigoCarrera());
    }

    @Test
    void getDepartamentoInt() {
        Carrera carrera = new Carrera();
        carrera.setDepartamentoInt(1);
        assertEquals(1, carrera.getDepartamentoInt());
    }

    @Test
    void setDepartamentoInt() {
        Carrera carrera = new Carrera();
        carrera.setDepartamentoInt(1);
        assertEquals(1, carrera.getDepartamentoInt());
    }

    @Test
    void getNombre() {
        Carrera carrera = new Carrera();
        carrera.setNombre("pepe");
        assertEquals("pepe", carrera.getNombre());
    }

    @Test
    void setNombre() {
        Carrera carrera = new Carrera("pepe", 1);
        carrera.setNombre("pepito");
        assertEquals("pepito", carrera.getNombre());
    }

    @Test
    void getCantidadCuatrimestres() {
        Carrera carrera = new Carrera();
        carrera.setCantidadCuatrimestres(1);
        assertEquals(1, carrera.getCantidadCuatrimestres());
    }

    @Test
    void setCantidadCuatrimestres() {
        Carrera carrera = new Carrera();
        carrera.setCantidadCuatrimestres(1);
        assertEquals(1, carrera.getCantidadCuatrimestres());
    }

    @Test
    void getMateriasList() {
        Carrera carrera = new Carrera();
        carrera.setMateriasList(new ArrayList<>());
        assertEquals(carrera.getMateriasList(),new ArrayList<>());
    }

    @Test
    void setMateriasList() {
        Carrera carrera = new Carrera();
        carrera.setMateriasList(new ArrayList<>());
        assertEquals(carrera.getMateriasList(),new ArrayList<>());
    }

    @Test
    void agregarMateria() {
        Carrera carrera = new Carrera();
        List<Materia> listaMaterias = new ArrayList<>();
        Materia materia = new Materia("pepe 1", 1,1, new Profesor("p","g","l"));
        listaMaterias.add(materia);
        carrera.setMateriasList(listaMaterias);
        carrera.agregarMateria(materia);
        assertTrue(carrera.getMateriasList().contains(materia));
    }
}