package com.appdynamics.boxoffice.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.appdynamics.boxoffice.web.rest.TestUtil;

public class ShowtimeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Showtime.class);
        Showtime showtime1 = new Showtime();
        showtime1.setId(1L);
        Showtime showtime2 = new Showtime();
        showtime2.setId(showtime1.getId());
        assertThat(showtime1).isEqualTo(showtime2);
        showtime2.setId(2L);
        assertThat(showtime1).isNotEqualTo(showtime2);
        showtime1.setId(null);
        assertThat(showtime1).isNotEqualTo(showtime2);
    }
}
