package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.persistence.AlumnoDao;
import ar.edu.utn.frbb.tup.persistence.AsignaturaDao;
import ar.edu.utn.frbb.tup.persistence.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AsignaturaServiceImplTest {

    @Mock
    private AlumnoDao alumnoDao;

    @Mock
    private AsignaturaDao dao;

    @InjectMocks
    private AsignaturaServiceImpl asignaturaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearAsignatura() {
        Materia materia = new Materia("pepe 1",1,1,new Profesor("pepe","gonzalez","Lic. Computación"));

        if (!(materia.getProfesor().getMateriasDictadas().contains(materia.getNombre()))) {
            materia.getProfesor().agregarMateriaDictada(materia.getNombre());
        }
        asignaturaService.crearAsignatura(materia);
    }

    @Test
    void putAsignatura() throws AsignaturaServiceException, AlumnoServiceException, AlumnoNotFoundException {
        List<Asignatura> asignaturas = new ArrayList<>();
        Asignatura asignatura1 = new Asignatura(new Materia("pepe 1",1,1,new Profesor("pepe","gonzalez","Lic. Computación")));
        Asignatura asignatura2 = new Asignatura(new Materia("pepe 2",1,1,new Profesor("pepe","gonzalez","Lic. Computación")));
        asignatura1.getMateria().setMateriaId(1);
        asignatura2.getMateria().setMateriaId(2);
        asignatura1.setEstado(EstadoAsignatura.NO_CURSADA);
        asignatura2.setEstado(EstadoAsignatura.NO_CURSADA);
        asignaturas.add(asignatura1);
        asignaturas.add(asignatura2);

        List<Asignatura> asignaturasList = new ArrayList<>(asignaturas);
        Alumno alumno = new Alumno("Speedy", "Gonzales",1);
        alumno.setId(123);
        alumno.setAsignaturas(asignaturasList);

        AsignaturaDto asignaturaDto = new AsignaturaDto();
        asignaturaDto.setNota(6);

//        asignaturaService.aprobarAsignatura(alumno.getId(),asignatura2.getMateria().getMateriaId(), asignaturaDto.getNota());
//        Mockito.verify(asignaturaService).aprobarAsignatura(alumno.getId(), asignatura2.getMateria().getMateriaId(), asignaturaDto.getNota());
    }

    @Test
    void buscarAsignaturaByNameAsignaturasAlumno() {
        List<Asignatura> asignaturas = new ArrayList<>();
        Asignatura asignatura1 = new Asignatura(new Materia("pepe 1",1,1,new Profesor("pepe","gonzalez","Lic. Computación")));
        Asignatura asignatura2 = new Asignatura(new Materia("pepe 2",1,1,new Profesor("pepe","gonzalez","Lic. Computación")));
        asignaturas.add(asignatura1);
        asignaturas.add(asignatura2);

        List<Asignatura> asignaturasList = new ArrayList<>(asignaturas);
        Alumno alumno = new Alumno("Speedy", "Gonzales",1);
        alumno.setAsignaturas(asignaturasList);
        AsignaturaServiceImpl.buscarAsignaturaByNameAsignaturasAlumno(alumno,"pepe 2");
    }

    @Test
    void aprobarAsignatura() throws AlumnoNotFoundException, AsignaturaServiceException, AlumnoServiceException {
        List<Asignatura> asignaturas = new ArrayList<>();
        Asignatura asignatura1 = new Asignatura(new Materia("pepe 1",1,1,new Profesor("pepe","gonzalez","Lic. Computación")));
        asignatura1.getMateria().setMateriaId(1);
        asignatura1.setEstado(EstadoAsignatura.NO_CURSADA);
        asignaturas.add(asignatura1);

        List<Asignatura> asignaturasList = new ArrayList<>(asignaturas);
        Alumno al = new Alumno("Speedy", "Gonzales",1);
        al.setId(123);
        al.setAsignaturas(asignaturasList);

        AsignaturaDto asignaturaDto = new AsignaturaDto();
        asignaturaDto.setNota(6);

        Mockito.when(alumnoDao.findById(123)).thenReturn(al);
        Alumno alumno = alumnoDao.findById(123);

        asignaturaService.aprobarAsignatura(alumno.getId(),asignatura1.getMateria().getMateriaId(),asignaturaDto.getNota());
    }

    @Test
    void checkAsignaturaDto() {
        AsignaturaDto asignaturaDto = new AsignaturaDto();
        asignaturaDto.setNota(1);
        assertDoesNotThrow(() -> AsignaturaServiceImpl.checkAsignaturaDto(asignaturaDto));
        if (asignaturaDto.getNota() < 0 || asignaturaDto.getNota()>10) {
            assertThrows(AsignaturaServiceException.class, () -> AsignaturaServiceImpl.checkAsignaturaDto(asignaturaDto));
        }
    }

    @Test
    void getSomeAsignaturaRandomFromAsignaturasDao() throws AsignaturaNotFoundException {
        if (dao.getAllAsignaturas().isEmpty()) {
            assertThrows(AsignaturaNotFoundException.class, () -> asignaturaService.getSomeAsignaturaRandomFromAsignaturasDao());
        }

        List<Asignatura> asignaturasNormales = new ArrayList<>();
        Asignatura asignatura1 = new Asignatura(new Materia("pepe 1",1,1,new Profesor("pepe","gonzalez","Lic. Computación")));
        Asignatura asignatura2 = new Asignatura(new Materia("pepe 2",1,1,new Profesor("pepe","gonzalez","Lic. Computación")));
        Asignatura asignatura3 = new Asignatura(new Materia("pepe 3",1,1,new Profesor("pepe","gonzalez","Lic. Computación")));
        asignatura1.getMateria().setMateriaId(1);
        asignatura2.getMateria().setMateriaId(2);
        asignatura3.getMateria().setMateriaId(3);
        asignaturasNormales.add(asignatura1);
        asignaturasNormales.add(asignatura2);
        asignaturasNormales.add(asignatura3);

        List<Asignatura> asignaturasTotales = new ArrayList<>(asignaturasNormales);
        Mockito.when(dao.getAllAsignaturas()).thenReturn(asignaturasTotales);

        List<Asignatura> asignaturasTemporal = new ArrayList<>(dao.getAllAsignaturas());
        for (Asignatura asignatura : asignaturasTemporal) {
            asignatura.setEstado(EstadoAsignatura.NO_CURSADA);
        }

        Asignatura asignatura = asignaturasTemporal.get(AsignaturaServiceImpl.crearNumeroEntreRangoRandom(0, asignaturasTemporal.size() - 1));
        List<Asignatura> asignaturas = asignaturaService.checkAsignaturaCorrelativas(asignatura, new ArrayList<>());

        for (Asignatura a : asignaturas) {
            asignaturaService.verificador(a, asignaturas);
        }
        asignaturaService.getSomeAsignaturaRandomFromAsignaturasDao();
    }

    @Test
    void verificador() {
        List<Asignatura> asignaturasSinDuplicado = new ArrayList<>();
        Asignatura asignatura1 = new Asignatura(new Materia("pepe 1",1,1,new Profesor("pepe","gonzalez","Lic. Computación")));
        Asignatura asignatura2 = new Asignatura(new Materia("pepe 2",1,1,new Profesor("pepe","gonzalez","Lic. Computación")));
        Asignatura asignatura3 = new Asignatura(new Materia("pepe 3",1,1,new Profesor("pepe","gonzalez","Lic. Computación")));
        Asignatura asignatura4 = new Asignatura(new Materia("pepe 4",1,1,new Profesor("pepe","gonzalez","Lic. Computación")));
        Asignatura asignatura5 = new Asignatura(new Materia("pepe 5",1,1,new Profesor("pepe","gonzalez","Lic. Computación")));
        Asignatura asignatura6 = new Asignatura(new Materia("pepe 6",1,1,new Profesor("pepe","gonzalez","Lic. Computación")));
        Asignatura asignatura7 = new Asignatura(new Materia("pepe 7",1,1,new Profesor("pepe","gonzalez","Lic. Computación")));
        Asignatura asignatura8 = new Asignatura(new Materia("pepe 8",1,1,new Profesor("pepe","gonzalez","Lic. Computación")));
        Asignatura asignatura13 = new Asignatura(new Materia("pepe 13",1,1,new Profesor("pepe","gonzalez","Lic. Computación")));
        Asignatura asignatura14 = new Asignatura(new Materia("pepe 14",1,1,new Profesor("pepe","gonzalez","Lic. Computación")));
        Asignatura asignatura15 = new Asignatura(new Materia("pepe 15",1,1,new Profesor("pepe","gonzalez","Lic. Computación")));
        Asignatura asignatura16 = new Asignatura(new Materia("pepe 16",1,1,new Profesor("pepe","gonzalez","Lic. Computación")));

        asignatura2.getMateria().getCorrelatividades().add("pepe 1");
        asignatura1.setEstado(EstadoAsignatura.CURSADA);
        asignatura2.setEstado(EstadoAsignatura.CURSADA);
        asignatura3.getMateria().getCorrelatividades().add("pepe 2");
        asignatura3.setEstado(EstadoAsignatura.CURSADA);
        asignatura4.getMateria().getCorrelatividades().add("pepe 3");

        asignatura6.getMateria().getCorrelatividades().add("pepe 5");
        asignatura5.setEstado(EstadoAsignatura.CURSADA);
        asignatura6.setEstado(EstadoAsignatura.CURSADA);
        asignatura7.getMateria().getCorrelatividades().add("pepe 6");
        asignatura7.setEstado(EstadoAsignatura.APROBADA);
        asignatura8.getMateria().getCorrelatividades().add("pepe 7");

        asignatura14.getMateria().getCorrelatividades().add("pepe 13");
        asignatura13.setEstado(EstadoAsignatura.APROBADA);
        asignatura14.setEstado(EstadoAsignatura.APROBADA);
        asignatura15.getMateria().getCorrelatividades().add("pepe 14");
        asignatura15.setEstado(EstadoAsignatura.APROBADA);
        asignatura16.getMateria().getCorrelatividades().add("pepe 15");

        asignaturasSinDuplicado.add(asignatura1);
        asignaturasSinDuplicado.add(asignatura2);
        asignaturasSinDuplicado.add(asignatura3);
        asignaturasSinDuplicado.add(asignatura4);
        asignaturasSinDuplicado.add(asignatura5);
        asignaturasSinDuplicado.add(asignatura6);
        asignaturasSinDuplicado.add(asignatura7);
        asignaturasSinDuplicado.add(asignatura8);
        asignaturasSinDuplicado.add(asignatura13);
        asignaturasSinDuplicado.add(asignatura14);
        asignaturasSinDuplicado.add(asignatura15);
        asignaturasSinDuplicado.add(asignatura16);

        for (Asignatura asignatura : asignaturasSinDuplicado) {
            asignaturaService.verificador(asignatura, asignaturasSinDuplicado);
        }
    }

    @Test
    void checkAsignaturaCorrelativas() {
        Materia materia1 = new Materia("pepe 1", 1, 1, new Profesor("pepe", "gonzalez", "Lic. Computación"));

        Asignatura asignatura1 = new Asignatura(materia1);

        asignatura1.getMateria().getCorrelatividades().add("pepe 2");
        List<Asignatura> listaAsignaturasExtraCursadasAprobadas = new ArrayList<>();
        asignaturaService.checkAsignaturaCorrelativas(asignatura1, listaAsignaturasExtraCursadasAprobadas);
    }

    @Test
    void setEstadoRandom() {
        Asignatura asignatura = new Asignatura(new Materia());
        AsignaturaServiceImpl.setEstadoRandom(asignatura);
        int numero = AsignaturaServiceImpl.crearNumeroEntreRangoRandom(0,2);
        if (numero>0) {
            int nota = AsignaturaServiceImpl.crearNumeroEntreRangoRandom(4,10);
            asignatura.setNota(nota);
            if (numero==1 || nota>=6) {
                asignatura.aprobarAsignatura();
            }
            else {
                asignatura.cursarAsignatura();
            }
        }
        else {
            asignatura.setNota(0);
            asignatura.setEstado(EstadoAsignatura.NO_CURSADA);
        }
    }

    @Test
    void buscarAsignaturaPorNombreAsignaturas() {
        List<Asignatura> asignaturas = new ArrayList<>();
        Asignatura asignatura2 = new Asignatura(new Materia("pepe 2",1,1,new Profesor("pepe","gonzalez","Lic. Computación")));
        Asignatura asignatura3 = new Asignatura(new Materia("pepe 3",1,1,new Profesor("pepe","gonzalez","Lic. Computación")));
        asignatura2.getMateria().setMateriaId(2);
        asignatura3.getMateria().setMateriaId(3);
        asignaturas.add(asignatura2);
        asignaturas.add(asignatura3);

        List<Asignatura> asignaturasList = new ArrayList<>(asignaturas);

        Asignatura asignatura = new Asignatura(new Materia("pepe 1",1,1,new Profesor("pepe","gonzalez","Lic. Computación")));
        asignatura.getMateria().getCorrelatividades().add("pepe 2");
        asignaturaService.buscarAsignaturaPorNombreAsignaturas(asignatura.getNombreAsignatura(),asignaturasList);
        assertEquals(asignatura.getMateria().getCorrelatividades().get(0), "pepe 2");
    }

    @Test
    void buscarAsignaturaPorNombre() {
        Materia materia = new Materia("pepe 1",1,1, new Profesor("pepe", "gonzalez", "Lic. Computación"));
        Asignatura asignatura = new Asignatura(materia);
        asignaturaService.buscarAsignaturaPorNombre(materia.getNombre());
        assertEquals(materia.getNombre(), asignatura.getMateria().getNombre());
    }

    @Test
    void getAllAsignaturas() {
        List<Asignatura> asignaturas = new ArrayList<>(dao.getAllAsignaturas());
        Mockito.when(asignaturaService.getAllAsignaturas()).thenReturn(asignaturas);
    }

    @Test
    void crearNumeroEntreRangoRandom() {
        int numero = AsignaturaServiceImpl.crearNumeroEntreRangoRandom(1, 3);
        assertTrue(numero>=1 && numero<=3);
    }

    @Test
    void actualizarAsignaturaByMateria() {
        Materia materia = new Materia();
        Asignatura asignatura = new Asignatura(materia);
        asignaturaService.actualizarAsignaturaByMateria(materia);
        assertEquals(materia, asignatura.getMateria());
    }

    @Test
    void delAsignaturaByMateria() throws MateriaNotFoundException {
        if (dao.getAllAsignaturas().isEmpty()) {
            assertThrows(MateriaNotFoundException.class, () -> asignaturaService.delAsignaturaByMateria(new Materia()));
        }
        List<Asignatura> asignaturas = new ArrayList<>();
        Asignatura asignatura1 = new Asignatura(new Materia("pepe 1",1,1,new Profesor("pepe","gonzalez","Lic. Computación")));
        Asignatura asignatura2 = new Asignatura(new Materia("pepe 2",1,1,new Profesor("pepe","gonzalez","Lic. Computación")));
        Asignatura asignatura3 = new Asignatura(new Materia("pepe 3",1,1,new Profesor("pepe","gonzalez","Lic. Computación")));
        asignatura1.getMateria().setMateriaId(1);
        asignatura2.getMateria().setMateriaId(2);
        asignatura3.getMateria().setMateriaId(3);
        asignaturas.add(asignatura1);
        asignaturas.add(asignatura2);
        asignaturas.add(asignatura3);

        List<Asignatura> asignaturasList = new ArrayList<>(asignaturas);
        Mockito.when(dao.getAllAsignaturas()).thenReturn(asignaturasList);

        Asignatura asignaturaElegida = asignaturas.get(1);
        asignaturaService.delAsignaturaByMateria(asignaturaElegida.getMateria());
        Mockito.verify(dao).del(asignaturaElegida.getMateria());
        assertThrows(MateriaNotFoundException.class, () -> asignaturaService.delAsignaturaByMateria(new Materia()));
    }
}