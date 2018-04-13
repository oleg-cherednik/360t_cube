package cop.cube;

import cop.cube.domain.Cube;
import cop.cube.domain.Direction;
import cop.cube.domain.Shape;
import cop.cube.domain.ShapeSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Supplier;

/**
 * @author Oleg Cherednik
 * @since 11.04.2018
 */
public class CubeGame {

    private static final char MARKER = 'o';

    private final ShapeSet shapeSet;
    private int totalSolution = -1;

    public CubeGame(ShapeSet shapeSet) {
        this.shapeSet = shapeSet != null ? shapeSet : ShapeSet.NULL;
    }

    public int findAllSolutions() {
        if (totalSolution == -1) {
            Cube cube = new Cube(shapeSet.getWidth());
            Queue<Shape> shapes = new LinkedList<>(shapeSet.getShapes());
            List<Cube> cubes = new ArrayList<>();

            foo(cube, shapes, cubes);


            // TODO find all solutions;
        }

        return totalSolution;
    }

    private static void foo(Cube cube, Queue<Shape> shapes, List<Cube> cubes) {
        if (shapes.isEmpty()) {
            if (cube.isComplete()) {
                // TODO copy
                cubes.add(cube);
            }
        } else {
            Shape shape = shapes.remove();

            for (Direction direction : shape.getUniqueDirections()) {
                Shape rotatedShape = direction.rotate().apply(Shape.create(shape.getName(), direction, shape.mask));

                if (cube.addNext(rotatedShape)) {
                    foo(cube, new LinkedList<>(shapes), cubes);
                    cube.removeCurrentSide();
                }
            }
        }
    }


    public static void main(String... args) {
        List<Supplier<Shape>> shapeSuppliers = Arrays.asList(SHAPE_A, SHAPE_E, SHAPE_B, SHAPE_F, SHAPE_D, SHAPE_C);
        ShapeSet shapeSet = ShapeSet.create(shapeSuppliers);
        CubeGame cubeGame = new CubeGame(shapeSet);

        int totalSolution = cubeGame.findAllSolutions();

        int a = 0;
        a++;

    }

    private static final Supplier<Shape> SHAPE_A = () -> Shape.create('A', "..o..\n.ooo.\nooooo\n.ooo.\n..o..", MARKER);
    private static final Supplier<Shape> SHAPE_B = () -> Shape.create('B', "oo.oo\n.ooo.\nooooo\n.ooo.\noo.oo", MARKER);
    private static final Supplier<Shape> SHAPE_C = () -> Shape.create('C', "..o..\n.oooo\noooo.\n.oooo\n..o..", MARKER);
    private static final Supplier<Shape> SHAPE_D = () -> Shape.create('D', "oo.oo\noooo.\n.oooo\noooo.\n.o.o.", MARKER);
    private static final Supplier<Shape> SHAPE_E = () -> Shape.create('E', "o.o..\nooooo\n.ooo.\nooooo\n.o.o.", MARKER);
    private static final Supplier<Shape> SHAPE_F = () -> Shape.create('F', ".o.o.\noooo.\n.oooo\noooo.\noo.o.", MARKER);

}
