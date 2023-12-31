package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;

public interface ProfesorService {
    Profesor buscarProfesor(int id) throws ProfesorNotFoundException;

    void actualizarProfesores(String nombreViejo, String nombreNuevo, int profesorId);

    void actualizarProfesoresByNombreMateriaDeleted(String nombreMateriaDeleted);
}
