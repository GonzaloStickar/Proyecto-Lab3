package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.persistence.ProfesorDao;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProfesorServiceImplTest {

    @Mock
    private ProfesorDao dao;

    @InjectMocks
    private ProfesorServiceImpl profesorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void buscarProfesor() throws ProfesorNotFoundException {
        Profesor profesor = new Profesor("pepe","gonzalez","título Computación");
        profesor.setprofesorId(123);
        Mockito.when(dao.get(123)).thenReturn(profesor);
        Profesor resultProfesor = profesorService.buscarProfesor(123);
        Mockito.verify(dao).get(123);
        assertEquals("pepe", resultProfesor.getNombre());
    }

    @Test
    void actualizarProfesores() {
        List<Profesor> profesores = new ArrayList<>();

        Profesor pA = new Profesor("a", "g", "L");
        Profesor pB = new Profesor("b", "g", "L");

        pA.setprofesorId(1);
        pB.setprofesorId(2);
        pA.getMateriasDictadas().add("pepe1");
        pA.getMateriasDictadas().add("pepe2");
        pB.getMateriasDictadas().add("pepe1");

        profesores.add(pA);
        profesores.add(pB);

        Mockito.when(dao.getAllProfesores()).thenReturn(profesores);

        profesorService.actualizarProfesores("pepe1","zepe123",1);
    }

    @Test
    void actualizarProfesoresByNombreMateriaDeleted() {
        List<Profesor> profesores = new ArrayList<>();

        Profesor pA = new Profesor("a", "g", "L");
        Profesor pB = new Profesor("b", "g", "L");

        pA.setprofesorId(1);
        pB.setprofesorId(2);
        pA.getMateriasDictadas().add("pepe1");
        pA.getMateriasDictadas().add("pepe2");
        pB.getMateriasDictadas().add("pepe1");

        profesores.add(pA);
        profesores.add(pB);

        Mockito.when(dao.getAllProfesores()).thenReturn(profesores);

        profesorService.actualizarProfesoresByNombreMateriaDeleted("pepe1");
    }
}