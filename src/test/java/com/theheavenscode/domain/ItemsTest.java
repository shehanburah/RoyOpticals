package com.theheavenscode.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.theheavenscode.web.rest.TestUtil;

public class ItemsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Items.class);
        Items items1 = new Items();
        items1.setId("id1");
        Items items2 = new Items();
        items2.setId(items1.getId());
        assertThat(items1).isEqualTo(items2);
        items2.setId("id2");
        assertThat(items1).isNotEqualTo(items2);
        items1.setId(null);
        assertThat(items1).isNotEqualTo(items2);
    }
}
