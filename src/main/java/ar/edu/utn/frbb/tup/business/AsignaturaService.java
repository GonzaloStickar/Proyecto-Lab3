package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Asignatura;

import java.util.List;

public interface AsignaturaService {
    Asignatura getAsignatura(int materiaId, int dni);

    void actualizarAsignatura(Asignatura a);

    List<Asignatura> getSomeAsignaturasRandomFromAsignaturasDao();
}
