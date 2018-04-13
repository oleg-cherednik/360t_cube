package cop.cube.domain;

/**
 * @author Oleg Cherednik
 * @since 11.04.2018
 */
@SuppressWarnings("MethodCanBeVariableArityMethod")
public enum Direction {
    UP(0) {
        @Override
        public int hash(boolean[][] mask) {
            final int width = width(mask);
            int hash = 0;

            for (int i = 0; i < width; i++)
                hash |= bit(i, mask[0][i]);

            return hash;
        }
    },
    RIGHT(1) {
        @Override
        public int hash(boolean[][] mask) {
            final int width = width(mask);
            int hash = 0;

            for (int i = 0, j = width - 1; i < width; i++, j--)
                hash |= bit(i, mask[j][0]);

            return hash;
        }
    },
    DOWN(2) {
        @Override
        public int hash(boolean[][] mask) {
            final int width = width(mask);
            int hash = 0;

            for (int i = 0, j = width - 1; i < width; i++, j--)
                hash |= bit(i, mask[width - 1][j]);

            return hash;
        }
    },
    LEFT(3) {
        @Override
        public int hash(boolean[][] mask) {
            final int width = width(mask);
            int hash = 0;

            for (int i = 0; i < width; i++)
                hash |= bit(i, mask[i][width - 1]);

            return hash;
        }
    };

    private final int rightRotateCount;

    Direction(int rightRotateCount) {
        this.rightRotateCount = rightRotateCount;
    }

    public final void apply(boolean[][] mask) {
        if (mask != null)
            for (int i = 0; i < rightRotateCount; i++)
                Shape.rotateRight(mask);
    }

    protected static int width(boolean[][] mask) {
        return mask.length;
    }

    protected static int bit(int i, boolean taken) {
        return taken ? 1 << i : 0x0;
    }

    public abstract int hash(boolean[][] mask);
}
