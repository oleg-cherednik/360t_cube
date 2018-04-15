package cop.cube.domain.cube;

import cop.cube.domain.SquareShape;

import java.util.function.BiFunction;
import java.util.function.IntFunction;

/**
 * Represents two sides of the cube:<br>
 * <tt>top</tt> - for standard cube, this is side with number <tt>four</tt>. For 3D array implementation, this is side with <tt>y = 0</tt> and <tt>z
 * from width - 1 to 0</tt>:
 * <pre>
 * [yxz]
 * [004][014][024][034][044]
 * [003][013][023][033][043]
 * [002][012][022][032][042]
 * [001][011][021][031][041]
 * [000][010][020][030][040]
 * </pre>
 * <tt>bottom</tt> - for standard cube, this is side with number <tt>three</tt>. For 3D array implementation, this is side with <tt>y = width - 1</tt>
 * and <tt>z from 0 to width - 1</tt>:
 * <pre>
 * [yxz]
 * [400][410][420][430][440]
 * [401][411][421][431][441]
 * [402][412][422][432][442]
 * [403][413][423][433][443]
 * [404][414][424][434][444]
 * </pre>
 *
 * @author Oleg Cherednik
 * @since 14.04.2018
 */
@SuppressWarnings({ "MethodCanBeVariableArityMethod", "FieldNamingConvention" })
final class TopBottomCubeSide extends CubeSide {

    private static final TopBottomCubeSide TOP = new TopBottomCubeSide('4', width -> 0, (z, width) -> width - z - 1);
    private static final TopBottomCubeSide BOTTOM = new TopBottomCubeSide('3', width -> width - 1, (z, width) -> z);

    public static TopBottomCubeSide getTopInstance() {
        return TOP;
    }

    public static TopBottomCubeSide getBottomInstance() {
        return BOTTOM;
    }

    private final IntFunction<Integer> axisY;
    private final BiFunction<Integer, Integer, Integer> axisZ;

    private TopBottomCubeSide(char marker, IntFunction<Integer> axisY, BiFunction<Integer, Integer, Integer> axisZ) {
        super(marker);
        this.axisY = axisY;
        this.axisZ = axisZ;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public boolean isCompleted(char[][][] data) {
        final int width = width(data);
        final int y = axisY.apply(width);

        for (int x = 0; x < width; x++)
            if (!isTaken(x, y, 0, data) || !isTaken(x, y, width - 1, data))
                return false;

        for (int z = 0; z < width; z++)
            if (!isTaken(0, y, z, data) || !isTaken(width - 1, y, z, data))
                return false;

        return true;
    }

    @Override
    public void clear(char[][][] data) {
        final int width = width(data);
        final int y = axisY.apply(width);

        for (int z = 0; z < width; z++)
            for (int x = 0; x < width; x++)
                clear(x, y, z, data);
    }

    @Override
    public boolean add(SquareShape shape, char[][][] data) {
        final int width = width(data);
        final int y = axisY.apply(width);

        for (int z = 0; z < width; z++)
            for (int x = 0; x < width; x++)
                if (!add(shape.isTaken(x, z), x, y, axisZ.apply(z, width), data))
                    return false;

        return true;
    }

    @Override
    public boolean[][] mask(char[][][] data) {
        boolean[][] mask = null;

        if (data != null) {
            final int width = width(data);
            final int y = axisY.apply(width);

            mask = new boolean[width][width];

            for (int z = 0; z < width; z++)
                for (int x = 0; x < width; x++)
                    mask[z][x] = data[y][x][axisZ.apply(z, width)] == getMarker();
        }

        return mask;
    }
}
