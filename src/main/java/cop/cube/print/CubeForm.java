package cop.cube.print;

import cop.cube.domain.Cube;

import java.io.PrintStream;
import java.util.Map;
import java.util.Objects;

/**
 * @author Oleg Cherednik
 * @since 13.04.2018
 */
public final class CubeForm {

    public static final CubeForm INSTANCE = new CubeForm();

    private CubeForm() {
    }

    public void print(Map<Cube.Side, boolean[][]> sides, char marker, PrintStream out) {
        int width = getWidth(sides);

        if (width == 0)
            return;

        for (int y = 0; y < 4; y++) {

//            for(int i = 0; i < width; i++) {
//                boolean[][] mask = sides.get(Cube.Side.SIDE_1);
//
//                for ()
//            }


            for (int x = 0; x < 3; x++) {
                if(x != 0)
                    out.print(' ');



                out.print("aa");
            }

            out.print("\n");
        }
    }

    private static int getWidth(Map<Cube.Side, boolean[][]> sides) {
        if (sides == null || sides.isEmpty())
            return 0;

        return sides.values().stream()
                    .filter(Objects::nonNull)
                    .map(mask -> mask.length)
                    .findFirst().orElse(0);
    }

}
