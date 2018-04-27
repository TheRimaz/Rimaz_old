package org.tha3rav.rimaz.core.ArchitecturalLayers.Model;

import org.tha3rav.rimaz.apk.ApkClass;

public abstract class ModelAbstractFactory
{
    public abstract Model getModel(ApkClass apkClass);

    public abstract boolean isBeanModel(ApkClass apkClass);

    public abstract boolean isFlatModel(ApkClass apkClass);

    public abstract boolean isParcelableModel(ApkClass apkClass);

    public abstract boolean isAutoValueModel(ApkClass apkClass);

    public abstract boolean isGSONSerializedModel(ApkClass apkClass);

    public abstract boolean isSQLiteOpenHelperModel(ApkClass apkClass);

    public abstract boolean isJavaIOSerializedModel(ApkClass apkClass);

    public abstract boolean isLiteSuitsORMModel(ApkClass apkClass);

    public abstract boolean isGreenDAOModel(ApkClass apkClass);

    public abstract boolean isORMLiteModel(ApkClass apkClass);

    public abstract boolean isRequeryModel(ApkClass apkClass);

    public abstract boolean isActiveAndroidModel(ApkClass apkClass);

    public abstract boolean isSugarModel(ApkClass apkClass);

    public abstract boolean isORManModel(ApkClass apkClass);

    public abstract boolean isOrmDroidModel(ApkClass apkClass);

    public abstract boolean isSprinklesModel(ApkClass apkClass);

    public abstract boolean isAndroidAnnotatedSQLModel(ApkClass apkClass);

    public abstract boolean isDBExecutorModel(ApkClass apkClass);

    public abstract boolean isLitePalModel(ApkClass apkClass);

    public abstract boolean isDBFlowModel(ApkClass apkClass);

    public abstract boolean isCupboardModel(ApkClass apkClass);

    public abstract boolean isSquiDBModel(ApkClass apkClass);

    public abstract boolean isAndroidOrmaModel(ApkClass apkClass);

    public abstract boolean isRushOrmModel(ApkClass apkClass);

    public abstract boolean isStorIOModel(ApkClass apkClass);

    public abstract boolean isRealmModel(ApkClass apkClass);

    public abstract boolean isNexusDataModel(ApkClass apkClass);

    public abstract boolean isPoetryModel(ApkClass apkClass);

    public abstract boolean isSchematicModel(ApkClass apkClass);

    public abstract boolean isAormModel(ApkClass apkClass);

    public abstract boolean isRoomModel(ApkClass apkClass);

    public abstract boolean isJacksonModel(ApkClass apkClass);

    public abstract boolean isIGJSONParserModel(ApkClass apkClass);

    public abstract boolean isShillelaghModel(ApkClass apkClass);

    public abstract boolean isOllieModel(ApkClass apkClass);

    public abstract boolean isTriOrmModel(ApkClass apkClass);
}
