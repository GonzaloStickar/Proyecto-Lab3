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

import static org.junit.jupiter.api.Assertions.*;

class ProfesorServiceImplTest {

    @Mock
    private ProfesorDao profesorDao;

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
        Mockito.when(profesorDao.get(123)).thenReturn(profesor);
        Profesor resultProfesor = profesorService.buscarProfesor(123);
        Mockito.verify(profesorDao).get(123);
        assertEquals("pepe", resultProfesor.getNombre());
    }
}