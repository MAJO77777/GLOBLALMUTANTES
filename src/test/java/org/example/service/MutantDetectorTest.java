package org.example.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MutantDetectorTest {

    private MutantDetector mutantDetector;

    @BeforeEach
    void setUp() {
        mutantDetector = new MutantDetector();
    }

    @Test
    void testMutantWithHorizontalAndVertical() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void testMutantWithDiagonalSequence() {
        String[] dna = {
                "ATGCGA",
                "CAGTAC",
                "TTCTGT",
                "AGTAGG",
                "CTCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void testMutantWithInverseDiagonal() {
        String[] dna = {
                "ATGCGT",
                "CAGTGG",
                "TTATGT",
                "AGATGG",
                "GCCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void testMutantWithMultipleDiagonals() {
        String[] dna = {
                "AAAAGA",
                "CAGTGC",
                "ATATGT",
                "AGTAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void testMutantHorizontalOnly() {
        String[] dna = {
                "AAAAAA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CGCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void testMutantVerticalOnly() {
        String[] dna = {
                "ATGCGA",
                "ATGTGC",
                "ATATGT",
                "ATGAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void testMutantDiagonalDownLeft() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGCAGG",
                "CTCCTA",
                "TCACTC"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void testHumanWithNoSequences() {
        String[] dna = {
                "ATGC",
                "CAGT",
                "TTAT",
                "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    void testHumanWithOnlyOneSequence() {
        String[] dna = {
                "AAAAGT",
                "CAGTGC",
                "TTCTGT",
                "AGTAGG",
                "CTCCTA",
                "TCACTG"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    void testHumanSingleDiagonalSequenceOnly() {
        String[] dna = {
                "ATGCAA",
                "CAGTGC",
                "TTATGT",
                "AGTAGG",
                "CTCCTA",
                "TCACTG"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    void testInvalidDnaNonSquare() {
        String[] dna = {
                "ATGC",
                "CAG",
                "TTAT"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    void testInvalidDnaCharacters() {
        String[] dna = {
                "ATGX",
                "CAGT",
                "TTAT",
                "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    void testNullDnaArray() {
        assertFalse(mutantDetector.isMutant(null));
    }

    @Test
    void testEmptyDnaArray() {
        assertFalse(mutantDetector.isMutant(new String[]{}));
    }

    @Test
    void testRowWithNullEntry() {
        String[] dna = {
                "ATGC",
                null,
                "TTAT",
                "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    void testRowWithDifferentLength() {
        String[] dna = {
                "ATGCA",
                "CAGTC",
                "TTATG"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    void testLowercaseCharactersReturnFalse() {
        String[] dna = {
                "atgc",
                "cagt",
                "ttat",
                "agac"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }
}
