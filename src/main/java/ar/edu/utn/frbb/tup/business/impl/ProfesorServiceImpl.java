package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.persistence.ProfesorDao;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfesorServiceImpl implements ProfesorService {
    @Autowired
    private ProfesorDao dao;

    @Autowired
    private AsignaturaService asignaturaService;

    public void crearProfesor() {
        for (Asignatura asignatura : asignaturaService.getAllAsignaturas()) {
            dao.save(asignatura.getMateria().getProfesor());
        }
    }

    @Override
    public Profesor buscarProfesor(int id) throws ProfesorNotFoundException {
        boolean crearProfesores = true;
        for (Profesor profesor : dao.getAllProfesores()) {
            if (profesor.getNombre().equals("Pedro")) {
                crearProfesores = false;
                break;
            }
        }
        if (crearProfesores) {
            crearProfesor();
        }
        return dao.get(id);
    }
}
