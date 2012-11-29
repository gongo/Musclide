package musclide.slide;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import musclide.Ring;
import musclide.event.MuscleEvent;
import musclide.sensor.Speaker;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2012 Wataru MIYAGUNI
 * <p/>
 * MIT License
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
public abstract class Slide extends Pane {
    /**
     * Image view node on root
     */
    protected ImageView imageView = new ImageView() {
        {
            setPreserveRatio(true);
        }
    };

    /**
     * View image list
     */
    protected List<Image> images = new ArrayList<Image>();

    /**
     * Index of current image.
     */
    protected IntegerProperty index = new SimpleIntegerProperty() {
        @Override
        public void set(int i) {
            super.set(i);
            fireValueChangedEvent();
        }
    };

    private ObjectBinding<Image> currentPage = new ObjectBinding<Image>() {
        {
            super.bind(index);
        }

        @Override
        protected Image computeValue() {
            if ((index.get() < 0) || (images.size() <= index.get())) {
                return null;
            }

            return images.get(index.get());
        }
    };

    private Ring ring;

    Slide(Ring ring) {
        this.ring = ring;
        setPrefSize(640.0, 480.0);
        getChildren().add(imageView);
        imageView.fitWidthProperty().bind(widthProperty());
        imageView.imageProperty().bind(currentPage);

        ring.setOnPosingDetected(new EventHandler<MuscleEvent>() {
            @Override
            public void handle(MuscleEvent muscleEvent) {
                switch (muscleEvent.pose) {
                    case NEXT:
                        next();
                        break;
                    case PREV:
                        prev();
                        break;
                    case FIRST:
                        first();
                        break;
                    case LAST:
                        last();
                        break;
                    case STANDBY:
                    default:
                        standby();
                }
            }
        });
    }

    abstract public void next();
    abstract public void prev();
    abstract public void first();
    abstract public void last();
    abstract public void standby();
}
