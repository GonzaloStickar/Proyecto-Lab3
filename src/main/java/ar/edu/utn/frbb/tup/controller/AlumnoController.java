package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoServiceException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    @PostMapping("/alumno") //POST: /alumno
    public ResponseEntity<?> crearAlumno(@RequestBody AlumnoDto alumnoDto) throws AlumnoServiceException, AsignaturaNotFoundException, AlumnoNotFoundException, AsignaturaServiceException {
        return ResponseEntity.status(HttpStatus.CREATED).body(alumnoService.crearAlumno(alumnoDto));
    }

    @PutMapping("/alumno/{idAlumno}") //PUT: /alumno/{idAlumno}
    public ResponseEntity<?> putAlumnoById(@PathVariable Integer idAlumno,
                                            @RequestBody AlumnoDto alumnoDto) throws AlumnoNotFoundException, AlumnoServiceException {
        return ResponseEntity.status(HttpStatus.OK).body(alumnoService.putAlumnoById(idAlumno,alumnoDto));
    }

    @DeleteMapping("/alumno/{idAlumno}") //DELETE: /alumno/{idAlumno}
    public ResponseEntity<?> delAlumnoById(@PathVariable Integer idAlumno) throws AlumnoNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(alumnoService.delAlumnoById(idAlumno));
    }

    @GetMapping("/alumno/{idAlumno}")
    public ResponseEntity<?> getAlumnoById(@PathVariable Integer idAlumno) throws AlumnoNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(alumnoService.getAlumnoById(idAlumno));
    }
}
