package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.CarreraService;
import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class CarreraControllerTest {

    @InjectMocks
    CarreraController carreraController;

    @Mock
    CarreraService carreraService;

    MockMvc mockMvc;

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(carreraController).build();
    }

    @Test
    void crearCarrera() throws Exception {
        Mockito.when(carreraService.crearCarrera(any(CarreraDto.class))).thenReturn(new Carrera());
        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setCantidadAnios(1);
        carreraDto.setNombre("pepe");

        mockMvc.perform(MockMvcRequestBuilders.post("/carrera")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(carreraDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void putCarreraById() throws Exception {
        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setCantidadAnios(1);
        carreraDto.setNombre("pepe");

        Carrera carrera = new Carrera();
        carrera.setCodigoCarrera(123);
        carrera.setNombre(carreraDto.getNombre());

        Mockito.when(carreraService.putCarreraById(123,carreraDto)).thenReturn(carrera);

        mockMvc.perform(MockMvcRequestBuilders.put("/carrera/{idCarrera}", 123)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(carreraDto)))
                .andExpect(status().isOk());
    }

    @Test
    void delaCarreraById() throws Exception {
        Carrera carrera = new Carrera("carrera", 1);
        carrera.setCodigoCarrera(123);
        Mockito.when(carreraService.delCarreraById(123)).thenReturn(carrera);
        mockMvc.perform(MockMvcRequestBuilders.delete("/carrera/{idCarrera}", 123)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getCarreraById() throws Exception {
        Carrera carrera = new Carrera("carrera", 1);
        carrera.setCodigoCarrera(123);
        Mockito.when(carreraService.getCarreraById(123)).thenReturn(carrera);
        mockMvc.perform(MockMvcRequestBuilders.get("/carrera/{idCarrera}", 123)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}