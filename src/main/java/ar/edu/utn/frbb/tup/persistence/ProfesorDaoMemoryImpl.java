package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfesorDaoMemoryImpl implements ProfesorDao {

    private static List<Profesor> repositorioProfesores = new ArrayList<>();

    @Override
    public void save(Profesor profesor) {
        repositorioProfesores.add(profesor);
    }

    @Override
    public Profesor get(int id) throws ProfesorNotFoundException {
        for (Profesor profesor : repositorioProfesores) {
            if (profesor.getprofesorId()==id) {
                return profesor;
            }
        }
        throw new ProfesorNotFoundException("No se encontro al profesor con un id: "+id);
    }

    public List<Profesor> getAllProfesores() {
        return repositorioProfesores;
    }
}
