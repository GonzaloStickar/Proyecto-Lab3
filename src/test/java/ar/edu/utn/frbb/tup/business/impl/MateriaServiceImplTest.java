package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MateriaServiceImplTest {

    @Mock
    private MateriaDao dao;

    @InjectMocks
    private MateriaServiceImpl materiaService;

    @Mock
    private ProfesorService profesorService;

    @Mock
    private AsignaturaService asignaturaService;

    @Mock
    private AlumnoService alumnoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearMateria() {

    }

    @Test
    void crearNumeroEntreRangoRandom() {
    }

    @Test
    void getAllMaterias() throws MateriaNotFoundException {
        List<Materia> materias = new ArrayList<>(dao.getAllMaterias().values());
        if (materias.size()==0) {
            assertThrows(MateriaNotFoundException.class, () -> materiaService.getAllMaterias());
        }
        else {
            Mockito.when(materiaService.getAllMaterias()).thenReturn(materias);
        }
    }

    @Test
    void getMateriaById() throws MateriaNotFoundException {
        Materia materia = new Materia("materia 1", 1, 1, new Profesor("pepe", "gonzalez", "Lic. Computación"));
        materia.setMateriaId(123);
        dao.save(materia);
        Mockito.when(dao.findById(123)).thenReturn(materia);
        Materia materiaEncontrada = materiaService.getMateriaById(123);
        assertEquals(materia, materiaEncontrada);
    }

    @Test
    void putMateriaById() {
    }

    @Test
    void delMateriaById() {
    }

    @Test
    void getAllMateriasSortedBy() throws MateriaNotFoundException {
//        String order = "nombre_asc";
//        Mockito.when(materiaService.getAllMateriasByName("nombre_asc")).thenReturn(getAllMateriasSortedByNameAsc());
//        Mockito.when(materiaService.getAllMateriasByName("nombre_desc")).thenReturn(materiasEncontradas);
//        Mockito.when(materiaService.getAllMateriasByName("codigo_asc")).thenReturn(materiasEncontradas);
//        Mockito.when(materiaService.getAllMateriasByName("codigo_desc")).thenReturn(materiasEncontradas);
//
//        switch (order) {
//            case "nombre_asc" -> {
//                return getAllMateriasSortedByNameAsc();
//            }
//            case "nombre_desc" -> {
//                return getAllMateriasSortedByNameDesc();
//            }
//            case "codigo_asc" -> {
//                return getAllMateriasSortedByCodAsc();
//            }
//            case "codigo_desc" -> {
//                return getAllMateriasSortedByCodDesc();
//            }
//            default -> throw new MateriaServiceException("Especifique el orden.", HttpStatus.BAD_REQUEST);
//        }
    }

    @Test
    void getAllMateriasSortedByNameAsc() throws MateriaNotFoundException {
        List<Materia> materias = new ArrayList<>();
        Materia materia1 = new Materia("Physics",1,1,new Profesor("p","g","L"));
        Materia materia2 = new Materia("Math",1,1,new Profesor("p","g","L"));
        Materia materia3 = new Materia("Chemistry",1,1,new Profesor("p","g","L"));
        materia1.setMateriaId(1);
        materia2.setMateriaId(2);
        materia3.setMateriaId(3);
        materias.add(materia1);
        materias.add(materia2);
        materias.add(materia3);

        //Simular que están las materias en el dao
        Map<Integer, Materia> materiasMap = new HashMap<>();
        for (Materia materia : materias) {
            materiasMap.put(materia.getMateriaId(), materia);
        }
        Mockito.when(dao.getAllMaterias()).thenReturn(materiasMap);

        List<Materia> sortedMaterias = materiaService.getAllMateriasSortedByNameAsc();
        materias.sort(Comparator.comparing(Materia::getNombre));
        assertEquals(materias, sortedMaterias);
    }

    @Test
    void getAllMateriasSortedByNameDesc() throws MateriaNotFoundException {
        List<Materia> materias = new ArrayList<>();
        Materia materia1 = new Materia("Physics",1,1,new Profesor("p","g","L"));
        Materia materia2 = new Materia("Math",1,1,new Profesor("p","g","L"));
        Materia materia3 = new Materia("Chemistry",1,1,new Profesor("p","g","L"));
        materia1.setMateriaId(1);
        materia2.setMateriaId(2);
        materia3.setMateriaId(3);
        materias.add(materia1);
        materias.add(materia2);
        materias.add(materia3);

        //Simular que están las materias en el dao
        Map<Integer, Materia> materiasMap = new HashMap<>();
        for (Materia materia : materias) {
            materiasMap.put(materia.getMateriaId(), materia);
        }
        Mockito.when(dao.getAllMaterias()).thenReturn(materiasMap);

        List<Materia> sortedMaterias = materiaService.getAllMateriasSortedByNameDesc();
        materias.sort(Comparator.comparing(Materia::getNombre).reversed());
        assertEquals(materias, sortedMaterias);
    }

    @Test
    void getAllMateriasSortedByCodAsc() throws MateriaNotFoundException {
        List<Materia> materias = new ArrayList<>();
        Materia materia1 = new Materia("Physics",1,1,new Profesor("p","g","L"));
        Materia materia2 = new Materia("Math",1,1,new Profesor("p","g","L"));
        Materia materia3 = new Materia("Chemistry",1,1,new Profesor("p","g","L"));
        materia1.setMateriaId(1);
        materia2.setMateriaId(2);
        materia3.setMateriaId(3);
        materias.add(materia1);
        materias.add(materia2);
        materias.add(materia3);

        //Simular que están las materias en el dao
        Map<Integer, Materia> materiasMap = new HashMap<>();
        for (Materia materia : materias) {
            materiasMap.put(materia.getMateriaId(), materia);
        }
        Mockito.when(dao.getAllMaterias()).thenReturn(materiasMap);

        List<Materia> sortedMaterias = materiaService.getAllMateriasSortedByCodAsc();
        materias.sort(Comparator.comparing(Materia::getMateriaId));
        assertEquals(materias, sortedMaterias);
    }

    @Test
    void getAllMateriasSortedByCodDesc() throws MateriaNotFoundException {
        List<Materia> materias = new ArrayList<>();
        Materia materia1 = new Materia("Physics",1,1,new Profesor("p","g","L"));
        Materia materia2 = new Materia("Math",1,1,new Profesor("p","g","L"));
        Materia materia3 = new Materia("Chemistry",1,1,new Profesor("p","g","L"));
        materia1.setMateriaId(1);
        materia2.setMateriaId(2);
        materia3.setMateriaId(3);
        materias.add(materia1);
        materias.add(materia2);
        materias.add(materia3);

        //Simular que están las materias en el dao
        Map<Integer, Materia> materiasMap = new HashMap<>();
        for (Materia materia : materias) {
            materiasMap.put(materia.getMateriaId(), materia);
        }
        Mockito.when(dao.getAllMaterias()).thenReturn(materiasMap);

        List<Materia> sortedMaterias = materiaService.getAllMateriasSortedByCodDesc();
        materias.sort(Comparator.comparing(Materia::getMateriaId).reversed());
        assertEquals(materias, sortedMaterias);
    }

    @Test
    void getAllMateriasByName() {
        Map<Integer, Materia> materiasMap = new HashMap<>();
        Materia materia1 = new Materia("materia1", 1, 1, new Profesor("pepe", "gonzalez", "Lic. Computación"));
        Materia materia2 = new Materia("materia123", 1, 1, new Profesor("pepe", "gonzalez", "Lic. Computación"));
        Materia materia3 = new Materia("materi1", 1, 1, new Profesor("pepe", "gonzalez", "Lic. Computación"));
        materiasMap.put(1, materia1);
        materiasMap.put(2, materia2);
        materiasMap.put(3, materia3);
        Mockito.when(dao.getAllMaterias()).thenReturn(materiasMap);

        String nombre = "materia1";
        List<Materia> materiasEncontradas = new ArrayList<>();
        for (Materia materia : materiasMap.values()) {
            if (materia.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                materiasEncontradas.add(materia);
            }
        }
        if (materiasEncontradas.size() > 0) {
            assertDoesNotThrow(() -> materiaService.getAllMateriasByName(nombre));
        }
        else {
            assertThrows(MateriaNotFoundException.class, () -> materiaService.getAllMateriasByName(nombre));
        }
    }

    @Test
    void checkMateriaDtoPut() {
        Profesor profesor = new Profesor("pepe", "gonzalez", "Lic. Computación");
        profesor.setprofesorId(1);
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setNombre("materia 1");
        materiaDto.setAnio(2023);
        materiaDto.setCuatrimestre(3);
        materiaDto.setProfesorId(profesor.getprofesorId());
        Materia materia = new Materia();
        materia.setNombre("materia 1");
        materia.setAnio(2023);
        materia.setCuatrimestre(3);
        materia.setProfesor(profesor);

        if (materia.getNombre().equals(materiaDto.getNombre()) && materia.getCuatrimestre() == materiaDto.getCuatrimestre() && materia.getAnio() == materiaDto.getAnio() && materia.getProfesor().getprofesorId() == materiaDto.getProfesorId()) {
            assertThrows(MateriaServiceException.class, () -> MateriaServiceImpl.checkMateriaDtoPut(materia, materiaDto));
        }
        else {
            assertDoesNotThrow(() -> MateriaServiceImpl.checkMateriaDtoPut(materia, materiaDto));
        }
    }

    @Test
    void checkMateriaDtoOk() {
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setNombre("materia 1");
        materiaDto.setAnio(2023);
        materiaDto.setCuatrimestre(3);
        materiaDto.setProfesorId(10);
        assertDoesNotThrow(() -> materiaService.checkMateriaDto(materiaDto));
        if (!materiaDto.getNombre().matches(".*[a-zA-Z0-9]+.*")) {
            assertThrows(MateriaServiceException.class, () -> materiaService.checkMateriaDto(materiaDto));
        }
        for (Materia materia : dao.getAllMaterias().values()) {
            if (materia.getNombre().equals(materiaDto.getNombre())) {
                assertThrows(MateriaServiceException.class, () -> materiaService.checkMateriaDto(materiaDto));
            }
        }
        if (materiaDto.getAnio() <= 0) {
            assertThrows(MateriaServiceException.class, () -> materiaService.checkMateriaDto(materiaDto));
        }
        else if (materiaDto.getCuatrimestre() <= 0) {
            assertThrows(MateriaServiceException.class, () -> materiaService.checkMateriaDto(materiaDto));
        }
        else if (materiaDto.getProfesorId() < 0) {
            assertThrows(MateriaServiceException.class, () -> materiaService.checkMateriaDto(materiaDto));
        }
    }
}