package cop.cube.domain.cube;

import cop.cube.domain.SquareShape;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 14.04.2018
 */
@Test
public class BackCubeSideTest {

    private static final CubeSide SIDE = FrontBackCubeSide.getBackInstance();
    private static final char MARKER = Side.BACK.marker;

    public void shouldAddShapeToTheBackSide() {
        char[][][] data = new char[3][3][3];
        SquareShape shape = SquareShape.create('A', createSquare());
        SIDE.add(shape, data);

        assertThat(data[2][0][2]).isEqualTo(MARKER);
        assertThat(data[2][1][2]).isEqualTo('\0');
        assertThat(data[2][2][2]).isEqualTo('\0');
        assertThat(data[1][0][2]).isEqualTo('\0');
        assertThat(data[1][1][2]).isEqualTo(MARKER);
        assertThat(data[1][2][2]).isEqualTo(MARKER);
        assertThat(data[0][0][2]).isEqualTo(MARKER);
        assertThat(data[0][1][2]).isEqualTo(MARKER);
        assertThat(data[0][2][2]).isEqualTo('\0');

        for (int z = 0; z < 2; z++)
            for (int y = 0; y < 3; y++)
                for (int x = 0; x < 3; x++)
                    assertThat(data[y][x][z]).isEqualTo('\0');
    }

    public void shouldRetrieveMaskFromBackSide() {
        char[][][] data = new char[3][3][3];
        SquareShape shape = SquareShape.create('A', createSquare());
        SIDE.add(shape, data);

        boolean[][] mask = SIDE.mask(data);
        assertThat(mask).isNotNull();
        assertThat(mask).isEqualTo(createSquare());
    }

    public void shouldClearBackSide() {
        char[][][] data = new char[3][3][3];

        for (int y = 0; y < 3; y++)
            for (int x = 0; x < 3; x++)
                data[y][x][2] = MARKER;

        SIDE.clear(data);

        for (int y = 0; y < 3; y++)
            for (int x = 0; x < 3; x++)
                assertThat(data[y][x][2]).isEqualTo('\0');
    }

    public void shouldRetrieveCompletedWhenAllBorderCellsTaken() {
        char[][][] data = new char[3][3][3];
        data[0][0][2] = 'A';
        data[0][1][2] = 'A';
        data[0][2][2] = 'A';

        data[1][0][2] = 'A';
        data[1][2][2] = 'A';

        data[2][0][2] = 'A';
        data[2][1][2] = 'A';
        data[2][2][2] = 'A';

        assertThat(SIDE.isCompleted(data)).isTrue();
    }

    private static boolean[][] createSquare() {
        return new boolean[][] {
                { true, false, false },
                { false, true, true },
                { true, true, false } };
    }

}
