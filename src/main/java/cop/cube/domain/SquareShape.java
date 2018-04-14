package cop.cube.domain;

import cop.cube.exceptions.CubeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Immutable shape implementation. Once created, it does not change internal state. Implementation could be used as key of collection. According to
 * the task, this shape represents only square shape (width and height should be the same).
 *
 * @author Oleg Cherednik
 * @since 11.04.2018
 */
@SuppressWarnings("MethodCanBeVariableArityMethod")
public final class SquareShape {

    /** {@link null} object, use instead of {@literal null} */
    public static final SquareShape NULL = new SquareShape('\0', Direction.UP, Mirror.OFF, null);

    private final String id;
    private final char name;
    private final Direction direction;
    private final boolean[][] mask;

    /**
     * List of unique shapes, that could be create with rotation and/or mirroring of the current shape. Only shape with {@link SquareShape#direction}
     * equals to {@link Direction#UP} could have related shapes. Current shape is included in this list as well, so to get all available shape it is
     * enough to invoke {@link #getRelatedShapes()}.
     */
    private List<SquareShape> relatedShapes = Collections.emptyList();

    public static SquareShape create(char name, boolean[][] mask) {
        return create(name, Direction.UP, Mirror.OFF, mask);
    }

    /**
     * Creates new {@link SquareShape} instance with given parameters. If given {@code mask} is not set or empty, then {@link SquareShape#NULL} object
     * will be returned.
     *
     * @param name      shape name
     * @param direction shape direction
     * @param mirror    shape mirror
     * @param mask      shape mask (should be a square)
     * @return create {@link SquareShape} object or {@link SquareShape#NULL} in case of {@code mask} is not set
     */
    public static SquareShape create(char name, Direction direction, Mirror mirror, boolean[][] mask) {
        if (mask == null || mask.length == 0)
            return NULL;

        checkMaskIsSquare(mask);
        return new SquareShape(name, direction, mirror, mask);
    }

    private static void checkMaskIsSquare(boolean[][] mask) {
        if (mask == null || mask.length == 0)
            return;

        int width = mask.length;

        for (int y = 0; y < width; y++)
            if (mask[y].length != width)
                throw new CubeException("Mask of the shape should be a square");
    }

    @SuppressWarnings({ "MethodCanBeVariableArityMethod", "AssignmentOrReturnOfFieldWithMutableType" })
    private SquareShape(char name, Direction direction, Mirror mirror, boolean[][] mask) {
        id = mask != null ? String.valueOf(name) + '-' + direction + '-' + mirror : "<empty>";
        this.name = name;
        this.direction = direction;
        this.mask = mask;
    }

    public int getWidth() {
        return getWidth(mask);
    }

    public static int getWidth(boolean[][] mask) {
        return mask != null ? mask.length : 0;
    }

    /**
     * Check if given cell is taken or not.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return {@literal true} if given cell is taken
     */
    boolean isTaken(int x, int y) {
        return this != NULL && mask[y][x];
    }

    /**
     * Retrieve {@literal null} or not empty mask. This is a full copy of the internal {@link SquareShape#mask}.
     *
     * @return {@literal null} or full copy of internal mask.
     */
    public boolean[][] getMask() {
        int width = getWidth();

        if (width == 0)
            return null;

        boolean[][] res = new boolean[width][];

        for (int y = 0; y < width; y++)
            res[y] = Arrays.copyOf(mask[y], width);

        return res;
    }

    /**
     * List of unique shapes, that could be create with rotation and/or mirroring of the current shape. Multiple invoke of this method generates this
     * list only once.
     *
     * @return not {@literal null} unmodifiable list of relates shapes
     */
    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    public List<SquareShape> getRelatedShapes() {
        if (this == NULL || !relatedShapes.isEmpty() || direction != Direction.UP)
            return relatedShapes;

        Set<String> hashes = new HashSet<>();
        List<SquareShape> shapes = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            for (Mirror mirror : Mirror.values()) {
                boolean[][] mask = getMask();
                direction.apply(mask);
                mirror.apply(mask);

                if (hashes.add(hash(mask)))
                    shapes.add(create(name, direction, mirror, mask));
            }
        }

        return relatedShapes = shapes.isEmpty() ? Collections.emptyList() : Collections.unmodifiableList(shapes);
    }

    /**
     * Retrieve unique hash string for the given {@code mask}. This string could be use to unique identify given 2D array.
     *
     * @param mask 2D array.
     * @return unique hash string; empty string for {@literal null} or empty {@code mask}
     */
    private static String hash(boolean[][] mask) {
        if (mask == null || mask.length == 0)
            return "";

        int width = getWidth(mask);
        StringBuilder buf = new StringBuilder();

        for (int y = 0; y < width; y++)
            for (int x = 0; x < width; x++)
                buf.append(mask[y][x] ? '1' : '0');

        return buf.toString();
    }

    /**
     * Rotate given {@code mask} to the right (90 degree clockwise) not using internal temporary array.
     *
     * @param mask 2D array for rotation
     */
    static void rotateRight(boolean[][] mask) {
        if (mask == null)
            return;

        boolean tmp;
        int width = mask.length;

        for (int i = 0, total = width / 2; i < total; i++) {
            for (int j = i; j < width - 1 - i; j++) {
                tmp = mask[i][j];
                mask[i][j] = mask[width - j - 1][i];
                mask[width - j - 1][i] = mask[width - i - 1][width - j - 1];
                mask[width - i - 1][width - j - 1] = mask[j][width - i - 1];
                mask[j][width - i - 1] = tmp;
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof SquareShape))
            return false;
        return id.equals(((SquareShape)obj).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id;
    }
}
