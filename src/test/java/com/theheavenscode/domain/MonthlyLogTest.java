package com.theheavenscode.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.theheavenscode.web.rest.TestUtil;

public class MonthlyLogTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MonthlyLog.class);
        MonthlyLog monthlyLog1 = new MonthlyLog();
        monthlyLog1.setId("id1");
        MonthlyLog monthlyLog2 = new MonthlyLog();
        monthlyLog2.setId(monthlyLog1.getId());
        assertThat(monthlyLog1).isEqualTo(monthlyLog2);
        monthlyLog2.setId("id2");
        assertThat(monthlyLog1).isNotEqualTo(monthlyLog2);
        monthlyLog1.setId(null);
        assertThat(monthlyLog1).isNotEqualTo(monthlyLog2);
    }
}
