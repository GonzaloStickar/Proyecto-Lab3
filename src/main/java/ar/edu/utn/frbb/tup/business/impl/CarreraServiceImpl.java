package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.business.CarreraService;
import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.persistence.CarreraDao;
import ar.edu.utn.frbb.tup.persistence.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CarreraServiceImpl implements CarreraService {

    @Autowired
    private CarreraDao dao;

    @Autowired
    private AsignaturaService asignaturaService;

    @Autowired
    private ProfesorService profesorService;

    @Override
    public Carrera crearCarrera(CarreraDto carreraDto) throws CarreraServiceException, AsignaturaServiceException, AsignaturaNotFoundException {
        Carrera c = new Carrera();
        checkCarreraDto(carreraDto);
        if (carreraDto.getCodigoCarrera() <= 0) throw new CarreraServiceException("Falta el código de la carrera", HttpStatus.UNPROCESSABLE_ENTITY);
        if (asignaturaService.getAllAsignaturas().isEmpty()) throw new AsignaturaServiceException("No hay asignaturas", HttpStatus.NOT_FOUND);
        c.setNombre(carreraDto.getNombre());
        c.setCantidadCuatrimestres((carreraDto.getCantidadAnios()*12)/4);
        c.setCodigoCarrera(carreraDto.getCodigoCarrera());

        List<Asignatura> asignaturas = new ArrayList<>(asignaturaService.getSomeAsignaturaRandomFromAsignaturasDao());
        List<Materia> materias = asignaturas.stream().map(Asignatura::getMateria).collect(Collectors.toList());

        c.setMateriasList(materias);
        for (Carrera carrera : dao.getAllCarreras().values()) if (carrera.getCodigoCarrera() == c.getCodigoCarrera()) throw new CarreraServiceException("Ya existe una Carrera con el mismo código.", HttpStatus.CONFLICT);
        dao.save(c);
        return c;
    }

    public static int crearNumeroEntreRangoRandom(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    @Override
    public Carrera getCarreraById(int idCarrera) throws CarreraNotFoundException {
        return dao.findById(idCarrera);
    }

    @Override
    public Carrera putCarreraById(int idCarrera, CarreraDto carreraDto) throws CarreraNotFoundException, CarreraServiceException {
        checkCarreraDto(carreraDto);
        Carrera c = getCarreraById(idCarrera);
        checkCarreraDtoPut(c, carreraDto);
        c.setNombre(carreraDto.getNombre());
        c.setCantidadCuatrimestres((carreraDto.getCantidadAnios()*12)/4);
        return c;
    }

    public Carrera delCarreraById(int idCarrera) throws CarreraNotFoundException {
        if (dao.getAllCarreras().values().size()==0) {
            throw new CarreraNotFoundException("No hay carreras.");
        }
        else {
            for (Carrera carrera : getAllCarreras()) {
                if (carrera.getCodigoCarrera()==idCarrera) {
                    dao.del(carrera);
                    return carrera;
                }
            }
            throw new CarreraNotFoundException("No se encontro la carrera con el id: "+idCarrera);
        }
    }

    public List<Carrera> getAllCarreras() throws CarreraNotFoundException {
        List<Carrera> carreras = new ArrayList<>(dao.getAllCarreras().values());
        if (carreras.size()==0) throw new CarreraNotFoundException("No hay carreras.");else return carreras;
    }

    public static void checkCarreraDtoPut(Carrera carrera, CarreraDto carreraDto) throws CarreraServiceException {
        if (carrera.getNombre().equals(carreraDto.getNombre()) && (carrera.getCantidadCuatrimestres()/3)==carreraDto.getCantidadAnios()) throw new CarreraServiceException("Esta carrera ya fue actualizada con esos datos", HttpStatus.CONFLICT);
    }

    public void checkCarreraDto(CarreraDto carreraDto) throws CarreraServiceException {
        if (!carreraDto.getNombre().matches(".*[a-zA-Z]+.*")) throw new CarreraServiceException("Falta el nombre de la carrera",HttpStatus.UNPROCESSABLE_ENTITY);
        else if (carreraDto.getCantidadAnios() <= 0) throw new CarreraServiceException("Falta el año de la carrera",HttpStatus.UNPROCESSABLE_ENTITY);
        //Código = 422 - La petición estaba bien formada pero no se pudo seguir debido a errores de semántica.
    }

    public void actualizarProfesoresDeLasCarreras() throws ProfesorNotFoundException {
        for (Carrera carrera : dao.getAllCarreras().values()) {
            //Actualizar los profesores de la carrera
            for (Materia materia : carrera.getMateriasList()) {
                materia.setProfesor(profesorService.buscarProfesor(materia.getProfesor().getprofesorId()));
            }
        }
    }

    public void actualizarNombreMateriaEnMateriaListDeCarreraYSusCorrelativas(String nombreMateriaViejo, String nombreMateriaNuevo) {
        for (Carrera carrera : dao.getAllCarreras().values()) {
            for (Materia materia : carrera.getMateriasList()) {
                //Nombre de la materia en la lista de materias de la carrera
                if (materia.getNombre().equals(nombreMateriaViejo)) {
                    materia.setNombre(nombreMateriaNuevo);
                }
                //Correlativas
                if (!materia.getCorrelatividades().isEmpty()) {
                    if (materia.getCorrelatividades().contains(nombreMateriaViejo)) {
                        materia.getCorrelatividades().remove(nombreMateriaViejo);
                        materia.getCorrelatividades().add(nombreMateriaNuevo);
                    }
                }
            }
        }
    }

    public void delMateriaEnCarreraByMateria(Materia materia) {
        for (Carrera carrera : dao.getAllCarreras().values()) {
            carrera.getMateriasList().remove(materia);
        }
    }

    public void actualizarCorrelativasCarreraByNameMateriaDeleted(String nombreMateriaDeleted) {
        for (Carrera carrera : dao.getAllCarreras().values()) {
            for (Materia materia : carrera.getMateriasList()) {
                materia.getCorrelatividades().remove(nombreMateriaDeleted);
            }
        }
    }
}
