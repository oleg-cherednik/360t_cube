package cop.cube.print;

import cop.cube.domain.Cube;

import java.io.PrintStream;

/**
 * Defines abstraction for printing the cube.
 *
 * @author Oleg Cherednik
 * @since 14.04.2018
 */
public interface PrintStrategy {

    /**
     * Print given {@code cube} to given {@code out} using given {@code marker} for taken cells. Ignore when {@code cube} or {@code out} is not set.
     *
     * @param cube   cube instance
     * @param marker taken cells marker
     * @param out    output stream
     */
    void print(Cube cube, char marker, PrintStream out);

}
