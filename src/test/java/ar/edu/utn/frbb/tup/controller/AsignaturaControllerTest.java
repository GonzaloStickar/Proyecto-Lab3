package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class AsignaturaControllerTest {

    @InjectMocks
    AsignaturaController asignaturaController;

    @Mock
    AsignaturaService asignaturaService;

    MockMvc mockMvc;

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(asignaturaController).build();
    }

    @Test
    void putEstadoAsignaturaByIdByAlumnoById() throws Exception {
        AsignaturaDto asignaturaDto = new AsignaturaDto();
        asignaturaDto.setNota(8);

        Asignatura asignatura = new Asignatura(new Materia());
        asignatura.getMateria().setMateriaId(456);

        Alumno alumno = new Alumno();
        alumno.setId(123);
        alumno.setAsignaturas(new ArrayList<>());
        alumno.getAsignaturas().add(asignatura);

        Mockito.when(asignaturaService.putAsignatura(123,456, asignaturaDto)).thenReturn(alumno);

        mockMvc.perform(MockMvcRequestBuilders.put("/alumno/{idAlumno}/asignatura/{idAsignatura}", 123,456)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(asignaturaDto)))
                .andExpect(status().isOk());
    }
}