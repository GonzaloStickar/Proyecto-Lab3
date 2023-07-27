package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/")
public class MateriaController {

    @Autowired
    private MateriaService materiaService;

//    @PostMapping("/materia")
//    public Materia crearMateria(@RequestBody MateriaDto materiaDto) {
//        materias.add(materiaService.crearMateria(materiaDto));
//        return materiaService.crearMateria(materiaDto);
//    }

    @PostMapping("/materia") //POST: /materia
    public ResponseEntity<?> crearMateria(@RequestBody MateriaDto materiaDto) {
        if (materiaDto.getNombre() == null || materiaDto.getNombre().isEmpty()) {
            return ResponseEntity.badRequest().body("Falta el nombre de la materia");
        }
        else if (materiaDto.getAnio() == 0) {
            return ResponseEntity.badRequest().body("Falta el año de la materia");
        }
        else if (materiaDto.getCuatrimestre() == 0) {
            return ResponseEntity.badRequest().body("Falta el cuatrimestre de la materia");
        }
        else if (materiaDto.getProfesorId()==0) {
            return ResponseEntity.badRequest().body("Falta el ID del profesor de la materia");
        }
        else {
            Materia materiaCreada = materiaService.crearMateria(materiaDto);
            return ResponseEntity.ok(materiaCreada);
        }
    }

    @PutMapping("/materia/{idMateria}") //PUT: /materia/{idMateria}
    public Materia putMateriaById(@PathVariable Integer idMateria,
                                  @RequestBody MateriaDto materiaDto) throws MateriaNotFoundException {
        return materiaService.putMateriaById(idMateria,materiaDto);
    }

    @GetMapping("/materia")
    public List<Materia> obtenerTodasLasMaterias(@RequestParam("nombre") String nombre) {
        return materiaService.getAllMaterias();
    }

    @GetMapping("/materia/{idMateria}")
    public Materia getMateriaById(@PathVariable Integer idMateria) throws MateriaNotFoundException {
        return materiaService.getMateriaById(idMateria);
    }
}
