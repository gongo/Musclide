package musclide.sensor;

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
public class Entrance {
    private static final int MAX_SPEAKERS = 15;
    private MuscleSensor sensor = null;
    private List<Speaker> speakers = new ArrayList<Speaker>(MAX_SPEAKERS);

    /**
     * Singleton Constructor
     */
    private Entrance() {}

    private static class EntranceHolder {
        private static final Entrance instance = new Entrance();
    }

    public static Entrance instance() {
        return EntranceHolder.instance;
    }

    public void setSensor(MuscleSensor sensor) {
        if (this.sensor == null) {
            speakers.clear();
        }

        this.sensor = sensor;

        for (int i = 1; i <= MAX_SPEAKERS; i++) {
            speakers.add(new Speaker(i, sensor));
        }
    }

    public Speaker entering(int id) {
        return speakers.get(id);
    }

    public Speaker firstSpeaker() {
        for (Speaker s : speakers) {
            if (s.isTracking()) {
                return s;
            }
        }

        return speakers.get(0);
    }
}
