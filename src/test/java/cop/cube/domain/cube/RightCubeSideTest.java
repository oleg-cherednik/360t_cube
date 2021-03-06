package cop.cube.domain.cube;

import cop.cube.domain.SquareShape;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 14.04.2018
 */
@Test
public class RightCubeSideTest {

    private static final CubeSide SIDE = LeftRightCubeSide.getRightInstance();
    private static final char MARKER = SIDE.getMarker();

    public void shouldAddShapeToTheRightSide() {
        char[][][] data = new char[3][3][3];
        SquareShape shape = SquareShape.create('A', createSquare());
        SIDE.add(shape, data);

        assertThat(data[0][2][0]).isEqualTo(MARKER);
        assertThat(data[0][2][1]).isEqualTo('\0');
        assertThat(data[0][2][2]).isEqualTo('\0');
        assertThat(data[1][2][0]).isEqualTo('\0');
        assertThat(data[1][2][1]).isEqualTo(MARKER);
        assertThat(data[1][2][2]).isEqualTo(MARKER);
        assertThat(data[2][2][0]).isEqualTo(MARKER);
        assertThat(data[2][2][1]).isEqualTo(MARKER);
        assertThat(data[2][2][2]).isEqualTo('\0');

        for (int z = 0; z < 3; z++)
            for (int y = 0; y < 3; y++)
                for (int x = 0; x < 2; x++)
                    assertThat(data[y][x][z]).isEqualTo('\0');
    }

    public void shouldRetrieveMaskFromRightSide() {
        char[][][] data = new char[3][3][3];
        SquareShape shape = SquareShape.create('A', createSquare());
        SIDE.add(shape, data);

        boolean[][] mask = SIDE.mask(data);
        assertThat(mask).isNotNull();
        assertThat(mask).isEqualTo(createSquare());
    }

    public void shouldClearRightSide() {
        char[][][] data = new char[3][3][3];

        for (int y = 0; y < 3; y++)
            for (int z = 0; z < 3; z++)
                data[y][2][z] = MARKER;

        SIDE.clear(data);

        for (int y = 0; y < 3; y++)
            for (int z = 0; z < 3; z++)
                assertThat(data[y][2][z]).isEqualTo('\0');
    }

    public void shouldRetrieveCompletedWhenAllBorderCellsTaken() {
        char[][][] data = new char[3][3][3];
        data[0][2][0] = 'A';
        data[0][2][1] = 'A';
        data[0][2][2] = 'A';

        data[1][2][0] = 'A';
        data[1][2][2] = 'A';

        data[2][2][0] = 'A';
        data[2][2][1] = 'A';
        data[2][2][2] = 'A';

        assertThat(SIDE.isSolved(data)).isTrue();
    }

    private static boolean[][] createSquare() {
        return new boolean[][] {
                { true, false, false },
                { false, true, true },
                { true, true, false } };
    }

}
