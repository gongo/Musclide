package musclide.sensor;

import org.OpenNI.Point3D;

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
public class Muscle extends Point3D {
    public static final double THRESHOLD_TOLERANCE  = 25.0f;
    public static final double THRESHOLD_ORTHOGONAL = 90.0f;
    public static final double THRESHOLD_STRAIGHT   = 180.0 - THRESHOLD_TOLERANCE;


    public Muscle() {
        super();
    }

    public Muscle(final Point3D p) {
        this.setPoint(p.getX(), p.getY(), p.getZ());
    }

    public Muscle(float x, float y, float z) {
        this.setPoint(x, y, z);
    }

    public static Muscle add(Muscle a, Muscle b) {
        return new Muscle(a.getX() + b.getX(), a.getY() + b.getY(), a.getZ() + b.getZ());
    }

    public static Muscle sub(Muscle a, Muscle b) {
        return new Muscle(a.getX() - b.getX(), a.getY() - b.getY(), a.getZ() - b.getZ());
    }

    private double magnitude() {
        return Math.sqrt(getX()*getX() + getY()*getY() + getZ()*getZ());
    }

    public double dot(final Muscle p) {

        return (getX()*p.getX() + getY()*p.getY() + getZ()*p.getZ()) / magnitude() / p.magnitude();
    }

    public boolean withinAngle(final Muscle p, double angle) {
        return dot(p) >= Math.cos(Math.toRadians(angle));
    }

    public boolean withoutAngle(final Muscle p, double angle) {
        return !withinAngle(p, angle);
    }

    public boolean betweenAngle(final Muscle p, double angle) {
        return withoutAngle(p, angle - THRESHOLD_TOLERANCE)
                && withinAngle(p, angle + THRESHOLD_TOLERANCE);
    }


    public boolean isStraight(final Muscle p) {
        return withinAngle(p, THRESHOLD_TOLERANCE);
    }

    public boolean isParallel(final Muscle p) {
        return isStraight(p) || withoutAngle(p, THRESHOLD_STRAIGHT);
    }


    public boolean isOrthogonal(final Muscle p)
    {
        return betweenAngle(p, THRESHOLD_ORTHOGONAL);
    }

}
