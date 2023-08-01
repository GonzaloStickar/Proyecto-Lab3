package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            return ResponseEntity.status(HttpStatus.OK).body(materiaService.crearMateria(materiaDto));
        }
    }

    @PutMapping("/materia/{idMateria}") //PUT: /materia/{idMateria}
    public ResponseEntity<?> putMateriaById(@PathVariable Integer idMateria,
                                  @RequestBody MateriaDto materiaDto) throws MateriaNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(materiaService.putMateriaById(idMateria,materiaDto));
    }

    @GetMapping("/materia")
    public ResponseEntity<?> getAllMateriasByName(@RequestParam("nombre") String nombre) {
        return ResponseEntity.status(HttpStatus.OK).body(materiaService.getAllMateriasByName(nombre));
    }

    @GetMapping("/materia/{idMateria}")
    public ResponseEntity<?> getMateriaById(@PathVariable Integer idMateria) throws MateriaNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(materiaService.getMateriaById(idMateria));
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

    @DeleteMapping("/materia/{idMateria}") //DELETE: /materia/{idMateria}
    public ResponseEntity<?> delMateriaById(@PathVariable Integer idMateria) throws MateriaNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(materiaService.delMateriaById(idMateria));
    }
}
