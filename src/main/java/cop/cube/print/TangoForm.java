package cop.cube.print;

import cop.cube.domain.cube.Cube;

import java.io.PrintStream;
import java.util.Map;

/**
 * Cube form printing strategy. Prints given {@link Cube} with following format int given {@link PrintStream}:
 *
 * <pre>
 * -------------------------
 * | 1 | 2 | 3 | 4 | 5 | 6 |
 * -------------------------
 * </pre>
 *
 * @author Oleg Cherednik
 * @since 14.04.2018
 */
public final class TangoForm implements PrintStrategy {

    private static final TangoForm INSTANCE = new TangoForm();

    public static TangoForm getInstance() {
        return INSTANCE;
    }

    @Override
    public void print(Cube cube, char marker, PrintStream out) {
        if (cube == null || out == null)
            return;

        int width = cube.getWidth();
        boolean[][] empty = new boolean[width][width];
        Map<Cube.Side, boolean[][]> sides = cube.getAllSideMask();

        for (int y = 0; y < width; y++) {
            boolean space = false;

            for (Cube.Side side : Cube.Side.values()) {
                if (space)
                    out.print(' ');

                boolean[] mask = sides.getOrDefault(side, empty)[y];

                for (int x = 0; x < mask.length; x++)
                    out.print(mask[x] ? marker : ' ');

                space = true;
            }

            out.println();
        }
    }

    private TangoForm() {
    }

}
