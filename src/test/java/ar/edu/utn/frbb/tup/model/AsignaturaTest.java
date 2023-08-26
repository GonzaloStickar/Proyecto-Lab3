package ar.edu.utn.frbb.tup.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AsignaturaTest {

    @Test
    void getNota() {
        Asignatura asignatura = new Asignatura(new Materia());
        asignatura.setNota(8);
        assertEquals(8, asignatura.getNota());
    }

    @Test
    void setNota() {
        Asignatura asignatura = new Asignatura(new Materia());
        asignatura.setNota(8);
        assertEquals(8, asignatura.getNota());
    }

    @Test
    void getEstado() {
        Asignatura asignatura = new Asignatura(new Materia());
        asignatura.setEstado(EstadoAsignatura.APROBADA);
        assertEquals(EstadoAsignatura.APROBADA, asignatura.getEstado());
    }

    @Test
    void setEstado() {
        Asignatura asignatura = new Asignatura(new Materia());
        asignatura.setEstado(EstadoAsignatura.APROBADA);
        assertEquals(EstadoAsignatura.APROBADA, asignatura.getEstado());
    }

    @Test
    void getNombreAsignatura() {
        Asignatura asignatura = new Asignatura(new Materia());
        asignatura.getMateria().setNombre("pepe 1");
        assertEquals("pepe 1", asignatura.getNombreAsignatura());
    }

    @Test
    void getMateria() {
        Materia materia = new Materia("pepe 1", 1,1, new Profesor("p","g","l"));
        Asignatura asignatura = new Asignatura(materia);
        assertEquals(materia, asignatura.getMateria());
    }

    @Test
    void setMateria() {
        Materia materia1 = new Materia("pepe 1", 1,1, new Profesor("p","g","l"));
        Materia materia2 = new Materia("pepe 2", 1,1, new Profesor("p","g","l"));
        Asignatura asignatura = new Asignatura(materia1);
        asignatura.setMateria(materia2);
        assertEquals(materia2, asignatura.getMateria());
    }

    @Test
    void cursarAsignatura() {
        Asignatura asignatura = new Asignatura(new Materia());
        asignatura.cursarAsignatura();
        assertEquals(EstadoAsignatura.CURSADA, asignatura.getEstado());
    }

    @Test
    void aprobarAsignatura() {
        Asignatura asignatura = new Asignatura(new Materia());
        asignatura.aprobarAsignatura();
        assertEquals(EstadoAsignatura.APROBADA, asignatura.getEstado());
    }
}