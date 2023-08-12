package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlumnoDaoMemoryImplTest {

    public static final AlumnoDao alumnoDao = new AlumnoDaoMemoryImpl();

    @DisplayName("Test 1")
    @Test
    void saveAlumno() throws AlumnoNotFoundException {
        Alumno alumno = new Alumno("pepe", "gonzalez", 1);
        alumnoDao.saveAlumno(alumno);
        Alumno alumnoCreado = alumnoDao.findAlumnoByDni(alumno.getDni());
        assertEquals(alumnoCreado,alumno);
    }

    @DisplayName("Test 2")
    @Test
    void findById() throws AlumnoNotFoundException {
        Alumno alumno = new Alumno("pepe", "gonzalez", 1);
        alumno.setId(123);
        alumnoDao.saveAlumno(alumno);
        assertThrows(AlumnoNotFoundException.class, () -> alumnoDao.findById(1));
        assertEquals(alumnoDao.findById(123),alumno);
    }

    @DisplayName("Test 3")
    @Test
    void findAlumnoByDni() throws AlumnoNotFoundException {
        assertThrows(AlumnoNotFoundException.class, () -> alumnoDao.findAlumnoByDni(123), "No se encontró el alumno");
        Alumno alumno = new Alumno("pepe", "gonzalez", 1);
        alumnoDao.saveAlumno(alumno);
        Alumno alumnoCreado = alumnoDao.findAlumnoByDni(alumno.getDni());
        assertEquals(alumnoCreado,alumno);
    }

    @DisplayName("Test 4")
    @Test
    void del() throws AlumnoNotFoundException {
        Alumno alumno = new Alumno("pepe", "gonzalez", 1);
        alumno.setId(123);
        alumnoDao.saveAlumno(alumno);
        alumnoDao.del(alumno);
        assertThrows(AlumnoNotFoundException.class, () -> alumnoDao.del(alumno));
        assertThrows(AlumnoNotFoundException.class, () -> alumnoDao.findById(123), "No se encontró el alumno");
    }

    @DisplayName("Test 5")
    @Test
    void getAllAlumnos() {
        Alumno alumno = new Alumno("pepe", "gonzalez", 1);
        alumnoDao.saveAlumno(alumno);
        if (alumnoDao.getAllAlumnos().isEmpty()) {
            assertThrows(AlumnoNotFoundException.class, () -> {}, "No hay alumnos");
        }
        else {
            assertTrue(alumnoDao.getAllAlumnos().containsValue(alumno));
        }
    }

    @DisplayName("Test 6")
    @Test
    void hayAlumnos() {
        if (!alumnoDao.getAllAlumnos().isEmpty()) {
            assertFalse(alumnoDao.getAllAlumnos().isEmpty());
        }
        else {
            assertTrue(alumnoDao.getAllAlumnos().isEmpty());
        }
    }
}