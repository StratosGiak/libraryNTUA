package com.stratos.giak.libraryntua;

import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;

public final class Constants {
    public static final Border BORDER_ERROR = new Border(new BorderStroke(Color.RED, new BorderStrokeStyle(StrokeType.OUTSIDE, null, null, 10, 0, null), new CornerRadii(3), null));

    private Constants() {
    }

}
