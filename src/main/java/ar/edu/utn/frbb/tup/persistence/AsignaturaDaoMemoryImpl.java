package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AsignaturaDaoMemoryImpl implements AsignaturaDao {

    private static final List<Asignatura> repositorioAsignaturas = new ArrayList<>();

    static {
        repositorioAsignaturas.add(new Asignatura(new Materia("Laboratorio 1",1,1, new Profesor("Pedro","Sánchez","Lic. COMPUTACIÓN"))));
        repositorioAsignaturas.add(new Asignatura(new Materia("Programación 1",1,1, new Profesor("Pepe","Sierra","Ing. COMPUTACIÓN"))));
        repositorioAsignaturas.add(new Asignatura(new Materia("Programación 2",1,1, new Profesor("Vicenta","Peiro","Ing. COMPUTACIÓN"))));
        repositorioAsignaturas.add(new Asignatura(new Materia("Laboratorio 2",1,1,new Profesor("Ismael","Montesinos","Lic. COMPUTACIÓN"))));
        repositorioAsignaturas.add(new Asignatura(new Materia("Laboratorio 3",1,1, new Profesor("Luciano","Salotto","Lic. COMPUTACIÓN")))); //The best! :)
        repositorioAsignaturas.add(new Asignatura(new Materia("Programación 3",1,1, new Profesor("Manuela","Rivera","Ing. COMPUTACIÓN"))));
        repositorioAsignaturas.add(new Asignatura(new Materia("Laboratorio 4",1,1, new Profesor("Gregorio","Barbero","Lic. COMPUTACIÓN"))));

        int i=0;
        for (Asignatura asignatura : repositorioAsignaturas) {
            if (i>0 && i%2==0) {
                asignatura.getMateria().agregarCorrelatividad(repositorioAsignaturas.get((i-1)).getMateria().getNombre());
                asignatura.getMateria().agregarCorrelatividad(repositorioAsignaturas.get((i-2)).getMateria().getNombre());
            }
            asignatura.getMateria().setMateriaId(i);
            asignatura.getMateria().getProfesor().setprofesorId(i);
            asignatura.getMateria().getProfesor().agregarMateriaDictada(asignatura.getMateria().getNombre());
            i++;
        }
    }

    public Asignatura getAsignaturaById(int idMateria) throws AsignaturaNotFoundException {
        for (Asignatura a: repositorioAsignaturas) {
            if (idMateria == a.getMateria().getMateriaId()) {
                return a;
            }
        }
        throw new AsignaturaNotFoundException("No se encontró la asignatura con id " + idMateria);
    }

    public void save(Materia materia) {
        Asignatura a = new Asignatura(materia);
        repositorioAsignaturas.add(a);
    }

    public void del(Integer materiaId) throws MateriaNotFoundException {
        for (Asignatura asignatura : repositorioAsignaturas) {
            if (asignatura.getMateria().getMateriaId() == materiaId) {
                repositorioAsignaturas.remove(asignatura);
                break;
            }
        }
    }

    public List<Asignatura> getAllAsignaturas() {
        return repositorioAsignaturas;
    }
}
