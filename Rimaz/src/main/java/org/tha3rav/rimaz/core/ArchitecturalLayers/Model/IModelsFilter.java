package org.tha3rav.rimaz.core.ArchitecturalLayers.Model;

import java.util.List;

public interface IModelsFilter
{
    List<Model> Filter();
    List<BeanModel> getRawModels();
    List<ParcelableModel> getParcelableModels();
    List<AutoValueModel> getAutoValueModels();
    List<FlatModel> getFlatModels();
    List<GSONSerializedModel> getGSONSerializedModels();
    List<SQLiteOpenHelperModel> getSQLiteOpenHelperModels();
    List<JavaIOSerializableModel> getJavaIOSerializableModels();
    List<LiteSuitsORMModel> getLiteSuitsORMModels();
}
