package cop.cube.domain.cube;

import cop.cube.domain.SquareShape;

/**
 * Bottom side implementation. For standard cube, this is side with number <tt>three</tt>. For 3D array implementation, this is side with
 * <tt>y=width-1</tt>.
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
@SuppressWarnings("MethodCanBeVariableArityMethod")
final class BottomCubeSide extends CubeSide {

    private static final BottomCubeSide INSTANCE = new BottomCubeSide();

    public static BottomCubeSide getInstance() {
        return INSTANCE;
    }

    private BottomCubeSide() {
        super(Cube.Side.BOTTOM, Cube.Side.TOP, Cube.Side.LEFT);
    }

    @Override
    public boolean isCompleted(char[][][] data) {
        final int width = width(data);
        final int y = width - 1;

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
        final int y = width - 1;

        for (int z = 0; z < width; z++)
            for (int x = 0; x < width; x++)
                clear(x, y, z, data);
    }

    @Override
    public boolean add(SquareShape shape, char[][][] data) {
        final int width = width(data);
        final int y = width - 1;

        for (int z = 0; z < width; z++)
            for (int x = 0; x < width; x++)
                if (!add(shape.isTaken(x, z), x, y, z, data))
                    return false;

        return true;
    }

    @Override
    public boolean[][] mask(char[][][] data) {
        boolean[][] mask = null;

        if (data != null) {
            final int width = width(data);
            final int y = width - 1;

            mask = new boolean[width][width];

            for (int z = 0; z < width; z++)
                for (int x = 0; x < width; x++)
                    mask[z][x] = data[y][x][z] == side.marker();
        }

        return mask;
    }
}
