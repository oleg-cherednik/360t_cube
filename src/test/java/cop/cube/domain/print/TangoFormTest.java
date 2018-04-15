package cop.cube.domain.print;

import cop.cube.domain.cube.Cube;
import cop.cube.print.TangoForm;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

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

    public void foo() throws IOException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream(); PrintStream out = new PrintStream(os, true, StandardCharsets.UTF_8.name())) {
            Cube cube = new Cube(6);
            INSTANCE.print(cube, '0', out);
            String str = new String(os.toByteArray(), StandardCharsets.UTF_8);

            int a = 0;
            a++;

        }
    }


}
