package cop.cube.domain.cube;

import cop.cube.domain.SquareShape;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 14.04.2018
 */
@Test
public class LeftCubeSideTest {

    public void shouldAddShapeToTheLeftSide() {
        char[][][] data = new char[3][3][3];
        SquareShape shape = SquareShape.create('A', createSquare());
        Cube.Side.LEFT.add(shape, data);

        assertThat(data[0][0][2]).isEqualTo(Cube.Side.LEFT.marker());
        assertThat(data[0][0][1]).isEqualTo('\0');
        assertThat(data[0][0][0]).isEqualTo('\0');
        assertThat(data[1][0][2]).isEqualTo('\0');
        assertThat(data[1][0][1]).isEqualTo(Cube.Side.LEFT.marker());
        assertThat(data[1][0][0]).isEqualTo(Cube.Side.LEFT.marker());
        assertThat(data[2][0][2]).isEqualTo(Cube.Side.LEFT.marker());
        assertThat(data[2][0][1]).isEqualTo(Cube.Side.LEFT.marker());
        assertThat(data[2][0][0]).isEqualTo('\0');

        for (int z = 0; z < 3; z++)
            for (int y = 0; y < 3; y++)
                for (int x = 1; x < 3; x++)
                    assertThat(data[y][x][z]).isEqualTo('\0');
    }

    public void shouldRetrieveMaskFromLeftSide() {
        char[][][] data = new char[3][3][3];
        SquareShape shape = SquareShape.create('A', createSquare());
        Cube.Side.LEFT.add(shape, data);

        boolean[][] mask = Cube.Side.LEFT.mask(data);
        assertThat(mask).isNotNull();
        assertThat(mask).isEqualTo(createSquare());
    }

    public void shouldClearLeftSide() {
        char[][][] data = new char[3][3][3];

        for (int y = 0; y < 3; y++)
            for (int z = 0; z < 3; z++)
                data[y][0][z] = Cube.Side.LEFT.marker();

        Cube.Side.LEFT.clear(data);

        for (int y = 0; y < 3; y++)
            for (int z = 0; z < 3; z++)
                assertThat(data[y][0][z]).isEqualTo('\0');
    }

    public void shouldRetrieveCompletedWhenAllBorderCellsTaken() {
        char[][][] data = new char[3][3][3];
        data[0][0][0] = 'A';
        data[0][0][1] = 'A';
        data[0][0][2] = 'A';

        data[1][0][0] = 'A';
        data[1][0][2] = 'A';

        data[2][0][0] = 'A';
        data[2][0][1] = 'A';
        data[2][0][2] = 'A';

        assertThat(Cube.Side.LEFT.isCompleted(data)).isTrue();
    }

    private static boolean[][] createSquare() {
        return new boolean[][] {
                { true, false, false },
                { false, true, true },
                { true, true, false } };
    }

}
