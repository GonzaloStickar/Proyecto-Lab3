package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    @PostMapping("/alumno")
    public Alumno crearAlumno(@RequestBody AlumnoDto alumnoDto) {
        return alumnoService.crearAlumno(alumnoDto);
    }
    @GetMapping("/alumno/buscar")
    public Alumno buscarAlumno(@RequestParam String apellido) {

       return alumnoService.buscarAlumno(apellido);

    }
}
