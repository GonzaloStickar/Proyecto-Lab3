package ar.edu.utn.frbb.tup.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstadoAsignaturaTest {

    @Test
    void estadoAsignaturaTest() {
        Asignatura asignatura = new Asignatura(new Materia());
        asignatura.setEstado(EstadoAsignatura.NO_CURSADA);
        assertEquals(asignatura.getEstado(),EstadoAsignatura.NO_CURSADA);
        asignatura.setEstado(EstadoAsignatura.CURSADA);
        assertEquals(asignatura.getEstado(),EstadoAsignatura.CURSADA);
        asignatura.setEstado(EstadoAsignatura.APROBADA);
        assertEquals(asignatura.getEstado(),EstadoAsignatura.APROBADA);
    }
}