package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.persistence.AlumnoDao;
import ar.edu.utn.frbb.tup.persistence.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AlumnoServiceImplTest {

    @Mock
    private AlumnoDao dao;

    @Mock
    private AsignaturaService asignaturaService;

    @InjectMocks
    private AlumnoServiceImpl alumnoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearAlumno() throws AsignaturaNotFoundException, AlumnoServiceException {
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setNombre("pepe");
        alumnoDto.setApellido("gonzales");

        alumnoDto.setDni(-1);
        assertThrows(AlumnoServiceException.class, () -> alumnoService.crearAlumno(alumnoDto));
        alumnoDto.setDni(1);

        Alumno a = new Alumno();

        a.setNombre(alumnoDto.getNombre());
        a.setApellido(alumnoDto.getApellido());
        a.setDni(alumnoDto.getDni());
        a.setId(123);
        for (Alumno alumno : dao.getAllAlumnos().values()) {
            if (alumno.getDni() == a.getDni()) {
                assertThrows(AlumnoServiceException.class, () -> alumnoService.crearAlumno(alumnoDto));
            }
        }
        a.setAsignaturas(asignaturaService.getSomeAsignaturaRandomFromAsignaturasDao());
        dao.saveAlumno(a);
        alumnoService.crearAlumno(alumnoDto);
    }

    @Test
    void getAlumnoById() throws AlumnoNotFoundException {
        Alumno alumno = new Alumno("pepe", "gonzalez",1);
        alumno.setId(123);
        dao.saveAlumno(alumno);
        Mockito.when(dao.findById(123)).thenReturn(alumno);
        Alumno alumnoEncontrado = alumnoService.getAlumnoById(123);
        assertEquals(alumno, alumnoEncontrado);
    }

    @Test
    void putAlumnoById() throws AlumnoNotFoundException, AlumnoServiceException {
        Alumno alumno = new Alumno("pepe", "gonzalez",1);
        alumno.setId(123);
        Mockito.when(alumnoService.getAlumnoById(123)).thenReturn(alumno);
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setApellido("gonza");
        alumnoDto.setNombre("pepito");
        alumnoService.putAlumnoById(123,alumnoDto);
    }

    @Test
    void checkAlumnoDtoPut() {
        Alumno alumno = new Alumno("pepe", "gonzalez",1);
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setNombre("pepe");
        alumnoDto.setApellido("gonzalez");

        if (alumno.getNombre().equals(alumnoDto.getNombre()) && alumno.getApellido().equals(alumnoDto.getApellido())) {
            assertThrows(AlumnoServiceException.class, () -> AlumnoServiceImpl.checkAlumnoDtoPut(alumno, alumnoDto));
        }

    }

    @Test
    void checkAlumnoDto() {
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setNombre("123");
        assertThrows(AlumnoServiceException.class, () -> alumnoService.checkAlumnoDto(alumnoDto));

        alumnoDto.setApellido("456");
        assertThrows(AlumnoServiceException.class, () -> alumnoService.checkAlumnoDto(alumnoDto));
    }

    @Test
    void delMateriaAlumnoByMateriaDel() {
        List<Asignatura> asignaturas = new ArrayList<>();
        Asignatura asignatura1 = new Asignatura(new Materia("pepe 1",1,1,new Profesor("pepe","gonzalez","Lic. Computación")));
        Asignatura asignatura2 = new Asignatura(new Materia("pepe 2",1,1,new Profesor("pepe","gonzalez","Lic. Computación")));

        asignatura2.getMateria().getCorrelatividades().add("pepe 1");

        asignaturas.add(asignatura1);
        asignaturas.add(asignatura2);

        List<Alumno> alumnos = new ArrayList<>();
        Alumno alumno = new Alumno("pepe", "gonzalez",1);
        alumno.setId(1);
        alumno.setAsignaturas(asignaturas);
        alumnos.add(alumno);
        Map<Integer, Alumno> alumnosMap = new HashMap<>();
        for (Alumno a : alumnos) {
            alumnosMap.put(a.getId(), a);
        }
        Mockito.when(dao.getAllAlumnos()).thenReturn(alumnosMap);

        alumnoService.delMateriaAlumnoByMateriaDel(asignatura1.getMateria());
    }

    @Test
    void delAlumnoById() throws AlumnoNotFoundException {
        List<Alumno> alumnos = new ArrayList<>();
        Alumno alumno1 = new Alumno("pepe", "gonzalez",1);
        Alumno alumno2 = new Alumno("pepito", "gonzalez",1);
        alumno1.setId(123);
        alumno2.setId(456);
        alumnos.add(alumno1);
        alumnos.add(alumno2);
        Map<Integer, Alumno> alumnosMap = new HashMap<>();
        for (Alumno a : alumnos) {
            alumnosMap.put(a.getId(), a);
        }
        assertThrows(AlumnoNotFoundException.class, () -> alumnoService.delAlumnoById(123));

        Mockito.when(dao.getAllAlumnos()).thenReturn(alumnosMap);
        Alumno alumnoEliminado = alumnoService.delAlumnoById(alumno2.getId());

        Mockito.verify(dao).del(alumno2);
        assertEquals(alumno2, alumnoEliminado);
        assertThrows(AlumnoNotFoundException.class, () -> alumnoService.delAlumnoById(123456));
    }
}