package org.tha3rav.rimaz.core.Statements;

import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.OOP.AbstractMethod;
import org.tha3rav.rimaz.core.OOP.Method;
import org.tha3rav.rimaz.core.OOP.Tuple;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import soot.RefType;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.ValueBox;
import soot.VoidType;
import soot.jimple.internal.AbstractInstanceInvokeExpr;
import soot.jimple.internal.AbstractInvokeExpr;
import soot.jimple.internal.ImmediateBox;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInterfaceInvokeExpr;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.internal.JSpecialInvokeExpr;
import soot.jimple.internal.JStaticInvokeExpr;
import soot.jimple.internal.JVirtualInvokeExpr;

import static org.tha3rav.rimaz.utils.ANDROID.AndroidHelpers.isOrExtendingInnerClassOfUIElement;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidHelpers.isUIElement;
import static org.tha3rav.rimaz.utils.OOP.OOPConstants.Numbers.ONE;
import static org.tha3rav.rimaz.utils.OOP.OOPConstants.Numbers.ZERO;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.fieldSignatureContainsType;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.getUpperHierarchy;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.isAssignmentStatement;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.isInvocationStatement;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.isRefType;

public class MethodCallStatement extends AnyMethodCallStatement
{

    public MethodCallStatement(Unit sootStatement)
    {
        super(sootStatement);

        this.calledMethod = retrieveCalledMethod(sootStatement);
        SootClass sootClass = retrieveSootClass(sootStatement);
        this.invokedObjectType = new ApkClass(sootClass.getName(), sootClass);
    }

    public MethodCallStatement(Unit sootStatement,
                               ApkTypeEntry declaringType)
    {
        super(sootStatement,
              declaringType);

        this.calledMethod = retrieveCalledMethod(sootStatement);
        SootClass sootClass = retrieveSootClass(sootStatement);
        this.invokedObjectType = new ApkClass(sootClass.getName(), sootClass);
    }

    public MethodCallStatement(Unit sootStatement,
                               AbstractMethod containingMethod)
    {
        super(sootStatement,
              containingMethod);

        this.calledMethod = retrieveCalledMethod(sootStatement);
        SootClass sootClass = retrieveSootClass(sootStatement);
        this.invokedObjectType = new ApkClass(sootClass.getName(), sootClass);
    }

    public MethodCallStatement(Unit sootStatement,
                               AbstractMethod containingMethod,
                               ApkTypeEntry declaringType)
    {
        super(sootStatement,
              containingMethod,
              declaringType);

        this.calledMethod = retrieveCalledMethod(sootStatement);
        SootClass sootClass = retrieveSootClass(sootStatement);
        this.invokedObjectType = new ApkClass(sootClass.getName(), sootClass);
    }

    private AbstractMethod retrieveCalledMethod(Unit sootStatement)
    {
        AbstractMethod calledMethod = null;
        if (isInvocationStatement(sootStatement))
        {
            calledMethod = new Method(((AbstractInvokeExpr)(((JInvokeStmt)sootStatement).getInvokeExprBox().getValue())).getMethod());
        }
        else
        {
            if (isAssignmentStatement(sootStatement))
            {
                calledMethod = new Method(((AbstractInvokeExpr)(((JAssignStmt)sootStatement).getRightOpBox()).getValue()).getMethod());
            }
        }
        return calledMethod;
    }

    private SootClass retrieveSootClass(Unit sootStatement)
    {
        SootClass sootClass = null;
        if (isInvocationStatement(sootStatement))
        {
            sootClass = ((AbstractInvokeExpr)(((JInvokeStmt)sootStatement).getInvokeExprBox()
                    .getValue()))
                    .getMethodRef()
                    .declaringClass();
        }
        else
        {
            if (isAssignmentStatement(sootStatement))
            {
                sootClass = ((AbstractInvokeExpr)(((JAssignStmt)sootStatement).getRightOpBox()).getValue()).getMethodRef().declaringClass();
            }
        }
        return sootClass;
    }

    public SootClass getPassedParameterType(int parameterNumber)
    {
        SootClass passedArgumentType = null;

        if (isInvocationStatement(this.getSootStatement()))
        {
            if((((JInvokeStmt) this.getSootStatement()).getInvokeExprBox().getValue() instanceof JInterfaceInvokeExpr)||
               (((JInvokeStmt) this.getSootStatement()).getInvokeExprBox().getValue() instanceof JVirtualInvokeExpr))
            {
                ValueBox argumentValueBox =((AbstractInstanceInvokeExpr) ((JInvokeStmt) this.getSootStatement()).getInvokeExprBox().getValue()).getArgBox(parameterNumber);
                if (argumentValueBox instanceof ImmediateBox)
                {
                    if (isRefType(argumentValueBox.getValue().getType()))
                    {
                        boolean argumentTypeHasSootClass = ((RefType) argumentValueBox.getValue().getType()).hasSootClass();
                        if (argumentTypeHasSootClass)
                        {
                            passedArgumentType = ((RefType) argumentValueBox.getValue().getType()).getSootClass();
                        }
                    }
                }
            }
        }
        else
        {
            if (isAssignmentStatement(this.getSootStatement()))
            {
                if ((((JAssignStmt)this.getSootStatement()).getRightOpBox().getValue() instanceof JSpecialInvokeExpr) ||
                    (((JAssignStmt)this.getSootStatement()).getRightOpBox().getValue() instanceof JVirtualInvokeExpr) ||
                    (((JAssignStmt)this.getSootStatement()).getRightOpBox().getValue() instanceof JInterfaceInvokeExpr) ||
                    (((JAssignStmt)this.getSootStatement()).getRightOpBox().getValue() instanceof JStaticInvokeExpr))
                {
                    ValueBox argumentValueBox = ((AbstractInvokeExpr)(((JAssignStmt)this.getSootStatement()).getRightOpBox()).getValue()).getArgBox(parameterNumber);
                    if (argumentValueBox instanceof ImmediateBox)
                    {
                        if (isRefType(argumentValueBox.getValue().getType()))
                        {
                            boolean argumentTypeHasSootClass = ((RefType) argumentValueBox.getValue().getType()).hasSootClass();
                            if (argumentTypeHasSootClass)
                            {
                                passedArgumentType = ((RefType) argumentValueBox.getValue().getType()).getSootClass();
                            }
                        }
                    }
                }
            }
        }


        return passedArgumentType;
    }


}
