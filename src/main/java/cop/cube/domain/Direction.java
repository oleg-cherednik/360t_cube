package cop.cube.domain;

import java.util.function.Function;

/**
 * @author Oleg Cherednik
 * @since 11.04.2018
 */
@SuppressWarnings("MethodCanBeVariableArityMethod")
public enum Direction {
    UP(shape -> shape) {
        @Override
        public int side(boolean[][] mask) {
            final int width = width(mask);
            int side = 0;

            for (int i = 0; i < width; i++)
                side |= bit(i, mask[0][i]);

            return side;
        }
    },
    RIGHT(Shape::rotateRight) {
        @Override
        public int side(boolean[][] mask) {
            final int width = width(mask);
            int side = 0;

            for (int i = 0, j = width - 1; i < width; i++, j--)
                side |= bit(i, mask[j][0]);

            return side;
        }
    },
    DOWN(shape -> shape.rotateRight().rotateRight()) {
        @Override
        public int side(boolean[][] mask) {
            final int width = width(mask);
            int side = 0;

            for (int i = 0, j = width - 1; i < width; i++, j--)
                side |= bit(i, mask[width - 1][j]);

            return side;
        }
    },
    LEFT(shape -> shape.rotateRight().rotateRight().rotateRight()) {
        @Override
        public int side(boolean[][] mask) {
            final int width = width(mask);
            int side = 0;

            for (int i = 0; i < width; i++)
                side |= bit(i, mask[i][width - 1]);

            return side;
        }
    };

    private final Function<Shape, Shape> rotate;

    Direction(Function<Shape, Shape> rotate) {
        this.rotate = rotate;
    }

    public Function<Shape, Shape> rotate() {
        return rotate;
    }

    protected static int width(boolean[][] mask) {
        return mask.length;
    }

    protected static int bit(int i, boolean taken) {
        return taken ? 1 << (i + 1) : 0x0;
    }

    public abstract int side(boolean[][] mask);
}
