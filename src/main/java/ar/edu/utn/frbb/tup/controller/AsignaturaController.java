package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class AsignaturaController {

    @Autowired
    private AsignaturaService asignaturaService;

    @PutMapping("/alumno/{idAlumno}/asignatura/{idAsignatura}") //PUT: /alumno/{idAlumno}/asignatura/{idAsignatura}
    public ResponseEntity<Alumno> putEstadoAsignaturaByIdByAlumnoById(@PathVariable int idAlumno,
                                                                      @PathVariable int idAsignatura,
                                                                      @RequestBody AsignaturaDto asignaturaDto) throws AsignaturaNotFoundException, AsignaturaServiceException, EstadoIncorrectoException, AlumnoServiceException, AlumnoNotFoundException, MateriaNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(asignaturaService.putAsignatura(idAlumno,idAsignatura, asignaturaDto));
    }
}
