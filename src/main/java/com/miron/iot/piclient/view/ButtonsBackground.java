package com.miron.iot.piclient.view;

import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class ButtonsBackground {

    public Background bedroomOpen(){
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image( getClass().getResource("/images/bedroom.png").toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        return background;
    }

    public Background bedroomClosed(){
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image( getClass().getResource("/images/bedroomClosed.png").toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        return background;
    }


}
