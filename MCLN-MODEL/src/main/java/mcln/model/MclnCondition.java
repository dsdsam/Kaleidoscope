package mcln.model;

import mcln.palette.MclnState;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 9/1/13
 * Time: 9:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class MclnCondition extends MclnNode<MclnCondition, MclnStatement> {

    public static final String MCLN_CONDITION_XML_TAG = "Mcln-Condition";
    public static final String MCLN_CONDITION_UID_TAG = "Condition-UID";

    private String name = "";

    /**
     * @param uid
     */
    MclnCondition(String uid) {
        this(uid, new double[]{0, 0, 0});
    }

    /**
     *
     */
    MclnCondition(String uid, double[] cSysLocation) {
        this("", uid, cSysLocation);
    }

    MclnCondition(String name, String uid, double[] cSysLocation) {
        super(uid, cSysLocation);
        this.name = name;
        setInitialMclnState(MclnState.CORE_STATE_CONTRADICTION);
        setCurrentMclnState(getInitialMclnState());
    }

    public void removeInputArc(MclnArc mclnArc) {
        super.removeInputArc(mclnArc);
    }

    public void removeOutputArc(MclnArc mclnArc) {
        super.removeOutputArc(mclnArc);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public double[] getCSysLocation() {
        return super.getCSysPoint();
    }

    public void setCSysLocation(double[] cSysLocation) {
        super.setCSysPoint(cSysLocation);
    }

    public String toString() {
        return getUID() + ", " + getCurrentMclnState().toString();
    }

    //
    //  X M L i z e r
    //

    public String toXml() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(MCLN_CONDITION_XML_TAG).append(">");
        stringBuilder.append("<").append(MCLN_CONDITION_UID_TAG).append(">");
        stringBuilder.append(getUID());
        stringBuilder.append("</").append(MCLN_CONDITION_UID_TAG).append(">");
        locationToXml(stringBuilder);
        stringBuilder.append("</").append(MCLN_CONDITION_XML_TAG + ">");
        return stringBuilder.toString();
    }

    private StringBuilder locationToXml(StringBuilder stringBuilder) {
        double[] cSysLocation = getCSysLocation();
        stringBuilder.append("<X-Location>").
                append(MclnCondition.doubleToFormattedString(cSysLocation[0])).
                append("</X-Location>");
        stringBuilder.append("<Y-Location>").
                append(MclnCondition.doubleToFormattedString(cSysLocation[1])).
                append("</Y-Location>");
        return stringBuilder;
    }

    public String toTooltip() {
        tooltipBuilder.delete(0, tooltipBuilder.length());

        tooltipBuilder.append("<HTML><BODY><font face=\"sanserif\" color=\"black\">")
                .append("<p align=\"center\">")
                .append("~ ~ ~&nbsp;&nbsp;&nbsp;C o n d i t i o n&nbsp;&nbsp;&nbsp;A t t r i b u t e s&nbsp;&nbsp;&nbsp;~ ~ ~<br>")
                .append("</p>")

                .append("UID = " + getUID() + "<br>")
                .append("Current State = " + getCurrentMclnState().toString() + "<br>");

        tooltipBuilder.append("<br>");

        List<MclnArc<MclnStatement, MclnCondition>> inboundArcs = getInboundArcs();
        tooltipBuilder.append("Inp Arcs = " + inboundArcs.size() + "<br>");
        for (MclnArc<MclnStatement, MclnCondition> mclnArc : inboundArcs) {
            MclnStatement mclnStatement = mclnArc.getInpNode();
            tooltipBuilder.append("Inp statement = " + mclnStatement.toString() + "<br>");
        }
        tooltipBuilder.append("<br>");

        List<MclnArc<MclnCondition, MclnStatement>> outboundArcs = getOutboundArcs();
        tooltipBuilder.append("Out Arcs = " + outboundArcs.size() + "<br>");
        for (MclnArc<MclnCondition, MclnStatement> mclnArc : outboundArcs) {
            MclnStatement mclnStatement = mclnArc.getOutNode();
            tooltipBuilder.append("Out statement = " + mclnStatement.toString() + "<br>");
        }

        tooltipBuilder.append("</font></BODY></HTML>");
        return tooltipBuilder.toString();
    }

    /**
     * @return
     */
    @Override
    public String getOneLineInfoMessage() {
        oneLineMessageBuilder.delete(0, oneLineMessageBuilder.length());
        MclnState mclnState = getCurrentMclnState();
        String interpretation = mclnState.getStateInterpretation().replace("$", name);

        oneLineMessageBuilder.append("Node: Condition [").
                append(" ID: " + getUID() + ", ").
                append(" Subject: \"" + name + "\" ").
                append(" State Interpretation: \"" + interpretation + "\"").
                append(" ]");
        return oneLineMessageBuilder.toString();
    }
}
