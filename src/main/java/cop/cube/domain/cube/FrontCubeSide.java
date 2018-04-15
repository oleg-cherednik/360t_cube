package cop.cube.domain.cube;

import cop.cube.domain.SquareShape;

/**
 * Front side implementation. For standard cube, this is side with number <tt>one</tt>. For 3D array implementation, this is side with <tt>z=0</tt>.
 * <pre>
 * [yxz]
 * [000][010][020][030][040]
 * [100][110][120][130][140]
 * [200][210][220][230][240]
 * [300][310][320][330][340]
 * [400][410][420][430][440]
 * </pre>
 *
 * @author Oleg Cherednik
 * @since 14.04.2018
 */
@SuppressWarnings("MethodCanBeVariableArityMethod")
final class FrontCubeSide extends CubeSide {

    private static final FrontCubeSide INSTANCE = new FrontCubeSide();

    public static FrontCubeSide getInstance() {
        return INSTANCE;
    }

    private FrontCubeSide() {
        super(Side.FRONT.marker);
    }

    @Override
    public boolean isCompleted(char[][][] data) {
        final int width = width(data);
        final int z = 0;

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
        final int z = 0;

        for (int y = 0; y < width; y++)
            for (int x = 0; x < width; x++)
                clear(x, y, z, data);
    }

    @Override
    public boolean add(SquareShape shape, char[][][] data) {
        final int width = width(data);
        final int z = 0;

        for (int y = 0; y < width; y++)
            for (int x = 0; x < width; x++)
                if (!add(shape.isTaken(x, y), x, y, z, data))
                    return false;

        return true;
    }

    @Override
    public boolean[][] mask(char[][][] data) {
        boolean[][] mask = null;

        if (data != null) {
            final int width = width(data);
            final int z = 0;

            mask = new boolean[width][width];

            for (int y = 0; y < width; y++)
                for (int x = 0; x < width; x++)
                    mask[y][x] = data[y][x][z] == marker;
        }

        return mask;
    }
}
