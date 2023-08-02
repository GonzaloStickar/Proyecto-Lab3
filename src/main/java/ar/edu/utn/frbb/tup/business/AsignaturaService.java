package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Asignatura;

public interface AsignaturaService {
    Asignatura getAsignatura(int materiaId, int dni);

    void actualizarAsignatura(Asignatura a);
}
