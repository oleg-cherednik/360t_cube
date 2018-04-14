package cop.cube.domain;

/**
 * Represents rotate direction of the {@link Shape}. {@link Direction#UP} is the default orientation.
 *
 * @author Oleg Cherednik
 * @since 11.04.2018
 */
@SuppressWarnings("MethodCanBeVariableArityMethod")
public enum Direction {
    /** default orientation */
    UP(0),
    /** rotate 90 degree clockwise */
    RIGHT(1),
    /** rotate 180 degree */
    DOWN(2),
    /** rotate 90 degree counterclockwise */
    LEFT(3);

    private final int rotateRightCount;

    Direction(int rotateRightCount) {
        this.rotateRightCount = rotateRightCount;
    }

    public final void apply(boolean[][] maskUpDirection) {
        if (maskUpDirection != null)
            for (int i = 0; i < rotateRightCount; i++)
                Shape.rotateRight(maskUpDirection);
    }
}
