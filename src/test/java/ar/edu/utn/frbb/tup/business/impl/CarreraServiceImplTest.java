package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.persistence.CarreraDao;
import ar.edu.utn.frbb.tup.persistence.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CarreraServiceImplTest {

    @Mock
    private CarreraDao dao;

    @Mock
    private AsignaturaService asignaturaService;

    @InjectMocks
    private CarreraServiceImpl carreraService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearCarrera() throws CarreraServiceException, AsignaturaServiceException, AsignaturaNotFoundException {
        Carrera c = new Carrera("carrera 1", 1);
        c.setCodigoCarrera(1);

        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setNombre("carrera 2");
        carreraDto.setCodigoCarrera(2);
        carreraDto.setCantidadAnios(2);

        List<Asignatura> asignaturas = new ArrayList<>();
        Materia materia1 = new Materia("Physics",1,1,new Profesor("p","g","L"));
        Materia materia2 = new Materia("Math",1,1,new Profesor("p","g","L"));
        Materia materia3 = new Materia("Chemistry",1,1,new Profesor("p","g","L"));
        materia1.setMateriaId(1);
        materia2.setMateriaId(2);
        materia3.setMateriaId(3);
        asignaturas.add(new Asignatura(materia1));
        asignaturas.add(new Asignatura(materia2));
        asignaturas.add(new Asignatura(materia3));

        //Simular que están las materias en el dao
        List<Asignatura> asignaturasList = new ArrayList<>(asignaturas);
        Mockito.when(asignaturaService.getAllAsignaturas()).thenReturn(asignaturasList);

        carreraService.crearCarrera(carreraDto);

        if (carreraDto.getCodigoCarrera() <= 0) {
            assertThrows(CarreraServiceException.class, () -> carreraService.crearCarrera(carreraDto));
        }
        if (asignaturaService.getAllAsignaturas().isEmpty()) {
            assertThrows(AsignaturaServiceException.class, () -> carreraService.crearCarrera(carreraDto));
        }

        c.setNombre(carreraDto.getNombre());
        c.setCantidadCuatrimestres((carreraDto.getCantidadAnios()*12)/4);
        c.setCodigoCarrera(carreraDto.getCodigoCarrera());

        int numero = CarreraServiceImpl.crearNumeroEntreRangoRandom(0,asignaturaService.getAllAsignaturas().size());
        ArrayList<Integer> numerosRandom = new ArrayList<>();
        while (numerosRandom.size()<numero) {
            int numeroNuevo = CarreraServiceImpl.crearNumeroEntreRangoRandom(0,(asignaturaService.getAllAsignaturas().size())-1);
            if (!numerosRandom.contains(numeroNuevo)) {
                numerosRandom.add(numeroNuevo);
            }
        }
        List<Materia> materias = new ArrayList<>();
        for (Integer n : numerosRandom) {
            materias.add(asignaturaService.getAllAsignaturas().get(n).getMateria());
        }

        c.setMateriasList(materias);
        for (Carrera carrera : dao.getAllCarreras().values()) {
            if (carrera.getCodigoCarrera() == c.getCodigoCarrera()) {
                assertThrows(CarreraServiceException.class, () -> carreraService.crearCarrera(carreraDto));
            }
        }
        dao.save(c);
        assertEquals(c.getNombre(),carreraDto.getNombre());
    }

    @Test
    void crearNumeroEntreRangoRandom() {
        int numero = CarreraServiceImpl.crearNumeroEntreRangoRandom(1, 3);
        assertTrue(numero>=1 && numero<=3);
    }

    @Test
    void getCarreraById() throws CarreraNotFoundException {
        Carrera carrera = new Carrera("carrera 1", 1);
        carrera.setCodigoCarrera(1);
        dao.save(carrera);
        Mockito.when(dao.findById(1)).thenReturn(carrera);
        Carrera carreraEncontrada = carreraService.getCarreraById(1);
        assertEquals(carrera, carreraEncontrada);
    }

    @Test
    void putCarreraById() throws CarreraNotFoundException, CarreraServiceException {
        Carrera carrera = new Carrera("carrera 1", 1);
        carrera.setCodigoCarrera(123);
        dao.save(carrera);
        Mockito.when(dao.findById(123)).thenReturn(carrera);
        Carrera carreraEncontrada = carreraService.getCarreraById(123);
        assertEquals(carrera, carreraEncontrada);

        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setNombre("carrera 2");
        carreraDto.setCodigoCarrera(2);
        carreraDto.setCantidadAnios(2);

        carreraService.putCarreraById(carrera.getCodigoCarrera(), carreraDto);
        carrera.setCantidadCuatrimestres((carreraDto.getCantidadAnios()*12)/4);
        carrera.setNombre(carreraDto.getNombre());
        assertEquals(carrera.getNombre(), carreraDto.getNombre());
    }

    @Test
    void delCarreraById() throws CarreraNotFoundException {
        if (dao.getAllCarreras().isEmpty()) {
            assertThrows(CarreraNotFoundException.class, () -> carreraService.delCarreraById(1));
        }

        List<Carrera> carreras = new ArrayList<>();
        Carrera carrera1 = new Carrera("carrera 1", 1);
        Carrera carrera2 = new Carrera("carrera 1", 1);
        carrera1.setCodigoCarrera(1);
        carrera2.setCodigoCarrera(2);
        carreras.add(carrera1);
        carreras.add(carrera2);

        Map<Integer, Carrera> carrerasMap = new HashMap<>();
        for (Carrera carrera : carreras) {
            carrerasMap.put(carrera.getCodigoCarrera(), carrera);
        }
        Mockito.when(dao.getAllCarreras()).thenReturn(carrerasMap);

        Carrera carreraEliminada = carreraService.delCarreraById(carrera2.getCodigoCarrera());

        Mockito.verify(dao).del(carreraEliminada);
        assertEquals(carrera2, carreraEliminada);
        assertThrows(CarreraNotFoundException.class, () -> carreraService.delCarreraById(4));
    }

    @Test
    void getAllCarreras() throws CarreraNotFoundException {
        List<Carrera> carreras = new ArrayList<>(dao.getAllCarreras().values());
        if (carreras.size()==0) {
            assertThrows(CarreraNotFoundException.class, () -> carreraService.getAllCarreras());
        }
        else {
            Mockito.when(carreraService.getAllCarreras()).thenReturn(carreras);
        }
    }

    @Test
    void checkCarreraDtoPut() {
        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setNombre("carrera 1");
        carreraDto.setCodigoCarrera(1);
        carreraDto.setCantidadAnios(1);
        Carrera carrera = new Carrera();
        carrera.setNombre("carrera 1");
        carrera.setCodigoCarrera(1);
        carrera.setCantidadCuatrimestres(3);

        if (carrera.getNombre().equals(carreraDto.getNombre()) && (carrera.getCantidadCuatrimestres()/3)==carreraDto.getCantidadAnios()) {
            assertThrows(CarreraServiceException.class, () -> CarreraServiceImpl.checkCarreraDtoPut(carrera, carreraDto));
        }
        else {
            assertDoesNotThrow(() -> CarreraServiceImpl.checkCarreraDtoPut(carrera, carreraDto));
        }
    }

    @Test
    void checkCarreraDto() {
        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setNombre("carrera 1");
        carreraDto.setCodigoCarrera(1);
        carreraDto.setCantidadAnios(1);
        assertDoesNotThrow(() -> carreraService.checkCarreraDto(carreraDto));

        if (!carreraDto.getNombre().matches(".*[a-zA-Z]+.*")) {
            assertThrows(CarreraServiceException.class, () -> carreraService.checkCarreraDto(carreraDto));
        }
        else if (carreraDto.getCantidadAnios() <= 0) {
            assertThrows(CarreraServiceException.class, () -> carreraService.checkCarreraDto(carreraDto));
        }
    }
}