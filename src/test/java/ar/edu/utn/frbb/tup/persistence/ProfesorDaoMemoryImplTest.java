package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfesorDaoMemoryImplTest {

    public final static ProfesorDao profesorDao = new ProfesorDaoMemoryImpl();

    @Test
    void save() throws ProfesorNotFoundException {
        Profesor profesor = new Profesor("pepe", "pepito", "Lic. En Computación");
        profesor.setprofesorId(123);
        profesorDao.save(profesor);
        Profesor profesorCreado = profesorDao.get(123);
        assertEquals(profesorCreado,profesor);
    }

    @Test
    void get() throws ProfesorNotFoundException {
        Profesor profesor = new Profesor("pepe", "pepito", "Lic. En Computación");
        profesor.setprofesorId(321);
        profesorDao.save(profesor);
        assertThrows(ProfesorNotFoundException.class, () -> profesorDao.get(555), "No hay un profesor con ese id");
        assertEquals(profesorDao.get(321),profesor);
    }

    @Test
    void getAllProfesores() {
        Profesor profesor = new Profesor("pepe", "pepito", "Lic. En Computación");
        profesorDao.save(profesor);
        if (profesorDao.getAllProfesores().isEmpty()) {
            assertThrows(ProfesorNotFoundException.class, () -> {}, "No hay alumnos");
        }
        else {
            assertTrue(profesorDao.getAllProfesores().contains(profesor));
        }
    }
}