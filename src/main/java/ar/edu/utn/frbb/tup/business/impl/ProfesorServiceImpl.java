package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.persistence.ProfesorDao;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfesorServiceImpl implements ProfesorService {

    @Autowired
    private ProfesorDao dao;

    @Override
    public Profesor buscarProfesor(int id) throws ProfesorNotFoundException {
        return dao.get(id);
    }

    public void actualizarProfesores(String nombreViejo, String nombreNuevo, int profesorId) {
        for (Profesor profesor : dao.getAllProfesores()) {
            profesor.getMateriasDictadas().remove(nombreViejo);
            if (profesor.getprofesorId()==profesorId) {
                if (!profesor.getMateriasDictadas().contains(nombreNuevo)) {
                    profesor.getMateriasDictadas().add(nombreNuevo);
                }
            }
        }
    }

    public void actualizarProfesoresByNombreMateriaDeleted(String nombreMateriaDeleted) {
        for (Profesor profesor : dao.getAllProfesores()) {
            profesor.getMateriasDictadas().remove(nombreMateriaDeleted);
        }
    }
}
