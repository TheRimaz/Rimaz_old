package org.tha3rav.rimaz.core.OOP;

import org.tha3rav.rimaz.apk.ApkTypeEntry;

import java.util.List;
import java.util.Optional;

import soot.SootField;
import soot.SootMethod;

import static org.tha3rav.rimaz.utils.OOP.OOPConstants.FieldPrefixes.M;
import static org.tha3rav.rimaz.utils.OOP.OOPConstants.MethodPrefixes.GET;
import static org.tha3rav.rimaz.utils.OOP.OOPConstants.MethodPrefixes.HAS;
import static org.tha3rav.rimaz.utils.OOP.OOPConstants.MethodPrefixes.IS;
import static org.tha3rav.rimaz.utils.OOP.OOPConstants.MethodPrefixes.SET;
import static org.tha3rav.rimaz.utils.OOP.OOPConstants.Numbers.ONE;
import static org.tha3rav.rimaz.utils.OOP.OOPConstants.Numbers.ZERO;

public class MethodFactory extends MethodAbstractFactory
{
    @Override
    public AbstractMethod getMethod(SootMethod sootMethod, List<SootField> fields)
    {
        AbstractMethod method = null;
        if (sootMethod.isConstructor())
        {
            method = new Constructor(sootMethod);
        }
        else
        {
            Optional<Field> isGetter = isGetter(sootMethod, fields);
            Optional<Field> isSetter = isSetter(sootMethod, fields);
            if (isGetter.isPresent())
            {
                method = new Getter(sootMethod, isGetter.get());
            }
            else
            {
                if (isSetter.isPresent())
                {
                    method = new Setter(sootMethod, isSetter.get());
                }
                else
                {
                    method = new Method(sootMethod);
                }
            }
        }
        return method;
    }

    @Override
    public AbstractMethod getMethod(ApkTypeEntry containingType, SootMethod sootMethod, List<SootField> fields)
    {
        AbstractMethod method = null;
        if (sootMethod.isConstructor())
        {
            method = new Constructor(sootMethod, containingType);
        }
        else
        {
            Optional<Field> isGetter = isGetter(sootMethod, fields);
            Optional<Field> isSetter = isSetter(sootMethod, fields);
            if (isGetter.isPresent())
            {
                method = new Getter(sootMethod, containingType, isGetter.get());
            }
            else
            {
                if (isSetter.isPresent())
                {
                    method = new Setter(sootMethod, containingType, isSetter.get());
                }
                else
                {
                    method = new Method(sootMethod, containingType);
                }
            }
        }
        return method;
    }

    public Optional<Field> isGetter(SootMethod sootMethod, List<SootField> fields)
    {
        Optional<Field> getter = Optional.empty();
        String methodName = sootMethod.getName().toLowerCase();
        if (sootMethod.isPublic() && !sootMethod.isStatic())
        {
            for (SootField field: fields)
            {
                if (field.isPrivate() || field.isProtected())
                {
                    String fieldName = field.getName().toLowerCase();
                    if (sootMethod.getReturnType().equals(field.getType()))
                    {
                        if (methodName.equals(HAS + fieldName) ||
                            methodName.equals(IS + fieldName) ||
                            methodName.equals(GET + fieldName))
                        {
                            getter = Optional.of(new Field(field));
                            break;
                        }
                        else
                        {
                            if (methodName.equals(HAS + fieldName.substring(ONE,fieldName.length())) ||
                                methodName.equals(IS + fieldName.substring(ONE,fieldName.length())) ||
                                methodName.equals(GET + fieldName.substring(ONE,fieldName.length())))
                            {
                                getter = Optional.of(new Field(field));
                                break;
                            }
                        }
                    }
                }
            }
        }
        return getter;
    }

    public Optional<Field> isSetter(SootMethod method, List<SootField> fields)
    {
        Optional<Field> setter = Optional.empty();
        String methodName = method.getName().toLowerCase();
        if (method.isPublic() && !method.isStatic())
        {
            for (SootField field : fields)
            {
                if (field.isPrivate() || field.isProtected())
                {
                    String fieldName = field.getName().toLowerCase();
                    if (method.getReturnType().getClass().equals(soot.VoidType.class) && method.getParameterCount() == ONE)
                    {
                        if (method.getParameterType(ZERO).equals(field.getType()))
                        {
                            if (methodName.equals(HAS + fieldName) ||
                                methodName.equals(IS + fieldName) ||
                                methodName.equals(SET + fieldName))
                            {
                                setter = Optional.of(new Field(field));
                                break;
                            }
                            else
                            {
                                if (methodName.equals(HAS + fieldName.substring(ONE,fieldName.length())) ||
                                    methodName.equals(IS + fieldName.substring(ONE,fieldName.length())) ||
                                    methodName.equals(SET + fieldName.substring(ONE,fieldName.length())))
                                {
                                    setter = Optional.of(new Field(field));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return setter;
    }
}
