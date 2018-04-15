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
        FRONT('1'),
        LEFT('2'),
        BOTTOM('3'),
        TOP('4'),
        RIGHT('5'),
        BACK('6');

        protected final char marker;

        Side(char marker) {
            this.marker = marker;
        }

        public Side next() {
            return ordinal() == values().length - 1 ? this : values()[ordinal() + 1];
        }

        public Side previous() {
            return ordinal() == 0 ? this : values()[ordinal() - 1];
        }

        public char marker() {
            return marker;
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
