package cop.cube.domain;

/**
 * @author Oleg Cherednik
 * @since 11.04.2018
 */
@SuppressWarnings("MethodCanBeVariableArityMethod")
public enum Direction {
    UP(0),
    RIGHT(1),
    DOWN(2),
    LEFT(3);

    private final int rotateRightCount;

    Direction(int rotateRightCount) {
        this.rotateRightCount = rotateRightCount;
    }

    public final void apply(boolean[][] mask) {
        if (mask != null)
            for (int i = 0; i < rotateRightCount; i++)
                Shape.rotateRight(mask);
    }
}
