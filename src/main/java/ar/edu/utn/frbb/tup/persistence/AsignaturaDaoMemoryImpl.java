package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AsignaturaDaoMemoryImpl implements AsignaturaDao {

    private static final List<Asignatura> repositorioAsignaturas = new ArrayList<>();

    static {
        Asignatura a1 = new Asignatura(
                new Materia("Laboratorio 1",1,1,
                        new Profesor("Pedro","Sánchez","Lic. COMPUTACIÓN")));
        Asignatura a2 = new Asignatura(
                new Materia("Laboratorio 2",1,1,
                        new Profesor("Ismael","Montesinos","Lic. COMPUTACIÓN")));
        Asignatura a3 = new Asignatura(
                new Materia("Laboratorio 3",1,1,
                        new Profesor("Luciano","Salotto","Lic. COMPUTACIÓN"))); //The best! :)
        Asignatura a4 = new Asignatura(
                new Materia("Laboratorio 4",1,1,
                        new Profesor("Gregorio","Barbero","Lic. COMPUTACIÓN")));
        Asignatura a5 = new Asignatura(
                new Materia("Programación 1",1,1,
                        new Profesor("Pepe","Sierra","Ing. COMPUTACIÓN")));
        Asignatura a6 = new Asignatura(
                new Materia("Programación 2",1,1,
                        new Profesor("Vicenta","Peiro","Ing. COMPUTACIÓN")));
        Asignatura a7 = new Asignatura(
                new Materia("Programación 3",1,1,
                        new Profesor("Manuela","Rivera","Ing. COMPUTACIÓN")));
        a1.getMateria().setMateriaId(1);
        a2.getMateria().setMateriaId(2);
        a3.getMateria().setMateriaId(3);
        a4.getMateria().setMateriaId(4);
        a5.getMateria().setMateriaId(5);
        a6.getMateria().setMateriaId(6);
        a7.getMateria().setMateriaId(7);
        repositorioAsignaturas.add(a1);
        repositorioAsignaturas.add(a2);
        repositorioAsignaturas.add(a3);
        repositorioAsignaturas.add(a4);
        repositorioAsignaturas.add(a5);
        repositorioAsignaturas.add(a6);
        repositorioAsignaturas.add(a7);
    }

    public Asignatura getAsignaturaById(int idMateria) throws AsignaturaNotFoundException {
        for (Asignatura a: repositorioAsignaturas) {
            if (idMateria == a.getMateria().getMateriaId()) {
                return a;
            }
        }
        throw new AsignaturaNotFoundException("No se encontró la asignatura con id " + idMateria);
    }

    public List<Asignatura> getAllAsignaturas() {
        return repositorioAsignaturas;
    }
}
