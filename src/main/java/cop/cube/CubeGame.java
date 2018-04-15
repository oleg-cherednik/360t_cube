package cop.cube;

import cop.cube.domain.SquareShape;
import cop.cube.domain.cube.Cube;
import cop.cube.exceptions.CubeException;
import cop.cube.print.CubeForm;
import cop.cube.print.PrintStrategy;
import cop.cube.print.TangoForm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is a game. This game contains one {@;link Cube} and at least {@link SquareShape}. This shapes could be added to the cube and check if this
 * cube <tt>solved</tt> or not. All found solutions could be send to the one of print strategy {@link PrintStrategy}.
 *
 * @author Oleg Cherednik
 * @since 11.04.2018
 */
public final class CubeGame {

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
        int min = Cube.Side.values().length;

        if (shapes.size() < min)
            throw new CubeException(String.format("Found %d shapes, but expected at least %d", shapes.size(), min));
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
        if (shapes.isEmpty() && cube.isSolved()) {
            cubes.add(cube.clone());
            return;
        }

        for (SquareShape shape : shapes) {
            List<SquareShape> relatedShapes = useRelated ? shape.getRelatedShapes() : Collections.singletonList(shape);
            Set<SquareShape> newAvailableShapes = copyAndRemoveCurrentShape(shape, shapes);

            for (SquareShape relatedShape : relatedShapes) {
                if (cube.addNextSide(relatedShape)) {
                    solve(cube, newAvailableShapes, true);
                    cube.clearCurrentSide();
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
        final char marker = 'o';
        final Set<SquareShape> shapes = new LinkedHashSet<>();
        shapes.add(SquareShape.create('A', convert("o.o.o\nooooo\n.ooo.\nooooo\no.o.o", marker)));
        shapes.add(SquareShape.create('B', convert(".o.o.\n.oooo\noooo.\n.oooo\noo.o.", marker)));
        shapes.add(SquareShape.create('C', convert("..o..\n.ooo.\nooooo\n.ooo.\n.o.o.", marker)));
        shapes.add(SquareShape.create('D', convert(".o.o.\noooo.\n.oooo\noooo.\noo.oo", marker)));
        shapes.add(SquareShape.create('E', convert("..o..\nooooo\n.ooo.\nooooo\n.o.oo", marker)));
        shapes.add(SquareShape.create('F', convert("..o..\n.ooo.\nooooo\n.ooo.\n..o..", marker)));

        CubeGame cubeGame = create(shapes);
        int totalSolution = cubeGame.findAllSolutions();
        System.out.println("Total " + totalSolution + " found");

        if (totalSolution > 0) {
            Cube cube = cubeGame.getFoundSolutions().iterator().next();
            CubeForm.getInstance().print(cube, marker, System.out);
            TangoForm.getInstance().print(cube, marker, System.out);
        }
    }

    public static boolean[][] convert(String data, char marker) {
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
