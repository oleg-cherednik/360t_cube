package cop.cube.domain.cube;

/**
 * @author Oleg Cherednik
 * @since 15.04.2018
 */
public enum Side {
    FRONT('1'),
    LEFT('2'),
    BOTTOM('3'),
    TOP('4'),
    RIGHT('5'),
    BACK('6');

    final char marker;

    Side(char marker) {
        this.marker = marker;
    }

    public Side next() {
        return ordinal() == values().length - 1 ? this : values()[ordinal() + 1];
    }

    public Side previous() {
        return ordinal() == 0 ? this : values()[ordinal() - 1];
    }
}
