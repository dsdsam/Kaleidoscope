package mclnmatrix.app;

import java.util.*;

public class TraceLogCorrespondenceBuilder {

    static List<String>[] logStatesAtStep = new ArrayList[3];
    static List<List<String>[]> dependencyPairListPairs = new ArrayList();
    static List<Dependency> stateDependencyPerStep = new ArrayList();

    static boolean traceLogToCorrespondence(String modelName, List<String> propertyNameList, List<List<String>> traceLogList) {


        traceLogListToDependenctPairList(traceLogList);
        //
        printStateDependencyPerStep(stateDependencyPerStep);
//            printTransitionStepPairs(dependencyPairListPairs);
        //
        findPremiseForConclusion(propertyNameList, stateDependencyPerStep);
        //
//            printTransitionStepPairs(dependencyPairListPairs);


        return true;
    }

    private static void findPremiseForConclusion(List<String> propertyNameList,
                                                 List<Dependency> stateDependencyPerStep) {
        int dependenciesSize = stateDependencyPerStep.size();
        Map<ChangedState, List<DependencyRelation>> independentVariablesMap = new HashMap();
        Map<ChangedState, List<DependencyRelation>> dependentVariableDependenciesMap = new HashMap();

//        List<DependencyRelation> independentVariablesList = new ArrayList();
//        List<DependencyRelation> dependentVariableDependenciesList = new ArrayList();

        int foundDependenciesCnt = 0;

        // for each dependency
        List<Integer> processesDependencies = new ArrayList();
        for (int i = 0; i < dependenciesSize; i++) {
            Dependency dependency = stateDependencyPerStep.get(i);
            List<ChangedState> changedStates = dependency.getChangedStates();
            if (changedStates.size() == 0) {
//                System.out.println();
                continue;
            }

            // for each property
            for (int propertyIndex = 0; propertyIndex < changedStates.size(); propertyIndex++) {
//                for (int propertyIndex = 0; propertyIndex < propertyNameList.size(); propertyIndex++) {
                Set<State> intersectionStatesSet = new HashSet();
                String propertyID = propertyNameList.get(propertyIndex);
                System.out.println("Processing property " + propertyID + "\n");


                // do search for each changed state
//                propertyIndex = 1;
                ChangedState changedConclusionState = changedStates.get(propertyIndex);
                System.out.println("Searching dependency for conclusion: " +
                        changedConclusionState.getStateIndex() +
                        "  " + changedConclusionState.getFromValue() +
                        "  " + changedConclusionState.getToValue() +
                        "\n");
                int conclusionStateIndex = changedConclusionState.getStateIndex();
                String conclusionChangedStateValue = changedConclusionState.getToValue();
                Set<State> prevStateAsSet = dependency.getPrevStateAsSet();
                intersectionStatesSet.addAll(prevStateAsSet);

                // loop via all other dependency and check if other conclusion
                // has same state changed. Collect all premises that make same conclusion changed
                // After all premises are processed a Dependency Relation is created
                for (int j = i + 1; j < dependenciesSize; j++) {
                    Dependency nextDependency = stateDependencyPerStep.get(j);
                    List<ChangedState> statesChangedOnNextStep = nextDependency.getChangedStates();

                    boolean conclusionFound = false;
                    // loop through all other conclusion changed states
                    for (int k = 0; k < statesChangedOnNextStep.size(); k++) {
                        ChangedState otherConclusionChangedState = statesChangedOnNextStep.get(k);
                        int otherConclusionChangedStateIndex = otherConclusionChangedState.getStateIndex();
                        String otherConclusionChangedStateValue = otherConclusionChangedState.getToValue();
                        if (conclusionStateIndex == otherConclusionChangedStateIndex &&
                                conclusionChangedStateValue.equals(otherConclusionChangedStateValue)) {
                            conclusionFound = true;
                            break;
                        }
                    }
                    if (!conclusionFound) {
                        continue;
                    }
                    Set<State> nextStates = nextDependency.getPrevStateAsSet();
                    State[] newElements = nextStates.toArray(new State[0]);
                    State newElement = newElements[0];
                    for (State element : intersectionStatesSet) {
                        boolean same = element.equals(newElement);
                        System.out.println();
                    }
                    intersectionStatesSet.retainAll(nextStates);
                    System.out.println();
                    // other dependencies are processed
                }
                // conclusion processed
                List<State> premisesAsListOfStates = new ArrayList(intersectionStatesSet);
                DependencyRelation dependencyRelation = new DependencyRelation(dependency.getLineNumber(),
                        changedConclusionState, premisesAsListOfStates);
                String dependencyRelationAsString = dependencyRelation.toString();
                System.out.println(dependencyRelationAsString);
                System.out.println();
                if (intersectionStatesSet.isEmpty()) {
                    List<DependencyRelation> dependencyRelationList = independentVariablesMap.get(changedConclusionState);
                    if (dependencyRelationList == null) {
                        dependencyRelationList = new ArrayList();
                        independentVariablesMap.put(changedConclusionState, dependencyRelationList);
                    }
                    dependencyRelationList.add(dependencyRelation);
//                    independentVariablesList.add(dependencyRelation);
                } else {
                    List<DependencyRelation> dependencyRelationList = dependentVariableDependenciesMap.get(changedConclusionState);
                    if (dependencyRelationList == null) {
                        dependencyRelationList = new ArrayList();
                        dependentVariableDependenciesMap.put(changedConclusionState, dependencyRelationList);
                    }
                    dependencyRelationList.add(dependencyRelation);
//                    dependentVariableDependenciesList.add(dependencyRelation);
                }

                foundDependenciesCnt++;
                System.out.println();
            } // end of conclusions loop

            // creating list of processed dependencies -
            // dependencies where rule's conclusion has changed
            processesDependencies.add(i);
            System.out.println();
        }

        printDependencyRules("Independent variables", independentVariablesMap);
        System.out.println();
        printDependencyRules("Dependent variables", dependentVariableDependenciesMap);
        System.out.println();

//        System.out.println("\nRemoving independent states\n");
//        for (DependencyRelation independentVariableDependency : independentVariablesList) {
//            State stateToBeRemove = independentVariableDependency.getConclusionVariable().getConclusionState();
//            for (DependencyRelation dependentVariableDependency : dependentVariableDependenciesList) {
//                dependentVariableDependency.removePremiseState(stateToBeRemove);
//            }
//        }

        // End of Search for Dependency Relations
//        printDependencyRules("Independent variables", independentVariablesList);
//        printDependencyRules("Dependent variables", dependentVariableDependenciesList);
        System.out.println();
    }

    private static void printDependencyRules(String header, Map<ChangedState, List<DependencyRelation>> dependencyMapToPrint) {
        System.out.println("\n" + header + ":\n");
        Set<ChangedState> dependencyMapKeys = dependencyMapToPrint.keySet();
        for (ChangedState changedState : dependencyMapKeys) {
            List<DependencyRelation> dependencyRelationsList = dependencyMapToPrint.get(changedState);
            System.out.println("For Conclusion State: " + "  " + changedState);
            int cnt = 0;
            for (DependencyRelation dependency : dependencyRelationsList) {
                System.out.println("     Rule # " + ++cnt + ":  Log line  "+dependency.getLineNumber()+" rule  " + dependency);
            }
        }
        System.out.println("\n");
    }

    private static void traceLogListToDependenctPairList(List<List<String>> traceLogList) {
        dependencyPairListPairs.clear();
        stateDependencyPerStep.clear();
        logStatesAtStep = new ArrayList[3];
        for (int lineIndex = 0; lineIndex < traceLogList.size(); lineIndex++) {
            List<String> entry = traceLogList.get(lineIndex);

            if (logStatesAtStep[0] == null) {
                logStatesAtStep[0] = entry;
            } else if (logStatesAtStep[1] == null) {
                logStatesAtStep[1] = entry;
            } else {
                logStatesAtStep[2] = entry;
                dependencyPairListPairs.add(logStatesAtStep);
                Dependency dependency = Dependency.createInstance(lineIndex+1, logStatesAtStep);
                stateDependencyPerStep.add(dependency);
                List<String>[] newlogStatesAtStep = new ArrayList[3];
                newlogStatesAtStep[0] = logStatesAtStep[1];
                newlogStatesAtStep[1] = logStatesAtStep[2];
                logStatesAtStep = newlogStatesAtStep;

            }

//            if (logStatesAtStep[0] == null) {
//                logStatesAtStep[0] = entry;
//            } else {
//                logStatesAtStep[1] = entry;
//                dependencyPairListPairs.add(logStatesAtStep);
//                logStatesAtStep = new ArrayList[3];
//                logStatesAtStep[0] = entry;
//            }
        }

    }


    private static void printTransitionStepPairs(List<List<String>[]> dependencyPairListPairs) {
        int cnt = 0;
        for (List<String>[] stepPair : dependencyPairListPairs) {
            String prevPrevStateString = stateListToString(stepPair[0]);
            String prevStateString = stateListToString(stepPair[1]);
            String nextStateString = stateListToString(stepPair[2]);
            System.out.println("Dependency pair " + cnt++);
            System.out.println("PrPr: " + prevPrevStateString);
            System.out.println("Prev: " + prevStateString);
            System.out.println("Next: " + nextStateString);
//            if(stepPair[2] != null){
//                String newStateString = stateListToString(stepPair[2]);
//                System.out.println("repl: " + newStateString);
//            }
            System.out.println("");
        }
    }

    private static void printStateDependencyPerStep(List<Dependency> stateDependencyPerStep) {
        int cnt = 0;
        for (Dependency dependency : stateDependencyPerStep) {
            System.out.println("Dependency pair " + cnt++);
            dependency.printStepStatesDependency();
            System.out.println("");
        }
    }

    private static String stateListToString(List<String> states) {
        StringBuilder sb = new StringBuilder();
        for (String state : states) {
            if (sb.length() != 0) {
                sb.append(",");
            }
            sb.append(state);
        }
        String allStatesString = sb.toString();
        return sb.toString();
    }

    private static void findPremiseForConclusionXXX(List<Dependency> stateDependencyPerStep) {
        int cnt = 0;
        int stepsSize = stateDependencyPerStep.size();
        for (int i = 1; i < stepsSize; i++) {
            Dependency prevDependency = stateDependencyPerStep.get(i - 1);
            Dependency nextDependency = stateDependencyPerStep.get(i);

//            System.out.println("Dependency pair " + cnt++);
//            dependency.printStepStatesDependency();
//            System.out.println("");
        }
//        for (Dependency dependency : stateDependencyPerStep) {
//            System.out.println("Dependency pair " + cnt++);
//            dependency.printStepStatesDependency();
//            System.out.println("");
//        }
    }
}

class Dependency {

    public static Dependency createInstance(int lineNumber, List<String>[] logStatesAtStep) {
        return new Dependency(lineNumber, logStatesAtStep);
    }

//    final SortedSet<VariableValuePair> premises = new TreeSet();
//    final List<VariableValuePair> premisesAsList = new ArrayList(premises);
//    final SortedSet<VariableValuePair> conclusions = new TreeSet();
//    final List<VariableValuePair> conclusionsAsList = new ArrayList(premises);

    private final int lineNumber;
    private final List<String>[] logStatesAtStep;
    private final List<String> prprState;
    private final List<String> prevState;
    private final List<String> nextState;

    private final String prevPrevStateString;
    private final String prevStateString;
    private final String nextStateString;

    private final Set<State> prevStateAsSet = new HashSet();
    private final List<ChangedState> changedStates = new ArrayList();

    private Dependency(int lineNumber, List<String>[] logStatesAtStep) {
        this.lineNumber = lineNumber;
        this.logStatesAtStep = logStatesAtStep;
        prprState = logStatesAtStep[0];
        prevState = logStatesAtStep[1];
        nextState = logStatesAtStep[2];

        prevPrevStateString = stateListToString(prprState);
        prevStateString = stateListToString(prevState);
        nextStateString = stateListToString(nextState);

        whatHasChangedAfterTransition(logStatesAtStep);
//        System.out.println();
    }

    public int getLineNumber() {
        return lineNumber;
    }

    private void whatHasChangedAfterTransition(List<String>[] logStatesAtStep) {
        changedStates.clear();
        for (int i = 0; i < prevState.size(); i++) {
            String fromValue = prevState.get(i);
            State state = State.createInstance(i, fromValue);
            prevStateAsSet.add(state);
            String toValue = nextState.get(i);
            String result = AoSUtils.applied(fromValue, toValue);
            if (!result.equals("!")) {
                continue;
            }
            ChangedState changedState = ChangedState.createInstance(i, fromValue, toValue);
            changedStates.add(changedState);
        }
    }

    public Set<State> getPrevStateAsSet() {
        return prevStateAsSet;
    }

    public List<ChangedState> getChangedStates() {
        return changedStates;
    }

    void printStepStatesDependency() {
        String prevPrevStateString = stateListToString(logStatesAtStep[0]);
        String prevStateString = stateListToString(logStatesAtStep[1]);
        String nextStateString = stateListToString(logStatesAtStep[2]);
//        System.out.println("Dependency pair " + cnt++);
        System.out.println("PrPr: " + prevPrevStateString);
        System.out.println("Prev: " + prevStateString);
        System.out.println("Next: " + nextStateString);
//            if(stepPair[2] != null){
//                String newStateString = stateListToString(stepPair[2]);
//                System.out.println("repl: " + newStateString);
//            }
    }

    private static String stateListToString(List<String> states) {
        StringBuilder sb = new StringBuilder();
        for (String state : states) {
            if (sb.length() != 0) {
                sb.append(",");
            }
            sb.append(state);
        }
        String allStatesString = sb.toString();
        return sb.toString();
    }

//    final void addPremisePair(String variableName, String value) {
//        VariableValuePair variableValuePair = VariableValuePair.creteInstance(variableName, value);
//        addPremisePair(variableValuePair);
//    }
//
//    final void addPremisePair(VariableValuePair variableValuePair) {
//        premises.add(variableValuePair);
//        premisesAsList.clear();
//        premisesAsList.addAll(premises);
//    }
//
//    final void addConclusionPair(String variableName, String value) {
//        VariableValuePair variableValuePair = VariableValuePair.creteInstance(variableName, value);
//        addConclusionPair(variableValuePair);
//    }
//
//    final void addConclusionPair(VariableValuePair variableValuePair) {
//        conclusions.add(variableValuePair);
//        // this should be done after each addition to have conclusionsAsList sorted
//        conclusionsAsList.clear();
//        conclusionsAsList.addAll(conclusions);
//    }
//
//    final void addAllConclusionPairs(List<VariableValuePair> variableValuePairs) {
//        conclusions.addAll(variableValuePairs);
//        // this should be done after each addition to have conclusionsAsList sorted
//        conclusionsAsList.clear();
//        conclusionsAsList.addAll(conclusions);
//    }
//
//    public final boolean hasEqualPremises(Correspondence otherCorrespondence) {
//        int premisesSize = premisesAsList.size();
//        if (premisesSize != otherCorrespondence.premisesAsList.size()) {
//            return false;
//        }
//        for (int i = 0; i < premisesSize; i++) {
//            VariableValuePair variableValuePair = premisesAsList.get(i);
//            VariableValuePair otherVariableValuePair = otherCorrespondence.premisesAsList.get(i);
//            boolean same = variableValuePair.equals(otherVariableValuePair);
//            if (!same) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    public final List<VariableValuePair> areThereNewConclusionsToBeAdded(Correspondence newCorrespondence) {
//        List<VariableValuePair> conclusionsToAdd = new ArrayList();
//        int otherConclusionsSize = newCorrespondence.conclusions.size();
//        for (int i = 0; i < otherConclusionsSize; i++) {
//            VariableValuePair newConclusionVariableValuePair = newCorrespondence.conclusionsAsList.get(i);
//            boolean exist = conclusions.contains(newConclusionVariableValuePair);
//            if (!exist) {
//                conclusionsToAdd.add(newConclusionVariableValuePair);
//            }
//        }
//        return conclusionsToAdd;
//    }


}

class State {

    static State createInstance(int stateIndex, String value) {
        return new State(stateIndex, value);
    }

    private final int stateIndex;
    private final String value;

    private State(int stateIndex, String value) {
        this.stateIndex = stateIndex;
        this.value = value;
    }

    public int getStateIndex() {
        return stateIndex;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return stateIndex + value.hashCode();
    }

    @Override
    public boolean equals(Object otherObj) {
        if (super.equals(otherObj)) {
            return true;
        }
        if (!(otherObj instanceof State)) {
            return false;
        }
        State otherValue = (State) otherObj;
        boolean same = stateIndex == otherValue.stateIndex && value.equals(otherValue.value);
        return stateIndex == otherValue.stateIndex && value.equals(otherValue.value);
    }
}

class ChangedState {

    static ChangedState createInstance(int stateIndex, String fromValue, String toValue) {
        return new ChangedState(stateIndex, fromValue, toValue);
    }

    private final int stateIndex;
    private final String fromValue;
    private final String toValue;
    private final State conclusionState;

    private ChangedState(int stateIndex, String fromValue, String toValue) {
        this.stateIndex = stateIndex;
        this.fromValue = fromValue;
        this.toValue = toValue;
        conclusionState = State.createInstance(stateIndex, toValue);
    }

    public int getStateIndex() {
        return stateIndex;
    }

    public String getFromValue() {
        return fromValue;
    }

    public String getToValue() {
        return toValue;
    }

    public State getConclusionState() {
        return conclusionState;
    }


    public boolean equals(Object otherObj) {
        if (super.equals(otherObj)) {
            return true;
        }
        if (!(otherObj instanceof ChangedState)) {
            return false;
        }
        ChangedState otherValue = (ChangedState) otherObj;
        return stateIndex == otherValue.stateIndex && fromValue.equals(otherValue.fromValue);
    }

    @Override
    public String toString() {
        return "State index = " + stateIndex + " changed from \"" + fromValue + "\"  to \"" + toValue+"\"";
    }
}

class DependencyRelation {

    private final int lineNumber;
    private ChangedState conclusionState;
    private List<State> premises;

    DependencyRelation(int lineNumber, ChangedState conclusionState, List<State> premises) {
        this.lineNumber = lineNumber;
        this.conclusionState = conclusionState;
        this.premises = premises;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    ChangedState getConclusionVariable() {
        return conclusionState;
    }

    void removePremiseState(State stateToBeRemoved) {
        premises.remove(stateToBeRemoved);
    }

    @Override
    public String toString() {
        String conclusionStr = conclusionState.getToValue();
        int conclusionVariableIndex = conclusionState.getStateIndex();
        StringBuilder sb = new StringBuilder();
        sb.append("Dependency Relation:");
        sb.append(" (Conclusion Index = " + conclusionVariableIndex + ") ");
        sb.append("\"").append(conclusionStr).append("\"");
        sb.append("  <=  ");
        int premisesSize = premises.size();
        sb.append("\"");
        for (int i = 0; i < premisesSize; i++) {
            State premiseState = premises.get(i);
            sb.append(premiseState.getValue());
            if (i < (premisesSize - 1)) {
                sb.append("\" : \"");
            } else {
                sb.append("\";");
            }
        }
//        sb.append("\"");
        String stringRepresentation = sb.toString();
        return stringRepresentation;
    }
}