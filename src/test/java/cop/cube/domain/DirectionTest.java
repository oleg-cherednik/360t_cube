package cop.cube.domain;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 11.04.2018
 */
@Test
public class DirectionTest {

    public void shouldNotRotateWhenUpDirection() {
        boolean[][] mask = createSquare();
        Direction.UP.apply(mask);
        assertThat(mask).isEqualTo(new boolean[][] { { true, false, false }, { false, true, true }, { true, true, false } });
    }

    public void shouldRotateRightWhenRightDirection() {
        boolean[][] mask = createSquare();
        Direction.RIGHT.apply(mask);
        assertThat(mask).isEqualTo(new boolean[][] { { true, false, true }, { true, true, false }, { false, true, false } });
    }

    public void shouldRotateLeftWhenLeftDirection() {
        boolean[][] mask = createSquare();
        Direction.LEFT.apply(mask);
        assertThat(mask).isEqualTo(new boolean[][] { { false, true, false }, { false, true, true }, { true, false, true } });
    }

    public void shouldRotateDownWhenDownDirection() {
        boolean[][] mask = createSquare();
        Direction.DOWN.apply(mask);
        assertThat(mask).isEqualTo(new boolean[][] { { false, true, true }, { true, true, false }, { false, false, true } });
    }

    private static boolean[][] createSquare() {
        return new boolean[][] {
                { true, false, false },
                { false, true, true },
                { true, true, false } };
    }
}
