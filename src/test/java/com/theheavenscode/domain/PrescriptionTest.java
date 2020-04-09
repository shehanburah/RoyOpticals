package com.theheavenscode.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.theheavenscode.web.rest.TestUtil;

public class PrescriptionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Prescription.class);
        Prescription prescription1 = new Prescription();
        prescription1.setId("id1");
        Prescription prescription2 = new Prescription();
        prescription2.setId(prescription1.getId());
        assertThat(prescription1).isEqualTo(prescription2);
        prescription2.setId("id2");
        assertThat(prescription1).isNotEqualTo(prescription2);
        prescription1.setId(null);
        assertThat(prescription1).isNotEqualTo(prescription2);
    }
}
