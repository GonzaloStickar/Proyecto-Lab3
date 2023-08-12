package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AsignaturaDaoMemoryImplTest {

    public static final AsignaturaDao asignaturaDao = new AsignaturaDaoMemoryImpl();

    @DisplayName("Test 7")
    @Test
    void getAsignaturaByName() throws AsignaturaNotFoundException {
        Asignatura asignatura = new Asignatura(new Materia("pepe",1,1,new Profesor("pepito", "pepitos","Lic. En Computación")));
        asignaturaDao.save(asignatura);
        assertEquals(asignaturaDao.getAllAsignaturas().get(0).getMateria(), asignaturaDao.getAsignaturaByName("pepe").getMateria());
        assertThrows(AsignaturaNotFoundException.class, () -> asignaturaDao.getAsignaturaByName("p"));
    }

    @DisplayName("Test 8")
    @Test
    void save() throws AsignaturaNotFoundException {
        Asignatura asignatura = new Asignatura(new Materia("pepe",1,1,new Profesor("pepito", "pepitos","Lic. En Computación")));
        asignaturaDao.save(asignatura);
        Asignatura asignaturaEncontrada = asignaturaDao.getAsignaturaByName(asignatura.getMateria().getNombre());
        assertEquals(asignaturaEncontrada.getMateria().getNombre(), "pepe");
    }

    @DisplayName("Test 9")
    @Test
    void del() {
        Asignatura asignatura = new Asignatura(new Materia("pepe",1,1,new Profesor("pepito", "pepitos","Lic. En Computación")));
        asignatura.getMateria().setMateriaId(123);
        asignaturaDao.save(asignatura);
        asignaturaDao.del(asignatura.getMateria());
    }

    @DisplayName("Test 10")
    @Test
    void getAllAsignaturas() {
        Asignatura asignatura = new Asignatura(new Materia("pepe",1,1,new Profesor("pepito", "pepitos","Lic. En Computación")));
        asignaturaDao.save(asignatura);
        if (asignaturaDao.getAllAsignaturas().isEmpty()) {
            assertThrows(AsignaturaNotFoundException.class, () -> {}, "No hay asignaturas");
        }
        else {
            assertEquals(asignaturaDao.getAllAsignaturas().get(0).getMateria(), asignatura.getMateria());
        }
    }
}