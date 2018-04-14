package cop.cube.domain;

import org.testng.annotations.Test;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 14.04.2018
 */
@Test
public class ShapeTest {

    public static final boolean[][] EMPTY_ARRAY = new boolean[0][0];

    public void shouldRetrieveNullObjectWhenMaskNotSet() {
        assertThat(Shape.create('A', null)).isSameAs(Shape.NULL);
        assertThat(Shape.create('A', EMPTY_ARRAY)).isSameAs(Shape.NULL);
        assertThat(Shape.create('A', Direction.UP, Mirror.OFF, null)).isSameAs(Shape.NULL);
        assertThat(Shape.create('A', Direction.UP, Mirror.OFF, EMPTY_ARRAY)).isSameAs(Shape.NULL);
    }

    public void shouldRetrieveCreatedShapeWithDefaultDirectionAndMirrorWhenMaskSet() {
        Shape shape = Shape.create('A', CREATE_MATRIX.get());
        assertThat(shape).isNotNull();
        assertThat(shape).isNotSameAs(Shape.NULL);
        assertThat(shape.toString()).isEqualTo("A-" + Direction.UP + '-' + Mirror.OFF);
    }

    public void shouldRetrieveFullCopyOfInternalMaskWhenGetMask() {
        Shape shape = Shape.create('A', CREATE_MATRIX.get());
        boolean[][] mask = shape.getMask();
        assertThat(mask).isNotNull();
        assertThat(mask).isEqualTo(new boolean[][] { { true, false, false }, { false, true, true }, { true, true, false } });

        mask[0][0] = !mask[0][0];
        assertThat(mask).isEqualTo(new boolean[][] { { false, false, false }, { false, true, true }, { true, true, false } });
        assertThat(shape.getMask()).isEqualTo(new boolean[][] { { true, false, false }, { false, true, true }, { true, true, false } });
    }

    public void shouldRetrieveDefaultWhenNullObject() {
        assertThat(Shape.NULL.toString()).isEqualTo("<empty>");
        assertThat(Shape.NULL.getMask()).isNull();
        assertThat(Shape.NULL.getWidth()).isZero();
        assertThat(Shape.NULL.isTaken(0, 0)).isFalse();
        assertThat(Shape.NULL.isTaken(5, 5)).isFalse();
        assertThat(Shape.NULL.getRelatedShapes()).isEmpty();
    }

    private static final Supplier<boolean[][]> CREATE_MATRIX =
            () -> new boolean[][] {
                    { true, false, false },
                    { false, true, true },
                    { true, true, false } };


}
