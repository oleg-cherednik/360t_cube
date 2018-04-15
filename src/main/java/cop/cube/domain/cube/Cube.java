package cop.cube.domain.cube;

import cop.cube.domain.SquareShape;
import cop.cube.exceptions.CubeException;

import java.util.Collections;
import java.util.Deque;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * <pre>
 *                             side:4
 *                             [004][014][024][034][044]
 *                             [003][013][023][033][043]
 *                             [002][012][022][032][042]
 *                             [001][011][021][031][041]
 *                             [000][010][020][030][040]
 *
 * side:2                      side:1                      side:5
 * [004][003][002][001][000]   [000][010][020][030][040]   [040][041][042][043][044]
 * [104][103][102][101][100]   [100][110][120][130][140]   [140][141][142][143][144]
 * [204][203][202][201][200]   [200][210][220][230][240]   [240][241][242][243][244]
 * [304][303][302][301][300]   [300][310][320][330][340]   [340][341][342][343][344]
 * [404][403][402][401][400]   [400][410][420][430][440]   [440][441][442][443][444]
 *
 *                             side:3
 *                             [400][410][420][430][440]
 *                             [401][411][421][431][441]
 *                             [402][412][422][432][442]
 *                             [403][413][423][433][443]
 *                             [404][414][424][434][444]
 *
 *                             side:6
 *                             [404][414][424][434][444]
 *                             [304][314][324][334][344]
 *                             [204][214][224][234][244]
 *                             [104][114][124][134][144]
 *                             [004][014][024][034][044]
 * </pre>
 *
 * @author Oleg Cherednik
 * @since 11.04.2018
 */
@SuppressWarnings("MethodCanBeVariableArityMethod")
public final class Cube implements Cloneable {

    private final int width;
    private final char[][][] data;
    private final Map<Side, CubeSide> sides = Side.getSideInstance();
    private final Deque<SquareShape> shapes = new LinkedList<>();

    private Side side = Side.FRONT;

    public Cube(int width) {
        this.width = width;
        data = new char[width][width][width];
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public Cube clone() {
        Cube cube = new Cube(width);
        cube.side = side;
        cube.shapes.addAll(shapes);

        for (int z = 0; z < width; z++)
            for (int y = 0; y < width; y++)
                for (int x = 0; x < width; x++)
                    cube.data[y][x][z] = data[y][x][z];

        return cube;
    }

    public int getWidth() {
        return width;
    }

    public boolean addNextSide(SquareShape shape) {
        if (width != shape.getWidth())
            throw new CubeException("Shape's width does not match with cube's width");

        boolean res = side.add(shape, data);

        if (res) {
            side = side.next();
            shapes.addLast(shape);
        } else
            side.clear(data);

        return res;
    }

    public void removeCurrentSide() {
        if (shapes.isEmpty())
            return;
        if (shapes.size() == 6)
            side.clear(data);
        else {
            side = side.previous();
            side.clear(data);
        }
        shapes.pollLast();
    }

    public boolean isComplete() {
        return Side.isAllCompleted(data);
    }

    public Map<Side, boolean[][]> getSideMask() {
        Map<Side, boolean[][]> map = new EnumMap<>(Side.class);

        for (Side side : Side.values())
            map.put(side, side.mask(data));

        return Collections.unmodifiableMap(map);
    }

    public enum Side {
        FRONT('1') {
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
            public Side next() {
                return LEFT;
            }

            @Override
            public Side previous() {
                return FRONT;
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
        },
        LEFT('2') {
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
            public Side next() {
                return BOTTOM;
            }

            @Override
            public Side previous() {
                return FRONT;
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
        },
        BOTTOM('3') {
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
            public Side next() {
                return TOP;
            }

            @Override
            public Side previous() {
                return LEFT;
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
                            mask[z][x] = data[y][x][z] == marker;
                }

                return mask;
            }
        },
        TOP('4') {
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
            public Side next() {
                return RIGHT;
            }

            @Override
            public Side previous() {
                return BOTTOM;
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
                            mask[z][x] = data[y][x][width - z - 1] == marker;
                }

                return mask;
            }
        },
        RIGHT('5') {
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
            public Side next() {
                return BACK;
            }

            @Override
            public Side previous() {
                return TOP;
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
                            mask[y][z] = data[y][x][z] == marker;
                }

                return mask;
            }
        },
        BACK('6') {
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
            public Side next() {
                return BACK;
            }

            @Override
            public Side previous() {
                return RIGHT;
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
                            mask[y][x] = data[width - y - 1][x][z] == marker;
                }

                return mask;
            }
        };

        protected final char marker;

        Side(char marker) {
            this.marker = marker;
        }

        public abstract boolean isCompleted(char[][][] data);

        public abstract void clear(char[][][] data);

        public abstract Side next();

        public abstract Side previous();

        public char marker() {
            return marker;
        }

        public abstract boolean add(SquareShape shape, char[][][] data);

        public abstract boolean[][] mask(char[][][] data);

        protected final void clear(int x, int y, int z, char[][][] data) {
            data[y][x][z] = data[y][x][z] == marker ? '\0' : data[y][x][z];
        }

        protected static boolean isTaken(int x, int y, int z, char[][][] data) {
            return data[y][x][z] != '\0';
        }

        protected static int width(char[][][] data) {
            return data.length;
        }

        protected final boolean add(boolean taken, int x, int y, int z, char[][][] data) {
            if (taken) {
                if (data[y][x][z] != '\0' && data[y][x][z] != marker)
                    return false;

                data[y][x][z] = marker;
            }

            return true;
        }

        public static boolean isAllCompleted(char[][][] data) {
            for (Side side : values())
                if (!side.isCompleted(data))
                    return false;
            return true;
        }

        private static Map<Side, CubeSide> getSideInstance() {
            Map<Side, CubeSide> map = new EnumMap<>(Side.class);
            map.put(FRONT, FrontCubeSide.getInstance());
            map.put(LEFT, LeftCubeSide.getInstance());
            map.put(BOTTOM, BottomCubeSide.getInstance());
            map.put(TOP, TopCubeSide.getInstance());
            map.put(RIGHT, RightCubeSide.getInstance());
            map.put(BACK, BackCubeSide.getInstance());
            return Collections.unmodifiableMap(map);
        }

    }

}
