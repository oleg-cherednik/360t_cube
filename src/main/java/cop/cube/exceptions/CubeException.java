package cop.cube.exceptions;

/**
 * @author Oleg Cherednik
 * @since 11.04.2018
 */
public class CubeException extends RuntimeException {

    private static final long serialVersionUID = -2257009909486248283L;

    public CubeException(String message) {
        super(message);
    }

}
