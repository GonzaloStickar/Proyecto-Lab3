package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.model.Materia;
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
    void delMateriaDictadaFromProfesor() {
        List<Profesor> profesores = new ArrayList<>();
        Profesor profesor1 = new Profesor("a","g","L");
        Profesor profesor2 = new Profesor("b","g","L");
        Profesor profesor3 = new Profesor("c","g","L");
        profesor1.setprofesorId(1);
        profesor1.getMateriasDictadas().add("pepe 1");
        profesor2.setprofesorId(2);
        profesor3.setprofesorId(3);
        profesores.add(profesor1);
        profesores.add(profesor2);
        profesores.add(profesor3);

        Materia materia = new Materia("pepe 1", 1,1, profesor1);

        Mockito.when(dao.getAllProfesores()).thenReturn(profesores);

        profesorService.delMateriaDictadaFromProfesor(materia.getNombre());
    }
}