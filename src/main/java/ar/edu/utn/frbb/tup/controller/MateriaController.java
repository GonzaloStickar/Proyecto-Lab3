package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/")
public class MateriaController {

    @Autowired
    private MateriaService materiaService;

    @PostMapping("/materia") //POST: /materia
    public ResponseEntity<?> crearMateria(@RequestBody MateriaDto materiaDto) {

        if (!materiaDto.getNombre().matches(".*[a-zA-Z]+.*")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falta el nombre de la materia");
        }

        else if (materiaDto.getAnio() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falta el año de la materia");
        }

        else if (materiaDto.getCuatrimestre() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falta el cuatrimestre de la materia");
        }
        else if (materiaDto.getProfesorId()==0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falta el ID del profesor de la materia");
        }
        else {
            materiaService.crearMateria(materiaDto);
            return ResponseEntity.status(HttpStatus.OK).body(materiaDto);
        }
    }

    @PutMapping("/materia/{idMateria}") //PUT: /materia/{idMateria}
    public Materia putMateriaById(@PathVariable Integer idMateria,
                                  @RequestBody MateriaDto materiaDto) throws MateriaNotFoundException {
        return materiaService.putMateriaById(idMateria,materiaDto);
    }

    @GetMapping("/materia")
    public List<Materia> getAllMateriasByName(@RequestParam("nombre") String nombre) {
        return materiaService.getAllMateriasByName(nombre);
    }

    @GetMapping("/materia/{idMateria}")
    public Materia getMateriaById(@PathVariable Integer idMateria) throws MateriaNotFoundException {
        return materiaService.getMateriaById(idMateria);
    }

    @GetMapping("/materias")
    public ResponseEntity<?> getMateriaByOrder(@RequestParam("order") String order) {
        return switch (order) {
            case "nombre_asc" -> ResponseEntity.ok(materiaService.getAllMateriasSortedByNameAsc());
            case "nombre_desc" -> ResponseEntity.ok(materiaService.getAllMateriasSortedByNameDesc());
            case "codigo_asc" -> ResponseEntity.ok(materiaService.getAllMateriasSortedByCodAsc());
            case "codigo_desc" -> ResponseEntity.ok(materiaService.getAllMateriasSortedByCodDesc());
            default -> ResponseEntity.badRequest().body("Tiene que especificar un orden.");
        };
    }
}
