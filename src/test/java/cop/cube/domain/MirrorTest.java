package cop.cube.domain;

import org.testng.annotations.Test;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 13.04.2018
 */
@Test
public class MirrorTest {

    public void shouldNotMirrorWhenOffMirror() {
        boolean[][] mask = CREATE_MATRIX.get();
        Mirror.OFF.apply(mask);
        assertThat(mask).isEqualTo(new boolean[][] { { true, false, false }, { false, true, true }, { true, true, false } });
    }

    public void shouldHorizontalMirrorWhenHorizontalMirror() {
        boolean[][] mask = CREATE_MATRIX.get();
        Mirror.ON.apply(mask);
        assertThat(mask).isEqualTo(new boolean[][] { { true, true, false }, { false, true, true }, { true, false, false } });
    }

    private static final Supplier<boolean[][]> CREATE_MATRIX =
            () -> new boolean[][] {
                    { true, false, false },
                    { false, true, true },
                    { true, true, false } };

}
