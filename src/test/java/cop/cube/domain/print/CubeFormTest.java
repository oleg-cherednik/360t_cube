package cop.cube.domain.print;

import cop.cube.domain.cube.Cube;
import cop.cube.print.CubeForm;
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
public class CubeFormTest {

    private static final CubeForm INSTANCE = CubeForm.getInstance();

    public void shouldNotThrowExceptionWhenNullObject() {
        INSTANCE.print(null, 'o', null);
    }

    @SuppressWarnings("DynamicRegexReplaceableByCompiledPattern")
    public void checkCubeForm() throws IOException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream(); PrintStream out = new PrintStream(os, true, StandardCharsets.UTF_8.name())) {
            Cube cube = TangoFormTest.createCube();
            INSTANCE.print(cube, 'o', out);
            String str = new String(os.toByteArray(), StandardCharsets.UTF_8);
            String[] lines = str.split("\r\n|\n|\r");
            assertThat(lines).hasSize(20);
            assertThat(lines[0]).isEqualTo("      o o        ");
            assertThat(lines[1]).isEqualTo("      ooooo      ");
            assertThat(lines[2]).isEqualTo("       ooo       ");
            assertThat(lines[3]).isEqualTo("      ooooo      ");
            assertThat(lines[4]).isEqualTo("       o o       ");
            assertThat(lines[5]).isEqualTo("  o   o o o   o  ");
            assertThat(lines[6]).isEqualTo(" ooo  ooooo  ooo ");
            assertThat(lines[7]).isEqualTo("ooooo  ooo  ooooo");
            assertThat(lines[8]).isEqualTo(" ooo  ooooo  ooo ");
            assertThat(lines[9]).isEqualTo(" o o  o o o   o  ");
            assertThat(lines[10]).isEqualTo("       o o       ");
            assertThat(lines[11]).isEqualTo("       oooo      ");
            assertThat(lines[12]).isEqualTo("      oooo       ");
            assertThat(lines[13]).isEqualTo("       oooo      ");
            assertThat(lines[14]).isEqualTo("      oo oo      ");
            assertThat(lines[15]).isEqualTo("        o        ");
            assertThat(lines[16]).isEqualTo("      ooooo      ");
            assertThat(lines[17]).isEqualTo("       ooo       ");
            assertThat(lines[18]).isEqualTo("      ooooo      ");
            assertThat(lines[19]).isEqualTo("       o oo      ");
        }
    }
}
