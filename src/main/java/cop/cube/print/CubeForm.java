package cop.cube.print;

import cop.cube.domain.cube.Cube;
import cop.cube.domain.cube.Side;

import java.io.PrintStream;
import java.util.Map;

/**
 * Cube form printing strategy. Prints given {@link Cube} with following format int given {@link PrintStream}:
 *
 * <pre>
 *     -----
 *     | 4 |
 * -------------
 * | 2 | 1 | 5 |
 * -------------
 *     | 3 |
 *     -----
 *     | 6 |
 *     -----
 * </pre>
 *
 * @author Oleg Cherednik
 * @since 13.04.2018
 */
public final class CubeForm implements PrintStrategy {

    public static final CubeForm INSTANCE = new CubeForm();

    public static CubeForm getInstance() {
        return INSTANCE;
    }

    @Override
    public void print(Cube cube, char marker, PrintStream out) {
        if (cube == null || out == null)
            return;

        int width = cube.getWidth();
        boolean[][] empty = new boolean[width][width];
        Map<Side, boolean[][]> sides = cube.getSideMask();

        boolean[][] mask1 = sides.getOrDefault(Side.FRONT, empty);
        boolean[][] mask2 = sides.getOrDefault(Side.LEFT, empty);
        boolean[][] mask3 = sides.getOrDefault(Side.BOTTOM, empty);
        boolean[][] mask4 = sides.getOrDefault(Side.TOP, empty);
        boolean[][] mask5 = sides.getOrDefault(Side.RIGHT, empty);
        boolean[][] mask6 = sides.getOrDefault(Side.BACK, empty);

        printLine(width, empty, mask4, empty, marker, out);
        printLine(width, mask2, mask1, mask5, marker, out);
        printLine(width, empty, mask3, empty, marker, out);
        printLine(width, empty, mask6, empty, marker, out);
    }

    private static void printLine(int width, boolean[][] mask1, boolean[][] mask2, boolean[][] mask3, char marker, PrintStream out) {
        for (int y = 0; y < width; y++) {
            print(mask1[y], marker, out);
            out.print(' ');
            print(mask2[y], marker, out);
            out.print(' ');
            print(mask3[y], marker, out);
            out.print('\n');
        }
    }

    private static void print(boolean[] mask, char marker, PrintStream out) {
        for (int x = 0; x < mask.length; x++)
            out.print(mask[x] ? marker : ' ');
    }

    private CubeForm() {
    }

}
