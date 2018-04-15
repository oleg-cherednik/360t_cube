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
        if (shapes.size() == Side.values().length)
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
        FRONT(FrontBackCubeSide.getFrontInstance()),
        LEFT(LeftRightCubeSide.getLeftInstance()),
        BOTTOM(TopBottomCubeSide.getBottomInstance()),
        TOP(TopBottomCubeSide.getTopInstance()),
        RIGHT(LeftRightCubeSide.getRightInstance()),
        BACK(FrontBackCubeSide.getBackInstance());

        final CubeSide cubeSide;

        Side(CubeSide cubeSide) {
            this.cubeSide = cubeSide;
        }

        public Side next() {
            return ordinal() == values().length - 1 ? this : values()[ordinal() + 1];
        }

        public Side previous() {
            return ordinal() == 0 ? this : values()[ordinal() - 1];
        }

        private static Map<Cube.Side, CubeSide> getSideInstance() {
            Map<Cube.Side, CubeSide> map = new EnumMap<>(Cube.Side.class);

            for (Side side : values())
                map.put(side, side.cubeSide);

            return Collections.unmodifiableMap(map);
        }
    }

}
