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
        public int getSide(boolean[][] mask) {
            int side = 0;

            for (int i = 0; i < mask.length; i++)
                if (mask[0][i])
                    side |= 1 << (i + 1);

            return side;
        }
    },
    RIGHT(Shape::rotateRight) {
        @Override
        public int getSide(boolean[][] mask) {
            int side = 0;

            for (int i = 0, j = mask.length - 1; i < mask.length; i++, j--)
                if (mask[j][0])
                    side |= 1 << (i + 1);

            return side;
        }
    },
    DOWN(shape -> shape.rotateRight().rotateRight()) {
        @Override
        public int getSide(boolean[][] mask) {
            int side = 0;

            for (int i = 0, j = mask.length - 1; i < mask.length; i++, j--)
                if (mask[mask.length - 1][j])
                    side |= 1 << (i + 1);

            return side;
        }
    },
    LEFT(shape -> shape.rotateRight().rotateRight().rotateRight()) {
        @Override
        public int getSide(boolean[][] mask) {
            int side = 0;

            for (int i = 0; i < mask.length; i++)
                if (mask[i][mask.length - 1])
                    side |= 1 << (i + 1);

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

    public abstract int getSide(boolean[][] mask);
}
