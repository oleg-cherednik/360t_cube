package cop.cube.domain.cube;

import cop.cube.domain.SquareShape;

/**
 * Represents cub side with set of method for the side. This is stateless implementation. Real data is {@code data} array, all methods work with it
 * and do not create additional temporary arrays.
 *
 * @author Oleg Cherednik
 * @since 14.04.2018
 */
@SuppressWarnings("MethodCanBeVariableArityMethod")
abstract class CubeSide {

    /** Marker is used to mark current side in the {@link Cube#data} array */
    private final char marker;

    protected CubeSide(char marker) {
        this.marker = marker;
    }

    public final char getMarker() {
        return marker;
    }

    /**
     * Check if the side is solved. It means that all edges of the side are taken and do not have any empty cells. Cells in the middle of the
     * side could be empty.
     *
     * @param data 3D data array
     * @return {@literal true} if sidecube is solved and all edges are taken
     */
    public abstract boolean isSolved(char[][][] data);

    /**
     * Clear current side.
     *
     * @param data 3D data array (will be modified)
     */
    public abstract void clear(char[][][] data);

    /**
     * Add given {@code shape} to the side. Retrieve {@literal true} in case of the shape was successfully added to the side and has no conflicts with
     * other sides.
     *
     * @param shape shape instance
     * @param data  3D data array (will be modified)
     * @return {@literal true} in case of given {@code shape} was successfully added to the side
     */
    public abstract boolean add(SquareShape shape, char[][][] data);

    /**
     * Retrieve new array that contains mask of the side. Returned array is not related to the given {@code data} and could be modified without
     * modification of {@code data}.
     *
     * @param data 3D data array
     * @return new 2D array with mask of the side
     */
    public abstract boolean[][] mask(char[][][] data);

    protected final void clear(int x, int y, int z, char[][][] data) {
        data[y][x][z] = data[y][x][z] == marker ? '\0' : data[y][x][z];
    }

    protected final boolean add(boolean taken, int x, int y, int z, char[][][] data) {
        if (taken) {
            if (data[y][x][z] != '\0' && data[y][x][z] != marker)
                return false;

            data[y][x][z] = marker;
        }

        return true;
    }

    protected static int width(char[][][] data) {
        return data.length;
    }

    protected static boolean isTaken(int x, int y, int z, char[][][] data) {
        return data[y][x][z] != '\0';
    }

}
