package org.tha3rav.rimaz.core.OOP;

import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.Statements.AbstractStatement;
import org.tha3rav.rimaz.core.Statements.GetterCallStatement;
import org.tha3rav.rimaz.core.Statements.InitializationStatement;
import org.tha3rav.rimaz.core.Statements.InstanceFieldAssignmentStatement;
import org.tha3rav.rimaz.core.Statements.InstanceFieldReadingStatement;
import org.tha3rav.rimaz.core.Statements.MethodCallStatement;
import org.tha3rav.rimaz.core.Statements.SetterCallStatement;
import org.tha3rav.rimaz.core.Statements.StatementsFilter;
import org.tha3rav.rimaz.core.Statements.StaticFieldAssignmentStatement;
import org.tha3rav.rimaz.core.Statements.StaticFieldReadingStatement;
import org.tha3rav.rimaz.core.concurrent.AbstractConcurrentOperation;
import org.tha3rav.rimaz.core.concurrent.ConcurrentOperationsFilter;
import org.tha3rav.rimaz.core.concurrent.IConcurrentOperationsFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import soot.AbstractSootFieldRef;
import soot.Body;
import soot.MethodSource;
import soot.RefType;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.StaticFieldRef;
import soot.jimple.internal.AbstractInvokeExpr;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInstanceFieldRef;
import soot.jimple.internal.JInterfaceInvokeExpr;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.internal.JSpecialInvokeExpr;
import soot.jimple.internal.JStaticInvokeExpr;
import soot.jimple.internal.JVirtualInvokeExpr;

import static org.tha3rav.rimaz.utils.OOP.OOPConstants.Numbers.ZERO;
import static org.tha3rav.rimaz.utils.OOP.OOPConstants.Symbols.EMPTY_STRING;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.fieldSignatureContainsType;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.getAllUpperTypes;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.getMethodBody;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.isReferenceTypeAssignment;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.typeHasGenericTypeParameter;

public abstract class AbstractMethod
{
    protected SootMethod                             sootMethod;
    protected String                                 name;
    protected ApkTypeEntry                           containingType;
    protected List<Unit>                             methodSootStatements;
    protected List<AbstractStatement>                allStatement;
    protected List<InitializationStatement>          initializationStatements;
    protected List<InstanceFieldAssignmentStatement> instanceFieldAssignmentStatements;
    protected List<InstanceFieldReadingStatement>    instanceFieldReadingStatements;
    protected List<StaticFieldAssignmentStatement>   staticFieldAssignmentStatements;
    protected List<StaticFieldReadingStatement>      staticFieldReadingStatements;
    protected List<MethodCallStatement>              methodCallStatements;
    protected List<GetterCallStatement>              getterCallStatements;
    protected List<SetterCallStatement>              setterCallStatements;
    protected boolean areStatementsLoaded;

    public AbstractMethod(SootMethod sootMethod)
    {
        this.setSootMethod(sootMethod);
        this.name = sootMethod.getName();

        if (RetrieveAllStatements() != null)
        {
            this.setMethodSootStatements(RetrieveAllStatements());
        }
    }

    public AbstractMethod(SootMethod sootMethod, ApkTypeEntry containingType)
    {
        this.setSootMethod(sootMethod);
        this.name = sootMethod.getName();
        this.containingType = containingType;

        if (RetrieveAllStatements() != null)
        {
            this.setMethodSootStatements(RetrieveAllStatements());
        }
    }

    public void loadStatements()
    {
        if (areStatementsLoaded)
        {
            return;
        }
        areStatementsLoaded = true;
        StatementsFilter statementsFilter = new StatementsFilter(getMethodSootStatements(), this, this.containingType);
        this.setAllStatement(statementsFilter.filter());
        this.setInitializationStatements(statementsFilter.getInitializationStatements());
        this.setInstanceFieldAssignmentStatements(statementsFilter.getInstanceFieldAssignmentStatements());
        this.setInstanceFieldReadingStatements(statementsFilter.getInstanceFieldReadingStatements());
        this.setStaticFieldAssignmentStatements(statementsFilter.getStaticFieldAssignmentStatements());
        this.setStaticFieldReadingStatements(statementsFilter.getStaticFieldReadingStatements());
        this.setMethodCallStatements(statementsFilter.getMethodCallStatements());
        this.setGetterCallStatements(statementsFilter.getGetterCallStatements());
        this.setSetterCallStatements(statementsFilter.getSetterCallStatements());
    }

    public SootMethod getSootMethod()
    {
        return sootMethod;
    }

    public void setSootMethod(SootMethod sootMethod)
    {
        this.sootMethod = sootMethod;
    }

    public Tuple<Boolean,List<AbstractStatement>> hasReadingManipulations(List<ApkClass> classes)
    {
        loadStatements();
        Tuple<Boolean,List<AbstractStatement>> readingManipulations = new Tuple<>(false, new ArrayList<>());
        Boolean hasReadingManipulations;
        List<AbstractStatement> readingStatements = new ArrayList<>();

        MethodFactory methodFactory = new MethodFactory();

        List<AbstractStatement> getterCallsStatements = getGetterCallStatements().stream()
                .filter(statement -> (classes.contains(statement.getInvokedObjectType())))
                .collect(Collectors.toList());

        List<AbstractStatement> instanceFieldReadingsStatements = getInstanceFieldReadingStatements().stream()
                .filter(statement -> (classes.contains(statement.getInvokedObjectType()))||
                                     (classes.stream().anyMatch(apkClass -> fieldSignatureContainsType(statement.getField().getSootField(), apkClass.getSootClass().getName()))))
                .collect(Collectors.toList());

        List<AbstractStatement> staticFieldReadingsStatements = getStaticFieldReadingStatements().stream()
                .filter(statement -> (classes.contains(statement.getInvokedObjectType()))||
                                     (classes.stream().anyMatch(apkClass -> fieldSignatureContainsType(statement.getField().getSootField(), apkClass.getSootClass().getName()))))
                .collect(Collectors.toList());

        readingStatements.addAll(getterCallsStatements);
        readingStatements.addAll(instanceFieldReadingsStatements);
        readingStatements.addAll(staticFieldReadingsStatements);

        readingManipulations.setSecondParameter(readingStatements);

        int getterCallsCount = getterCallsStatements.size();
        int instanceFieldReadingsCount = instanceFieldReadingsStatements.size();
        int staticFieldReadingsCount = staticFieldReadingsStatements.size();

        hasReadingManipulations = (instanceFieldReadingsCount +
                                   staticFieldReadingsCount +
                                   getterCallsCount != 0);

        readingManipulations.setFirstParameter(hasReadingManipulations);

        return readingManipulations;
    }

    public Tuple<Boolean,List<AbstractStatement>> hasAssignmentManipulations(List<ApkClass> classes)
    {
        loadStatements();
        Tuple<Boolean,List<AbstractStatement>> assignmentManipulations = new Tuple<>(false, new ArrayList<>());
        Boolean hasAssignmentManipulations;
        List<AbstractStatement> assignmentStatements = new ArrayList<>();

        MethodFactory methodFactory = new MethodFactory();

        List<AbstractStatement> initializationStatements = getInitializationStatements().stream()
                .filter(statement -> (classes.contains(statement.getInvokedObjectType()))||
                                     (classes.stream().anyMatch(apkClass -> statement.getInvokedObjectType().getSootClass().getName().contains(apkClass.getSootClass().getName()))))
                .collect(Collectors.toList());

        List<AbstractStatement> instanceFieldAssignmentStatements = getInstanceFieldAssignmentStatements().stream()
                .filter(statement -> (classes.contains(statement.getInvokedObjectType()))||
                                     (classes.stream().anyMatch(apkClass -> fieldSignatureContainsType(statement.getField().getSootField(), apkClass.getSootClass().getName()))))
                .collect(Collectors.toList());
        List<AbstractStatement> staticFieldAssignmentStatements = getStaticFieldAssignmentStatements().stream()
                .filter(statement -> (classes.contains(statement.getInvokedObjectType()))||
                                     (classes.stream().anyMatch(apkClass -> fieldSignatureContainsType(statement.getField().getSootField(), apkClass.getSootClass().getName()))))
                .collect(Collectors.toList());
        List<AbstractStatement> setterCallStatements = getSetterCallStatements().stream()
                .filter(statement -> (classes.contains(statement.getInvokedObjectType())))
                .collect(Collectors.toList());

        List<AbstractStatement> methodCallStatements = getMethodCallStatements().stream()
                .filter(statement -> (classes.contains(statement.getInvokedObjectType())))
                .collect(Collectors.toList());


        assignmentStatements.addAll(initializationStatements);
        assignmentStatements.addAll(instanceFieldAssignmentStatements);
        assignmentStatements.addAll(staticFieldAssignmentStatements);
        assignmentStatements.addAll(setterCallStatements);
        assignmentStatements.addAll(methodCallStatements);

        assignmentManipulations.setSecondParameter(assignmentStatements);

        int initializationStatementsCount = initializationStatements.size();
        int instanceFieldAssignmentsCount = instanceFieldAssignmentStatements.size();
        int staticFieldAssignmentsCount = staticFieldAssignmentStatements.size();
        int setterCallsCount = setterCallStatements.size();
        int methodCallsCount = methodCallStatements.size();

        hasAssignmentManipulations = (initializationStatementsCount +
                                      instanceFieldAssignmentsCount +
                                      staticFieldAssignmentsCount +
                                      setterCallsCount +
                                      methodCallsCount != 0);

        assignmentManipulations.setFirstParameter(hasAssignmentManipulations);

        return assignmentManipulations;
    }

    public Tuple<Boolean,List<AbstractStatement>> hasAssignmentAndReadingManipulations(List<ApkClass> classes)
    {
        loadStatements();
        Tuple<Boolean,List<AbstractStatement>> assignmentReadingManipulations = new Tuple<>(false, new ArrayList<>());

        Tuple<Boolean,List<AbstractStatement>> hasReadingManipulations = hasReadingManipulations(classes);
        Tuple<Boolean,List<AbstractStatement>> hasAssignmentManipulations = hasAssignmentManipulations(classes);

        assignmentReadingManipulations.setFirstParameter(hasReadingManipulations.getFirstParameter() && hasAssignmentManipulations.getFirstParameter());
        assignmentReadingManipulations.setSecondParameter(Stream.concat(hasReadingManipulations.getSecondParameter().stream(),hasAssignmentManipulations.getSecondParameter().stream()).collect(Collectors.toList()));

        return assignmentReadingManipulations;
    }

    public Tuple<Boolean,List<AbstractStatement>> hasAssignmentOrReadingManipulations(List<ApkClass> classes)
    {
        loadStatements();
        Tuple<Boolean,List<AbstractStatement>> assignmentReadingManipulations = new Tuple<>(false, new ArrayList<>());

        Tuple<Boolean,List<AbstractStatement>> hasReadingManipulations = hasReadingManipulations(classes);
        Tuple<Boolean,List<AbstractStatement>> hasAssignmentManipulations = hasAssignmentManipulations(classes);

        assignmentReadingManipulations.setFirstParameter(hasReadingManipulations.getFirstParameter() || hasAssignmentManipulations.getFirstParameter());
        assignmentReadingManipulations.setSecondParameter(Stream.concat(hasReadingManipulations.getSecondParameter().stream(),hasAssignmentManipulations.getSecondParameter().stream()).collect(Collectors.toList()));

        return assignmentReadingManipulations;
    }

    public List<Unit> findInitializationStatements(List<ApkClass> classes)
    {
        List<Unit> initializationStatementsStatement;
        List<Unit> allStatements = RetrieveAllStatements();
        List<Unit> refTypeAssignment = allStatements.stream()
                .filter(unit -> isReferenceTypeAssignment(unit))
                .collect(Collectors.toList());

        initializationStatementsStatement = refTypeAssignment.stream()
                .filter(unit -> classes.stream()
                        .anyMatch(apkClass -> apkClass.getSootClass()
                                .equals(((RefType)((JAssignStmt)unit).getLeftOpBox()
                                        .getValue()
                                        .getType()).getSootClass())))
                .collect(Collectors.toList());

        return initializationStatementsStatement;
    }

    public List<Unit> findInstanceFieldAssignments(List<ApkClass> classes)
    {
        List<Unit> instanceFieldAssignment ;
        List<Unit> allStatements = RetrieveAllStatements();
        List<Unit> allInstanceFieldsAssignments = allStatements.stream()
                .filter(unit -> unit instanceof JAssignStmt)
                .filter(unit -> ((JAssignStmt)unit).getLeftOpBox().getValue() instanceof JInstanceFieldRef)
                .filter(unit -> ((JInstanceFieldRef)((JAssignStmt)unit).getLeftOpBox().getValue()).getFieldRef() instanceof AbstractSootFieldRef)
                .collect(Collectors.toList());

        instanceFieldAssignment = allInstanceFieldsAssignments.stream()
                .filter(unit -> classes.stream()
                        .anyMatch(apkClass -> apkClass.getSootClass()
                                .equals(((JInstanceFieldRef)((JAssignStmt)unit).getLeftOpBox()
                                        .getValue())
                                                .getFieldRef()
                                                .declaringClass())))
                .collect(Collectors.toList());

        return instanceFieldAssignment;
    }

    public List<Unit> findInstanceFieldReadings(List<ApkClass> classes)
    {
        List<Unit> instanceFieldReadings ;
        List<Unit> allStatements = RetrieveAllStatements();
        List<Unit> allInstanceFieldsReading = allStatements.stream()
                .filter(unit -> unit instanceof JAssignStmt)
                .filter(unit -> ((JAssignStmt)unit).getRightOpBox().getValue() instanceof JInstanceFieldRef)
                .filter(unit -> ((JInstanceFieldRef)((JAssignStmt)unit).getRightOpBox().getValue()).getFieldRef() instanceof AbstractSootFieldRef)
                .collect(Collectors.toList());

        instanceFieldReadings = allInstanceFieldsReading.stream()
                .filter(unit -> classes.stream()
                        .anyMatch(apkClass -> apkClass.getSootClass()
                                .equals(((JInstanceFieldRef)((JAssignStmt)unit).getRightOpBox()
                                        .getValue())
                                                .getFieldRef()
                                                .declaringClass())))
                .collect(Collectors.toList());

        return instanceFieldReadings;
    }

    public List<Unit> findStaticFieldAssignments(List<ApkClass> classes)
    {
        List<Unit> staticFieldAssignments;
        List<Unit> allStatements = RetrieveAllStatements();
        List<Unit> allStaticFieldAssignments = allStatements.stream()
                .filter(unit -> unit instanceof JAssignStmt)
                .filter(unit -> ((JAssignStmt)unit).getLeftOpBox().getValue() instanceof StaticFieldRef)
                .filter(unit -> ((StaticFieldRef)((JAssignStmt)unit).getLeftOpBox().getValue()).getFieldRef() instanceof AbstractSootFieldRef)
                .collect(Collectors.toList());

        staticFieldAssignments = allStaticFieldAssignments.stream()
                .filter(unit -> classes.stream()
                        .anyMatch(apkClass -> apkClass.getSootClass()
                                .equals(((StaticFieldRef)((JAssignStmt)unit).getLeftOpBox()
                                        .getValue())
                                                .getFieldRef()
                                                .declaringClass())))
                .collect(Collectors.toList());

        return staticFieldAssignments;
    }

    public List<Unit> findStaticFieldReadings(List<ApkClass> classes)
    {
        List<Unit> staticFieldReadings;
        List<Unit> allStatements = RetrieveAllStatements();
        List<Unit> allStaticFieldReadings = allStatements.stream()
                .filter(unit -> unit instanceof JAssignStmt)
                .filter(unit -> ((JAssignStmt)unit).getRightOpBox().getValue() instanceof StaticFieldRef)
                .filter(unit -> ((StaticFieldRef)((JAssignStmt)unit).getRightOpBox().getValue()).getFieldRef() instanceof AbstractSootFieldRef)
                .collect(Collectors.toList());

        staticFieldReadings = allStaticFieldReadings.stream()
                .filter(unit -> classes.stream()
                        .anyMatch(apkClass -> apkClass.getSootClass()
                                .equals(((StaticFieldRef)((JAssignStmt)unit).getRightOpBox()
                                        .getValue())
                                                .getFieldRef()
                                                .declaringClass())))
                .collect(Collectors.toList());

        return staticFieldReadings;
    }

    public List<Unit> findAllMethodCallsStatements(List<ApkTypeEntry> classes)
    {
        List<Unit> methodCalls = new ArrayList<>();
        List<Unit> allStatements = RetrieveAllStatements();

        List<Unit> allJInvokeStmtMethodCalls = allStatements.stream()
                .filter(unit -> unit instanceof JInvokeStmt)
                .filter(unit -> (((JInvokeStmt)unit).getInvokeExprBox().getValue() instanceof JSpecialInvokeExpr) ||
                                (((JInvokeStmt)unit).getInvokeExprBox().getValue() instanceof JVirtualInvokeExpr) ||
                                (((JInvokeStmt)unit).getInvokeExprBox().getValue() instanceof JInterfaceInvokeExpr) ||
                                (((JInvokeStmt)unit).getInvokeExprBox().getValue() instanceof JStaticInvokeExpr))
                .collect(Collectors.toList());

        List<Unit> allJAssignStmtMethodCalls = allStatements.stream()
                .filter(unit -> unit instanceof JAssignStmt)
                .filter(unit -> (((JAssignStmt)unit).getRightOpBox().getValue() instanceof JSpecialInvokeExpr) ||
                                (((JAssignStmt)unit).getRightOpBox().getValue() instanceof JVirtualInvokeExpr) ||
                                (((JAssignStmt)unit).getRightOpBox().getValue() instanceof JInterfaceInvokeExpr) ||
                                (((JAssignStmt)unit).getRightOpBox().getValue() instanceof JStaticInvokeExpr))
                .collect(Collectors.toList());

        if (this.getSootMethod().getName().contains("onCompleted"))
        {
            int i = 0;
        }

        methodCalls.addAll(allJInvokeStmtMethodCalls.stream()
                                   .filter(unit -> classes.stream()
                                           .anyMatch(apkClass -> getAllUpperTypes(apkClass.getSootClass())
                                                   .stream()
                                                   .anyMatch(upperType -> upperType
                                                   .equals(((AbstractInvokeExpr)(((JInvokeStmt)unit).getInvokeExprBox()
                                                           .getValue()))
                                                                   .getMethodRef()
                                                                   .declaringClass()))))
                                   .collect(Collectors.toList()));

        methodCalls.addAll(allJAssignStmtMethodCalls.stream()
                                   .filter(unit -> classes.stream()
                                           .anyMatch(apkClass -> getAllUpperTypes(apkClass.getSootClass())
                                                   .stream()
                                                   .anyMatch(upperType -> upperType
                                                   .equals(((AbstractInvokeExpr)(((JAssignStmt)unit).getRightOpBox()).getValue()).getMethodRef().declaringClass()))))
                                   .collect(Collectors.toList()));

        return methodCalls;
    }

    public List<Unit> findGetterCalls(List<Unit> methodCalls, MethodFactory methodFactory)
    {
        List<Unit> getterCalls = new ArrayList<>();

        getterCalls.addAll(methodCalls.stream()
                .filter(unit -> unit instanceof JInvokeStmt)
                .filter(unit -> (((JInvokeStmt)unit).getInvokeExprBox().getValue() instanceof JVirtualInvokeExpr)||
                                (((JInvokeStmt)unit).getInvokeExprBox().getValue() instanceof JInterfaceInvokeExpr))
                .filter(unit -> methodFactory.isGetter(((AbstractInvokeExpr)(((JInvokeStmt)unit).getInvokeExprBox()
                                                               .getValue()))
                                                               .getMethod(),
                                                       ((AbstractInvokeExpr)(((JInvokeStmt)unit).getInvokeExprBox()
                                                               .getValue()))
                                                               .getMethodRef()
                                                               .declaringClass()
                                                               .getFields()
                                                               .stream()
                                                               .collect(Collectors.toList()))
                        .isPresent())
                .collect(Collectors.toList()));

        getterCalls.addAll(methodCalls.stream()
                                   .filter(unit -> unit instanceof JAssignStmt)
                                   .filter(unit -> (((JAssignStmt)unit).getRightOpBox().getValue() instanceof JVirtualInvokeExpr) ||
                                                   (((JAssignStmt)unit).getRightOpBox().getValue() instanceof JInterfaceInvokeExpr))
                                   .filter(unit -> methodFactory.isGetter(((AbstractInvokeExpr)(((JAssignStmt)unit).getRightOpBox()).getValue())
                                                                                  .getMethod(),
                                                                          ((AbstractInvokeExpr)(((JAssignStmt)unit).getRightOpBox()).getValue())
                                                                                  .getMethodRef()
                                                                                  .declaringClass()
                                                                                  .getFields()
                                                                                  .stream()
                                                                                  .collect(Collectors.toList()))
                                           .isPresent())
                                   .collect(Collectors.toList()));

        return getterCalls;
    }

    public List<Unit> findSetterCalls(List<Unit> methodCalls, MethodFactory methodFactory)
    {
        List<Unit> setterCalls = new ArrayList<>();

        setterCalls.addAll(methodCalls.stream()
                .filter(unit -> unit instanceof JInvokeStmt)
                .filter(unit -> (((JInvokeStmt)unit).getInvokeExprBox().getValue() instanceof JVirtualInvokeExpr)||
                                (((JInvokeStmt)unit).getInvokeExprBox().getValue() instanceof JInterfaceInvokeExpr))
                .filter(unit -> methodFactory.isSetter(((AbstractInvokeExpr)(((JInvokeStmt)unit).getInvokeExprBox()
                                                               .getValue()))
                                                               .getMethod(),
                                                       ((AbstractInvokeExpr)(((JInvokeStmt)unit).getInvokeExprBox()
                                                               .getValue()))
                                                               .getMethodRef()
                                                               .declaringClass()
                                                               .getFields()
                                                               .stream()
                                                               .collect(Collectors.toList()))
                        .isPresent())
                .collect(Collectors.toList()));

        setterCalls.addAll(methodCalls.stream()
                                   .filter(unit -> unit instanceof JAssignStmt)
                                   .filter(unit -> (((JAssignStmt)unit).getRightOpBox().getValue() instanceof JVirtualInvokeExpr) ||
                                                   (((JAssignStmt)unit).getRightOpBox().getValue() instanceof JInterfaceInvokeExpr))
                                   .filter(unit -> methodFactory.isSetter(((AbstractInvokeExpr)(((JAssignStmt)unit).getRightOpBox()).getValue())
                                                                                  .getMethod(),
                                                                          ((AbstractInvokeExpr)(((JAssignStmt)unit).getRightOpBox()).getValue())
                                                                                  .getMethodRef()
                                                                                  .declaringClass()
                                                                                  .getFields()
                                                                                  .stream()
                                                                                  .collect(Collectors.toList()))
                                           .isPresent())
                                   .collect(Collectors.toList()));

        return setterCalls;
    }

    public List<Unit> findMethodCalls(List<Unit> allMethodCalls, MethodFactory methodFactory)
    {
        List<Unit> methodCalls = new ArrayList<>();
        methodCalls.addAll(allMethodCalls.stream()
                .filter(unit -> unit instanceof JInvokeStmt)
                .filter(unit -> (((JInvokeStmt)unit).getInvokeExprBox().getValue() instanceof JVirtualInvokeExpr)||
                                (((JInvokeStmt)unit).getInvokeExprBox().getValue() instanceof JInterfaceInvokeExpr)||
                                (((JInvokeStmt)unit).getInvokeExprBox().getValue() instanceof JStaticInvokeExpr))
                .filter(unit -> !methodFactory.isSetter(((AbstractInvokeExpr)(((JInvokeStmt)unit).getInvokeExprBox()
                                                               .getValue()))
                                                               .getMethod(),
                                                       ((AbstractInvokeExpr)(((JInvokeStmt)unit).getInvokeExprBox()
                                                               .getValue()))
                                                               .getMethodRef()
                                                               .declaringClass()
                                                               .getFields()
                                                               .stream()
                                                               .collect(Collectors.toList()))
                        .isPresent())
                .filter(unit -> !methodFactory.isGetter(((AbstractInvokeExpr)(((JInvokeStmt)unit).getInvokeExprBox()
                                                                .getValue()))
                                                                .getMethod(),
                                                        ((AbstractInvokeExpr)(((JInvokeStmt)unit).getInvokeExprBox()
                                                                .getValue()))
                                                                .getMethodRef()
                                                                .declaringClass()
                                                                .getFields()
                                                                .stream()
                                                                .collect(Collectors.toList()))
                        .isPresent())
                .collect(Collectors.toList()));

        methodCalls.addAll(allMethodCalls.stream()
                                   .filter(unit -> unit instanceof JAssignStmt)
                                   .filter(unit -> (((JAssignStmt)unit).getRightOpBox().getValue() instanceof JVirtualInvokeExpr) ||
                                                   (((JAssignStmt)unit).getRightOpBox().getValue() instanceof JInterfaceInvokeExpr) ||
                                                   (((JAssignStmt)unit).getRightOpBox().getValue() instanceof JStaticInvokeExpr))
                                   .filter(unit -> !methodFactory.isSetter(((AbstractInvokeExpr)(((JAssignStmt)unit).getRightOpBox()).getValue())
                                                                                   .getMethod(),
                                                                           ((AbstractInvokeExpr)(((JAssignStmt)unit).getRightOpBox()).getValue())
                                                                                   .getMethodRef()
                                                                                   .declaringClass()
                                                                                   .getFields()
                                                                                   .stream()
                                                                                   .collect(Collectors.toList()))
                                           .isPresent())
                                   .filter(unit -> !methodFactory.isGetter(((AbstractInvokeExpr)(((JAssignStmt)unit).getRightOpBox()).getValue())
                                                                                   .getMethod(),
                                                                           ((AbstractInvokeExpr)(((JAssignStmt)unit).getRightOpBox()).getValue())
                                                                                   .getMethodRef()
                                                                                   .declaringClass()
                                                                                   .getFields()
                                                                                   .stream()
                                                                                   .collect(Collectors.toList()))
                                           .isPresent())
                                   .collect(Collectors.toList()));


        return methodCalls;
    }

    /**
     * Returns the called methods that belongs to the targeted classes
     *
     * @param targetedClasses are the classes to which the called methods belong to.
     * @param allApplicationClasses used to obtain AbstractMethod object from SootMethod
     * @return
     */
    public List<AbstractMethod> findAllCalledMethods(List<ApkTypeEntry> targetedClasses, List<ApkClass> allApplicationClasses)
    {
        if (this.getSootMethod().getName().contains("onCompleted"))
        {
            int i = 0;
        }

        List<AbstractMethod> allMethods = new ArrayList<>();
        List<Unit> allMethodCallStatements = findAllMethodCallsStatements(targetedClasses);

        List<Unit> allJInvokeStmtMethodCalls = allMethodCallStatements.stream()
                .filter(unit -> unit instanceof JInvokeStmt)
                .filter(unit -> (((JInvokeStmt)unit).getInvokeExprBox().getValue() instanceof JSpecialInvokeExpr) ||
                                (((JInvokeStmt)unit).getInvokeExprBox().getValue() instanceof JVirtualInvokeExpr) ||
                                (((JInvokeStmt)unit).getInvokeExprBox().getValue() instanceof JInterfaceInvokeExpr) ||
                                (((JInvokeStmt)unit).getInvokeExprBox().getValue() instanceof JStaticInvokeExpr))
                .collect(Collectors.toList());

        List<Unit> allJAssignStmtMethodCalls = allMethodCallStatements.stream()
                .filter(unit -> unit instanceof JAssignStmt)
                .filter(unit -> (((JAssignStmt)unit).getRightOpBox().getValue() instanceof JSpecialInvokeExpr) ||
                                (((JAssignStmt)unit).getRightOpBox().getValue() instanceof JVirtualInvokeExpr) ||
                                (((JAssignStmt)unit).getRightOpBox().getValue() instanceof JInterfaceInvokeExpr) ||
                                (((JAssignStmt)unit).getRightOpBox().getValue() instanceof JStaticInvokeExpr))
                .collect(Collectors.toList());

        allMethods.addAll(allJInvokeStmtMethodCalls.stream()
                               .map(unit -> ((AbstractInvokeExpr)((JInvokeStmt)unit).getInvokeExprBox().getValue()).getMethod())
                               .map(sootMethod -> getMethodFromSootMethod(sootMethod, allApplicationClasses))
                               .filter(optionalAbstractMethod -> optionalAbstractMethod.size() != 0)
                               .flatMap(abstractMethods -> abstractMethods.stream())
                               .collect(Collectors.toList()));

        allMethods.addAll(allJAssignStmtMethodCalls.stream()
                                  .map(unit -> ((AbstractInvokeExpr)((JAssignStmt)unit).getRightOpBox().getValue()).getMethod())
                                  .map(sootMethod -> getMethodFromSootMethod(sootMethod, allApplicationClasses))
                                  .filter(optionalAbstractMethod -> optionalAbstractMethod.size() != 0)
                                  .flatMap(abstractMethods -> abstractMethods.stream())
                                  .collect(Collectors.toList()));


        return allMethods;

    }

    private List<Unit> RetrieveAllStatements()
    {
        List<Unit> allStatements = new ArrayList<>();
        Body methodBody = getMethodBody(this);
        if (methodBody != null)
        {
            allStatements = methodBody.getUnits().stream().collect(Collectors.toList());
        }
        return allStatements;
    }

    public static List<AbstractMethod> getMethodFromSootMethod(SootMethod sootMethod, List<ApkClass> classes)
    {
        List<AbstractMethod> method = new ArrayList<>();

        method.addAll(classes.stream()
                .filter(apkClass -> apkClass.getSootClass().equals(sootMethod.getDeclaringClass()))
                .filter(apkClass -> apkClass.getAllMethods()
                        .stream()
                        .anyMatch(m -> m.getSootMethod().equals(sootMethod)))
                .map(apkClass -> apkClass.getAllMethods()
                        .stream()
                        .filter(m -> m.getSootMethod().equals(sootMethod))
                        .findFirst()
                        .get())
                .collect(Collectors.toList()));

        if(method.size() == 0)
        {
            method.addAll(classes
                    .stream()
                    .filter(apkClass -> apkClass.getMethods()
                    .stream()
                    .anyMatch(abstractMethod -> (abstractMethod.getSootMethod().getSubSignature().equals(sootMethod.getSubSignature()))&&
                                                (abstractMethod.overridesOrImplementsMethodInUpperType().getFirstParameter())&&
                                                (getAllUpperTypes(apkClass.getSootClass()).stream().anyMatch(sootClass -> sootClass.equals(sootMethod.getDeclaringClass())))))
                    .map(apkClass -> apkClass.getMethods())
                    .flatMap(methods -> methods.stream())
                    .filter(abstractMethod -> (abstractMethod.getSootMethod().getSubSignature().equals(sootMethod.getSubSignature()))&&
                                               (abstractMethod.overridesOrImplementsMethodInUpperType().getFirstParameter())&&
                                               (getAllUpperTypes(abstractMethod.getContainingType().getSootClass()).stream().anyMatch(sootClass -> sootClass.equals(sootMethod.getDeclaringClass()))))
                    .collect(Collectors.toList()));

        }



        return method;
    }



    public String getName()
    {
        return this.name;
    }

    public List<InitializationStatement> getInitializationStatements()
    {
        return initializationStatements;
    }

    public void setInitializationStatements(List<InitializationStatement> initializationStatements)
    {
        this.initializationStatements = initializationStatements;
    }

    public List<InstanceFieldAssignmentStatement> getInstanceFieldAssignmentStatements()
    {
        return instanceFieldAssignmentStatements;
    }

    public void setInstanceFieldAssignmentStatements(List<InstanceFieldAssignmentStatement>
                                                             instanceFieldAssignmentStatements)
    {
        this.instanceFieldAssignmentStatements = instanceFieldAssignmentStatements;
    }

    public List<InstanceFieldReadingStatement> getInstanceFieldReadingStatements()
    {
        return instanceFieldReadingStatements;
    }

    public void setInstanceFieldReadingStatements(List<InstanceFieldReadingStatement>
                                                          instanceFieldReadingStatements)
    {
        this.instanceFieldReadingStatements = instanceFieldReadingStatements;
    }

    public List<StaticFieldAssignmentStatement> getStaticFieldAssignmentStatements()
    {
        return staticFieldAssignmentStatements;
    }

    public void setStaticFieldAssignmentStatements(List<StaticFieldAssignmentStatement>
                                                           staticFieldAssignmentStatements)
    {
        this.staticFieldAssignmentStatements = staticFieldAssignmentStatements;
    }

    public List<StaticFieldReadingStatement> getStaticFieldReadingStatements()
    {
        return staticFieldReadingStatements;
    }

    public void setStaticFieldReadingStatements(List<StaticFieldReadingStatement>
                                                        staticFieldReadingStatements)
    {
        this.staticFieldReadingStatements = staticFieldReadingStatements;
    }

    public List<MethodCallStatement> getMethodCallStatements()
    {
        return methodCallStatements;
    }

    public void setMethodCallStatements(List<MethodCallStatement> methodCallStatements)
    {
        this.methodCallStatements = methodCallStatements;
    }

    public List<GetterCallStatement> getGetterCallStatements()
    {
        return getterCallStatements;
    }

    public void setGetterCallStatements(List<GetterCallStatement> getterCallStatements)
    {
        this.getterCallStatements = getterCallStatements;
    }

    public List<SetterCallStatement> getSetterCallStatements()
    {
        return setterCallStatements;
    }

    public void setSetterCallStatements(List<SetterCallStatement> setterCallStatements)
    {
        this.setterCallStatements = setterCallStatements;
    }

    public List<Unit> getMethodSootStatements()
    {
        return methodSootStatements;
    }

    public void setMethodSootStatements(List<Unit> methodSootStatements)
    {
        this.methodSootStatements = methodSootStatements;
    }

    public List<AbstractStatement> getAllStatement()
    {
        return allStatement;
    }

    public void setAllStatement(List<AbstractStatement> allStatement)
    {
        this.allStatement = allStatement;
    }

    public ApkTypeEntry getContainingType()
    {
        return containingType;
    }

    public void setContainingType(ApkTypeEntry containingType)
    {
        this.containingType = containingType;
    }

    public List<AbstractConcurrentOperation> retrieveConcurrentOperations(List<ApkClass> allClasses)
    {
        loadStatements();
        List<AbstractStatement>     allStatements              = getAllStatement();
        IConcurrentOperationsFilter concurrentOperationsFilter = new ConcurrentOperationsFilter(allStatements, allClasses);
        return concurrentOperationsFilter.filter();
    }

}
