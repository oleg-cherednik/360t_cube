package cop.cube.domain.cube;

import cop.cube.domain.SquareShape;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author Oleg Cherednik
 * @since 14.04.2018
 */
@SuppressWarnings("MethodCanBeVariableArityMethod")
public abstract class CubeSide {

    protected final Side side;

    protected CubeSide(Side side) {
        this.side = side;
    }

    public final Side getSide() {
        return side;
    }

    public abstract boolean isCompleted(char[][][] data);

    public abstract void clear(char[][][] data);

    public abstract boolean add(SquareShape shape, char[][][] data);

    public abstract boolean[][] mask(char[][][] data);

    protected final void clear(int x, int y, int z, char[][][] data) {
        data[y][x][z] = data[y][x][z] == side.marker ? '\0' : data[y][x][z];
    }

    protected final boolean add(boolean taken, int x, int y, int z, char[][][] data) {
        if (taken) {
            if (data[y][x][z] != '\0' && data[y][x][z] != side.marker)
                return false;

            data[y][x][z] = side.marker;
        }

        return true;
    }

    protected static int width(char[][][] data) {
        return data.length;
    }

    protected static boolean isTaken(int x, int y, int z, char[][][] data) {
        return data[y][x][z] != '\0';
    }

    static Map<Side, CubeSide> getSideInstance() {
        Map<Side, CubeSide> map = new EnumMap<>(Side.class);
        map.put(Side.FRONT, FrontBackCubeSide.getFrontInstance());
        map.put(Side.LEFT, LeftRightCubeSide.getLeftInstance());
        map.put(Side.BOTTOM, BottomCubeSide.getInstance());
        map.put(Side.TOP, TopCubeSide.getInstance());
        map.put(Side.RIGHT, LeftRightCubeSide.getRightInstance());
        map.put(Side.BACK, FrontBackCubeSide.getBackInstance());
        return Collections.unmodifiableMap(map);
    }

}
