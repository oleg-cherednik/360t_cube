package cop.cube.domain.cube;

import cop.cube.domain.SquareShape;

/**
 * Right side implementation. For standard cube, this is side with number <tt>five</tt>. For 3D array implementation, this is side with
 * <tt>x=width-1</tt>.
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
@SuppressWarnings("MethodCanBeVariableArityMethod")
final class RightCubeSide extends CubeSide {

    private static final RightCubeSide INSTANCE = new RightCubeSide();

    public static RightCubeSide getInstance() {
        return INSTANCE;
    }

    private RightCubeSide() {
        super(Cube.Side.RIGHT, Cube.Side.BACK, Cube.Side.TOP);
    }

    @Override
    public boolean isCompleted(char[][][] data) {
        final int width = width(data);
        final int x = width - 1;

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
        final int x = width - 1;

        for (int y = 0; y < width; y++)
            for (int z = 0; z < width; z++)
                clear(x, y, z, data);
    }

    @Override
    public boolean add(SquareShape shape, char[][][] data) {
        final int width = width(data);
        final int x = width - 1;

        for (int y = 0; y < width; y++)
            for (int z = 0; z < width; z++)
                if (!add(shape.isTaken(z, y), x, y, z, data))
                    return false;

        return true;
    }

    @Override
    public boolean[][] mask(char[][][] data) {
        boolean[][] mask = null;

        if (data != null) {
            final int width = width(data);
            final int x = width - 1;

            mask = new boolean[width][width];

            for (int y = 0; y < width; y++)
                for (int z = 0; z < width; z++)
                    mask[y][z] = data[y][x][z] == side.marker();
        }

        return mask;
    }
}
