package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MateriaDaoMemoryImplTest {

    public static final MateriaDao materiaDao = new MateriaDaoMemoryImpl();

    @Test
    void save() throws MateriaNotFoundException {
        Materia materia = new Materia("pepe",1,1,new Profesor("pepito", "pepitos","Lic. En Computación"));
        materia.setMateriaId(1);
        materiaDao.save(materia);
        Materia materiaEncontrada = materiaDao.findById(1);
        assertEquals(materiaEncontrada, materia);
    }

    @Test
    void findById() throws MateriaNotFoundException {
        Materia materia = new Materia("pepe",1,1,new Profesor("pepito", "pepitos","Lic. En Computación"));
        materia.setMateriaId(123);
        materiaDao.save(materia);
        assertThrows(MateriaNotFoundException.class, () -> materiaDao.findById(1), "Materia no encontrada");
        assertEquals(materiaDao.findById(123),materia);
    }

    @Test
    void getAllMaterias() {
        Materia materia = new Materia("pepe",1,1,new Profesor("pepito", "pepitos","Lic. En Computación"));
        materiaDao.save(materia);
        if (materiaDao.getAllMaterias().isEmpty()) {
            assertThrows(MateriaNotFoundException.class, () -> {}, "No hay alumnos");
        }
        else {
            assertTrue(materiaDao.getAllMaterias().containsValue(materia));
        }
    }

    @Test
    void del() throws MateriaNotFoundException {
        Materia materia = new Materia("pepe",1,1,new Profesor("pepito", "pepitos","Lic. En Computación"));
        materia.setMateriaId(123);
        materiaDao.save(materia);
        materiaDao.del(materia);
        assertThrows(MateriaNotFoundException.class, () -> materiaDao.findById(123), "No se encontró la materia");
    }

    @Test
    void hayMaterias() {
        if (!materiaDao.getAllMaterias().isEmpty()) {
            assertFalse(materiaDao.getAllMaterias().isEmpty());
        }
        else {
            assertTrue(materiaDao.getAllMaterias().isEmpty());
        }
    }
}