package org.tha3rav.rimaz.utils;

import java.util.Arrays;
import java.util.List;

import soot.SootClass;

import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.CONCURRENCY_TYPES.ASYNC_TASK;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.CONCURRENCY_TYPES.CALLABLE;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.CONCURRENCY_TYPES.EXECUTOR;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.CONCURRENCY_TYPES.FUTURE;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.CONCURRENCY_TYPES.RUNNABLE;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.EVENT_HANDLERS_INTERFACES
        .APPLICATION_ACTIVITY_LIFECYCLE_CALLBACKS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.EVENT_HANDLERS_INTERFACES
        .COMPONENT_CALLBACKS;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.EVENT_HANDLERS_INTERFACES
        .COMPONENT_CALLBACKS2;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.EVENT_HANDLERS_INTERFACES
        .KEYEVENT_CALLBACK;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.EVENT_HANDLERS_INTERFACES
        .LAYOUT_INFLATOR_FACTORY;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.EVENT_HANDLERS_INTERFACES
        .LAYOUT_INFLATOR_FACTORY2;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.EVENT_HANDLERS_INTERFACES
        .VIEW_ONCREATE_CONTEXT_MENU_LISTENER;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.EVENT_HANDLERS_INTERFACES
        .WINDOW_CALLBACK;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.EVENT_HANDLERS_OVERRIDERS
        .ANDROID_APP_ACTIVITY;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.EVENT_HANDLERS_OVERRIDERS
        .ANDROID_APP_APPLICATION;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.EVENT_HANDLERS_OVERRIDERS
        .ANDROID_APP_DIALOG;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.EVENT_HANDLERS_OVERRIDERS
        .ANDROID_APP_FRAGMENT;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.EVENT_HANDLERS_OVERRIDERS
        .ANDROID_VIEW_VIEW;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.LIFE_CYCLE_EVENT_HANDLERS.ON_CREATE;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.LIFE_CYCLE_EVENT_HANDLERS.ON_DESTROY;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.LIFE_CYCLE_EVENT_HANDLERS.ON_PAUSE;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.LIFE_CYCLE_EVENT_HANDLERS.ON_RESTART;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.LIFE_CYCLE_EVENT_HANDLERS.ON_RESUME;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.LIFE_CYCLE_EVENT_HANDLERS.ON_START;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.LIFE_CYCLE_EVENT_HANDLERS.ON_STOP;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.PACKAGES.ANDROID;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.PACKAGES.ANDROID_DATABINDING;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.PACKAGES.COM_ANDROID;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.PACKAGES.DALVIK;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.PACKAGES.JAVA;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.PACKAGES.JAVAX;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.PACKAGES.JUNIT;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.PACKAGES.ORG_APACHE_HTTP;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.PACKAGES.ORG_JSON;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.PACKAGES.ORG_W3C_DOM;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.PACKAGES.ORG_XMLPULL;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.PACKAGES.ORG_XML_SAX;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.getOuterHierarchy;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.getUpperHierarchy;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.isSubClassOf;

public class ANDROID
{
    public static final class AndroidConstants
    {
        public static class PACKAGES
        {
            public static final String JAVA         = "java.";
            public static final String JAVAX         = "javax.";
            public static final String JUNIT         = "junit.";
            public static final String ORG_APACHE_HTTP = "org.apache.http.";
            public static final String ORG_JSON = "org.json.";
            public static final String ORG_W3C_DOM = "org.w3c.dom.";
            public static final String ORG_XML_SAX = "org.xml.sax.";
            public static final String ORG_XMLPULL = "org.xmlpull.";
            public static final String ANDROID      = "android.";
            public static final String ANDROID_DATABINDING = "android.databinding";
            public static final String COM_ANDROID  = "com.android.";
            public static final String DALVIK       = "dalvik.";
        }

        public static class OBJECTS
        {
            public static final String JAVA_LANG_OBJECT   = "java.lang.Object";
            public static final String PARCELABLE         = "android.os.Parcelable";
            public static final String VIEW_DATA_BINDING  = "android.databinding.ViewDataBinding";
            public static final String ANDROID_APP_APPLICATION  = ".app.Application";
            public static final String ANDROID_VIEW_VIEW  = "android.view.View";
            public static final String ANDROID_APP_ACTIVITY  = ".app.Activity";
            public static final String ANDROID_APP_DIALOG  = ".app.Dialog";
            public static final String ANDROID_APP_FRAGMENT  = ".app.Fragment";

            //DataBases
            //SQLiteOpenHelper
            public static final String ANDROID_SQLITE_OPEN_HELPER = "android.database.sqlite.SQLiteOpenHelper";
            public static final String ANDROID_SQLITE_DATA_BASE = "android.database.sqlite.SQLiteDatabase";

            //Java.io.Serializable
            public static final String JAVA_IO_SERIALIZABLE = "java.io.Serializable";

        }

        public static class ANNOTATIONS
        {
            //Auto Value
            public static final String AUTO_VALUE = "com.google.auto.value.AutoValue";

            //LiteSuitsORM
            public static final String LITESUITSORM__ANNOTATION = "com.litesuits.orm.db.annotation";

            //GSON
            public static final String GSONG_ANNOTATIONS = "com.google.gson.annotations";

            //IG-JSON-Parser
            public static final String IGJSONPARSER_ANNOTATIONS = "com.instagram.common.json.annotation";

            //GreenDAO
            public static final String GREENDAO_ANNOTATIONS = "org.greenrobot.greendao.annotation";

            //ORMLite
            public static final String ORMLITE_ANNOTATIONS = "com.j256.ormlite";

            //DBFlow
            public static final String DBFLOW_ANNOTATIONS = "com.raizlabs.android.dbflow.annotation";

            //SUGAR
            public static final String SUGAR_ANNOTATIONS = "com.orm.annotation";
            public static final String SUGAR_SUGARRECORD = "com.orm.annotation.SugarRecord";

            //ACTIVEHANDROID
            public static final String ACTIVEANDROID_ANNOTATIONS = "com.activeandroid.annotation";
            public static final String ACTIVEANDROID_MODEL = "com.activeandroid.Model";

            //Cupboard
            public static final String CUPBOARD_ANNOTATIONS = "nl.qbusict.cupboard.annotation";

            //RushORM
            public static final String RUSHORM_ANNOTATIONS = "co.uk.rushorm.core.annotations";
            public static final String RUSHORM_RUSHOBJECT = "co.uk.rushorm.core.RushObject";
            public static final String RUSHORM_RUSH = "co.uk.rushorm.core.Rush";

            //Requery
            public static final String REQUERY_ANNOTATIONS = "io.requery";
            public static final String REQUERY_PERSISTABLE = "io.requery.Persistable";

            //AndroidORMa
            public static final String ANDROIDORMA_ANNOTATIONS = "com.github.gfx.android.orma.annotation";

            //Realm
            public static final String REALM_ANNOTATIONS = "io.realm.annotations";
            public static final String REALM_REALMOBJECT = "io.realm.RealmObject";
            public static final String REALM_REALMMODEL = "io.realm.RealmModel";

            //Ollie
            public static final String OLLIE_ANNOTATIONS = "ollie.annotation";
            public static final String OLLIE_MODELOBJECT = "ollie.Model";

            //Orman
            public static final String ORMAN_ANNOTATIONS = "org.orman.mapper.annotation";
            public static final String ORMAN_MODELOBJECT = "org.orman.mapper.Model";

            //ORMDroid
            public static final String ORMDROID_ANNOTATIONS = "com.roscopeco.ormdroid";
            public static final String ORMDROID_ENTITYOBJECT = "com.roscopeco.ormdroid.Entity";

            //Sprinkles
            public static final String SPRINKLES_ANNOTATIONS = "se.emilsjolander.sprinkles.annotations";
            public static final String SPRINKLES_MODELOBJECT = "se.emilsjolander.sprinkles.Model";

            //Android-AnnotatedSQL
            public static final String ANDROID_ANNOTATEDSQL_ANNOTATIONS = "com.annotatedsql.annotation";

            //DBExecutor
            public static final String DBEXECUTOR_ANNOTATIONS = "com.shizhefei.db.annotations";

            //LitePal
            public static final String LITEPAL_ANNOTATIONS = "org.litepal.annotation";
            public static final String LITEPAL_DATASUPPORTOBJECT = "org.litepal.crud.DataSupport";

            //SquiDB
            public static final String SQUIDB_ANNOTATIONS = "com.yahoo.squidb.annotations";

            //StorIO
            public static final String STORIO_ANNOTATIONS = "com.pushtorefresh.storio3.sqlite.annotations";

            //NexusData
            public static final String NEXUSDATA_MANAGEDOBJECT = "com.github.dkharrat.nexusdata.core.ManagedObject";

            //Poetry
            public static final String POETRY_ANNOTATIONS = "nl.elastique.poetry.json.annotations";

            //Schematic
            public static final String SCHEMATIC_ANNOTATIONS = "net.simonvt.schematic.annotation";

            //Aorm
            public static final String AORM_ANNOTATIONS = "cn.ieclipse.aorm.annotation";

            //Room
            public static final String ROOM_ANNOTATIONS = "android.arch.persistence.room";

            //Jackson
            public static final String JACKSON_ANNOTATIONS = "com.fasterxml.jackson.annotation";

            //Shillelagh
            public static final String SHILLELAGH_ANNOTATIONS = "shillelagh";

            //TriOrm
            public static final String TRIORM_ANNOTATIONS = "com.hendrix.triorm.annotations";

        }

        public static class METHODS
        {
            //SQLiteOpenHelper
            public static final String SQLITEOPENHELPER_ONCREATE = "onCreate";
        }

        public static class CLASSES
        {
            public static final String R = "R";
            public static final String BR = "BR";
            public static final String BUILDCONFIG = "BuildConfig";
        }

        public static class EventHandlersPrefixes
        {
            public static final String ON = "on";
        }

        public static class EVENT_HANDLERS_INTERFACES
        {
            public static final String KEYEVENT_CALLBACK = "KeyEvent.Callback";
            public static final String APPLICATION_ACTIVITY_LIFECYCLE_CALLBACKS = "Application.ActivityLifecycleCallbacks";
            public static final String WINDOW_CALLBACK = "Window.Callback";
            public static final String LAYOUT_INFLATOR_FACTORY = "LayoutInflater.Factory";
            public static final String LAYOUT_INFLATOR_FACTORY2 = "LayoutInflater.Factory2";
            public static final String COMPONENT_CALLBACKS = "ComponentCallbacks";
            public static final String COMPONENT_CALLBACKS2 = "ComponentCallbacks2";
            public static final String VIEW_ONCREATE_CONTEXT_MENU_LISTENER = "View.OnCreateContextMenuListener";

        }

        public static class EVENT_HANDLERS_OVERRIDERS
        {
            public static final String ANDROID_APP_ACTIVITY  = ".app.Activity";
            public static final String ANDROID_APP_FRAGMENT  = ".app.Fragment";
            public static final String ANDROID_APP_APPLICATION  = ".app.Application";
            public static final String ANDROID_APP_DIALOG = ".app.Dialog";
            public static final String ANDROID_VIEW_VIEW  = ".view.View";

        }

        public static class LIFE_CYCLE_EVENT_HANDLERS
        {
            public static final String ON_CREATE  = "onCreate";
            public static final String ON_START  = "onStart";
            public static final String ON_RESUME  = "onResume";
            public static final String ON_PAUSE = "onPause";
            public static final String ON_STOP  = "onStop";
            public static final String ON_DESTROY  = "onDestroy";
            public static final String ON_RESTART  = "onRestart";

        }

        public static class CONCURRENCY_TYPES
        {
            public static final String RUNNABLE  = "java.lang.Runnable";
            public static final String EXECUTOR  = "java.util.concurrent.Executor";
            public static final String CALLABLE  = "java.util.concurrent.Callable";
            public static final String FUTURE = "java.util.concurrent.Future<V>";
            public static final String ASYNC_TASK  = "android.os.AsyncTask";
        }

        public static class CONCURRENCY_TYPES_INVOCATION_METHODS
        {
            public static final String EXECUTE  = "execute";
            public static final String EXECUTE_ON_EXECUTOR  = "executeOnExecutor";
        }
    }

    public static class AndroidUtils
    {
        public List<String> getPlatformExcludedPackages()
        {
            //JAVA,
            return Arrays.asList(JAVAX,
                                 JUNIT,
                                 ORG_APACHE_HTTP,
                                 ORG_JSON,
                                 ORG_W3C_DOM,
                                 ORG_XML_SAX,
                                 ORG_XMLPULL,
                                 ANDROID,
                                 COM_ANDROID,
                                 DALVIK);
        }

        public List<String> getPlatformIncludedPackages()
        {
            return Arrays.asList(ANDROID_DATABINDING);
        }

        public static List<String> getEventHandlerInterfaces()
        {
            return Arrays.asList(KEYEVENT_CALLBACK,
                                 APPLICATION_ACTIVITY_LIFECYCLE_CALLBACKS,
                                 WINDOW_CALLBACK,
                                 LAYOUT_INFLATOR_FACTORY,
                                 LAYOUT_INFLATOR_FACTORY2,
                                 COMPONENT_CALLBACKS,
                                 COMPONENT_CALLBACKS2,
                                 VIEW_ONCREATE_CONTEXT_MENU_LISTENER);
        }

        public static List<String> getEventHandlerOverriders()
        {
            return Arrays.asList(ANDROID_APP_ACTIVITY,
                                 ANDROID_APP_FRAGMENT,
                                 ANDROID_APP_APPLICATION,
                                 ANDROID_APP_DIALOG,
                                 ANDROID_VIEW_VIEW);
        }

        public static List<String> getLifeCycleEventHandlers()
        {
            return Arrays.asList(ON_CREATE,
                                 ON_START,
                                 ON_RESUME,
                                 ON_PAUSE,
                                 ON_STOP,
                                 ON_DESTROY,
                                 ON_RESTART);
        }

        public static List<String> getConcurrencyTypes()
        {
            return Arrays.asList(RUNNABLE,
                                 EXECUTOR,
                                 CALLABLE,
                                 FUTURE,
                                 ASYNC_TASK);
        }
    }

    public static class AndroidHelpers
    {
        public static boolean isUIElement(SootClass sootClass)
        {
            return isSubClassOf(sootClass, AndroidConstants.OBJECTS.ANDROID_VIEW_VIEW);
        }

        public static boolean isOrExtendingInnerClassOfUIElement(SootClass sootClass)
        {
            return getUpperHierarchy(sootClass)
                    .stream()
                    .filter(upperClass -> upperClass.isInnerClass())
                    .anyMatch(upperClass -> getOuterHierarchy(upperClass)
                            .stream()
                            .anyMatch(outerClass -> isUIElement(outerClass)));
        }
    }
}
