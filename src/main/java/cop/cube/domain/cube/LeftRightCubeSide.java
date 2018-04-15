package cop.cube.domain.cube;

import cop.cube.domain.SquareShape;

import java.util.function.BiFunction;
import java.util.function.IntFunction;

/**
 * Represents two sides of the cube:<br>
 * <tt>left</tt> - for standard cube, this is side with number <tt>two</tt>. For 3D array implementation, this is side with <tt>x = 0</tt> and <tt>z
 * from width - 1 to 0</tt>:
 * <pre>
 * [yxz]
 * [004][003][002][001][000]
 * [104][103][102][101][100]
 * [204][203][202][201][200]
 * [304][303][302][301][300]
 * [404][403][402][401][400]
 * </pre>
 * <tt>right</tt> - for standard cube, this is side with number <tt>five</tt>. For 3D array implementation, this is side with <tt>x = width - 1</tt>
 * and <tt>z from 0 to width - 1</tt>:
 * <pre>
 * [yxz]
 * [040][041][042][043][044]
 * [140][141][142][143][144]
 * [240][241][242][243][244]
 * [340][341][342][343][344]
 * [440][441][442][443][444]
 * </pre>
 *
 * @author Oleg Cherednik
 * @since 14.04.2018
 */
@SuppressWarnings({ "MethodCanBeVariableArityMethod", "FieldNamingConvention" })
final class LeftRightCubeSide extends CubeSide {

    private static final LeftRightCubeSide LEFT = new LeftRightCubeSide('2', width -> 0, (z, width) -> width - z - 1);
    private static final LeftRightCubeSide RIGHT = new LeftRightCubeSide('5', width -> width - 1, (z, width) -> z);

    public static LeftRightCubeSide getLeftInstance() {
        return LEFT;
    }

    public static LeftRightCubeSide getRightInstance() {
        return RIGHT;
    }

    private final IntFunction<Integer> axisX;
    private final BiFunction<Integer, Integer, Integer> axisZ;

    private LeftRightCubeSide(char marker, IntFunction<Integer> axisX, BiFunction<Integer, Integer, Integer> axisZ) {
        super(marker);
        this.axisX = axisX;
        this.axisZ = axisZ;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public boolean isCompleted(char[][][] data) {
        final int width = width(data);
        final int x = axisX.apply(width);

        for (int z = 0; z < width; z++)
            if (!isTaken(x, 0, z, data) || !isTaken(x, width - 1, z, data))
                return false;

        for (int y = 0; y < width; y++)
            if (!isTaken(x, y, 0, data) || !isTaken(x, y, width - 1, data))
                return false;

        return true;
    }

    @Override
    public void clear(char[][][] data) {
        final int width = width(data);
        final int x = axisX.apply(width);

        for (int y = 0; y < width; y++)
            for (int z = 0; z < width; z++)
                clear(x, y, z, data);
    }

    @Override
    public boolean add(SquareShape shape, char[][][] data) {
        final int width = width(data);
        final int x = axisX.apply(width);

        for (int y = 0; y < width; y++)
            for (int z = 0; z < width; z++)
                if (!add(shape.isTaken(z, y), x, y, axisZ.apply(z, width), data))
                    return false;

        return true;
    }

    @Override
    public boolean[][] mask(char[][][] data) {
        boolean[][] mask = null;

        if (data != null) {
            final int width = width(data);
            final int x = axisX.apply(width);

            mask = new boolean[width][width];

            for (int y = 0; y < width; y++)
                for (int z = width - 1; z >= 0; z--)
                    mask[y][z] = data[y][x][axisZ.apply(z, width)] == getMarker();
        }

        return mask;
    }
}
