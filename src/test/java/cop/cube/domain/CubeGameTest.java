package cop.cube.domain;

import cop.cube.CubeGame;
import cop.cube.domain.cube.Cube;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 15.04.2018
 */
@Test
public class CubeGameTest {

    public void shouldFind51Solutions() {
        CubeGame cubeGame = createCubeGame();
        // TODO windows application from 'http://www.happycube.com/' retrieves on 36 solutions (it seems my solutions still retrieves duplicates)
        assertThat(cubeGame.findAllSolutions()).isEqualTo(51);
    }

    public void checkFirstSolution() {
        CubeGame cubeGame = createCubeGame();
        Cube cube = cubeGame.getFoundSolutions().iterator().next();
        List<SquareShape> usedShapes = new ArrayList<>(cube.getShapes());

        assertThat(usedShapes).hasSize(6);
        assertThat(usedShapes.get(0).getId()).isEqualTo("A-UP-OFF");
        assertThat(usedShapes.get(1).getId()).isEqualTo("C-UP-OFF");
        assertThat(usedShapes.get(2).getId()).isEqualTo("D-DOWN-ON");
        assertThat(usedShapes.get(3).getId()).isEqualTo("B-RIGHT-OFF");
        assertThat(usedShapes.get(4).getId()).isEqualTo("F-UP-OFF");
        assertThat(usedShapes.get(5).getId()).isEqualTo("E-UP-OFF");
    }

    private static CubeGame createCubeGame() {
        final char marker = 'o';
        final Set<SquareShape> shapes = new LinkedHashSet<>();
        shapes.add(SquareShape.create('A', CubeGame.convert("o.o.o\nooooo\n.ooo.\nooooo\no.o.o", marker)));
        shapes.add(SquareShape.create('B', CubeGame.convert(".o.o.\n.oooo\noooo.\n.oooo\noo.o.", marker)));
        shapes.add(SquareShape.create('C', CubeGame.convert("..o..\n.ooo.\nooooo\n.ooo.\n.o.o.", marker)));
        shapes.add(SquareShape.create('D', CubeGame.convert(".o.o.\noooo.\n.oooo\noooo.\noo.oo", marker)));
        shapes.add(SquareShape.create('E', CubeGame.convert("..o..\nooooo\n.ooo.\nooooo\n.o.oo", marker)));
        shapes.add(SquareShape.create('F', CubeGame.convert("..o..\n.ooo.\nooooo\n.ooo.\n..o..", marker)));

        return CubeGame.create(shapes);
    }

}
