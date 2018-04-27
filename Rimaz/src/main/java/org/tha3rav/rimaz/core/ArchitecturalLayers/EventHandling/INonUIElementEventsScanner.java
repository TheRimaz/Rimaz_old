package org.tha3rav.rimaz.core.ArchitecturalLayers.EventHandling;

import org.tha3rav.rimaz.apk.ApkTypeEntry;

import java.util.List;

public interface INonUIElementEventsScanner
{
    List<NonUIElementEvent> scanForNonUIElementEvents(ApkTypeEntry targetedType);
}
