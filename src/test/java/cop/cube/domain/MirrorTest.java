package cop.cube.domain;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 13.04.2018
 */
@Test
public class MirrorTest {

    public void shouldNotMirrorWhenOffMirror() {
        boolean[][] mask = createSquare();
        Mirror.OFF.apply(mask);
        assertThat(mask).isEqualTo(new boolean[][] { { true, false, false }, { false, true, true }, { true, true, false } });
    }

    public void shouldHorizontalMirrorWhenHorizontalMirror() {
        boolean[][] mask = createSquare();
        Mirror.ON.apply(mask);
        assertThat(mask).isEqualTo(new boolean[][] { { true, true, false }, { false, true, true }, { true, false, false } });
    }

    private static boolean[][] createSquare() {
        return new boolean[][] {
                { true, false, false },
                { false, true, true },
                { true, true, false } };
    }
}
