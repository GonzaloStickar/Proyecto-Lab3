package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadesNoAprobadasException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.AlumnoDao;
import ar.edu.utn.frbb.tup.persistence.AsignaturaDao;
import ar.edu.utn.frbb.tup.persistence.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class AsignaturaServiceImpl implements AsignaturaService {

    @Autowired
    AlumnoDao alumnoDao;

    @Autowired
    AsignaturaDao dao;

    @Override
    public Asignatura getAsignatura(int materiaId) throws AsignaturaNotFoundException {
        return dao.getAsignaturaById(materiaId);
    }

    @Override
    public Alumno putAsignatura(int idAlumno, int idAsignatura, AsignaturaDto asignaturaDto) throws AlumnoNotFoundException, AsignaturaNotFoundException, AsignaturaServiceException, AlumnoServiceException, EstadoIncorrectoException {
        checkAsignaturaDto(asignaturaDto);
        return aprobarAsignatura(idAlumno, idAsignatura, asignaturaDto.getNota());
    }

    @Override
    public Alumno aprobarAsignatura(int idAlumno, int idAsignatura, int nota) throws AlumnoNotFoundException, AsignaturaNotFoundException, AlumnoServiceException, EstadoIncorrectoException {
        Alumno alumno = alumnoDao.findById(idAlumno);
        Asignatura asignatura = getAsignatura(idAsignatura);

        List<Asignatura> asignaturasAlumnoList = new ArrayList<>(alumno.getAsignaturas());

        if (asignaturasAlumnoList.contains(asignatura)) {
            for (Asignatura a : asignaturasAlumnoList) {
                if (a.equals(asignatura)) {
                    a.aprobarAsignatura(nota); //Esto es lo que tengo que implementar ahora.
                    //a.setNota(nota);
                    break;
                }
            }
            alumno.setAsignaturas(asignaturasAlumnoList);
            return alumno;
        }
        else {
            throw new AlumnoServiceException("El alumno no tiene registro de la asignatura con id: " + idAsignatura, HttpStatus.NOT_FOUND);
        }
    }

    public static void checkAsignaturaDto(AsignaturaDto asignaturaDto) throws AsignaturaServiceException {
        if (asignaturaDto.getNota() <= 0 || asignaturaDto.getNota()>10) {
            throw new AsignaturaServiceException("Falta el cuatrimestre de la materia", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public List<Asignatura> getSomeAsignaturasRandomFromAsignaturasDao() throws AsignaturaNotFoundException {
        List<Asignatura> listaAsignaturas = dao.getAllAsignaturas();
        if (listaAsignaturas.isEmpty()) {
            throw new AsignaturaNotFoundException("No hay Asignaturas"); //Lo cual no va a suceder, ya que las Asignaturas estan "Hardcodeadas".
        }
        ArrayList<Integer> listaNumerosRandom = new ArrayList<>();
        List<Asignatura> listaAsignaturasRandom = new ArrayList<>();
        while (listaNumerosRandom.size()<3) { //Cantidad de Asignaturas a asignar al Alumno (yo le asigno 3 solamente)
            int i = crearNumeroEntreRangoRandom(0,(listaAsignaturas.size())-1);
            if (!listaNumerosRandom.contains(i)) {
                listaNumerosRandom.add(i);
            }
        }
        for (Integer numero : listaNumerosRandom) {
            listaAsignaturasRandom.add(listaAsignaturas.get(numero));
        }
        return listaAsignaturasRandom;
    }

    public static int crearNumeroEntreRangoRandom(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
}
