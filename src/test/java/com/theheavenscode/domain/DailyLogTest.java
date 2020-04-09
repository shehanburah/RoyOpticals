package com.theheavenscode.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.theheavenscode.web.rest.TestUtil;

public class DailyLogTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DailyLog.class);
        DailyLog dailyLog1 = new DailyLog();
        dailyLog1.setId("id1");
        DailyLog dailyLog2 = new DailyLog();
        dailyLog2.setId(dailyLog1.getId());
        assertThat(dailyLog1).isEqualTo(dailyLog2);
        dailyLog2.setId("id2");
        assertThat(dailyLog1).isNotEqualTo(dailyLog2);
        dailyLog1.setId(null);
        assertThat(dailyLog1).isNotEqualTo(dailyLog2);
    }
}
