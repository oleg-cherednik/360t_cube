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
            sides.get(side).clear(data);

        return res;
    }

    public void removeCurrentSide() {
        if (shapes.isEmpty())
            return;
        if (shapes.size() == 6)
            sides.get(side).clear(data);
        else {
            side = side.previous();
            sides.get(side).clear(data);
        }
        shapes.pollLast();
    }

    public boolean isComplete() {
        for (CubeSide cubeSide : sides.values())
            if (!cubeSide.isCompleted(data))
                return false;
        return true;
    }

    public Map<Side, boolean[][]> getSideMask() {
        Map<Side, boolean[][]> map = new EnumMap<>(Side.class);
        sides.forEach((side, cubeSide) -> map.put(side, cubeSide.mask(data)));
        return Collections.unmodifiableMap(map);
    }

    public enum Side {
        FRONT('1') {
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

        public abstract Side next();

        public abstract Side previous();

        public char marker() {
            return marker;
        }

        protected final void clear(int x, int y, int z, char[][][] data) {
            data[y][x][z] = data[y][x][z] == marker ? '\0' : data[y][x][z];
        }

        protected static int width(char[][][] data) {
            return data.length;
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
