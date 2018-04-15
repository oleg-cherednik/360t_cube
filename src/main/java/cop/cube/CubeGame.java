package cop.cube;

import cop.cube.domain.cube.Cube;
import cop.cube.domain.SquareShape;
import cop.cube.exceptions.CubeException;
import cop.cube.print.CubeForm;
import cop.cube.print.TangoForm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Oleg Cherednik
 * @since 11.04.2018
 */
public final class CubeGame {

    private static final char MARKER = 'o';
    private static final int TOTAL = 6;

    private final int width;
    private final Set<SquareShape> shapes;
    private final List<Cube> cubes = new ArrayList<>();

    private int totalSolution = -1;

    public static CubeGame create(Set<SquareShape> shapes) {
        shapes = shapes != null ? shapes : Collections.emptySet();

        checkTotalNumberOfShapes(shapes);
        checkShapesWithSameWidth(shapes);

        return new CubeGame(shapes);
    }

    private static void checkTotalNumberOfShapes(Set<SquareShape> shapes) {
        if (shapes.size() != TOTAL)
            throw new CubeException(String.format("Found %d shapes, but exactly %d expected", shapes.size(), TOTAL));
    }

    private static void checkShapesWithSameWidth(Set<SquareShape> shapes) {
        Set<Integer> widths = shapes.stream().map(SquareShape::getWidth).collect(Collectors.toSet());

        if (widths.size() != 1)
            throw new CubeException("All shapes in the cube should be with same width");
    }

    private CubeGame(Set<SquareShape> shapes) {
        width = shapes.isEmpty() ? 0 : shapes.iterator().next().getWidth();
        this.shapes = Collections.unmodifiableSet(shapes);
    }

    public int findAllSolutions() {
        if (totalSolution == -1) {
            solve(new Cube(width), shapes, false);
            totalSolution = cubes.size();
        }

        return totalSolution;
    }

    public List<Cube> getFoundSolutions() {
        findAllSolutions();
        return Collections.unmodifiableList(cubes);
    }

    private void solve(Cube cube, Set<SquareShape> shapes, boolean useRelated) {
        if (shapes.isEmpty() && cube.isComplete()) {
            cubes.add(cube.clone());
            return;
        }

        for (SquareShape shape : shapes) {
            List<SquareShape> relatedShapes = useRelated ? shape.getRelatedShapes() : Collections.singletonList(shape);
            Set<SquareShape> newAvailableShapes = copyAndRemoveCurrentShape(shape, shapes);

            for (SquareShape relatedShape : relatedShapes) {
                if (cube.addNextSide(relatedShape)) {
                    solve(cube, newAvailableShapes, true);
                    cube.removeCurrentSide();
                }
            }
        }
    }

    private static Set<SquareShape> copyAndRemoveCurrentShape(SquareShape shape, Set<SquareShape> shapes) {
        if (shapes.isEmpty())
            return Collections.emptySet();

        Set<SquareShape> res = new LinkedHashSet<>(shapes);
        res.remove(shape);

        return res.isEmpty() ? Collections.emptySet() : Collections.unmodifiableSet(res);
    }

    public static void main(String... args) {
        Set<SquareShape> shapes = Stream.of(SHAPE_A, SHAPE_B, SHAPE_C, SHAPE_D, SHAPE_E, SHAPE_F)
                                        .map(Supplier::get)
                                        .collect(Collectors.toSet());

        CubeGame cubeGame = create(shapes);
        int totalSolution = cubeGame.findAllSolutions();
        System.out.println("Total " + totalSolution + " found");

        if (totalSolution > 0) {
            Cube cube = cubeGame.getFoundSolutions().iterator().next();
            CubeForm.getInstance().print(cube, MARKER, System.out);
            TangoForm.getInstance().print(cube, MARKER, System.out);
        }
    }

    private static final Supplier<SquareShape> SHAPE_A = () -> SquareShape.create('A', convert("o.o.o\nooooo\n.ooo.\nooooo\no.o.o", MARKER));
    private static final Supplier<SquareShape> SHAPE_B = () -> SquareShape.create('B', convert(".o.o.\n.oooo\noooo.\n.oooo\noo.o.", MARKER));
    private static final Supplier<SquareShape> SHAPE_C = () -> SquareShape.create('C', convert("..o..\n.ooo.\nooooo\n.ooo.\n.o.o.", MARKER));
    private static final Supplier<SquareShape> SHAPE_D = () -> SquareShape.create('D', convert(".o.o.\noooo.\n.oooo\noooo.\noo.oo", MARKER));
    private static final Supplier<SquareShape> SHAPE_E = () -> SquareShape.create('E', convert("..o..\nooooo\n.ooo.\nooooo\n.o.oo", MARKER));
    private static final Supplier<SquareShape> SHAPE_F = () -> SquareShape.create('F', convert("..o..\n.ooo.\nooooo\n.ooo.\n..o..", MARKER));

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
