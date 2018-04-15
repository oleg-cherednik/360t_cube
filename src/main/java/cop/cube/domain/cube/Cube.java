package cop.cube.domain.cube;

import cop.cube.domain.SquareShape;
import cop.cube.exceptions.CubeException;

import java.util.Collections;
import java.util.Deque;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Map;

/**
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

        boolean res = sides.get(side).add(shape, data);

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
        sides.forEach((side, cubeSide) -> map.put(side, cubeSide.mask(data)));
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

        protected final void clear(int x, int y, int z, char[][][] data) {
            data[y][x][z] = data[y][x][z] == marker ? '\0' : data[y][x][z];
        }

        protected static boolean isTaken(int x, int y, int z, char[][][] data) {
            return data[y][x][z] != '\0';
        }

        protected static int width(char[][][] data) {
            return data.length;
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
