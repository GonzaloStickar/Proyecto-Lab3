package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class AsignaturaController {

    @Autowired
    private AsignaturaService asignaturaService;
//    @PutMapping("/alumno/{idAlumno}/asignatura/{idAsignatura}") //PUT: /alumno/{idAlumno}/asignatura/{idAsignatura}
//    public ResponseEntity<?> putEstadoAsignaturaByIdByAlumnoById(@PathVariable int idAlumno, @PathVariable int idAsignatura) throws AsignaturaNotFoundException {
//        return ResponseEntity.status(HttpStatus.OK).body(asignaturaService.putAsignatura(idAlumno,idAsignatura));
//    }
//
//    @PostMapping("/asignaturas")
//    public ResponseEntity<?> getAllAsignaturas(@RequestBody MateriaDto materiaDto) throws AsignaturaNotFoundException {
//        return ResponseEntity.status(HttpStatus.CREATED).body(asignaturaService.getAsignaturaRandom());
//    }
}
