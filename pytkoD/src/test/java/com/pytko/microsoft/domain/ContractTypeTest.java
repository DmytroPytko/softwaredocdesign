package com.pytko.microsoft.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.pytko.microsoft.web.rest.TestUtil;

public class ContractTypeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContractType.class);
        ContractType contractType1 = new ContractType();
        contractType1.setId(1L);
        ContractType contractType2 = new ContractType();
        contractType2.setId(contractType1.getId());
        assertThat(contractType1).isEqualTo(contractType2);
        contractType2.setId(2L);
        assertThat(contractType1).isNotEqualTo(contractType2);
        contractType1.setId(null);
        assertThat(contractType1).isNotEqualTo(contractType2);
    }
}
