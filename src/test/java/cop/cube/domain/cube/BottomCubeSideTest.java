package cop.cube.domain.cube;

import cop.cube.domain.SquareShape;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 14.04.2018
 */
@Test
public class BottomCubeSideTest {

    public void shouldAddShapeToTheBottomSide() {
        char[][][] data = new char[3][3][3];
        SquareShape shape = SquareShape.create('A', createSquare());
        Cube.Side.BOTTOM.add(shape, data);

        assertThat(data[2][0][0]).isEqualTo(Cube.Side.BOTTOM.marker());
        assertThat(data[2][1][0]).isEqualTo('\0');
        assertThat(data[2][2][0]).isEqualTo('\0');
        assertThat(data[2][0][1]).isEqualTo('\0');
        assertThat(data[2][1][1]).isEqualTo(Cube.Side.BOTTOM.marker());
        assertThat(data[2][2][1]).isEqualTo(Cube.Side.BOTTOM.marker());
        assertThat(data[2][0][2]).isEqualTo(Cube.Side.BOTTOM.marker());
        assertThat(data[2][1][2]).isEqualTo(Cube.Side.BOTTOM.marker());
        assertThat(data[2][2][2]).isEqualTo('\0');

        for (int z = 0; z < 3; z++)
            for (int y = 0; y < 2; y++)
                for (int x = 0; x < 3; x++)
                    assertThat(data[y][x][z]).isEqualTo('\0');
    }

    public void shouldRetrieveMaskFromBottomSide() {
        char[][][] data = new char[3][3][3];
        SquareShape shape = SquareShape.create('A', createSquare());
        Cube.Side.BOTTOM.add(shape, data);

        boolean[][] mask = Cube.Side.BOTTOM.mask(data);
        assertThat(mask).isNotNull();
        assertThat(mask).isEqualTo(createSquare());
    }

    public void shouldClearBottomSide() {
        char[][][] data = new char[3][3][3];

        for (int z = 0; z < 3; z++)
            for (int x = 0; x < 3; x++)
                data[2][x][z] = Cube.Side.BOTTOM.marker();

        Cube.Side.BOTTOM.clear(data);

        for (int z = 0; z < 3; z++)
            for (int x = 0; x < 3; x++)
                assertThat(data[2][x][z]).isEqualTo('\0');
    }

    public void shouldRetrieveCompletedWhenAllBorderCellsTaken() {
        char[][][] data = new char[3][3][3];
        data[2][0][0] = 'A';
        data[2][1][0] = 'A';
        data[2][2][0] = 'A';

        data[2][0][1] = 'A';
        data[2][2][1] = 'A';

        data[2][0][2] = 'A';
        data[2][1][2] = 'A';
        data[2][2][2] = 'A';

        assertThat(Cube.Side.BOTTOM.isCompleted(data)).isTrue();
    }

    private static boolean[][] createSquare() {
        return new boolean[][] {
                { true, false, false },
                { false, true, true },
                { true, true, false } };
    }

}
