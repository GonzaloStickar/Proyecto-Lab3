package ar.edu.utn.frbb.tup.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlumnoTest {

    @Test
    void getAsignaturas() {
        Alumno alumno = new Alumno("pepe", "gonzalez", 1);
        List<Asignatura> asignaturas = new ArrayList<>();
        Asignatura asignatura1 = new Asignatura(new Materia("pepe 1", 1,1, new Profesor("p","g","l")));
        Asignatura asignatura2 = new Asignatura(new Materia("pepe 2", 1,1, new Profesor("p","g","l")));
        asignaturas.add(asignatura1);
        asignaturas.add(asignatura2);
        alumno.setAsignaturas(asignaturas);
        assertTrue(alumno.getAsignaturas().contains(asignatura1));
    }

    @Test
    void setAsignaturas() {
        Alumno alumno = new Alumno();
        List<Asignatura> asignaturas = new ArrayList<>();
        Asignatura asignatura1 = new Asignatura(new Materia("pepe 1", 1,1, new Profesor("p","g","l")));
        Asignatura asignatura2 = new Asignatura(new Materia("pepe 2", 1,1, new Profesor("p","g","l")));
        asignaturas.add(asignatura1);
        asignaturas.add(asignatura2);
        alumno.setAsignaturas(asignaturas);
        assertTrue(alumno.getAsignaturas().contains(asignatura1));
    }

    @Test
    void setNombre() {
        Alumno alumno = new Alumno();
        alumno.setNombre("pepito");
        assertEquals("pepito", alumno.getNombre());
    }

    @Test
    void setApellido() {
        Alumno alumno = new Alumno();
        alumno.setApellido("gonzalez");
        assertEquals("gonzalez", alumno.getApellido());
    }

    @Test
    void setDni() {
        Alumno alumno = new Alumno();
        alumno.setDni(1);
        assertEquals(1, alumno.getDni());
    }

    @Test
    void getNombre() {
        Alumno alumno = new Alumno();
        alumno.setNombre("pepito");
        assertEquals("pepito", alumno.getNombre());
    }

    @Test
    void getApellido() {
        Alumno alumno = new Alumno();
        alumno.setApellido("gonzalez");
        assertEquals("gonzalez", alumno.getApellido());
    }

    @Test
    void getDni() {
        Alumno alumno = new Alumno();
        alumno.setDni(1);
        assertEquals(1, alumno.getDni());
    }

    @Test
    void agregarAsignatura() {
        Alumno alumno = new Alumno();
        List<Asignatura> asignaturas = new ArrayList<>();
        Asignatura asignatura1 = new Asignatura(new Materia("pepe 1", 1,1, new Profesor("p","g","l")));
        Asignatura asignatura2 = new Asignatura(new Materia("pepe 2", 1,1, new Profesor("p","g","l")));
        asignaturas.add(asignatura1);
        alumno.setAsignaturas(asignaturas);
        alumno.agregarAsignatura(asignatura2);
        assertTrue(alumno.getAsignaturas().contains(asignatura2));
    }

    @Test
    void obtenerListaAsignaturas() {
        Alumno alumno = new Alumno();
        List<Asignatura> asignaturas = new ArrayList<>();
        Asignatura asignatura1 = new Asignatura(new Materia("pepe 1", 1,1, new Profesor("p","g","l")));
        Asignatura asignatura2 = new Asignatura(new Materia("pepe 2", 1,1, new Profesor("p","g","l")));
        asignaturas.add(asignatura1);
        alumno.setAsignaturas(asignaturas);
        alumno.agregarAsignatura(asignatura2);
        assertEquals(alumno.obtenerListaAsignaturas(), asignaturas);
    }

    @Test
    void getId() {
        Alumno alumno = new Alumno();
        alumno.setId(123);
        assertEquals(123, alumno.getId());
    }

    @Test
    void setId() {
        Alumno alumno = new Alumno();
        alumno.setId(123);
        assertEquals(123, alumno.getId());
    }
}