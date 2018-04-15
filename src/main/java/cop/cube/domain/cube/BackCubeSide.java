package cop.cube.domain.cube;

import cop.cube.domain.SquareShape;

/**
 * Back side implementation. For standard cube, this is side with number <tt>six</tt>. For 3D array implementation, this is side with
 * <tt>z=width-1</tt>.
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
@SuppressWarnings("MethodCanBeVariableArityMethod")
final class BackCubeSide extends CubeSide {

    private static final BackCubeSide INSTANCE = new BackCubeSide();

    public static BackCubeSide getInstance() {
        return INSTANCE;
    }

    private BackCubeSide() {
        super(Cube.Side.BACK);
    }

    @Override
    public boolean isCompleted(char[][][] data) {
        final int width = width(data);
        final int z = width - 1;

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
        final int z = width - 1;

        for (int y = 0; y < width; y++)
            for (int x = 0; x < width; x++)
                clear(x, y, z, data);
    }

    @Override
    public boolean add(SquareShape shape, char[][][] data) {
        final int width = width(data);
        final int z = width - 1;

        for (int y = 0; y < width; y++)
            for (int x = 0; x < width; x++)
                if (!add(shape.isTaken(x, y), x, width - y - 1, z, data))
                    return false;

        return true;
    }

    @Override
    public boolean[][] mask(char[][][] data) {
        boolean[][] mask = null;

        if (data != null) {
            final int width = width(data);
            final int z = width - 1;

            mask = new boolean[width][width];

            for (int y = 0; y < width; y++)
                for (int x = 0; x < width; x++)
                    mask[y][x] = data[width - y - 1][x][z] == side.marker();
        }

        return mask;
    }
}
