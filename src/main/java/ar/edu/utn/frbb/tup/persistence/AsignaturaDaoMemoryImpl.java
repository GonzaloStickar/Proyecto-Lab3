package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AsignaturaDaoMemoryImpl implements AsignaturaDao {

    private static final List<Asignatura> repositorioAsignaturas = new ArrayList<>();

    static {
        repositorioAsignaturas.add(new Asignatura(
                new Materia("Laboratorio 1",1,1,
                        new Profesor("Pedro","Sánchez","Lic. COMPUTACIÓN"))));
        repositorioAsignaturas.add(new Asignatura(
                new Materia("Laboratorio 2",1,1,
                        new Profesor("Ismael","Montesinos","Lic. COMPUTACIÓN"))));
        repositorioAsignaturas.add(new Asignatura(
                new Materia("Laboratorio 3",1,1,
                        new Profesor("Luciano","Salotto","Lic. COMPUTACIÓN")))); //The best! :)
        repositorioAsignaturas.add(new Asignatura(
                new Materia("Laboratorio 4",1,1,
                        new Profesor("Gregorio","Barbero","Lic. COMPUTACIÓN"))));
        repositorioAsignaturas.add(new Asignatura(
                new Materia("Programación 1",1,1,
                        new Profesor("Pepe","Sierra","Ing. COMPUTACIÓN"))));
        repositorioAsignaturas.add(new Asignatura(
                new Materia("Programación 2",1,1,
                        new Profesor("Vicenta","Peiro","Ing. COMPUTACIÓN"))));
        repositorioAsignaturas.add(new Asignatura(
                new Materia("Programación 3",1,1,
                        new Profesor("Manuela","Rivera","Ing. COMPUTACIÓN"))));
    }

    public List<Asignatura> getAllAsignaturas() {
        return repositorioAsignaturas;
    }
}
