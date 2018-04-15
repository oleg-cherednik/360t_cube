package cop.cube.domain.cube;

import cop.cube.domain.SquareShape;
import cop.cube.exceptions.CubeException;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Oleg Cherednik
 * @since 14.04.2018
 */
@Test
public class CubeTest {

    public void shouldSideContainsCorrectImplementation() {
        assertThat(Cube.Side.values()).hasSize(6);
        assertThat(Cube.Side.FRONT.cubeSide).isSameAs(FrontBackCubeSide.getFrontInstance());
        assertThat(Cube.Side.LEFT.cubeSide).isSameAs(LeftRightCubeSide.getLeftInstance());
        assertThat(Cube.Side.BOTTOM.cubeSide).isSameAs(TopBottomCubeSide.getBottomInstance());
        assertThat(Cube.Side.TOP.cubeSide).isSameAs(TopBottomCubeSide.getTopInstance());
        assertThat(Cube.Side.RIGHT.cubeSide).isSameAs(LeftRightCubeSide.getRightInstance());
        assertThat(Cube.Side.BACK.cubeSide).isSameAs(FrontBackCubeSide.getBackInstance());
    }

    public void shouldThrowExceptionWhenAddShapeWithNotMatchWidth() {
        assertThatExceptionOfType(CubeException.class).isThrownBy(() ->
                new Cube(6).addNextSide(SquareShape.create('A', createSquare())));
    }

    private static boolean[][] createSquare() {
        return new boolean[][] {
                { true, false, false },
                { false, true, true },
                { true, true, false } };
    }
}
