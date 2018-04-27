package org.tha3rav.rimaz.core.ArchitecturalLayers.Model;

import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.core.OOP.Field;

import java.util.List;

import soot.RefType;
import soot.tagkit.VisibilityAnnotationTag;

import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS
        .ACTIVEANDROID_ANNOTATIONS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.ACTIVEANDROID_MODEL;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.ANDROIDORMA_ANNOTATIONS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS
        .ANDROID_ANNOTATEDSQL_ANNOTATIONS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.AORM_ANNOTATIONS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.AUTO_VALUE;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.CUPBOARD_ANNOTATIONS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.DBEXECUTOR_ANNOTATIONS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.DBFLOW_ANNOTATIONS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.GREENDAO_ANNOTATIONS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.GSONG_ANNOTATIONS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.IGJSONPARSER_ANNOTATIONS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.JACKSON_ANNOTATIONS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.LITEPAL_ANNOTATIONS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS
        .LITEPAL_DATASUPPORTOBJECT;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.LITESUITSORM__ANNOTATION;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.NEXUSDATA_MANAGEDOBJECT;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.OLLIE_ANNOTATIONS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.OLLIE_MODELOBJECT;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.ORMAN_ANNOTATIONS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.ORMAN_MODELOBJECT;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.ORMDROID_ANNOTATIONS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.ORMDROID_ENTITYOBJECT;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.ORMLITE_ANNOTATIONS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.POETRY_ANNOTATIONS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.REALM_ANNOTATIONS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.REALM_REALMMODEL;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.REALM_REALMOBJECT;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.REQUERY_ANNOTATIONS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.REQUERY_PERSISTABLE;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.ROOM_ANNOTATIONS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.RUSHORM_ANNOTATIONS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.RUSHORM_RUSH;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.RUSHORM_RUSHOBJECT;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.SCHEMATIC_ANNOTATIONS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.SHILLELAGH_ANNOTATIONS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.SPRINKLES_ANNOTATIONS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.SPRINKLES_MODELOBJECT;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.SQUIDB_ANNOTATIONS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.STORIO_ANNOTATIONS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.SUGAR_ANNOTATIONS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.SUGAR_SUGARRECORD;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.ANNOTATIONS.TRIORM_ANNOTATIONS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.METHODS.SQLITEOPENHELPER_ONCREATE;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.OBJECTS.ANDROID_SQLITE_DATA_BASE;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.OBJECTS.ANDROID_SQLITE_OPEN_HELPER;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.OBJECTS.JAVA_IO_SERIALIZABLE;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.OBJECTS.PARCELABLE;
import static org.tha3rav.rimaz.utils.OOP.OOPConstants.Numbers.ZERO;
import static org.tha3rav.rimaz.utils.OOP.OOPConstants.Symbols.DOT;
import static org.tha3rav.rimaz.utils.OOP.OOPConstants.Symbols.SLASH;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.getAllUpperTypes;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.isRefType;

public class ModelFactory extends ModelAbstractFactory
{
    private List<String> platformExcludedPackages;
    private List<String> platformIncludedPackages;

    public ModelFactory(List<String> platformExcludedPackages, List<String> platformIncludedPackages)
    {
        this.platformExcludedPackages = platformExcludedPackages;
        this.platformIncludedPackages = platformIncludedPackages;
    }

    @Override
    public Model getModel(ApkClass apkClass)
    {
        Model madeModel = null;

        if(isAutoValueModel(apkClass))
        {
            return new AutoValueModel(apkClass);
        }
        if(isParcelableModel(apkClass))
        {
            return new ParcelableModel(apkClass);
        }
        if(isBeanModel(apkClass))
        {
            return new BeanModel(apkClass);
        }
        if(isFlatModel(apkClass))
        {
            return new FlatModel(apkClass);
        }
        if(isGSONSerializedModel(apkClass))
        {
            return new GSONSerializedModel(apkClass);
        }
        if(isSQLiteOpenHelperModel(apkClass))
        {
            return new SQLiteOpenHelperModel(apkClass);
        }
        if(isJavaIOSerializedModel(apkClass))
        {
            return new JavaIOSerializableModel(apkClass);
        }
        if(isLiteSuitsORMModel(apkClass))
        {
            return new LiteSuitsModel(apkClass);
        }
        if(isGreenDAOModel(apkClass))
        {
            return new GreenDAOModel(apkClass);
        }
        if(isORMLiteModel(apkClass))
        {
            return new ORMLiteModel(apkClass);
        }
        if(isRequeryModel(apkClass))
        {
            return new RequeryModel(apkClass);
        }
        if(isActiveAndroidModel(apkClass))
        {
            return new ActiveAndroidModel(apkClass);
        }
        if(isSugarModel(apkClass))
        {
            return new SugarModel(apkClass);
        }
        if(isORManModel(apkClass))
        {
            return new ORManModel(apkClass);
        }
        if(isOrmDroidModel(apkClass))
        {
            return new OrmDroidModel(apkClass);
        }
        if(isSprinklesModel(apkClass))
        {
            return new SprinklesModel(apkClass);
        }
        if(isAndroidAnnotatedSQLModel(apkClass))
        {
            return new AndroidAnnotatedSQLModel(apkClass);
        }
        if(isDBExecutorModel(apkClass))
        {
            return new DBExecutorModel(apkClass);
        }
        if(isLitePalModel(apkClass))
        {
            return new LitePalModel(apkClass);
        }
        if(isDBFlowModel(apkClass))
        {
            return new DBFlowModel(apkClass);
        }
        if(isCupboardModel(apkClass))
        {
            return new CupboardModel(apkClass);
        }
        if(isSquiDBModel(apkClass))
        {
            return new SquiDBModel(apkClass);
        }
        if(isAndroidOrmaModel(apkClass))
        {
            return new AndroidOrmaModel(apkClass);
        }
        if(isRushOrmModel(apkClass))
        {
            return new RushOrmModel(apkClass);
        }
        if(isStorIOModel(apkClass))
        {
            return new StorIOModel(apkClass);
        }
        if(isRealmModel(apkClass))
        {
            return new RealmModel(apkClass);
        }

        if(isNexusDataModel(apkClass))
        {
            return new NexusDataModel(apkClass);
        }
        if(isPoetryModel(apkClass))
        {
            return new PoetryModel(apkClass);
        }
        if(isSchematicModel(apkClass))
        {
            return new SchematicModel(apkClass);
        }
        if(isAormModel(apkClass))
        {
            return new AormModel(apkClass);
        }
        if(isRoomModel(apkClass))
        {
            return new RoomModel(apkClass);
        }
        if(isJacksonModel(apkClass))
        {
            return new JacksonModel(apkClass);
        }
        if(isIGJSONParserModel(apkClass))
        {
            return new IGJSONParserModel(apkClass);
        }
        if(isShillelaghModel(apkClass))
        {
            return new ShillelaghModel(apkClass);
        }
        if(isOllieModel(apkClass))
        {
            return new OllieModel(apkClass);
        }
        if(isTriOrmModel(apkClass))
        {
            return new TriOrmModel(apkClass);
        }


        return madeModel;
    }

    @Override
    public boolean isBeanModel(ApkClass apkClass)
    {
        boolean isStatic = apkClass.getSootClass().isStatic();
        boolean isExtendingPlatformClass = apkClass.isExtendingPlatformClass(platformExcludedPackages,platformIncludedPackages);
        boolean hasAtLeastOneConstructor = apkClass.getConstructors().size() > ZERO;
        boolean hasFields = apkClass.getFields().size() > ZERO;
        boolean eachFieldHasGetterAndSetter = true;
        List<Field> fields = apkClass.getFields();
        for (Field field: fields)
        {
            if (!field.hasGetter() || !field.hasSetter())
            {
                eachFieldHasGetterAndSetter = false;
                break;
            }
        }
        return !isStatic &&
                hasFields &&
               !isExtendingPlatformClass &&
                hasAtLeastOneConstructor &&
                eachFieldHasGetterAndSetter;
    }

    @Override
    public boolean isFlatModel(ApkClass apkClass)
    {
        boolean isPublicClass = apkClass.getSootClass().isPublic();

        boolean isStaticClass = apkClass.getSootClass().isStatic();

        boolean hasFields = apkClass.getFields().size() > ZERO;

        boolean allFieldsArePublic = apkClass.getFields()
                .stream()
                .allMatch(field -> field.getSootField().isPublic());

        boolean hasNoSettersNoGetters = (apkClass.getGetters().size() == 0) &&
                                        (apkClass.getSetters().size() == 0) ;

        boolean hasNoOtherMethodsOtherThanConstructors = apkClass.getMethods().size() == 0;

        return isPublicClass &&
               !isStaticClass &&
               hasFields &&
               allFieldsArePublic &&
               hasNoSettersNoGetters &&
               hasNoOtherMethodsOtherThanConstructors;
    }

    @Override
    public boolean isParcelableModel(ApkClass apkClass)
    {
        boolean isExtendingPlatformClass = apkClass.isExtendingPlatformClass(platformExcludedPackages,platformIncludedPackages);
        boolean isImplementingParcelable = getAllUpperTypes(apkClass.getSootClass())
                .stream()
                .anyMatch(sootClass -> sootClass.implementsInterface(PARCELABLE));
        return !isExtendingPlatformClass &&
                isImplementingParcelable;
    }

    @Override
    public boolean isAutoValueModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass, AUTO_VALUE);
        return hasJsonAdapterAnnotation;
    }

    @Override
    public boolean isGSONSerializedModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass,
                                                            GSONG_ANNOTATIONS);

        boolean hasMemberAnnotated = areMembersAnnotated(apkClass,
                                                         GSONG_ANNOTATIONS);

        return hasJsonAdapterAnnotation || hasMemberAnnotated;
    }

    @Override
    public boolean isSQLiteOpenHelperModel(ApkClass apkClass)
    {
        boolean extendsSQLiteOpenHelperClass =
                getAllUpperTypes(apkClass.getSootClass()).stream()
                .anyMatch(sootClass -> sootClass.getName().contains(ANDROID_SQLITE_OPEN_HELPER));

        boolean hasSQLiteOpenHelperOnCreateLikeMethod = apkClass.getMethods()
                .stream()
                .anyMatch(method -> {
                    boolean isOnCreateMethod = method.getName().contains(SQLITEOPENHELPER_ONCREATE);
                    boolean hasParameters = method.getSootMethod().getParameterCount() > ZERO;
                    boolean hasAParameterOfTypeAndroidSqliteDataBase = false;
                    if (hasParameters)
                    {
                        hasAParameterOfTypeAndroidSqliteDataBase = method.getSootMethod()
                                .getParameterTypes()
                                .stream()
                                .filter(type -> isRefType(type))
                                .map(type -> (RefType)type)
                                .anyMatch(type -> type.getClassName().contains(ANDROID_SQLITE_DATA_BASE));
                    }
                    return isOnCreateMethod &&
                           hasAParameterOfTypeAndroidSqliteDataBase;
                });

        return extendsSQLiteOpenHelperClass ||
               hasSQLiteOpenHelperOnCreateLikeMethod;
    }

    @Override
    public boolean isJavaIOSerializedModel(ApkClass apkClass)
    {
        return getAllUpperTypes(apkClass.getSootClass())
                .stream()
                .anyMatch(sootClass -> sootClass.implementsInterface(JAVA_IO_SERIALIZABLE));
    }

    @Override
    public boolean isGreenDAOModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass, GREENDAO_ANNOTATIONS);

        boolean hasMemberAnnotated = areMembersAnnotated(apkClass, GREENDAO_ANNOTATIONS);

        return hasJsonAdapterAnnotation || hasMemberAnnotated;
    }

    @Override
    public boolean isORMLiteModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass, ORMLITE_ANNOTATIONS);

        boolean hasMemberAnnotated = areMembersAnnotated(apkClass, ORMLITE_ANNOTATIONS);

        return hasJsonAdapterAnnotation || hasMemberAnnotated;
    }

    @Override
    public boolean isRequeryModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass, REQUERY_ANNOTATIONS);

        boolean hasMemberAnnotated = areMembersAnnotated(apkClass, REQUERY_ANNOTATIONS);

        boolean classImplementsPersistableInterface = getAllUpperTypes(apkClass.getSootClass())
                .stream()
                .anyMatch(sootClass -> sootClass.implementsInterface(REQUERY_PERSISTABLE));

        return hasJsonAdapterAnnotation ||
               hasMemberAnnotated ||
               classImplementsPersistableInterface;
    }

    @Override
    public boolean isActiveAndroidModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass, ACTIVEANDROID_ANNOTATIONS);

        boolean hasMemberAnnotated = areMembersAnnotated(apkClass, ACTIVEANDROID_ANNOTATIONS);

        boolean classExtendsSugarClass = getAllUpperTypes(apkClass.getSootClass())
                .stream()
                .filter(sootClass -> sootClass.hasSuperclass())
                .anyMatch(sootClass -> sootClass.getSuperclass().getName().contains(ACTIVEANDROID_MODEL));

        return hasJsonAdapterAnnotation ||
               hasMemberAnnotated ||
               classExtendsSugarClass;
    }

    @Override
    public boolean isSugarModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass, SUGAR_ANNOTATIONS);

        boolean hasMemberAnnotated = areMembersAnnotated(apkClass, SUGAR_ANNOTATIONS);

        boolean classExtendsSugarClass = getAllUpperTypes(apkClass.getSootClass())
                .stream()
                .filter(sootClass -> sootClass.hasSuperclass())
                .anyMatch(sootClass -> sootClass.getSuperclass().getName().contains(SUGAR_SUGARRECORD));

        return hasJsonAdapterAnnotation ||
               hasMemberAnnotated ||
               classExtendsSugarClass;
    }

    @Override
    public boolean isORManModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass, ORMAN_ANNOTATIONS);

        boolean hasMemberAnnotated = areMembersAnnotated(apkClass, ORMAN_ANNOTATIONS);

        boolean classExtendsModelObject = getAllUpperTypes(apkClass.getSootClass())
                .stream()
                .filter(sootClass -> sootClass.hasSuperclass())
                .anyMatch(sootClass -> sootClass.getSuperclass().getName().contains(ORMAN_MODELOBJECT));

        return hasJsonAdapterAnnotation ||
               hasMemberAnnotated ||
               classExtendsModelObject;
    }

    @Override
    public boolean isOrmDroidModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass, ORMDROID_ANNOTATIONS);

        boolean hasMemberAnnotated = areMembersAnnotated(apkClass, ORMDROID_ANNOTATIONS);

        boolean classExtendsModelObject = getAllUpperTypes(apkClass.getSootClass())
                .stream()
                .filter(sootClass -> sootClass.hasSuperclass())
                .anyMatch(sootClass -> sootClass.getSuperclass().getName().contains(ORMDROID_ENTITYOBJECT));

        return hasJsonAdapterAnnotation ||
               hasMemberAnnotated ||
               classExtendsModelObject;
    }

    @Override
    public boolean isSprinklesModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass, SPRINKLES_ANNOTATIONS);

        boolean hasMemberAnnotated = areMembersAnnotated(apkClass, SPRINKLES_ANNOTATIONS);

        boolean classExtendsModelObject = getAllUpperTypes(apkClass.getSootClass())
                .stream()
                .filter(sootClass -> sootClass.hasSuperclass())
                .anyMatch(sootClass -> sootClass.getSuperclass().getName().contains(SPRINKLES_MODELOBJECT));

        return hasJsonAdapterAnnotation ||
               hasMemberAnnotated ||
               classExtendsModelObject;
    }

    @Override
    public boolean isAndroidAnnotatedSQLModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass, ANDROID_ANNOTATEDSQL_ANNOTATIONS);

        boolean hasMemberAnnotated = areMembersAnnotated(apkClass, ANDROID_ANNOTATEDSQL_ANNOTATIONS);

        return hasJsonAdapterAnnotation ||
               hasMemberAnnotated ;
    }

    @Override
    public boolean isDBExecutorModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass, DBEXECUTOR_ANNOTATIONS);

        boolean hasMemberAnnotated = areMembersAnnotated(apkClass, DBEXECUTOR_ANNOTATIONS);

        return hasJsonAdapterAnnotation ||
               hasMemberAnnotated ;
    }

    @Override
    public boolean isLitePalModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass, LITEPAL_ANNOTATIONS);

        boolean hasMemberAnnotated = areMembersAnnotated(apkClass, LITEPAL_ANNOTATIONS);

        boolean classExtendsDataSupportObject = getAllUpperTypes(apkClass.getSootClass())
                .stream()
                .filter(sootClass -> sootClass.hasSuperclass())
                .anyMatch(sootClass -> sootClass.getSuperclass().getName().contains(LITEPAL_DATASUPPORTOBJECT));

        return hasJsonAdapterAnnotation ||
               hasMemberAnnotated ||
               classExtendsDataSupportObject;
    }

    @Override
    public boolean isDBFlowModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass, DBFLOW_ANNOTATIONS);

        boolean hasMemberAnnotated = areMembersAnnotated(apkClass, DBFLOW_ANNOTATIONS);

        return hasJsonAdapterAnnotation || hasMemberAnnotated;

    }

    @Override
    public boolean isCupboardModel(ApkClass apkClass)
    {
        boolean hasMemberAnnotated = areMembersAnnotated(apkClass, CUPBOARD_ANNOTATIONS);

        return hasMemberAnnotated;
    }

    @Override
    public boolean isSquiDBModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass, SQUIDB_ANNOTATIONS);

        boolean hasMemberAnnotated = areMembersAnnotated(apkClass, SQUIDB_ANNOTATIONS);

        return hasJsonAdapterAnnotation ||
               hasMemberAnnotated  ;
    }

    @Override
    public boolean isAndroidOrmaModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass, ANDROIDORMA_ANNOTATIONS);

        boolean hasMemberAnnotated = areMembersAnnotated(apkClass, ANDROIDORMA_ANNOTATIONS);

        return hasJsonAdapterAnnotation ||
               hasMemberAnnotated;
    }

    @Override
    public boolean isRushOrmModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass, RUSHORM_ANNOTATIONS);

        boolean hasMemberAnnotated = areMembersAnnotated(apkClass, RUSHORM_ANNOTATIONS);

        boolean classExtendsRushObject = getAllUpperTypes(apkClass.getSootClass())
                .stream()
                .filter(sootClass -> sootClass.hasSuperclass())
                .anyMatch(sootClass -> sootClass.getSuperclass().getName().contains(RUSHORM_RUSHOBJECT));

        boolean classImplementsRushInterface = getAllUpperTypes(apkClass.getSootClass())
                .stream()
                .anyMatch(sootClass -> sootClass.implementsInterface(RUSHORM_RUSH));

        return hasJsonAdapterAnnotation ||
               hasMemberAnnotated ||
               classExtendsRushObject ||
               classImplementsRushInterface;
    }

    @Override
    public boolean isStorIOModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass, STORIO_ANNOTATIONS);

        boolean hasMemberAnnotated = areMembersAnnotated(apkClass, STORIO_ANNOTATIONS);

        return hasJsonAdapterAnnotation ||
               hasMemberAnnotated  ;
    }

    @Override
    public boolean isRealmModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass, REALM_ANNOTATIONS);

        boolean hasMemberAnnotated = areMembersAnnotated(apkClass, REALM_ANNOTATIONS);

        boolean classExtendsRealmObject = getAllUpperTypes(apkClass.getSootClass())
                .stream()
                .filter(sootClass -> sootClass.hasSuperclass())
                .anyMatch(sootClass -> sootClass.getSuperclass().getName().contains(REALM_REALMOBJECT));

        boolean classImplementsRealmModelInterface = getAllUpperTypes(apkClass.getSootClass())
                .stream()
                .anyMatch(sootClass -> sootClass.implementsInterface(REALM_REALMMODEL));

        return hasJsonAdapterAnnotation ||
               hasMemberAnnotated ||
               classExtendsRealmObject ||
               classImplementsRealmModelInterface;
    }

    @Override
    public boolean isNexusDataModel(ApkClass apkClass)
    {
        boolean classExtendsDataSupportObject = getAllUpperTypes(apkClass.getSootClass())
                .stream()
                .filter(sootClass -> sootClass.hasSuperclass())
                .anyMatch(sootClass -> sootClass.getSuperclass().getName().contains(NEXUSDATA_MANAGEDOBJECT));

        return classExtendsDataSupportObject;
    }

    @Override
    public boolean isPoetryModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass, POETRY_ANNOTATIONS);

        boolean hasMemberAnnotated = areMembersAnnotated(apkClass, POETRY_ANNOTATIONS);

        return hasJsonAdapterAnnotation ||
               hasMemberAnnotated  ;
    }

    @Override
    public boolean isSchematicModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass, SCHEMATIC_ANNOTATIONS);

        boolean hasMemberAnnotated = areMembersAnnotated(apkClass, SCHEMATIC_ANNOTATIONS);

        return hasJsonAdapterAnnotation || hasMemberAnnotated;
    }

    @Override
    public boolean isAormModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass, AORM_ANNOTATIONS);

        boolean hasMemberAnnotated = areMembersAnnotated(apkClass, AORM_ANNOTATIONS);

        return hasJsonAdapterAnnotation || hasMemberAnnotated;
    }

    @Override
    public boolean isRoomModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass, ROOM_ANNOTATIONS);

        boolean hasMemberAnnotated = areMembersAnnotated(apkClass, ROOM_ANNOTATIONS);

        return hasJsonAdapterAnnotation || hasMemberAnnotated;
    }

    @Override
    public boolean isJacksonModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass, JACKSON_ANNOTATIONS);

        boolean hasMemberAnnotated = areMembersAnnotated(apkClass, JACKSON_ANNOTATIONS);

        return hasJsonAdapterAnnotation || hasMemberAnnotated;
    }

    @Override
    public boolean isIGJSONParserModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass, IGJSONPARSER_ANNOTATIONS);

        boolean hasMemberAnnotated = areMembersAnnotated(apkClass, IGJSONPARSER_ANNOTATIONS);

        return hasJsonAdapterAnnotation || hasMemberAnnotated;
    }

    @Override
    public boolean isShillelaghModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass, SHILLELAGH_ANNOTATIONS);

        boolean hasMemberAnnotated = areMembersAnnotated(apkClass, SHILLELAGH_ANNOTATIONS);

        return hasJsonAdapterAnnotation || hasMemberAnnotated;
    }

    @Override
    public boolean isOllieModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass, OLLIE_ANNOTATIONS);

        boolean hasMemberAnnotated = areMembersAnnotated(apkClass, OLLIE_ANNOTATIONS);

        boolean classExtendsModelObject = getAllUpperTypes(apkClass.getSootClass())
                .stream()
                .filter(sootClass -> sootClass.hasSuperclass())
                .anyMatch(sootClass -> sootClass.getSuperclass().getName().contains(OLLIE_MODELOBJECT));

        return hasJsonAdapterAnnotation ||
               hasMemberAnnotated ||
               classExtendsModelObject;
    }

    @Override
    public boolean isTriOrmModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass, TRIORM_ANNOTATIONS);

        boolean hasMemberAnnotated = areMembersAnnotated(apkClass, TRIORM_ANNOTATIONS);

        return hasJsonAdapterAnnotation ||
               hasMemberAnnotated ;
    }

    @Override
    public boolean isLiteSuitsORMModel(ApkClass apkClass)
    {
        boolean hasJsonAdapterAnnotation = isClassAnnotated(apkClass,
                                                            LITESUITSORM__ANNOTATION);

        boolean hasMemberAnnotated = areMembersAnnotated(apkClass,
                                                         LITESUITSORM__ANNOTATION);

        return hasJsonAdapterAnnotation || hasMemberAnnotated;
    }






    private boolean areMembersAnnotated(ApkClass apkClass,
                                        String gsongAnnotations)
    {
        return apkClass.getMembers()
                .stream()
                .anyMatch(abstractHost -> abstractHost.getTags()
                        .stream()
                        .filter(tag -> tag instanceof VisibilityAnnotationTag)
                        .map(tag -> (VisibilityAnnotationTag) tag)
                        .map(visibilityAnnotationTag -> visibilityAnnotationTag.getAnnotations())
                        .flatMap(annotationTags -> annotationTags.stream())
                        .map(annotationTag -> annotationTag.getType())
                        .map(type -> type.replace(SLASH,
                                                  DOT))
                        .anyMatch(type -> type.contains(gsongAnnotations)));
    }

    private boolean isClassAnnotated(ApkClass apkClass,
                                     String annotationsPath)
    {
        return apkClass.getSootClass()
                .getTags()
                .stream()
                .filter(tag -> tag instanceof VisibilityAnnotationTag)
                .map(tag -> (VisibilityAnnotationTag) tag)
                .map(visibilityAnnotationTag -> visibilityAnnotationTag.getAnnotations())
                .flatMap(annotationTags -> annotationTags.stream())
                .map(annotationTag -> annotationTag.getType())
                .map(type -> type.replace(SLASH, DOT))
                .anyMatch(tag -> tag.toString().contains(annotationsPath));
    }
}

