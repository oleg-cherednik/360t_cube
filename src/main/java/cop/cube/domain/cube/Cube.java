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

    /** Current cube side that is ready to add next shape */
    private Side side = Side.FRONT;

    public Cube(int width) {
        this.width = width;
        data = new char[width][width][width];
    }

    public int getWidth() {
        return width;
    }

    /**
     * Add given {@code shape} to the current side (i.e. to the {@link #side}). Given {@code shape} should have same with as cube. Returns {@literal
     * true} if given {@code shape} was successfully added to the current side and does not conflict with other not empty sides. After that, {@link
     * #side} will be changed to the next side. Otherwise, in case of any conflict, {@literal false} will be returned and given {@code shape} will not
     * be added.
     *
     * @param shape shape instance (should have same width as cube)
     * @return {@literal true} in case of given {@code shape} was successfully added to the current side
     */
    public boolean addNextSide(SquareShape shape) {
        checkShapeAndCubeWidth(shape);

        boolean res = sides.get(side).add(shape, data);

        if (res) {
            side = side.next();
            shapes.addLast(shape);
        } else
            sides.get(side).clear(data);

        return res;
    }

    private void checkShapeAndCubeWidth(SquareShape shape) {
        if (width != shape.getWidth())
            throw new CubeException("Shape's width does not match with cube's width");
    }

    /** Clear current side and set {@link #side} to the previous side. */
    public void clearCurrentSide() {
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

    /**
     * Check if the cube is completed. It means that all edges of all sides are taken and do not have any empty cells. Cells in the middle of the
     * side could be empty. If cube is <tt>solved</tt>, it means that we have found a solution and {@link #shapes} contains list of used shapes.
     *
     * @return {@literal true} if cube is solved and all edges are taken
     */
    public boolean isComplete() {
        return Side.isComplete(data);
    }

    /**
     * Retrieve masks of all sides of the cube. This is copy of internal masks and modification of the returned arrays is not affects internal state.
     *
     * @return not {@literal map} of masks for all sides
     */
    public Map<Side, boolean[][]> getAllSideMask() {
        Map<Side, boolean[][]> map = new EnumMap<>(Side.class);
        sides.forEach((side, cubeSide) -> map.put(side, cubeSide.mask(data)));
        return Collections.unmodifiableMap(map);
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

    /** Represents enumeration of each cube's side. Each side contains it's own implementation. */
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

        public static boolean isComplete(char[][][] data) {
            for (Side side : values())
                if (!side.cubeSide.isCompleted(data))
                    return false;
            return true;
        }

        private static Map<Cube.Side, CubeSide> getSideInstance() {
            Map<Cube.Side, CubeSide> map = new EnumMap<>(Cube.Side.class);

            for (Side side : values())
                map.put(side, side.cubeSide);

            return Collections.unmodifiableMap(map);
        }
    }

}
