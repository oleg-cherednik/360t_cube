package cop.cube.domain.cube;

import cop.cube.domain.SquareShape;

import java.util.function.BiFunction;
import java.util.function.IntFunction;

/**
 * Represents two sides of the cube:<br>
 * <tt>front</tt> - for standard cube, this is side with number <tt>one</tt>. For 3D array implementation, this is side with <tt>z = 0</tt> and <tt>y
 * from 0 to width - 1</tt>:
 * <pre>
 * [yxz]
 * [000][010][020][030][040]
 * [100][110][120][130][140]
 * [200][210][220][230][240]
 * [300][310][320][330][340]
 * [400][410][420][430][440]
 * </pre>
 * <tt>back</tt> - for standard cube, this is side with number <tt>six</tt>. For 3D array implementation, this is side with <tt>z = width - 1</tt> and
 * <tt>y from width - 1 to 0</tt>:
 * <pre>
 * [yxz]
 * [404][414][424][434][444]
 * [304][314][324][334][344]
 * [204][214][224][234][244]
 * [104][114][124][134][144]
 * [004][014][024][034][044]
 * </pre>
 *
 * @author Oleg Cherednik
 * @since 14.04.2018
 */
@SuppressWarnings({ "MethodCanBeVariableArityMethod", "FieldNamingConvention" })
final class FrontBackCubeSide extends CubeSide {

    private static final FrontBackCubeSide FRONT = new FrontBackCubeSide('1', width -> 0, (y, width) -> y);
    private static final FrontBackCubeSide BACK = new FrontBackCubeSide('6', width -> width - 1, (y, width) -> width - y - 1);

    public static FrontBackCubeSide getFrontInstance() {
        return FRONT;
    }

    public static FrontBackCubeSide getBackInstance() {
        return BACK;
    }

    private final IntFunction<Integer> axisZ;
    private final BiFunction<Integer, Integer, Integer> axisY;

    private FrontBackCubeSide(char marker, IntFunction<Integer> axisZ, BiFunction<Integer, Integer, Integer> axisY) {
        super(marker);
        this.axisZ = axisZ;
        this.axisY = axisY;
    }

    @Override
    public boolean isCompleted(char[][][] data) {
        final int width = width(data);
        final int z = axisZ.apply(width);

        for (int x = 0; x < width; x++)
            if (!isTaken(x, 0, z, data) || !isTaken(x, width - 1, z, data))
                return false;

        for (int y = 0; y < width; y++)
            if (!isTaken(0, y, z, data) || !isTaken(width - 1, y, z, data))
                return false;

        return true;
    }

    @Override
    public void clear(char[][][] data) {
        final int width = width(data);
        final int z = axisZ.apply(width);

        for (int y = 0; y < width; y++)
            for (int x = 0; x < width; x++)
                clear(x, y, z, data);
    }

    @Override
    public boolean add(SquareShape shape, char[][][] data) {
        final int width = width(data);
        final int z = axisZ.apply(width);

        for (int y = 0; y < width; y++)
            for (int x = 0; x < width; x++)
                if (!add(shape.isTaken(x, y), x, axisY.apply(y, width), z, data))
                    return false;

        return true;
    }

    @Override
    public boolean[][] mask(char[][][] data) {
        boolean[][] mask = null;

        if (data != null) {
            final int width = width(data);
            final int z = axisZ.apply(width);

            mask = new boolean[width][width];

            for (int y = 0; y < width; y++)
                for (int x = 0; x < width; x++)
                    mask[y][x] = data[axisY.apply(y, width)][x][z] == getMarker();
        }

        return mask;
    }
}
