package mcln.palette;

import mcln.model.MclnAlgebra;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 2/8/14
 * Time: 11:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class CreationStatePalette {

    private static final Color CREATION_COLOR = new Color(0xD0D0D0);
    static final int CREATION_STATE_ID = CREATION_COLOR.getRGB() & 0xFFFFFF;

    private static final Color NOT_CREATION_COLOR = new Color(0x3F3F3F);
    static final int NOT_CREATION_STATE_ID = NOT_CREATION_COLOR.getRGB() & 0xFFFFFF;

    public static final MclnState CREATION_STATE = MclnState.createState("Creation",
            MclnAlgebra.MCL_VALUE_BASE, CREATION_STATE_ID);

    static final MclnState NOT_CREATION_STATE = MclnState.createState("Creation",
            (-1) * MclnAlgebra.MCL_VALUE_BASE, NOT_CREATION_STATE_ID);

    private CreationStatePalette(){

    }
}
