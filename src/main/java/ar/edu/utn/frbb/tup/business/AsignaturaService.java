package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;

import java.util.List;

public interface AsignaturaService {
    Asignatura getAsignatura(int materiaId);

    void actualizarAsignatura(Asignatura a);

    List<Asignatura> getSomeAsignaturasRandomFromAsignaturasDao() throws AsignaturaNotFoundException;
}
