package org.example.service;

import org.example.entity.DnaRecord;
import org.example.exception.DnaHashCalculationException;
import org.example.repository.DnaRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MutantServiceTest {

    @Mock
    private DnaRecordRepository repository;

    @Mock
    private MutantDetector mutantDetector;

    @InjectMocks
    private MutantService mutantService;

    @Test
    void testAnalyzeDnaReturnsCachedMutant() {
        DnaRecord record = new DnaRecord();
        record.setMutant(true);
        when(repository.findByDnaHash(anyString())).thenReturn(Optional.of(record));

        boolean result = mutantService.analyzeDna(new String[]{"AAAA", "AAAA", "AAAA", "AAAA"});

        assertTrue(result);
        verify(repository, never()).save(any());
        verify(mutantDetector, never()).isMutant(any());
    }

    @Test
    void testAnalyzeDnaReturnsCachedHuman() {
        DnaRecord record = new DnaRecord();
        record.setMutant(false);
        when(repository.findByDnaHash(anyString())).thenReturn(Optional.of(record));

        boolean result = mutantService.analyzeDna(new String[]{"ATGC", "CAGT", "TTAT", "AGAC"});

        assertFalse(result);
        verify(repository, never()).save(any());
        verify(mutantDetector, never()).isMutant(any());
    }

    @Test
    void testAnalyzeDnaSavesNewMutant() {
        when(repository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(any())).thenReturn(true);

        boolean result = mutantService.analyzeDna(new String[]{"AAAA", "TTTT", "CCCC", "GGGG"});

        assertTrue(result);
        ArgumentCaptor<DnaRecord> captor = ArgumentCaptor.forClass(DnaRecord.class);
        verify(repository).save(captor.capture());
        assertTrue(captor.getValue().isMutant());
    }

    @Test
    void testAnalyzeDnaSavesNewHuman() {
        when(repository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(any())).thenReturn(false);

        boolean result = mutantService.analyzeDna(new String[]{"ATGC", "CAGT", "TTAT", "AGAC"});

        assertFalse(result);
        ArgumentCaptor<DnaRecord> captor = ArgumentCaptor.forClass(DnaRecord.class);
        verify(repository).save(captor.capture());
        assertFalse(captor.getValue().isMutant());
    }

    @Test
    void testCalculateDnaHashIsDeterministic() {
        String[] dna = {"ATGC", "CAGT", "TTAT", "AGAC"};
        String hash1 = mutantService.calculateDnaHash(dna);
        String hash2 = mutantService.calculateDnaHash(dna);
        assertEquals(hash1, hash2);
        assertFalse(hash1.isBlank());
    }

    @Test
    void testAnalyzeDnaHandlesHashFailure() {
        MutantService spyService = spy(mutantService);
        doThrow(new DnaHashCalculationException("Error", new RuntimeException()))
                .when(spyService).calculateDnaHash(any());
        assertThrows(DnaHashCalculationException.class, () -> spyService.analyzeDna(new String[]{"ATGC"}));
    }
}
