package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.business.CarreraService;
import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaServiceException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MateriaServiceImplTest {

    @Mock
    private MateriaDao dao;

    @Mock
    private MateriaServiceImpl materiaServiceImpl;

    @Mock
    private ProfesorService profesorService;

    @Mock
    private AsignaturaService asignaturaService;

    @Mock
    private AlumnoService alumnoService;

    @Mock
    private CarreraService carreraService;

    @InjectMocks
    private MateriaServiceImpl materiaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearMateria() throws ProfesorNotFoundException, MateriaServiceException {
        Materia materia = new Materia("materia 1", 1, 1, new Profesor("pepe", "gonzalez", "Lic. Computación"));
        materia.setMateriaId(1);

        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setNombre("pepe1");
        materiaDto.setAnio(1);
        materiaDto.setCuatrimestre(1);
        materiaDto.setProfesorId(1);

        materia.setNombre(materiaDto.getNombre());
        materia.setAnio(materiaDto.getAnio());
        materia.setCuatrimestre(materiaDto.getCuatrimestre());
        materia.setProfesor(new Profesor("pepito", "gonzalez", "lic Computación"));

        materiaService.crearMateria(materiaDto);

        Materia mat = new Materia();
        Random random = new Random();
        mat.setMateriaId(random.nextInt());
        mat.setMateriaId(1);
        dao.save(mat);
        for (Materia m : dao.getAllMaterias().values()) {
            if (m.getMateriaId() == materia.getMateriaId()) {
                assertThrows(MateriaNotFoundException.class, () -> materiaService.crearMateria(new MateriaDto()));
            }
        }
        List<String> materiasListParaCorrelatividades = new ArrayList<>();
        int cantidadMateriasCorrelativas = MateriaServiceImpl.crearNumeroEntreRangoRandom(0, (asignaturaService.getAllAsignaturas().size()));
        for (Asignatura asignatura : asignaturaService.getAllAsignaturas()) {
            MateriaServiceImpl.crearNumeroEntreRangoRandom(0,(asignaturaService.getAllAsignaturas().size()-1));
            int numeroRandomMateriaCorrelativa = MateriaServiceImpl.crearNumeroEntreRangoRandom(0,(asignaturaService.getAllAsignaturas().size()-1));
            if (!(materiasListParaCorrelatividades.contains(asignatura.getMateria().getNombre())) && materiasListParaCorrelatividades.size()<cantidadMateriasCorrelativas) {
                if (!(asignaturaService.getAllAsignaturas().get(numeroRandomMateriaCorrelativa).getMateria().getNombre().contains(materiaDto.getNombre()))) {
                    if (!(materiasListParaCorrelatividades.contains(asignaturaService.getAllAsignaturas().get(numeroRandomMateriaCorrelativa).getMateria().getNombre()))) {
                        materiasListParaCorrelatividades.add(asignaturaService.getAllAsignaturas().get(numeroRandomMateriaCorrelativa).getMateria().getNombre());
                    }
                }
            }
        }
        materia.setCorrelatividades(materiasListParaCorrelatividades);
        asignaturaService.crearAsignatura(materia);
        dao.save(materia);
        assertEquals(materia.getNombre(),materiaDto.getNombre());
    }

    @Test
    void crearNumeroEntreRangoRandom() {
        int numero = MateriaServiceImpl.crearNumeroEntreRangoRandom(1, 3);
        assertTrue(numero>=1 && numero<=3);
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
    void putMateriaById() throws MateriaNotFoundException, ProfesorNotFoundException, MateriaServiceException {
        Profesor pA = new Profesor("a", "g", "L");
        Profesor pB = new Profesor("b", "g", "L");
        Profesor pC = new Profesor("c", "g", "L");

        Mockito.when(profesorService.buscarProfesor(2)).thenReturn(pB);

        List<Materia> materias = new ArrayList<>();
        Materia materia1 = new Materia("Physics", 1, 1, pA);
        Materia materia2 = new Materia("Math", 2, 1, pB);
        Materia materia3 = new Materia("Chemistry", 3, 1, pC);
        materia1.setMateriaId(1);
        materia2.setMateriaId(2);
        materia2.getCorrelatividades().add("pepe");
        materia3.setMateriaId(3);
        materia3.getCorrelatividades().add("Physics");
        materia3.getCorrelatividades().add("Math");
        materias.add(materia1);
        materias.add(materia2);
        materias.add(materia3);

        // Simular que están las materias en el dao
        Map<Integer, Materia> materiasMap = new HashMap<>();
        for (Materia m : materias) {
            materiasMap.put(m.getMateriaId(), m);
        }

        dao.save(materia1);
        Mockito.when(dao.findById(1)).thenReturn(materia1);
        Materia materiaEncontrada = materiaService.getMateriaById(1);
        assertEquals(materia1, materiaEncontrada);

        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setAnio(1);
        materiaDto.setProfesorId(1);
        materiaDto.setCuatrimestre(1);
        materiaDto.setNombre("pepe");

        materia1.setAnio(materiaDto.getAnio());
        materia1.setCuatrimestre(materiaDto.getCuatrimestre());
        materia1.setNombre(materiaDto.getNombre());
        Mockito.when(profesorService.buscarProfesor(1)).thenReturn(pA);
        Mockito.when(profesorService.buscarProfesor(2)).thenReturn(pB);
        materia1.setProfesor(profesorService.buscarProfesor(materiaDto.getProfesorId()));
        assertEquals(materia1.getNombre(), materiaDto.getNombre());

        Mockito.when(dao.getAllMaterias()).thenReturn(materiasMap);


        materiaService.putMateriaById(materia1.getMateriaId(), materiaDto);
    }

    @Test
    void delMateriaById() throws MateriaNotFoundException, ProfesorNotFoundException {
        if (dao.getAllMaterias().isEmpty()) {
            assertThrows(MateriaNotFoundException.class, () -> materiaService.delMateriaById(1));
        }
        Profesor pA = new Profesor("a", "g", "L");
        Profesor pB = new Profesor("b", "g", "L");
        Profesor pC = new Profesor("c", "g", "L");

        Mockito.when(profesorService.buscarProfesor(2)).thenReturn(pB);

        List<Materia> materias = new ArrayList<>();
        Materia materia1 = new Materia("Physics", 1, 1, pA);
        Materia materia2 = new Materia("Math", 2, 1, pB);
        Materia materia3 = new Materia("Chemistry", 3, 1, pC);
        materia1.setMateriaId(1);
        materia2.setMateriaId(2);
        materia2.getCorrelatividades().add("Physics");
        materia3.setMateriaId(3);
        materia3.getCorrelatividades().add("Physics");
        materia3.getCorrelatividades().add("Math");
        materias.add(materia1);
        materias.add(materia2);
        materias.add(materia3);

        // Simular que están las materias en el dao
        Map<Integer, Materia> materiasMap = new HashMap<>();
        for (Materia materia : materias) {
            materiasMap.put(materia.getMateriaId(), materia);
        }
        Mockito.when(dao.getAllMaterias()).thenReturn(materiasMap);

        Materia materiaElegida = materias.get(1);

        materiaService.delMateriaById(2);
        Mockito.verify(asignaturaService).delAsignaturaByMateria(materiaElegida);
        Mockito.verify(alumnoService).delMateriaAlumnoByMateriaDel(materiaElegida);
        Mockito.verify(dao).del(materiaElegida);
        assertThrows(MateriaNotFoundException.class, () -> materiaService.delMateriaById(4));
    }

    @Test
    void getAllMateriasSortedBy() throws MateriaNotFoundException, MateriaServiceException {
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

        Map<Integer, Materia> materiasMap = new HashMap<>();
        for (Materia materia : materias) {
            materiasMap.put(materia.getMateriaId(), materia);
        }
        Mockito.when(dao.getAllMaterias()).thenReturn(materiasMap);

        List<Materia> sortedByNombreAsc = new ArrayList<>(materias);
        sortedByNombreAsc.sort(Comparator.comparing(Materia::getNombre));
        List<Materia> resultNombreAsc = materiaService.getAllMateriasSortedBy("nombre_asc");
        assertEquals(sortedByNombreAsc, resultNombreAsc);

        List<Materia> sortedByNombreDesc = new ArrayList<>(materias);
        sortedByNombreDesc.sort(Comparator.comparing(Materia::getNombre).reversed());
        List<Materia> resultNombreDesc = materiaService.getAllMateriasSortedBy("nombre_desc");
        assertEquals(sortedByNombreDesc, resultNombreDesc);

        List<Materia> sortedByCodAsc = new ArrayList<>(materias);
        sortedByCodAsc.sort(Comparator.comparing(Materia::getMateriaId));
        List<Materia> resultCodAsc = materiaService.getAllMateriasSortedBy("codigo_asc");
        assertEquals(sortedByCodAsc, resultCodAsc);

        List<Materia> sortedByCodDesc = new ArrayList<>(materias);
        sortedByCodDesc.sort(Comparator.comparing(Materia::getMateriaId).reversed());
        List<Materia> resultCodDesc = materiaService.getAllMateriasSortedBy("codigo_desc");
        assertEquals(sortedByCodDesc, resultCodDesc);

        assertThrows(MateriaServiceException.class, () -> materiaService.getAllMateriasSortedBy("orden_erroneo"));
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