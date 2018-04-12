package cop.cube.domain;

import cop.cube.exceptions.CubeException;

import java.util.function.Predicate;

/**
 * <pre>
 *         -----
 *         | 4 |
 * -----------------
 * | 5 | 1 | 2 | 6 |
 * -----------------
 *         | 5 |
 *         -----
 * </pre>
 *
 * @author Oleg Cherednik
 * @since 11.04.2018
 */
public class Cube {

    private final int width;
    private final char[][][] data;

    public Cube(int width) {
        this.width = width;
        data = new char[width][width][width];
    }

    private Side side = Side.SIDE_1;

    public boolean addNext(Shape shape) {
        if (width != shape.getWidth())
            throw new CubeException("Shape's width does not match with cube's width");

        if (side == Side.SIDE_1) {
            if (addSide1(shape)) {
                side = Side.SIDE_2;
                return true;
            }
            return false;
        }

        return true;
    }

    public void removeCurrentSide() {

    }

    public boolean isComplete() {
        return false;
    }

    private boolean addSide1(Shape shape) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                if (shape.mask[i][j]) {
                    if (data[i][j][0] == '\0' || data[i][j][0] == '1')
                        data[i][j][0] = '1';
                    else {
                        // clear
                        for (int ii = 0; ii < width; ii++)
                            for (int jj = 0; jj < width; jj++)
                                if (data[ii][jj][0] == '\0' || data[ii][jj][0] == '1')
                                    data[ii][jj][0] = '\0';

                        return false;
                    }
                }
            }
        }

        return false;
    }

    private enum Side {
        SIDE_1,
        SIDE_2,
        SIDE_3,
        SIDE_4,
        SIDE_5,
        SIDE_6;

        public boolean add(Shape shape, boolean[][][] data) {

            return false;
        }

    }

    private Predicate<Shape> IS_VALID_1 = new Predicate<Shape>() {
        @Override
        public boolean test(Shape shape) {

            return false;
        }
    };

    public void setSide2(Shape side2) {
        this.side2 = side2;
    }

    public void setSide3(Shape side3) {
        this.side3 = side3;
    }

    public void setSide4(Shape side4) {
        this.side4 = side4;
    }

    public void setSide5(Shape side5) {
        this.side5 = side5;
    }

    public void setSide6(Shape side6) {
        this.side6 = side6;
    }


}
