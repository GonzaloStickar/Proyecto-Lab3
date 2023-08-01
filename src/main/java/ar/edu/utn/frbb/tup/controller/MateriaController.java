package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaServiceException;
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
    public ResponseEntity<?> crearMateria(@RequestBody MateriaDto materiaDto) throws MateriaServiceException {
        return ResponseEntity.status(HttpStatus.OK).body(materiaService.crearMateria(materiaDto));
    }

    @PutMapping("/materia/{idMateria}") //PUT: /materia/{idMateria}
    public ResponseEntity<?> putMateriaById(@PathVariable Integer idMateria,
                                  @RequestBody MateriaDto materiaDto) throws MateriaNotFoundException, MateriaServiceException {
        return ResponseEntity.status(HttpStatus.OK).body(materiaService.putMateriaById(idMateria,materiaDto));
    }

    @GetMapping("/materia")
    public ResponseEntity<?> getAllMateriasByName(@RequestParam("nombre") String nombre) throws MateriaNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(materiaService.getAllMateriasByName(nombre));
    }

    @GetMapping("/materia/{idMateria}")
    public ResponseEntity<?> getMateriaById(@PathVariable Integer idMateria) throws MateriaNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(materiaService.getMateriaById(idMateria));
    }

    @GetMapping("/materias")
    public ResponseEntity<?> getMateriaByOrder(@RequestParam("order") String order) throws MateriaNotFoundException, MateriaServiceException {
        return ResponseEntity.ok().body(materiaService.getAllMateriasSortedBy(order));
    }

    @DeleteMapping("/materia/{idMateria}") //DELETE: /materia/{idMateria}
    public ResponseEntity<?> delMateriaById(@PathVariable Integer idMateria) throws MateriaNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(materiaService.delMateriaById(idMateria));
    }
}
