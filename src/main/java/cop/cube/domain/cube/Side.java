package cop.cube.domain.cube;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

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

    static Map<Side, CubeSide> getSideInstance() {
        Map<Side, CubeSide> map = new EnumMap<>(Side.class);
        map.put(FRONT, FrontBackCubeSide.getFrontInstance());
        map.put(LEFT, LeftCubeSide.getInstance());
        map.put(BOTTOM, BottomCubeSide.getInstance());
        map.put(TOP, TopCubeSide.getInstance());
        map.put(RIGHT, RightCubeSide.getInstance());
        map.put(BACK, FrontBackCubeSide.getBackInstance());
        return Collections.unmodifiableMap(map);
    }
}
