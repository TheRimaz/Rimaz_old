package org.tha3rav.rimaz.core.ArchitecturalLayers.EventHandling;

import org.tha3rav.rimaz.apk.ApkTypeEntry;

import java.util.List;

public interface IInputEventScanner
{
    List<InputEvent> scanForInputEvents(ApkTypeEntry eventfulType);
}
