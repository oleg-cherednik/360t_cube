package cop.cube.domain;

/**
 * Represents mirroring of the {@link Shape}. Only {@link Mirror#HORIZONTAL} mirror is supported, because vertical mirror is the same as 180 degree
 * rotation. Be default shape mirroring is {@link Mirror#OFF}.
 *
 * @author Oleg Cherednik
 * @since 13.04.2018
 */
@SuppressWarnings("MethodCanBeVariableArityMethod")
public enum Mirror {
    /** default is off */
    OFF,
    /** mirror horizontal */
    HORIZONTAL {
        @Override
        public void apply(boolean[][] mask) {
            if (mask != null) {
                final int width = mask.length;

                for (int y1 = 0, y2 = width - 1; y1 < y2; y1++, y2--)
                    for (int x = 0; x < width; x++)
                        swap(x, y1, x, y2, mask);
            }
        }
    };

    public void apply(boolean[][] mask) {
    }

    protected static void swap(int x1, int y1, int x2, int y2, boolean[][] mask) {
        boolean tmp = mask[y1][x1];
        mask[y1][x1] = mask[y2][x2];
        mask[y2][x2] = tmp;
    }
}
