package cop.cube.domain.cube;

import cop.cube.domain.SquareShape;

/**
 * Top side implementation. For standard cube, this is side with number <tt>four</tt>. For 3D array implementation, this is side with <tt>y=0</tt>.
 * <pre>
 * [yxz]
 * [004][014][024][034][044]
 * [003][013][023][033][043]
 * [002][012][022][032][042]
 * [001][011][021][031][041]
 * [000][010][020][030][040]
 * </pre>
 *
 * @author Oleg Cherednik
 * @since 14.04.2018
 */
@SuppressWarnings("MethodCanBeVariableArityMethod")
final class TopCubeSide extends CubeSide {

    private static final TopCubeSide INSTANCE = new TopCubeSide();

    public static TopCubeSide getInstance() {
        return INSTANCE;
    }

    private TopCubeSide() {
        super(Side.TOP);
    }

    @Override
    public boolean isCompleted(char[][][] data) {
        final int width = width(data);
        final int y = 0;

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
        final int y = 0;

        for (int z = width - 1; z >= 0; z--)
            for (int x = 0; x < width; x++)
                clear(x, y, z, data);
    }

    @Override
    public boolean add(SquareShape shape, char[][][] data) {
        final int width = width(data);
        final int y = 0;

        for (int z = width - 1; z >= 0; z--)
            for (int x = 0; x < width; x++)
                if (!add(shape.isTaken(x, width - z - 1), x, y, z, data))
                    return false;

        return true;
    }

    @Override
    public boolean[][] mask(char[][][] data) {
        boolean[][] mask = null;

        if (data != null) {
            final int width = width(data);
            final int y = 0;

            mask = new boolean[width][width];

            for (int z = 0; z < width; z++)
                for (int x = 0; x < width; x++)
                    mask[z][x] = data[y][x][width - z - 1] == side.marker;
        }

        return mask;
    }
}
