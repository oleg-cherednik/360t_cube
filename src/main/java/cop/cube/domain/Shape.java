package cop.cube.domain;

import cop.cube.exceptions.CubeException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Oleg Cherednik
 * @since 11.04.2018
 */
public final class Shape {

    public static final Shape NULL = new Shape('\0', Direction.UP, null);

    private final char name;
    private final Direction direction;
    public final boolean[][] mask;

    public static Shape create(char name, String data, char marker) {
        if (data == null || data.trim().isEmpty()) {
            return NULL;
        }

        String[] rows = data.split("\n");
        int width = rows.length;
        boolean[][] mask = new boolean[width][width];

        for (int i = 0; i < width; i++) {
            String row = rows[i];

            if (row.length() != width)
                throw new CubeException("Shape should be a square");

            for (int j = 0; j < width; j++)
                mask[i][j] = row.charAt(j) == marker;
        }

        return new Shape(name, Direction.UP, mask);
    }

    public static Shape create(char name, Direction direction, boolean[][] mask) {
        return mask != null && mask.length > 0 ? new Shape(name, direction, mask) : NULL;
    }

    @SuppressWarnings({ "MethodCanBeVariableArityMethod", "AssignmentOrReturnOfFieldWithMutableType" })
    private Shape(char name, Direction direction, boolean[][] mask) {
        this.name = name;
        this.direction = direction;
        this.mask = mask;
    }

    public int getWidth() {
        return mask != null ? mask.length : 0;
    }

    public char getName() {
        return name;
    }

    public Set<Direction> getUniqueDirections() {
        if (mask == null) {
            return Collections.emptySet();
        }

        int up = getSide(Direction.UP);
        int right = getSide(Direction.RIGHT);
        int down = getSide(Direction.DOWN);
        int left = getSide(Direction.LEFT);

        Set<Direction> directions = new LinkedHashSet<>();
        Set<Integer> sides = new HashSet<>();

        if (sides.add(up)) {
            directions.add(Direction.UP);
        }
        if (sides.add(right)) {
            directions.add(Direction.RIGHT);
        }
        if (sides.add(down)) {
            directions.add(Direction.DOWN);
        }
        if (sides.add(left)) {
            directions.add(Direction.LEFT);
        }

        return directions;
    }

    public int getSide(Direction direction) {
        return direction.getSide(mask);
    }

    public Shape rotateRight() {
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

        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Shape)) {
            return false;
        }
        return Arrays.deepEquals(mask, ((Shape)obj).mask);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(mask);
    }

    @Override
    public String toString() {
        return this != NULL ? String.valueOf(name) + '-' + direction : "<empty>";
    }
}
