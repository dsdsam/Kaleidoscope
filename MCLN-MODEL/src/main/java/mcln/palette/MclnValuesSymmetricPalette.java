package mcln.palette;

import mcln.model.MclnAlgebra;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 4/5/14
 * Time: 3:59 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MclnValuesSymmetricPalette {

    int VALUE_CREATION = MclnAlgebra.MCL_CORE_MAX + 1;
    int VALUE_NOT_CREATION = -VALUE_CREATION;

    int VALUE_GRAY = MclnAlgebra.MCL_CORE_MAX + 2;
    int VALUE_NOT_GRAY = -VALUE_GRAY;

    int VALUE_RED = MclnAlgebra.MCL_CORE_MAX + 3;
    int VALUE_NOT_RED = -VALUE_RED;

    int VALUE_GREEN = MclnAlgebra.MCL_CORE_MAX + 4;
    int VALUE_NOT_GREEN = -VALUE_GREEN;

    int VALUE_BLUE = MclnAlgebra.MCL_CORE_MAX + 5;
    int VALUE_NOT_BLUE = -VALUE_BLUE;

    int VALUE_PINK = MclnAlgebra.MCL_CORE_MAX + 6;
    int VALUE_NOT_PINK = -VALUE_PINK;
}
