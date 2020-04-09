package com.theheavenscode.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.theheavenscode.web.rest.TestUtil;

public class SolderingJobInvoiceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SolderingJobInvoice.class);
        SolderingJobInvoice solderingJobInvoice1 = new SolderingJobInvoice();
        solderingJobInvoice1.setId("id1");
        SolderingJobInvoice solderingJobInvoice2 = new SolderingJobInvoice();
        solderingJobInvoice2.setId(solderingJobInvoice1.getId());
        assertThat(solderingJobInvoice1).isEqualTo(solderingJobInvoice2);
        solderingJobInvoice2.setId("id2");
        assertThat(solderingJobInvoice1).isNotEqualTo(solderingJobInvoice2);
        solderingJobInvoice1.setId(null);
        assertThat(solderingJobInvoice1).isNotEqualTo(solderingJobInvoice2);
    }
}
