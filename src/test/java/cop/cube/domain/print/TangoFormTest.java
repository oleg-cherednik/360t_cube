package cop.cube.domain.print;

import cop.cube.CubeGame;
import cop.cube.domain.SquareShape;
import cop.cube.domain.cube.Cube;
import cop.cube.print.TangoForm;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 15.04.2018
 */
@Test
public class TangoFormTest {

    private static final TangoForm INSTANCE = TangoForm.getInstance();

    public void shouldNotThrowExceptionWhenNullObject() {
        INSTANCE.print(null, 'o', null);
    }

    @SuppressWarnings("DynamicRegexReplaceableByCompiledPattern")
    public void checkTangoForm() throws IOException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream(); PrintStream out = new PrintStream(os, true, StandardCharsets.UTF_8.name())) {
            Cube cube = createCube();
            INSTANCE.print(cube, 'o', out);
            String str = new String(os.toByteArray(), StandardCharsets.UTF_8);
            String[] lines = str.split("\r\n|\n|\r");
            assertThat(lines).hasSize(5);
            assertThat(lines[0]).isEqualTo("o o o   o    o o  o o     o     o  ");
            assertThat(lines[1]).isEqualTo("ooooo  ooo   oooo ooooo  ooo  ooooo");
            assertThat(lines[2]).isEqualTo(" ooo  ooooo oooo   ooo  ooooo  ooo ");
            assertThat(lines[3]).isEqualTo("ooooo  ooo   oooo ooooo  ooo  ooooo");
            assertThat(lines[4]).isEqualTo("o o o  o o  oo oo  o o    o    o oo");
        }
    }

    static Cube createCube() {
        final char marker = 'o';
        Cube cube = new Cube(5);
        cube.addNextSide(SquareShape.create('A', CubeGame.convert("o.o.o\nooooo\n.ooo.\nooooo\no.o.o", marker)));
        cube.addNextSide(SquareShape.create('B', CubeGame.convert("..o..\n.ooo.\nooooo\n.ooo.\n.o.o.", marker)));
        cube.addNextSide(SquareShape.create('C', CubeGame.convert(".o.o.\n.oooo\noooo.\n.oooo\noo.oo", marker)));
        cube.addNextSide(SquareShape.create('D', CubeGame.convert("o.o..\nooooo\n.ooo.\nooooo\n.o.o.", marker)));
        cube.addNextSide(SquareShape.create('E', CubeGame.convert("..o..\n.ooo.\nooooo\n.ooo.\n..o..", marker)));
        cube.addNextSide(SquareShape.create('F', CubeGame.convert("..o..\nooooo\n.ooo.\nooooo\n.o.oo", marker)));
        return cube;
    }

}
