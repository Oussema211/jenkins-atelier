package org.example.classtp.service;

import org.example.classtp.entities.Etudiant;
import org.example.classtp.repository.EtudiantRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EtudiantServiceTest {

    @Test
    public void testAddEtudiant() {
        EtudiantRepository repo = mock(EtudiantRepository.class);
        EtudiantService service = new EtudiantService();
        
        // inject repo sans toucher au code source
        java.lang.reflect.Field field;
        try {
            field = EtudiantService.class.getDeclaredField("etudiantRepository");
            field.setAccessible(true);
            field.set(service, repo);
        } catch (Exception e) {
            fail("Reflection injection failed");
        }

        Etudiant e = new Etudiant();
        e.setNomEt("Test");
        e.setPrenomEt("User");

        when(repo.save(e)).thenReturn(e);

        Etudiant result = service.addEtudiant(e);

        assertNotNull(result);
        verify(repo, times(1)).save(e);
    }
}

