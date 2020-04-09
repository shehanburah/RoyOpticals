package com.theheavenscode.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.theheavenscode.web.rest.TestUtil;

public class PettyCashStockTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PettyCashStock.class);
        PettyCashStock pettyCashStock1 = new PettyCashStock();
        pettyCashStock1.setId("id1");
        PettyCashStock pettyCashStock2 = new PettyCashStock();
        pettyCashStock2.setId(pettyCashStock1.getId());
        assertThat(pettyCashStock1).isEqualTo(pettyCashStock2);
        pettyCashStock2.setId("id2");
        assertThat(pettyCashStock1).isNotEqualTo(pettyCashStock2);
        pettyCashStock1.setId(null);
        assertThat(pettyCashStock1).isNotEqualTo(pettyCashStock2);
    }
}
