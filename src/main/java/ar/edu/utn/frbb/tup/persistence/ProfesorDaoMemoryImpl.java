package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfesorDaoMemoryImpl implements ProfesorDao {

    private static final List<Profesor> repositorioProfesores = new ArrayList<>();

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

    static {
        repositorioProfesores.add(new Profesor("Pedro","Sánchez","Lic. COMPUTACIÓN"));
        repositorioProfesores.add(new Profesor("Pepe","Sierra","Ing. COMPUTACIÓN"));
        repositorioProfesores.add(new Profesor("Ismael","Montesinos","Lic. COMPUTACIÓN"));
        repositorioProfesores.add(new Profesor("Vicenta","Peiro","Ing. COMPUTACIÓN"));
        repositorioProfesores.add(new Profesor("Luciano","Salotto","Lic. COMPUTACIÓN")); //The best! :)
        repositorioProfesores.add(new Profesor("Manuela","Rivera","Ing. COMPUTACIÓN"));
        repositorioProfesores.add(new Profesor("Gregorio","Barbero","Lic. COMPUTACIÓN"));
        int i=0;
        for (Profesor profesor : repositorioProfesores) {
            profesor.setprofesorId(i);
            i++;
        }
    }
}
