package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
    public Materia findById(int materiaId) throws MateriaNotFoundException {
        for (Materia m: repositorioMateria.values()) {
            if (materiaId == m.getMateriaId()) {
                return m;
            }
        }
        throw new MateriaNotFoundException("No se encontró la materia con id " + materiaId);
    }

    public Map<Integer, Materia> getAllMaterias() {
        return repositorioMateria;
    }

    public void del(Materia delMateria) {
        for (Materia materia : repositorioMateria.values()) {
            if (materia.getMateriaId() == delMateria.getMateriaId()) {
                repositorioMateria.values().remove(delMateria);
                break;
            }
        }
    }
}
