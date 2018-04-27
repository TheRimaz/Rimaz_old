package org.tha3rav.rimaz.core.Statements;

import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.OOP.AbstractMethod;
import org.tha3rav.rimaz.core.OOP.MethodFactory;

import java.util.List;
import java.util.stream.Collectors;

import soot.AbstractSootFieldRef;
import soot.SootField;
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

import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.isAssignmentStatement;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.isInvocationStatement;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.isReferenceTypeAssignment;

public class StatementFactory extends StatementAbstractFactory
{
    private MethodFactory methodFactory;

    public StatementFactory()
    {
        methodFactory = new MethodFactory();
    }

    @Override
    public AbstractStatement getStatement(Unit statement)
    {
        AbstractStatement abstractStatement = null ;
        boolean isInitializationStatement = isInitializationStatement(statement);
        boolean isInstanceFieldAssignmentStatement = isInstanceFieldAssignmentStatement(statement);
        boolean isInstanceFieldReadingStatement = isInstanceFieldReadingStatement(statement);
        boolean isStaticFieldAssignmentStatement = isStaticFieldAssignmentStatement(statement);
        boolean isStaticFieldReadingStatement = isStaticFieldReadingStatement(statement);
        boolean isMethodCallStatement = isMethodCallStatement(statement);
        boolean isGetterCallStatement = isGetterCallStatement(statement);
        boolean isSetterCallStatement = isSetterCallStatement(statement);

        if (isInitializationStatement)
        {
            abstractStatement = new InitializationStatement(statement);
        }
        if (isInstanceFieldAssignmentStatement)
        {
            abstractStatement = new InstanceFieldAssignmentStatement(statement);
        }
        if (isInstanceFieldReadingStatement)
        {
            abstractStatement = new InstanceFieldReadingStatement(statement);
        }
        if (isStaticFieldAssignmentStatement)
        {
            abstractStatement = new StaticFieldAssignmentStatement(statement);
        }
        if (isStaticFieldReadingStatement)
        {
            abstractStatement = new StaticFieldReadingStatement(statement);
        }
        if (isMethodCallStatement)
        {
            abstractStatement = new MethodCallStatement(statement);
        }
        if (isGetterCallStatement)
        {
            abstractStatement = new GetterCallStatement(statement);
        }
        if (isSetterCallStatement)
        {
            abstractStatement = new SetterCallStatement(statement);
        }
        return abstractStatement;
    }

    @Override
    public AbstractStatement getStatement(Unit statement, AbstractMethod containingMethod, ApkTypeEntry containingType)
    {
        AbstractStatement abstractStatement = null ;
        boolean isInitializationStatement = isInitializationStatement(statement);
        boolean isInstanceFieldAssignmentStatement = isInstanceFieldAssignmentStatement(statement);
        boolean isInstanceFieldReadingStatement = isInstanceFieldReadingStatement(statement);
        boolean isStaticFieldAssignmentStatement = isStaticFieldAssignmentStatement(statement);
        boolean isStaticFieldReadingStatement = isStaticFieldReadingStatement(statement);
        boolean isMethodCallStatement = isMethodCallStatement(statement);
        boolean isGetterCallStatement = isGetterCallStatement(statement);
        boolean isSetterCallStatement = isSetterCallStatement(statement);

        if (isInitializationStatement)
        {
            abstractStatement = new InitializationStatement(statement, containingMethod, containingType);
        }
        if (isInstanceFieldAssignmentStatement)
        {
            abstractStatement = new InstanceFieldAssignmentStatement(statement, containingMethod, containingType);
        }
        if (isInstanceFieldReadingStatement)
        {
            abstractStatement = new InstanceFieldReadingStatement(statement, containingMethod, containingType);
        }
        if (isStaticFieldAssignmentStatement)
        {
            abstractStatement = new StaticFieldAssignmentStatement(statement, containingMethod, containingType);
        }
        if (isStaticFieldReadingStatement)
        {
            abstractStatement = new StaticFieldReadingStatement(statement, containingMethod, containingType);
        }
        if (isMethodCallStatement)
        {
            abstractStatement = new MethodCallStatement(statement, containingMethod, containingType);
        }
        if (isGetterCallStatement)
        {
            abstractStatement = new GetterCallStatement(statement, containingMethod, containingType);
        }
        if (isSetterCallStatement)
        {
            abstractStatement = new SetterCallStatement(statement, containingMethod, containingType);
        }
        return abstractStatement;
    }

    @Override
    public boolean isInitializationStatement(Unit statement)
    {
        return isReferenceTypeAssignment(statement);
    }

    @Override
    public boolean isInstanceFieldAssignmentStatement(Unit statement)
    {
        boolean isInstanceFieldAssignmentStatement = false;

        if (isAssignmentStatement(statement))
        {
            if (((JAssignStmt)statement).getLeftOpBox().getValue() instanceof JInstanceFieldRef)
            {
                if (((JInstanceFieldRef)((JAssignStmt)statement).getLeftOpBox().getValue()).getFieldRef() instanceof AbstractSootFieldRef)
                {
                    isInstanceFieldAssignmentStatement = true;
                }
            }
        }

        return isInstanceFieldAssignmentStatement;
    }

    @Override
    public boolean isInstanceFieldReadingStatement(Unit statement)
    {
        boolean isInstanceFieldReadingStatement = false;

        if (isAssignmentStatement(statement))
        {
            if (((JAssignStmt)statement).getRightOpBox().getValue() instanceof JInstanceFieldRef)
            {
                if (((JInstanceFieldRef)((JAssignStmt)statement).getRightOpBox().getValue()).getFieldRef() instanceof AbstractSootFieldRef)
                {
                    isInstanceFieldReadingStatement = true;
                }
            }
        }

        return isInstanceFieldReadingStatement;
    }

    @Override
    public boolean isStaticFieldAssignmentStatement(Unit statement)
    {
        boolean isStaticFieldAssignmentStatement = false;

        if (isAssignmentStatement(statement))
        {
            if (((JAssignStmt)statement).getLeftOpBox().getValue() instanceof StaticFieldRef)
            {
                if (((StaticFieldRef)((JAssignStmt)statement).getLeftOpBox().getValue()).getFieldRef() instanceof AbstractSootFieldRef)
                {
                    isStaticFieldAssignmentStatement = true;
                }
            }
        }

        return isStaticFieldAssignmentStatement;
    }

    @Override
    public boolean isStaticFieldReadingStatement(Unit statement)
    {
        boolean isStaticFieldReadingStatement = false;

        if (isAssignmentStatement(statement))
        {
            if (((JAssignStmt)statement).getRightOpBox().getValue() instanceof StaticFieldRef)
            {
                if (((StaticFieldRef)((JAssignStmt)statement).getRightOpBox().getValue()).getFieldRef() instanceof AbstractSootFieldRef)
                {
                    isStaticFieldReadingStatement = true;
                }
            }
        }

        return isStaticFieldReadingStatement;
    }

    @Override
    public boolean isAnyMethodCallStatement(Unit statement)
    {
        boolean isAnyMethodCallStatement = false;

        if (isInvocationStatement(statement))
        {
            if ((((JInvokeStmt)statement).getInvokeExprBox().getValue() instanceof JSpecialInvokeExpr) ||
                (((JInvokeStmt)statement).getInvokeExprBox().getValue() instanceof JVirtualInvokeExpr) ||
                (((JInvokeStmt)statement).getInvokeExprBox().getValue() instanceof JInterfaceInvokeExpr) ||
                (((JInvokeStmt)statement).getInvokeExprBox().getValue() instanceof JStaticInvokeExpr))
            {
                isAnyMethodCallStatement = true;
            }
        }
        else
        {
            if (isAssignmentStatement(statement))
            {
                if ((((JAssignStmt)statement).getRightOpBox().getValue() instanceof JSpecialInvokeExpr) ||
                    (((JAssignStmt)statement).getRightOpBox().getValue() instanceof JVirtualInvokeExpr) ||
                    (((JAssignStmt)statement).getRightOpBox().getValue() instanceof JInterfaceInvokeExpr) ||
                    (((JAssignStmt)statement).getRightOpBox().getValue() instanceof JStaticInvokeExpr))
                {
                    isAnyMethodCallStatement = true;
                }
            }
        }

        return isAnyMethodCallStatement;
    }

    @Override
    public boolean isMethodCallStatement(Unit statement)
    {
        boolean isMethodCallStatement = false;

        if (isInvocationStatement(statement))
        {
            if ((((JInvokeStmt)statement).getInvokeExprBox().getValue() instanceof JSpecialInvokeExpr) ||
                (((JInvokeStmt)statement).getInvokeExprBox().getValue() instanceof JVirtualInvokeExpr)||
                (((JInvokeStmt)statement).getInvokeExprBox().getValue() instanceof JInterfaceInvokeExpr)||
                (((JInvokeStmt)statement).getInvokeExprBox().getValue() instanceof JStaticInvokeExpr))
            {
                SootMethod method = ((AbstractInvokeExpr)(((JInvokeStmt)statement).getInvokeExprBox().getValue())).getMethod();
                List<SootField> potentialEncapsulatedFields = ((AbstractInvokeExpr)(((JInvokeStmt)statement).getInvokeExprBox()
                        .getValue()))
                        .getMethodRef()
                        .declaringClass()
                        .getFields()
                        .stream()
                        .collect(Collectors.toList());

                if ((!methodFactory.isSetter(method,potentialEncapsulatedFields).isPresent()) &&
                    (!methodFactory.isGetter(method,potentialEncapsulatedFields).isPresent()))
                {
                    isMethodCallStatement = true;
                }
            }
        }
        else
        {
            if (isAssignmentStatement(statement))
            {
                if ((((JAssignStmt)statement).getRightOpBox().getValue() instanceof JSpecialInvokeExpr) ||
                    (((JAssignStmt)statement).getRightOpBox().getValue() instanceof JVirtualInvokeExpr) ||
                    (((JAssignStmt)statement).getRightOpBox().getValue() instanceof JInterfaceInvokeExpr) ||
                    (((JAssignStmt)statement).getRightOpBox().getValue() instanceof JStaticInvokeExpr))
                {
                    SootMethod method = ((AbstractInvokeExpr)(((JAssignStmt)statement).getRightOpBox()).getValue()).getMethod();
                    List<SootField> potentialEncapsulatedFields =  ((AbstractInvokeExpr)(((JAssignStmt)statement).getRightOpBox()).getValue())
                            .getMethodRef()
                            .declaringClass()
                            .getFields()
                            .stream()
                            .collect(Collectors.toList());

                    if ((!methodFactory.isSetter(method,potentialEncapsulatedFields).isPresent()) &&
                        (!methodFactory.isGetter(method,potentialEncapsulatedFields).isPresent()))
                    {
                        isMethodCallStatement = true;
                    }
                }
            }
        }
        return isMethodCallStatement;
    }

    @Override
    public boolean isGetterCallStatement(Unit statement)
    {
        boolean isGetterCallStatement = false;

        if (isInvocationStatement(statement))
        {
            if ((((JInvokeStmt)statement).getInvokeExprBox().getValue() instanceof JVirtualInvokeExpr)||
                (((JInvokeStmt)statement).getInvokeExprBox().getValue() instanceof JInterfaceInvokeExpr))
            {
                SootMethod method = ((AbstractInvokeExpr)(((JInvokeStmt)statement).getInvokeExprBox().getValue())).getMethod();
                List<SootField> potentialEncapsulatedFields = ((AbstractInvokeExpr)(((JInvokeStmt)statement).getInvokeExprBox()
                        .getValue()))
                        .getMethodRef()
                        .declaringClass()
                        .getFields()
                        .stream()
                        .collect(Collectors.toList());

                if (methodFactory.isGetter(method,potentialEncapsulatedFields).isPresent())
                {
                    isGetterCallStatement = true;
                }
            }
        }
        else
        {
            if (isAssignmentStatement(statement))
            {
                if ((((JAssignStmt)statement).getRightOpBox().getValue() instanceof JVirtualInvokeExpr) ||
                    (((JAssignStmt)statement).getRightOpBox().getValue() instanceof JInterfaceInvokeExpr))
                {
                    SootMethod method = ((AbstractInvokeExpr)(((JAssignStmt)statement).getRightOpBox()).getValue()).getMethod();
                    List<SootField> potentialEncapsulatedFields = ((AbstractInvokeExpr)(((JAssignStmt)statement).getRightOpBox()).getValue())
                            .getMethodRef()
                            .declaringClass()
                            .getFields()
                            .stream()
                            .collect(Collectors.toList());

                    if (methodFactory.isGetter(method,potentialEncapsulatedFields).isPresent())
                    {
                        isGetterCallStatement = true;
                    }
                }
            }
        }
        return isGetterCallStatement;
    }

    @Override
    public boolean isSetterCallStatement(Unit statement)
    {
        boolean isSetterCallStatement = false;

        if (isInvocationStatement(statement))
        {
            if ((((JInvokeStmt)statement).getInvokeExprBox().getValue() instanceof JVirtualInvokeExpr)||
                (((JInvokeStmt)statement).getInvokeExprBox().getValue() instanceof JInterfaceInvokeExpr))
            {
                SootMethod method = ((AbstractInvokeExpr)(((JInvokeStmt)statement).getInvokeExprBox().getValue())).getMethod();
                List<SootField> potentialEncapsulatedFields = ((AbstractInvokeExpr)(((JInvokeStmt)statement).getInvokeExprBox()
                        .getValue()))
                        .getMethodRef()
                        .declaringClass()
                        .getFields()
                        .stream()
                        .collect(Collectors.toList());

                if (methodFactory.isSetter(method,potentialEncapsulatedFields).isPresent())
                {
                    isSetterCallStatement = true;
                }
            }
        }
        else
        {
            if (isAssignmentStatement(statement))
            {
                if ((((JAssignStmt)statement).getRightOpBox().getValue() instanceof JVirtualInvokeExpr) ||
                    (((JAssignStmt)statement).getRightOpBox().getValue() instanceof JInterfaceInvokeExpr))
                {
                    SootMethod method = ((AbstractInvokeExpr)(((JAssignStmt)statement).getRightOpBox()).getValue()).getMethod();
                    List<SootField> potentialEncapsulatedFields = ((AbstractInvokeExpr)(((JAssignStmt)statement).getRightOpBox()).getValue())
                            .getMethodRef()
                            .declaringClass()
                            .getFields()
                            .stream()
                            .collect(Collectors.toList());

                    if (methodFactory.isSetter(method,potentialEncapsulatedFields).isPresent())
                    {
                        isSetterCallStatement = true;
                    }
                }
            }
        }
        return isSetterCallStatement;
    }


}
