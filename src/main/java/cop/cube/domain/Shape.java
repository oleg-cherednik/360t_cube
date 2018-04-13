package cop.cube.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Oleg Cherednik
 * @since 11.04.2018
 */
@SuppressWarnings("MethodCanBeVariableArityMethod")
public final class Shape {

    public static final Shape NULL = new Shape('\0', Direction.UP, Mirror.OFF, null);

    private final String id;
    private final char name;
    private final Direction direction;
    private final Mirror mirror;
    private final boolean[][] mask;

    public static Shape create(char name, boolean[][] mask) {
        return create(name, Direction.UP, Mirror.OFF, mask);
    }

    public static Shape create(char name, Direction direction, Mirror mirror, boolean[][] mask) {
        return mask != null && mask.length > 0 ? new Shape(name, direction, mirror, mask) : NULL;
    }

    @SuppressWarnings({ "MethodCanBeVariableArityMethod", "AssignmentOrReturnOfFieldWithMutableType" })
    private Shape(char name, Direction direction, Mirror mirror, boolean[][] mask) {
        id = this != NULL ? String.valueOf(name) + '-' + direction + '-' + mirror : "<empty>";
        this.name = name;
        this.direction = direction;
        this.mirror = mirror;
        this.mask = mask;
    }

    public int getWidth() {
        return getWidth(mask);
    }

    public static int getWidth(boolean[][] mask) {
        return mask != null ? mask.length : 0;
    }

    public char getName() {
        return name;
    }

    boolean isTaken(int x, int y) {
        return this != NULL && mask[y][x];
    }

    public boolean[][] getMask() {
        int width = getWidth();

        if (width == 0)
            return null;

        boolean[][] res = new boolean[width][];

        for (int y = 0; y < width; y++)
            res[y] = Arrays.copyOf(mask[y], width);

        return res;
    }

    public static String hash(boolean[][] mask) {
        if (mask == null)
            return "";

        int width = getWidth(mask);
        StringBuilder buf = new StringBuilder();

        for (int y = 0; y < width; y++)
            for (int x = 0; x < width; x++)
                buf.append(mask[y][x] ? '1' : '0');

        return buf.toString();
    }

    private List<Shape> relatedShapes = Collections.emptyList();

    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    public List<Shape> getRelatedShapes() {
        if (this == NULL || !relatedShapes.isEmpty())
            return relatedShapes;

        Set<String> hashes = new HashSet<>();
        List<Shape> shapes = new ArrayList<>();

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
        if (!(obj instanceof Shape))
            return false;
        return id.equals(((Shape)obj).id);
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
