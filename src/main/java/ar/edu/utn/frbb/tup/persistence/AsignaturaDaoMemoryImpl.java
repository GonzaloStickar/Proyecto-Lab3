package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AsignaturaDaoMemoryImpl implements AsignaturaDao {

    private static final List<Asignatura> repositorioAsignaturas = new ArrayList<>();

    public Asignatura getAsignaturaByName(String nombreMateria) throws AsignaturaNotFoundException {
        for (Asignatura a: repositorioAsignaturas) {
            if (nombreMateria.equals(a.getMateria().getNombre()))
                return a;
        }
        throw new AsignaturaNotFoundException("No se encontró la asignatura con el nombre " + nombreMateria);
    }

    public void save(Asignatura asignatura) {
        repositorioAsignaturas.add(asignatura);
    }

    public void del(Materia delMateria) {
        repositorioAsignaturas.removeIf(asignatura -> asignatura.getMateria().equals(delMateria));
    }

    public List<Asignatura> getAllAsignaturas() {
        return repositorioAsignaturas;
    }
}
