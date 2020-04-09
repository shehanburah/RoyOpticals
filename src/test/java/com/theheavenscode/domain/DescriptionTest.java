package com.theheavenscode.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.theheavenscode.web.rest.TestUtil;

public class DescriptionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Description.class);
        Description description1 = new Description();
        description1.setId("id1");
        Description description2 = new Description();
        description2.setId(description1.getId());
        assertThat(description1).isEqualTo(description2);
        description2.setId("id2");
        assertThat(description1).isNotEqualTo(description2);
        description1.setId(null);
        assertThat(description1).isNotEqualTo(description2);
    }
}
