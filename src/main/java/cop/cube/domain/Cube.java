package cop.cube.domain;

import cop.cube.exceptions.CubeException;

import java.util.function.Predicate;

/**
 * <pre>
 *                             side:4
 *                             [004][014][024][034][044]
 *                             [003][013][023][033][043]
 *                             [002][012][022][032][042]
 *                             [001][011][021][031][041]
 *                             [000][010][020][030][040]
 *
 * side:2                      side:1                      side:5
 * [004][003][002][001][000]   [000][010][020][030][040]   [040][041][042][043][044]
 * [104][103][102][101][100]   [100][110][120][130][140]   [140][141][142][143][144]
 * [204][203][202][201][200]   [200][210][220][230][240]   [240][241][242][243][244]
 * [304][303][302][301][300]   [300][310][320][330][340]   [340][341][342][343][344]
 * [404][403][402][401][400]   [400][410][420][430][440]   [440][441][442][443][444]
 *
 *                             side:3
 *                             [400][410][420][430][440]
 *                             [401][411][421][431][441]
 *                             [402][412][422][432][442]
 *                             [403][413][423][433][443]
 *                             [404][414][424][434][444]
 *
 *                             side:6
 *                             [004][014][024][034][044]
 *                             [104][114][124][134][144]
 *                             [204][214][224][234][244]
 *                             [304][314][324][334][344]
 *                             [404][414][424][434][444]
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
        if (width != shape.getWidth()) {
            throw new CubeException("Shape's width does not match with cube's width");
        }

        if (side == Side.SIDE_1) {
            if (addSide1(shape)) {
                side = Side.SIDE_2;
                return true;
            }
            return false;
        }

        if (side == Side.SIDE_2) {
            if (addSide2(shape)) {
                side = Side.SIDE_3;
                return true;
            }
            return false;
        }

        if (side == Side.SIDE_3) {
            if (addSide3(shape)) {
                side = Side.SIDE_4;
                return true;
            }
            return false;
        }

        if (side == Side.SIDE_4) {
            if (addSide4(shape)) {
                side = Side.SIDE_5;
                return true;
            }
            return false;
        }

        if (side == Side.SIDE_5) {
            if (addSide5(shape)) {
                side = Side.SIDE_6;
                return true;
            }
            return false;
        }

        if (side == Side.SIDE_6) {
            if (addSide6(shape)) {
                side = Side.SIDE_1;
                return true;
            }
            return false;
        }

        return true;
    }

    public void removeCurrentSide() {

    }

    public boolean isComplete() {
        return isSide1Complete() && isSide2Complete() && isSide3Complete() && isSide4Complete() && isSide5Complete() && isSide6Complete();
    }

    private boolean isSide1Complete() {
        final int z = 0;

        for (int y = 0; y < width; y++)
            for (int x = 0; x < width; x++)
                if (data[y][x][z] == '\0')
                    return false;

        return true;
    }

    private boolean isSide2Complete() {
        final int x = 0;

        for (int y = 0; y < width; y++)
            for (int z = width - 1; z >= 0; z--)
                if (data[y][x][z] == '\0')
                    return false;

        return true;
    }

    private boolean isSide3Complete() {
        final int y = width - 1;

        for (int z = 0; z < width; z++)
            for (int x = 0; x < width; x++)
                if (data[y][x][z] == '\0')
                    return false;

        return true;
    }

    private boolean isSide4Complete() {
        final int y = 0;

        for (int z = width - 1; z >= 0; z--)
            for (int x = 0; x < width; x++)
                if (data[y][x][z] == '\0')
                    return false;

        return true;
    }

    private boolean isSide5Complete() {
        final int x = width - 1;

        for (int y = 0; y < width; y++)
            for (int z = 0; z < width; z++)
                if (data[y][x][z] == '\0')
                    return false;

        return true;
    }

    private boolean isSide6Complete() {
        final int z = 4;

        for (int y = 0; y < width; y++)
            for (int x = 0; x < width; x++)
                if (data[y][x][z] == '\0')
                    return false;

        return true;
    }

    private boolean addSide1(Shape shape) {
        System.out.println("side:1");

        final int z = 0;

        for (int y = 0; y < width; y++) {
            for (int x = 0; x < width; x++) {
                System.out.format("[%s%d%d]", y, x, z);
                if (shape.mask[y][x]) {
                    if (data[y][x][z] == '\0' || data[y][x][z] == '1') {
                        data[y][x][z] = '1';
                    } else {
                        // clear
                        for (int ii = 0; ii < width; ii++) {
                            for (int jj = 0; jj < width; jj++) {
                                if (data[ii][jj][z] == '\0' || data[ii][jj][z] == '1') {
                                    data[ii][jj][z] = '\0';
                                }
                            }
                        }

                        return false;
                    }
                }
                System.out.print(data[y][x][z] == '\0' ? ' ' : data[y][x][z]);
            }
            System.out.println();
        }

        return true;
    }

    private boolean addSide2(Shape shape) {
        System.out.println("side:2");

        final int x = 0;

        for (int y = 0; y < width; y++) {
            for (int z = width - 1; z >= 0; z--) {
                System.out.format("[%s%d%d]", y, x, z);
                if (shape.mask[y][z]) {
                    if (data[y][x][z] == '\0' || data[y][x][z] == '2') {
                        data[y][x][z] = '2';
                    } else {
                        // clear
                        for (int ii = 0; ii < width; ii++) {
                            for (int jj = 0; jj < width; jj++) {
                                if (data[ii][x][width - jj - 1] == '\0' || data[ii][x][width - jj - 1] == '2') {
                                    data[ii][x][width - jj - 1] = '\0';
                                }
                            }
                        }

                        return false;
                    }
                }
                System.out.print(data[y][x][z] == '\0' ? ' ' : data[y][x][z]);
            }
            System.out.println();
        }

        return true;
    }

    private boolean addSide3(Shape shape) {
        System.out.println("side:3");

        final int y = width - 1;

        for (int z = 0; z < width; z++) {
            for (int x = 0; x < width; x++) {
                System.out.format("[%s%d%d]", y, x, z);
                if (shape.mask[z][x]) {
                    if (data[y][x][z] == '\0' || data[y][x][z] == '3') {
                        data[y][x][z] = '3';
                    } else {
                        // clear
                        for (int ii = 0; ii < width; ii++) {
                            for (int jj = 0; jj < width; jj++) {
                                if (data[y][jj][ii] == '\0' || data[y][jj][ii] == '3') {
                                    data[y][jj][ii] = '\0';
                                }
                            }
                        }

                        return false;
                    }
                }
                System.out.print(data[y][x][z] == '\0' ? ' ' : data[y][x][z]);
            }
            System.out.println();
        }

        return true;
    }

    private boolean addSide4(Shape shape) {
        System.out.println("side:4");

        final int y = 0;

        for (int z = width - 1; z >= 0; z--) {
            for (int x = 0; x < width; x++) {
                System.out.format("[%s%d%d]", y, x, z);
                if (shape.mask[z][x]) {
                    if (data[y][x][z] == '\0' || data[y][x][z] == '4') {
                        data[y][x][z] = '4';
                    } else {
                        // clear
                        for (int ii = 0; ii < width; ii++) {
                            for (int jj = 0; jj < width; jj++) {
                                if (data[y][jj][ii] == '\0' || data[y][jj][ii] == '4') {
                                    data[y][jj][ii] = '\0';
                                }
                            }
                        }

                        return false;
                    }
                }
                System.out.print(data[y][x][z] == '\0' ? ' ' : data[y][x][z]);
            }
            System.out.println();
        }

        return true;
    }

    private boolean addSide5(Shape shape) {
        System.out.println("side:5");

        final int x = width - 1;

        for (int y = 0; y < width; y++) {
            for (int z = 0; z < width; z++) {
                System.out.format("[%s%d%d]", y, x, z);
                if (shape.mask[y][z]) {
                    if (data[y][x][z] == '\0' || data[y][x][z] == '5') {
                        data[y][x][z] = '5';
                    } else {
                        // clear
                        for (int ii = 0; ii < width; ii++) {
                            for (int jj = 0; jj < width; jj++) {
                                if (data[ii][x][width - jj - 1] == '\0' || data[ii][x][width - jj - 1] == '5') {
                                    data[ii][x][width - jj - 1] = '\0';
                                }
                            }
                        }

                        return false;
                    }
                }
                System.out.print(data[y][x][z] == '\0' ? ' ' : data[y][x][z]);
            }
            System.out.println();
        }

        return true;
    }

    private boolean addSide6(Shape shape) {
        System.out.println("side:6");

        final int z = 4;

        for (int y = 0; y < width; y++) {
            for (int x = 0; x < width; x++) {
                System.out.format("[%s%d%d]", y, x, z);
                if (shape.mask[y][x]) {
                    if (data[y][x][z] == '\0' || data[y][x][z] == '6') {
                        data[y][x][z] = '6';
                    } else {
                        // clear
                        for (int ii = 0; ii < width; ii++) {
                            for (int jj = 0; jj < width; jj++) {
                                if (data[ii][jj][z] == '\0' || data[ii][jj][z] == '6') {
                                    data[ii][jj][z] = '\0';
                                }
                            }
                        }

                        return false;
                    }
                }
                System.out.print(data[y][x][z] == '\0' ? ' ' : data[y][x][z]);
            }
            System.out.println();
        }

        return true;
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

}
