package cop.cube.domain;

import org.testng.annotations.Test;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 11.04.2018
 */
@Test
public class DirectionTest {

    public void shouldNotRotateWhenUpDirection() {
        boolean[][] mask = CREATE_MATRIX.get();
        Direction.UP.apply(mask);
        assertThat(mask).isEqualTo(new boolean[][] { { true, false, false }, { false, true, true }, { true, true, false } });
    }

    public void shouldRotateRightWhenRightDirection() {
        boolean[][] mask = CREATE_MATRIX.get();
        Direction.RIGHT.apply(mask);
        assertThat(mask).isEqualTo(new boolean[][] { { true, false, true }, { true, true, false }, { false, true, false } });
    }

    public void shouldRotateLeftWhenLeftDirection() {
        boolean[][] mask = CREATE_MATRIX.get();
        Direction.LEFT.apply(mask);
        assertThat(mask).isEqualTo(new boolean[][] { { false, true, false }, { false, true, true }, { true, false, true } });
    }

    public void shouldRotateDownWhenDownDirection() {
        boolean[][] mask = CREATE_MATRIX.get();
        Direction.DOWN.apply(mask);
        assertThat(mask).isEqualTo(new boolean[][] { { false, true, true }, { true, true, false }, { false, false, true } });
    }

    private static final Supplier<boolean[][]> CREATE_MATRIX =
            () -> new boolean[][] {
                    { true, false, false },
                    { false, true, true },
                    { true, true, false } };
}
