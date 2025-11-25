package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.DnaRequest;
import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DnaRecordRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void testMutantEndpoint_ReturnOk() throws Exception {
        DnaRequest request = new DnaRequest(new String[]{
                "ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"
        });

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testHumanEndpoint_ReturnForbidden() throws Exception {
        DnaRequest request = new DnaRequest(new String[]{
                "ATGCGA","CAGTGC","TTATTT","AGACGG","GCGTCA","TCACTG"
        });

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testInvalidDna_ReturnBadRequest() throws Exception {
        DnaRequest request = new DnaRequest(new String[]{"ATGX","CAGT"});

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testStatsEndpoint_ReturnOk() throws Exception {
        DnaRecord mutant = new DnaRecord();
        mutant.setDnaHash("hash1");
        mutant.setMutant(true);
        mutant.setCreatedAt(LocalDateTime.now());

        DnaRecord human = new DnaRecord();
        human.setDnaHash("hash2");
        human.setMutant(false);
        human.setCreatedAt(LocalDateTime.now());

        repository.save(mutant);
        repository.save(human);

        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna", is(1)))
                .andExpect(jsonPath("$.count_human_dna", is(1)))
                .andExpect(jsonPath("$.ratio", is(1.0)));
    }
}
