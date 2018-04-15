package cop.cube.domain.cube;

import cop.cube.domain.SquareShape;

/**
 * Left side implementation. For standard cube, this is side with number <tt>two</tt>. For 3D array implementation, this is side with <tt>x=0</tt>.
 * <pre>
 * [yxz]
 * [004][003][002][001][000]
 * [104][103][102][101][100]
 * [204][203][202][201][200]
 * [304][303][302][301][300]
 * [404][403][402][401][400]
 * </pre>
 *
 * @author Oleg Cherednik
 * @since 14.04.2018
 */
@SuppressWarnings("MethodCanBeVariableArityMethod")
final class LeftCubeSide extends CubeSide {

    private static final LeftCubeSide INSTANCE = new LeftCubeSide();

    public static LeftCubeSide getInstance() {
        return INSTANCE;
    }

    private LeftCubeSide() {
        super(Side.LEFT.marker);
    }

    @Override
    public boolean isCompleted(char[][][] data) {
        final int width = width(data);
        final int x = 0;

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
        final int x = 0;

        for (int y = 0; y < width; y++)
            for (int z = width - 1; z >= 0; z--)
                clear(x, y, z, data);
    }

    @Override
    public boolean add(SquareShape shape, char[][][] data) {
        final int width = width(data);
        final int x = 0;

        for (int y = 0; y < width; y++)
            for (int z = width - 1; z >= 0; z--)
                if (!add(shape.isTaken(width - z - 1, y), x, y, z, data))
                    return false;

        return true;
    }

    @Override
    public boolean[][] mask(char[][][] data) {
        boolean[][] mask = null;

        if (data != null) {
            final int width = width(data);
            final int x = 0;

            mask = new boolean[width][width];

            for (int y = 0; y < width; y++)
                for (int z = width - 1; z >= 0; z--)
                    mask[y][z] = data[y][x][width - z - 1] == marker;
        }

        return mask;
    }
}
