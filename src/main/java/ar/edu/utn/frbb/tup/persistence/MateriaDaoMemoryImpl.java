package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MateriaDaoMemoryImpl implements MateriaDao {

    private static final Map<Integer, Materia> repositorioMateria = new HashMap<>();

    @Override
    public void save(Materia m) {
        repositorioMateria.put(m.getMateriaId(), m);
    }

    @Override
    public Materia findById(int materiaId) throws MateriaNotFoundException {
        hayMaterias();
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

    public void del(Materia delMateria) throws MateriaNotFoundException {
        hayMaterias();
        for (Materia materia : repositorioMateria.values()) {
            if (materia.getMateriaId() == delMateria.getMateriaId()) {
                repositorioMateria.values().remove(delMateria);
                break;
            }
        }
    }

    public static void hayMaterias() throws MateriaNotFoundException {
        if (repositorioMateria.values().size()==0) throw new MateriaNotFoundException("No hay materias.");
        }
}
