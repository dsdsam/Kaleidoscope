package mcln.palette;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 4/6/14
 * Time: 8:00 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MclnStatesNewPalette {

    public String getPaletteName();

//    public MclnState getMclnCreationState();

    public boolean isPairsOfOppositeStatesPalette();

    public MclnState getMclnState(Integer stateValue);

    public default List<MclnState> getPaletteAsList() {
        return null;
    }

}
