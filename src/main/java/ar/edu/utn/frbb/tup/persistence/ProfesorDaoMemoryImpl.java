package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfesorDaoMemoryImpl implements ProfesorDao{

    private static List<Profesor> repositorioProfesores = new ArrayList<>();

    static {
        repositorioProfesores.add(new Profesor("Pedro","Sánchez","Lic. COMPUTACIÓN",1));
        repositorioProfesores.add(new Profesor("Ismael","Montesinos","Lic. COMPUTACIÓN",2));
        repositorioProfesores.add(new Profesor("Luciano","Salotto","Lic. COMPUTACIÓN",3));
        repositorioProfesores.add(new Profesor("Gregorio","Barbero","Lic. COMPUTACIÓN",4));
        repositorioProfesores.add(new Profesor("Pepe","Sierra","Ing. COMPUTACIÓN",5));
        repositorioProfesores.add(new Profesor("Vicenta","Peiro","Ing. COMPUTACIÓN",6));
        repositorioProfesores.add(new Profesor("Manuela","Rivera","Ing. COMPUTACIÓN",7));
    }

    @Override
    public Profesor get(int id) throws ProfesorNotFoundException {
        for (Profesor profesor : repositorioProfesores) {
            if (profesor.getId()==id) {
                return profesor;
            }
        }
        throw new ProfesorNotFoundException("No se encontro al profesor con un id: "+id);
    }

    public List<Profesor> getAllProfesores() {
        return repositorioProfesores;
    }
}
