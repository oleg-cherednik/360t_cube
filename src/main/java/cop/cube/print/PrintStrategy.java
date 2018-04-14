package cop.cube.print;

import cop.cube.domain.Cube;

import java.io.PrintStream;

/**
 * @author Oleg Cherednik
 * @since 14.04.2018
 */
public interface PrintStrategy {

    void print(Cube cube, char marker, PrintStream out);

}
