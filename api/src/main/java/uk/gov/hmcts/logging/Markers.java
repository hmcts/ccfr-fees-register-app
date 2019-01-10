package uk.gov.hmcts.logging;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class Markers {
    public static final Marker fatal = MarkerFactory.getMarker("FATAL");

    private Markers() {}
}
