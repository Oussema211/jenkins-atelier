package org.example.classtp.service;

import org.example.classtp.entities.Etudiant;
import org.example.classtp.repository.EtudiantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(EtudiantService.class) // On importe le service réel
class EtudiantServiceIntegrationTest {

    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Test
    void testAddEtudiant() {
        // Arrange
        Etudiant e = new Etudiant();
        e.setNomEt("Oussema");
        e.setPrenomEt("Haggui");
        e.setCin(12345678L);

        // Act
        Etudiant saved = etudiantService.addEtudiant(e);

        // Assert
        assertNotNull(saved.getIdEtudiant(), "ID should be generated");
        assertEquals("Oussema", saved.getNomEt());
        assertEquals("Haggui", saved.getPrenomEt());
        assertEquals(12345678L, saved.getCin());

        // Vérifier qu'il est bien dans la base
        Etudiant fromDb = etudiantRepository.findById(saved.getIdEtudiant()).orElse(null);
        assertNotNull(fromDb, "Etudiant should be in DB");
        assertEquals(saved.getNomEt(), fromDb.getNomEt());
    }
}

