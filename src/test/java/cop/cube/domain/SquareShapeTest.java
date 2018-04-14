package cop.cube.domain;

import cop.cube.exceptions.CubeException;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Oleg Cherednik
 * @since 14.04.2018
 */
@Test
public class SquareShapeTest {

    public static final boolean[][] EMPTY_ARRAY = new boolean[0][0];

    public void shouldRetrieveNullObjectWhenMaskNotSet() {
        assertThat(SquareShape.create('A', null)).isSameAs(SquareShape.NULL);
        assertThat(SquareShape.create('A', EMPTY_ARRAY)).isSameAs(SquareShape.NULL);
        assertThat(SquareShape.create('A', Direction.UP, Mirror.OFF, null)).isSameAs(SquareShape.NULL);
        assertThat(SquareShape.create('A', Direction.UP, Mirror.OFF, EMPTY_ARRAY)).isSameAs(SquareShape.NULL);
    }

    public void shouldRetrieveCreatedShapeWithDefaultDirectionAndMirrorWhenMaskSet() {
        SquareShape shape = SquareShape.create('A', createSquare());
        assertThat(shape).isNotNull();
        assertThat(shape).isNotSameAs(SquareShape.NULL);
        assertThat(shape.toString()).isEqualTo("A-" + Direction.UP + '-' + Mirror.OFF);
    }

    public void shouldRetrieveFullCopyOfInternalMaskWhenGetMask() {
        SquareShape shape = SquareShape.create('A', createSquare());
        boolean[][] mask = shape.getMask();
        assertThat(mask).isNotNull();
        assertThat(mask).isEqualTo(new boolean[][] { { true, false, false }, { false, true, true }, { true, true, false } });

        mask[0][0] = !mask[0][0];
        assertThat(mask).isEqualTo(new boolean[][] { { false, false, false }, { false, true, true }, { true, true, false } });
        assertThat(shape.getMask()).isEqualTo(new boolean[][] { { true, false, false }, { false, true, true }, { true, true, false } });
    }

    public void shouldRetrieveDefaultWhenNullObject() {
        assertThat(SquareShape.NULL.toString()).isEqualTo("<empty>");
        assertThat(SquareShape.NULL.getMask()).isNull();
        assertThat(SquareShape.NULL.getWidth()).isZero();
        assertThat(SquareShape.NULL.isTaken(0, 0)).isFalse();
        assertThat(SquareShape.NULL.isTaken(5, 5)).isFalse();
        assertThat(SquareShape.NULL.getRelatedShapes()).isEmpty();
    }

    public void shouldGenerateRelatedShapesOnlyOnceWhenGetRelatedShapes() {
        SquareShape shape = SquareShape.create('A', createSquare());
        List<SquareShape> relatedShapes = shape.getRelatedShapes();

        assertThat(relatedShapes).isNotEmpty();
        assertThat(shape.getRelatedShapes()).isSameAs(relatedShapes);
    }

    public void shouldGenerateRelatedShapesOnlyForDefaultShape() {
        SquareShape shape = SquareShape.create('A', Direction.DOWN, Mirror.ON, createSquare());
        assertThat(shape.getRelatedShapes()).isEmpty();
    }

    public void shouldRetrieveOnlyUniqueRelatedShapes() {
        SquareShape shape = SquareShape.create('A', new boolean[][] { { false, true, false }, { true, true, true }, { false, true, false } });
        assertThat(shape.getRelatedShapes()).hasSize(1);
    }

    public void shouldThrowExceptionWhenCreateShapeWithNotSquareMask() {
        assertThatExceptionOfType(CubeException.class).isThrownBy(() ->
                SquareShape.create('A', new boolean[][] {
                        { false, false, false },
                        { false, true, true },
                        { true, true, false },
                        { true, true, false } }));
    }

    private static boolean[][] createSquare() {
        return new boolean[][] {
                { true, false, false },
                { false, true, true },
                { true, true, false } };
    }

}
