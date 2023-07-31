package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MateriaDaoMemoryImpl implements MateriaDao {

    private static final Map<Integer, Materia> repositorioMateria = new HashMap<>();
    @Override
    public void save(Materia materia) {
        Random random = new Random();
        materia.setMateriaId(random.nextInt());
        boolean crear=true;
        for (Materia materiaId : repositorioMateria.values()) {
            if (materia.getMateriaId() == materiaId.getMateriaId()) {
                crear = false;
                break;
            }
        }
        if (crear) {
            repositorioMateria.put(materia.getMateriaId(), materia);
        }
    }

    @Override
    public Materia findById(int idMateria) throws MateriaNotFoundException {
        for (Materia m: repositorioMateria.values()) {
            if (idMateria == m.getMateriaId()) {
                return m;
            }
        }
        throw new MateriaNotFoundException("No se encontró la materia con id " + idMateria);
    }

    public Map<Integer, Materia> getAllMaterias() {
        return repositorioMateria;
    }
}
