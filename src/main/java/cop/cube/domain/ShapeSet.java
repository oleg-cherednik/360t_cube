package cop.cube.domain;

import cop.cube.exceptions.CubeException;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Oleg Cherednik
 * @since 11.04.2018
 */
public final class ShapeSet implements Iterable<Shape> {

    public static final ShapeSet NULL = new ShapeSet(Collections.emptySet());
    private static final int TOTAL = 6;

    private final Set<Shape> shapes;
    private final int width;

    public static ShapeSet create(List<Supplier<Shape>> shapeSuppliers) {
        Set<Shape> shapes = Optional.ofNullable(shapeSuppliers).orElse(Collections.emptyList()).stream()
                                    .filter(Objects::nonNull)
                                    .map(Supplier::get)
                                    .filter(shape -> shape != null && shape != Shape.NULL)
                                    .collect(Collectors.toCollection(LinkedHashSet::new));

        checkTotalNumberOfShapes(shapes);
        checkShapesWithSameWidth(shapes);

        return new ShapeSet(shapes);
    }

    private static void checkTotalNumberOfShapes(Set<Shape> shapes) {
        if (shapes.size() != TOTAL)
            throw new CubeException(String.format("Found %d shapes, but exactly %d expected", shapes.size(), TOTAL));
    }

    private static void checkShapesWithSameWidth(Set<Shape> shapes) {
        Set<Integer> widths = shapes.stream().map(Shape::getWidth).collect(Collectors.toSet());

        if (widths.size() != 1)
            throw new CubeException("All shapes in the cube should be with same width");
    }

    private ShapeSet(Set<Shape> shapes) {
        this.shapes = Collections.unmodifiableSet(shapes);
        width = shapes.isEmpty() ? 0 : shapes.iterator().next().getWidth();
    }

    public int getWidth() {
        return width;
    }

    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    public Set<Shape> getShapes() {
        return shapes;
    }

    @Override
    public Iterator<Shape> iterator() {
        return shapes.iterator();
    }
}
