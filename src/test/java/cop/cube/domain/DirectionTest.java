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
        assertThat(Direction.UP.rotate().apply(CREATE_MATRIX.get())).isEqualTo(Shape.create(new boolean[][] {
                { true, false, false },
                { false, true, true },
                { true, true, false } }));
    }

    public void shouldRotateRightWhenRightDirection() {
        assertThat(Direction.RIGHT.rotate().apply(CREATE_MATRIX.get())).isEqualTo(Shape.create(new boolean[][] {
                { true, false, true },
                { true, true, false },
                { false, true, false } }));
    }

    public void shouldRotateLeftWhenLeftDirection() {
        assertThat(Direction.LEFT.rotate().apply(CREATE_MATRIX.get())).isEqualTo(Shape.create(new boolean[][] {
                { false, true, false },
                { false, true, true },
                { true, false, true } }));
    }

    public void shouldRotateDownWhenDownDirection() {
        assertThat(Direction.DOWN.rotate().apply(CREATE_MATRIX.get())).isEqualTo(Shape.create(new boolean[][] {
                { false, true, true },
                { true, true, false },
                { false, false, true } }));
    }

    private static final Supplier<Shape> CREATE_MATRIX =
            () -> Shape.create(new boolean[][] {
                    { true, false, false },
                    { false, true, true },
                    { true, true, false } });
}
