package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import ar.edu.utn.frbb.tup.persistence.MateriaDaoMemoryImpl;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MateriaServiceImpl implements MateriaService {
    @Autowired
    private MateriaDao dao;

    @Autowired
    private ProfesorService profesorService;

    @Override
    public Materia crearMateria(MateriaDto materia) throws IllegalArgumentException {
        Materia m = new Materia();
        m.setNombre(materia.getNombre());
        m.setAnio(materia.getAnio());
        m.setCuatrimestre(materia.getCuatrimestre());
        m.setProfesor(profesorService.buscarProfesor(materia.getProfesorId()));
        dao.save(m);
        return m;
    }

    @Override
    public List<Materia> getAllMaterias() {
        Materia m = new Materia("labo 1", 2, 1, new Profesor("Lucho", "Salotto", "Lic"));
        Materia m1 = new Materia("labo 2", 2, 1, new Profesor("Juan", "Perez", "Lic"));
        List<Materia> materiasList = new ArrayList<>();
        Map<Integer, Materia> materias = new MateriaDaoMemoryImpl().getAllMaterias();
        for (Materia materia : materias.values()) {
            if (!materiasList.contains(materia)) {
                materiasList.add(materia);
            }
        }
        if (!materiasList.contains(m) && !materiasList.contains(m1)) {
            materiasList.add(m);
            materiasList.add(m1);
        }
        return materiasList;
    }


    @Override
    public Materia getMateriaById(int idMateria) throws MateriaNotFoundException {
        return dao.findById(idMateria);
    }

    @Override
    public Materia putMateriaById(int idMateria, MateriaDto materia) throws MateriaNotFoundException {
        Materia m = getMateriaById(idMateria);
        m.setAnio(materia.getAnio());
        m.setCuatrimestre(materia.getCuatrimestre());
        m.setNombre(materia.getNombre());
        m.setProfesor(profesorService.buscarProfesor(materia.getProfesorId()));
        return m;
    }

    public Materia delMateriaById(Integer materiaId) throws MateriaNotFoundException {
        List<Materia> materiaList = getAllMaterias();
        for (Materia materia : materiaList) {
            if (materia.getMateriaId()==materiaId) {
                dao.del(materia);
                return materia;
            }
        }
        throw new MateriaNotFoundException("No se encontro la materia con el id: "+materiaId);
    }

    public List<Materia> getAllMateriasSortedByNameAsc() {
        List<Materia> materias = getAllMaterias();
        materias.sort(Comparator.comparing(Materia::getNombre));
        return materias;
    }
    public List<Materia> getAllMateriasSortedByNameDesc() {
        List<Materia> materias = getAllMaterias();
        materias.sort(Comparator.comparing(Materia::getNombre).reversed());
        return materias;
    }
    public List<Materia> getAllMateriasSortedByCodAsc() {
        List<Materia> materias = getAllMaterias();
        materias.sort(Comparator.comparing(Materia::getMateriaId));
        return materias;
    }
    public List<Materia> getAllMateriasSortedByCodDesc() {
        List<Materia> materias = getAllMaterias();
        materias.sort(Comparator.comparing(Materia::getMateriaId).reversed());
        return materias;
    }

    public List<Materia> getAllMateriasByName(String nombre) {
        List<Materia> materias = getAllMaterias();
        List<Materia> materiasEncontradas = new ArrayList<>();
        for (Materia materia : materias) {
            if (materia.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                materiasEncontradas.add(materia);
            }
        }
        return materiasEncontradas;
    }
}
