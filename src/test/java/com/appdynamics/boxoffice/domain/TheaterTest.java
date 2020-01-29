package com.appdynamics.boxoffice.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.appdynamics.boxoffice.web.rest.TestUtil;

public class TheaterTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Theater.class);
        Theater theater1 = new Theater();
        theater1.setId(1L);
        Theater theater2 = new Theater();
        theater2.setId(theater1.getId());
        assertThat(theater1).isEqualTo(theater2);
        theater2.setId(2L);
        assertThat(theater1).isNotEqualTo(theater2);
        theater1.setId(null);
        assertThat(theater1).isNotEqualTo(theater2);
    }
}
