package cop.cube.domain.cube;

import cop.cube.domain.SquareShape;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 14.04.2018
 */
@Test
public class FrontCubeSideTest {

    private static final CubeSide SIDE = FrontCubeSide.getInstance();
    private static final char MARKER = Cube.Side.FRONT.marker();

    public void shouldAddShapeToTheFrontSide() {
        char[][][] data = new char[3][3][3];
        SquareShape shape = SquareShape.create('A', createSquare());
        SIDE.add(shape, data);

        assertThat(data[0][0][0]).isEqualTo(MARKER);
        assertThat(data[0][1][0]).isEqualTo('\0');
        assertThat(data[0][2][0]).isEqualTo('\0');
        assertThat(data[1][0][0]).isEqualTo('\0');
        assertThat(data[1][1][0]).isEqualTo(MARKER);
        assertThat(data[1][2][0]).isEqualTo(MARKER);
        assertThat(data[2][0][0]).isEqualTo(MARKER);
        assertThat(data[2][1][0]).isEqualTo(MARKER);
        assertThat(data[2][2][0]).isEqualTo('\0');

        for (int z = 1; z < 3; z++)
            for (int y = 0; y < 3; y++)
                for (int x = 0; x < 3; x++)
                    assertThat(data[y][x][z]).isEqualTo('\0');
    }

    public void shouldRetrieveMaskFromFrontSide() {
        char[][][] data = new char[3][3][3];
        SquareShape shape = SquareShape.create('A', createSquare());
        SIDE.add(shape, data);

        boolean[][] mask = SIDE.mask(data);
        assertThat(mask).isNotNull();
        assertThat(mask).isEqualTo(createSquare());
    }

    public void shouldClearFrontSide() {
        char[][][] data = new char[3][3][3];

        for (int y = 0; y < 3; y++)
            for (int x = 0; x < 3; x++)
                data[y][x][0] = MARKER;

        SIDE.clear(data);

        for (int y = 0; y < 3; y++)
            for (int x = 0; x < 3; x++)
                assertThat(data[y][x][0]).isEqualTo('\0');
    }

    public void shouldRetrieveCompletedWhenAllBorderCellsTaken() {
        char[][][] data = new char[3][3][3];
        data[0][0][0] = 'A';
        data[0][1][0] = 'A';
        data[0][2][0] = 'A';

        data[1][0][0] = 'A';
        data[1][2][0] = 'A';

        data[2][0][0] = 'A';
        data[2][1][0] = 'A';
        data[2][2][0] = 'A';

        assertThat(SIDE.isCompleted(data)).isTrue();
    }

    private static boolean[][] createSquare() {
        return new boolean[][] {
                { true, false, false },
                { false, true, true },
                { true, true, false } };
    }

}
