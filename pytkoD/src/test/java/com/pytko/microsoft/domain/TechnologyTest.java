package com.pytko.microsoft.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.pytko.microsoft.web.rest.TestUtil;

public class TechnologyTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Technology.class);
        Technology technology1 = new Technology();
        technology1.setId(1L);
        Technology technology2 = new Technology();
        technology2.setId(technology1.getId());
        assertThat(technology1).isEqualTo(technology2);
        technology2.setId(2L);
        assertThat(technology1).isNotEqualTo(technology2);
        technology1.setId(null);
        assertThat(technology1).isNotEqualTo(technology2);
    }
}
