package musclide.slide;

import javafx.event.Event;
import javafx.scene.image.Image;
import musclide.Ring;
import musclide.event.MuscleEvent;

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
public class OpeningSlide extends Slide {
    /**
     * Directory of images
     */
    private static final String IMAGE_DIR = "images";

    /**
     * Name list of images
     */
    private static final String[] imageNames = new String[] {
            "First.png",
            "Last.png",
            "Left.png",
            "Right.png",
            "Center.png"
    };

    public OpeningSlide(Ring ring) {
        super(ring);
        images.clear();

        for (String name : imageNames) {
            images.add(new Image(IMAGE_DIR + "/" + name));
        }

        index.set(4);
    }

    @Override
    public void next() {
        Event.fireEvent(this, new MuscleEvent());
        index.set(3);
    }

    @Override
    public void prev() {
        index.set(2);
    }

    @Override
    public void first() {
        index.set(0);
    }

    @Override
    public void last() {
        index.set(1);
    }

    @Override
    public void standby() {
        index.set(4);
    }
}
