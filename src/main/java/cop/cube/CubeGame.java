package cop.cube;

import cop.cube.domain.Cube;
import cop.cube.domain.Direction;
import cop.cube.domain.Mirror;
import cop.cube.domain.Shape;
import cop.cube.domain.ShapeSet;
import cop.cube.exceptions.CubeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author Oleg Cherednik
 * @since 11.04.2018
 */
public final class CubeGame {

    private static final char MARKER = 'o';

    private final ShapeSet shapeSet;
    private final List<Cube> cubes = new ArrayList<>();

    private int totalSolution = -1;

    private CubeGame(ShapeSet shapeSet) {
        this.shapeSet = shapeSet != null ? shapeSet : ShapeSet.NULL;
    }

    public int findAllSolutions() {
        if (totalSolution == -1) {
            Cube cube = new Cube(shapeSet.getWidth());
            solve(cube, shapeSet.getShapes());
            totalSolution = cubes.size();
        }

        return totalSolution;
    }

    private void solve(Cube cube, Set<Shape> availableShapes) {
        if (availableShapes.isEmpty()) {
            System.out.println(cube.print(MARKER));
            System.out.println();
            if (cube.isComplete())
                cubes.add(cube.clone());
        } else {
            if(cube.shapes.size() == 5) {
                List<Shape> aa = new ArrayList<>(cube.shapes);
                if(aa.get(0).toString().equals("A-" + Direction.RIGHT + '-' + Mirror.OFF)
                        && aa.get(1).toString().equals("B-" + Direction.UP + '-' + Mirror.OFF)
                        && aa.get(2).toString().equals("F-" + Direction.UP + '-' + Mirror.OFF)
                        && aa.get(3).toString().equals("C-" + Direction.LEFT + '-' + Mirror.OFF)
                        && aa.get(4).toString().equals("D-" + Direction.LEFT + '-' + Mirror.OFF)) {
                    int bb = 0;
                    bb++;
                }
            }
            for (Shape shape : availableShapes) {
                List<Shape> relatedShapes = shape.getRelatedShapes();

                int aaa = 0;
                aaa++;

                for (Shape sh : relatedShapes) {
                    if (cube.addNextSide(sh)) {

                        System.out.println();

                        Set<Shape> aa = new LinkedHashSet<>(availableShapes);
                        aa.remove(shape);

                        solve(cube, Collections.unmodifiableSet(aa));
                        cube.removeCurrentSide();
                    } else {
//                    System.out.println(cube.print(MARKER));
//                    System.out.println();
                    }
                }
            }

            int a = 0;
            a++;
        }
    }

    public static void main(String... args) {
//        List<Supplier<Shape>> shapeSuppliers = Arrays.asList(SHAPE_A, SHAPE_E, SHAPE_B, SHAPE_F, SHAPE_D, SHAPE_C);
        List<Supplier<Shape>> shapeSuppliers = Arrays.asList(SHAPE_A, SHAPE_B, SHAPE_C, SHAPE_D, SHAPE_E, SHAPE_F);
        ShapeSet shapeSet = ShapeSet.create(shapeSuppliers);
        CubeGame cubeGame = new CubeGame(shapeSet);

        int totalSolution = cubeGame.findAllSolutions();

        int a = 0;
        a++;

    }

//    private static final Supplier<Shape> SHAPE_A = () -> Shape.create('A', convert("..o..\n.ooo.\nooooo\n.ooo.\n..o..", MARKER));
//    private static final Supplier<Shape> SHAPE_B = () -> Shape.create('B', convert("oo.oo\n.ooo.\nooooo\n.ooo.\noo.oo", MARKER));
//    private static final Supplier<Shape> SHAPE_C = () -> Shape.create('C', convert("..o..\n.oooo\noooo.\n.oooo\n..o..", MARKER));
//    private static final Supplier<Shape> SHAPE_D = () -> Shape.create('D', convert("oo.oo\noooo.\n.oooo\noooo.\n.o.o.", MARKER));
//    private static final Supplier<Shape> SHAPE_E = () -> Shape.create('E', convert("o.o..\nooooo\n.ooo.\nooooo\n.o.o.", MARKER));
//    private static final Supplier<Shape> SHAPE_F = () -> Shape.create('F', convert(".o.o.\noooo.\n.oooo\noooo.\noo.o.", MARKER));

    private static final Supplier<Shape> SHAPE_A = () -> Shape.create('A', convert("o.o.o\nooooo\n.ooo.\nooooo\no.o.o", MARKER));
    private static final Supplier<Shape> SHAPE_B = () -> Shape.create('B', convert(".o.o.\n.oooo\noooo.\n.oooo\noo.o.", MARKER));
    private static final Supplier<Shape> SHAPE_C = () -> Shape.create('C', convert("..o..\n.ooo.\nooooo\n.ooo.\n.o.o.", MARKER));
    private static final Supplier<Shape> SHAPE_D = () -> Shape.create('D', convert(".o.o.\noooo.\n.oooo\noooo.\noo.oo", MARKER));
    private static final Supplier<Shape> SHAPE_E = () -> Shape.create('E', convert("..o..\nooooo\n.ooo.\nooooo\n.o.oo", MARKER));
    private static final Supplier<Shape> SHAPE_F = () -> Shape.create('F', convert("..o..\n.ooo.\nooooo\n.ooo.\n..o..", MARKER));

    private static boolean[][] convert(String data, char marker) {
        if (data == null || data.trim().isEmpty() || marker == '\0')
            return null;

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

        return mask;
    }

}
