package musclide.sensor;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.OpenNI.SkeletonJoint;
import org.OpenNI.StatusException;

import java.util.HashMap;

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
public class Speaker {
    protected MuscleSensor sensor = null;
    protected int userId;

    private DoubleProperty headX            = new SimpleDoubleProperty();
    private DoubleProperty headY            = new SimpleDoubleProperty();
    private DoubleProperty headZ            = new SimpleDoubleProperty();
    private DoubleProperty neckX            = new SimpleDoubleProperty();
    private DoubleProperty neckY            = new SimpleDoubleProperty();
    private DoubleProperty neckZ            = new SimpleDoubleProperty();
    private DoubleProperty torsoX           = new SimpleDoubleProperty();
    private DoubleProperty torsoY           = new SimpleDoubleProperty();
    private DoubleProperty torsoZ           = new SimpleDoubleProperty();
    private DoubleProperty waistX           = new SimpleDoubleProperty();
    private DoubleProperty waistY           = new SimpleDoubleProperty();
    private DoubleProperty waistZ           = new SimpleDoubleProperty();
    private DoubleProperty leftCollarX      = new SimpleDoubleProperty();
    private DoubleProperty leftCollarY      = new SimpleDoubleProperty();
    private DoubleProperty leftCollarZ      = new SimpleDoubleProperty();
    private DoubleProperty leftShoulderX    = new SimpleDoubleProperty();
    private DoubleProperty leftShoulderY    = new SimpleDoubleProperty();
    private DoubleProperty leftShoulderZ    = new SimpleDoubleProperty();
    private DoubleProperty leftElbowX       = new SimpleDoubleProperty();
    private DoubleProperty leftElbowY       = new SimpleDoubleProperty();
    private DoubleProperty leftElbowZ       = new SimpleDoubleProperty();
    private DoubleProperty leftWristX       = new SimpleDoubleProperty();
    private DoubleProperty leftWristY       = new SimpleDoubleProperty();
    private DoubleProperty leftWristZ       = new SimpleDoubleProperty();
    private DoubleProperty leftHandX        = new SimpleDoubleProperty();
    private DoubleProperty leftHandY        = new SimpleDoubleProperty();
    private DoubleProperty leftHandZ        = new SimpleDoubleProperty();
    private DoubleProperty leftFingerTipX   = new SimpleDoubleProperty();
    private DoubleProperty leftFingerTipY   = new SimpleDoubleProperty();
    private DoubleProperty leftFingerTipZ   = new SimpleDoubleProperty();
    private DoubleProperty rightCollarX     = new SimpleDoubleProperty();
    private DoubleProperty rightCollarY     = new SimpleDoubleProperty();
    private DoubleProperty rightCollarZ     = new SimpleDoubleProperty();
    private DoubleProperty rightShoulderX   = new SimpleDoubleProperty();
    private DoubleProperty rightShoulderY   = new SimpleDoubleProperty();
    private DoubleProperty rightShoulderZ   = new SimpleDoubleProperty();
    private DoubleProperty rightElbowX      = new SimpleDoubleProperty();
    private DoubleProperty rightElbowY      = new SimpleDoubleProperty();
    private DoubleProperty rightElbowZ      = new SimpleDoubleProperty();
    private DoubleProperty rightWristX      = new SimpleDoubleProperty();
    private DoubleProperty rightWristY      = new SimpleDoubleProperty();
    private DoubleProperty rightWristZ      = new SimpleDoubleProperty();
    private DoubleProperty rightHandX       = new SimpleDoubleProperty();
    private DoubleProperty rightHandY       = new SimpleDoubleProperty();
    private DoubleProperty rightHandZ       = new SimpleDoubleProperty();
    private DoubleProperty rightFingerTipX  = new SimpleDoubleProperty();
    private DoubleProperty rightFingerTipY  = new SimpleDoubleProperty();
    private DoubleProperty rightFingerTipZ  = new SimpleDoubleProperty();
    private DoubleProperty leftHipX         = new SimpleDoubleProperty();
    private DoubleProperty leftHipY         = new SimpleDoubleProperty();
    private DoubleProperty leftHipZ         = new SimpleDoubleProperty();
    private DoubleProperty leftKneeX        = new SimpleDoubleProperty();
    private DoubleProperty leftKneeY        = new SimpleDoubleProperty();
    private DoubleProperty leftKneeZ        = new SimpleDoubleProperty();
    private DoubleProperty leftAnkleX       = new SimpleDoubleProperty();
    private DoubleProperty leftAnkleY       = new SimpleDoubleProperty();
    private DoubleProperty leftAnkleZ       = new SimpleDoubleProperty();
    private DoubleProperty leftFootX        = new SimpleDoubleProperty();
    private DoubleProperty leftFootY        = new SimpleDoubleProperty();
    private DoubleProperty leftFootZ        = new SimpleDoubleProperty();
    private DoubleProperty rightHipX        = new SimpleDoubleProperty();
    private DoubleProperty rightHipY        = new SimpleDoubleProperty();
    private DoubleProperty rightHipZ        = new SimpleDoubleProperty();
    private DoubleProperty rightKneeX       = new SimpleDoubleProperty();
    private DoubleProperty rightKneeY       = new SimpleDoubleProperty();
    private DoubleProperty rightKneeZ       = new SimpleDoubleProperty();
    private DoubleProperty rightAnkleX      = new SimpleDoubleProperty();
    private DoubleProperty rightAnkleY      = new SimpleDoubleProperty();
    private DoubleProperty rightAnkleZ      = new SimpleDoubleProperty();
    private DoubleProperty rightFootX       = new SimpleDoubleProperty();
    private DoubleProperty rightFootY       = new SimpleDoubleProperty();
    private DoubleProperty rightFootZ       = new SimpleDoubleProperty();


    private HashMap<SkeletonJoint, Muscle> skeletonJoints
            = new HashMap<SkeletonJoint, Muscle>();

    public Speaker(int id, MuscleSensor sensor) {
        this.userId = id;
        this.sensor = sensor;
    }

    public boolean isTracking() {
        return sensor.isTracking(userId);
    }

    private void updateSkeletonJoint(SkeletonJoint joint, DoubleProperty x, DoubleProperty y) {
        Muscle point = sensor.getSkeletonJointPosition(userId, joint);
        if (point == null) {
            return;
        }
        skeletonJoints.put(joint, point);
        x.set(point.getX());
        y.set(point.getY());
    }

    private Muscle getSkeletonPosition(SkeletonJoint joint) {
        Muscle p = skeletonJoints.get(joint);
        if (p != null) {
            return p;
        }

        return new Muscle();
    }

    /**
     * コメントアウトしてるところは、骨格情報取るところでエラーでたので
     * 多分対応してない場所
     *
     * @throws StatusException
     */
    public void update() throws StatusException {
        updateSkeletonJoint(SkeletonJoint.HEAD, headX, headY);
        updateSkeletonJoint(SkeletonJoint.NECK, neckX, neckY);
        updateSkeletonJoint(SkeletonJoint.TORSO, torsoX, torsoY);
        //updateSkeletonJoint(SkeletonJoint.WAIST, waistX, waistY);
        //updateSkeletonJoint(SkeletonJoint.LEFT_COLLAR, leftCollarX, leftCollarY);
        updateSkeletonJoint(SkeletonJoint.LEFT_SHOULDER, leftShoulderX, leftShoulderY);
        updateSkeletonJoint(SkeletonJoint.LEFT_ELBOW, leftElbowX, leftElbowY);
        //updateSkeletonJoint(SkeletonJoint.LEFT_WRIST, leftWristX, leftWristY);
        updateSkeletonJoint(SkeletonJoint.LEFT_HAND, leftHandX, leftHandY);
        //updateSkeletonJoint(SkeletonJoint.LEFT_FINGER_TIP, leftFingerTipX, leftFingerTipY);
        //updateSkeletonJoint(SkeletonJoint.RIGHT_COLLAR, rightCollarX, rightCollarY);
        updateSkeletonJoint(SkeletonJoint.RIGHT_SHOULDER, rightShoulderX, rightShoulderY);
        updateSkeletonJoint(SkeletonJoint.RIGHT_ELBOW, rightElbowX, rightElbowY);
        //updateSkeletonJoint(SkeletonJoint.RIGHT_WRIST, rightWristX, rightWristY);
        updateSkeletonJoint(SkeletonJoint.RIGHT_HAND, rightHandX, rightHandY);
        //updateSkeletonJoint(SkeletonJoint.RIGHT_FINGER_TIP, rightFingerTipX, rightFingerTipY);
        updateSkeletonJoint(SkeletonJoint.LEFT_HIP, leftHipX, leftHipY);
        updateSkeletonJoint(SkeletonJoint.LEFT_KNEE, leftKneeX, leftKneeY);
        //updateSkeletonJoint(SkeletonJoint.LEFT_ANKLE, leftAnkleX, leftAnkleY);
        updateSkeletonJoint(SkeletonJoint.LEFT_FOOT, leftFootX, leftFootY);
        updateSkeletonJoint(SkeletonJoint.RIGHT_HIP, rightHipX, rightHipY);
        updateSkeletonJoint(SkeletonJoint.RIGHT_KNEE, rightKneeX, rightKneeY);
        //updateSkeletonJoint(SkeletonJoint.RIGHT_ANKLE, rightAnkleX, rightAnkleY);
        updateSkeletonJoint(SkeletonJoint.RIGHT_FOOT, rightFootX, rightFootY);
    }

    public Muscle head() {
        return getSkeletonPosition(SkeletonJoint.HEAD);
    }

    public Muscle neck() {
        return getSkeletonPosition(SkeletonJoint.NECK);
    }

    public Muscle torso() {
        return getSkeletonPosition(SkeletonJoint.TORSO);
    }

    public Muscle waist() {
        return getSkeletonPosition(SkeletonJoint.WAIST);
    }

    public Muscle leftCollar() {
        return getSkeletonPosition(SkeletonJoint.LEFT_COLLAR);
    }

    public Muscle leftShoulder() {
        return getSkeletonPosition(SkeletonJoint.LEFT_SHOULDER);
    }

    public Muscle leftElbow() {
        return getSkeletonPosition(SkeletonJoint.LEFT_ELBOW);
    }

    public Muscle leftWrist() {
        return getSkeletonPosition(SkeletonJoint.LEFT_WRIST);
    }

    public Muscle leftHand() {
        return getSkeletonPosition(SkeletonJoint.LEFT_HAND);
    }

    public Muscle leftFingerTip() {
        return getSkeletonPosition(SkeletonJoint.LEFT_FINGER_TIP);
    }

    public Muscle rightCollar() {
        return getSkeletonPosition(SkeletonJoint.RIGHT_COLLAR);
    }

    public Muscle rightShoulder() {
        return getSkeletonPosition(SkeletonJoint.RIGHT_SHOULDER);
    }

    public Muscle rightElbow() {
        return getSkeletonPosition(SkeletonJoint.RIGHT_ELBOW);
    }

    public Muscle rightWrist() {
        return getSkeletonPosition(SkeletonJoint.RIGHT_WRIST);
    }

    public Muscle rightHand() {
        return getSkeletonPosition(SkeletonJoint.RIGHT_HAND);
    }

    public Muscle rightFingerTip() {
        return getSkeletonPosition(SkeletonJoint.RIGHT_FINGER_TIP);
    }

    public Muscle leftHip() {
        return getSkeletonPosition(SkeletonJoint.LEFT_HIP);
    }

    public Muscle leftKnee() {
        return getSkeletonPosition(SkeletonJoint.LEFT_KNEE);
    }

    public Muscle leftAnkle() {
        return getSkeletonPosition(SkeletonJoint.LEFT_ANKLE);
    }

    public Muscle leftFoot() {
        return getSkeletonPosition(SkeletonJoint.LEFT_FOOT);
    }

    public Muscle rightHip() {
        return getSkeletonPosition(SkeletonJoint.RIGHT_HIP);
    }

    public Muscle rightKnee() {
        return getSkeletonPosition(SkeletonJoint.RIGHT_KNEE);
    }

    public Muscle rightAnkle() {
        return getSkeletonPosition(SkeletonJoint.RIGHT_ANKLE);
    }

    public Muscle rightFoot() {
        return getSkeletonPosition(SkeletonJoint.RIGHT_FOOT);
    }

    public DoubleProperty headXProperty() {
        return headX;
    }

    public DoubleProperty headYProperty() {
        return headY;
    }

    public DoubleProperty headZProperty() {
        return headZ;
    }

    public DoubleProperty neckXProperty() {
        return neckX;
    }

    public DoubleProperty neckYProperty() {
        return neckY;
    }

    public DoubleProperty neckZProperty() {
        return neckZ;
    }

    public DoubleProperty torsoXProperty() {
        return torsoX;
    }

    public DoubleProperty torsoYProperty() {
        return torsoY;
    }

    public DoubleProperty torsoZProperty() {
        return torsoZ;
    }

    public DoubleProperty waistXProperty() {
        return waistX;
    }

    public DoubleProperty waistYProperty() {
        return waistY;
    }

    public DoubleProperty waistZProperty() {
        return waistZ;
    }

    public DoubleProperty leftCollarXProperty() {
        return leftCollarX;
    }

    public DoubleProperty leftCollarYProperty() {
        return leftCollarY;
    }

    public DoubleProperty leftCollarZProperty() {
        return leftCollarZ;
    }

    public DoubleProperty leftShoulderXProperty() {
        return leftShoulderX;
    }

    public DoubleProperty leftShoulderYProperty() {
        return leftShoulderY;
    }

    public DoubleProperty leftShoulderZProperty() {
        return leftShoulderZ;
    }

    public DoubleProperty leftElbowXProperty() {
        return leftElbowX;
    }

    public DoubleProperty leftElbowYProperty() {
        return leftElbowY;
    }

    public DoubleProperty leftElbowZProperty() {
        return leftElbowZ;
    }

    public DoubleProperty leftWristXProperty() {
        return leftWristX;
    }

    public DoubleProperty leftWristYProperty() {
        return leftWristY;
    }

    public DoubleProperty leftWristZProperty() {
        return leftWristZ;
    }

    public DoubleProperty leftHandXProperty() {
        return leftHandX;
    }

    public DoubleProperty leftHandYProperty() {
        return leftHandY;
    }

    public DoubleProperty leftHandZProperty() {
        return leftHandZ;
    }

    public DoubleProperty leftFingerTipXProperty() {
        return leftFingerTipX;
    }

    public DoubleProperty leftFingerTipYProperty() {
        return leftFingerTipY;
    }

    public DoubleProperty leftFingerTipZProperty() {
        return leftFingerTipZ;
    }

    public DoubleProperty rightCollarXProperty() {
        return rightCollarX;
    }

    public DoubleProperty rightCollarYProperty() {
        return rightCollarY;
    }

    public DoubleProperty rightCollarZProperty() {
        return rightCollarZ;
    }

    public DoubleProperty rightShoulderXProperty() {
        return rightShoulderX;
    }

    public DoubleProperty rightShoulderYProperty() {
        return rightShoulderY;
    }

    public DoubleProperty rightShoulderZProperty() {
        return rightShoulderZ;
    }

    public DoubleProperty rightElbowXProperty() {
        return rightElbowX;
    }

    public DoubleProperty rightElbowYProperty() {
        return rightElbowY;
    }

    public DoubleProperty rightElbowZProperty() {
        return rightElbowZ;
    }

    public DoubleProperty rightWristXProperty() {
        return rightWristX;
    }

    public DoubleProperty rightWristYProperty() {
        return rightWristY;
    }

    public DoubleProperty rightWristZProperty() {
        return rightWristZ;
    }

    public DoubleProperty rightHandXProperty() {
        return rightHandX;
    }

    public DoubleProperty rightHandYProperty() {
        return rightHandY;
    }

    public DoubleProperty rightHandZProperty() {
        return rightHandZ;
    }

    public DoubleProperty rightFingerTipXProperty() {
        return rightFingerTipX;
    }

    public DoubleProperty rightFingerTipYProperty() {
        return rightFingerTipY;
    }

    public DoubleProperty rightFingerTipZProperty() {
        return rightFingerTipZ;
    }

    public DoubleProperty leftHipXProperty() {
        return leftHipX;
    }

    public DoubleProperty leftHipYProperty() {
        return leftHipY;
    }

    public DoubleProperty leftHipZProperty() {
        return leftHipZ;
    }

    public DoubleProperty leftKneeXProperty() {
        return leftKneeX;
    }

    public DoubleProperty leftKneeYProperty() {
        return leftKneeY;
    }

    public DoubleProperty leftKneeZProperty() {
        return leftKneeZ;
    }

    public DoubleProperty leftAnkleXProperty() {
        return leftAnkleX;
    }

    public DoubleProperty leftAnkleYProperty() {
        return leftAnkleY;
    }

    public DoubleProperty leftAnkleZProperty() {
        return leftAnkleZ;
    }

    public DoubleProperty leftFootXProperty() {
        return leftFootX;
    }

    public DoubleProperty leftFootYProperty() {
        return leftFootY;
    }

    public DoubleProperty leftFootZProperty() {
        return leftFootZ;
    }

    public DoubleProperty rightHipXProperty() {
        return rightHipX;
    }

    public DoubleProperty rightHipYProperty() {
        return rightHipY;
    }

    public DoubleProperty rightHipZProperty() {
        return rightHipZ;
    }

    public DoubleProperty rightKneeXProperty() {
        return rightKneeX;
    }

    public DoubleProperty rightKneeYProperty() {
        return rightKneeY;
    }

    public DoubleProperty rightKneeZProperty() {
        return rightKneeZ;
    }

    public DoubleProperty rightAnkleXProperty() {
        return rightAnkleX;
    }

    public DoubleProperty rightAnkleYProperty() {
        return rightAnkleY;
    }

    public DoubleProperty rightAnkleZProperty() {
        return rightAnkleZ;
    }

    public DoubleProperty rightFootXProperty() {
        return rightFootX;
    }

    public DoubleProperty rightFootYProperty() {
        return rightFootY;
    }

    public DoubleProperty rightFootZProperty() {
        return rightFootZ;
    }

    public Muscle muscleHead() {
        return Muscle.sub(head(), neck());
    }
    public Muscle muscleRightForearm() {
        return Muscle.sub(rightHand(), rightElbow());
    }

    public Muscle muscleRightUpperArm() {
        return Muscle.sub(rightElbow(), rightShoulder());
    }

    public Muscle muscleLeftForearm() {
        return Muscle.sub(leftHand(), leftElbow());
    }

    public Muscle muscleLeftUpperArm() {
        return Muscle.sub(leftElbow(), leftShoulder());
    }

    public boolean isRightArmIsStraight() {
        return muscleRightForearm().isStraight(muscleRightUpperArm());
    }

    public boolean isLeftArmIsStraight() {
        return muscleLeftForearm().isStraight(muscleLeftUpperArm());
    }

    public boolean isRightArmIsBentRightAngle() {
        return muscleRightUpperArm().isOrthogonal(muscleRightForearm());
    }

    public boolean isLeftArmIsBentRightAngle() {
        return muscleLeftUpperArm().isOrthogonal(muscleLeftForearm());
    }

    public boolean isBothForearmIsStraight() {
        return muscleLeftForearm().isParallel(muscleRightForearm());
    }


    /**
     * <pre>
     *
     *     \ ○ /
     *       |
     *       |
     *      / \
     *
     * </pre>
     * @return
     */
    public boolean owataPoseDetected() {
        boolean angle    = isRightArmIsStraight() && isLeftArmIsStraight();
        boolean position = (rightHand().getY() < head().getY()) && (leftHand().getY() < head().getY());

        return angle && position;
    }

    /**
     *
     * <pre>
     *      _ ○ _
     *     |  |  |
     *        |
     *       / \
     *
     * </pre>
     *
     *
     * @return
     */
    public boolean hangerPoseDetected() {
        boolean angle = isRightArmIsBentRightAngle()
                && isLeftArmIsBentRightAngle()
                && isBothForearmIsStraight();
        boolean position = (rightHand().getY() > rightElbow().getY())
                && (leftHand().getY() > leftElbow().getY());

        double rightAngle = Math.toDegrees(Math.acos(muscleRightForearm().dot(muscleRightUpperArm())));
        double leftAngle = Math.toDegrees(Math.acos(muscleLeftForearm().dot(muscleLeftUpperArm())));
        double botharm = Math.toDegrees(Math.acos(muscleRightForearm().dot(new Muscle(1.0f, 0.0f, 0.0f))));
        double botharm2 = Math.toDegrees(Math.acos(muscleLeftForearm().dot(new Muscle(1.0f, 0.0f, 0.0f))));


        System.out.println(rightAngle + " " + leftAngle + " " + botharm + " " + botharm2);

        return angle && position;
    }

    /**
     * <pre>
     *        ○
     *     -- | --
     *        |
     *       / \
     *
     * </pre>
     * @return
     */
    public boolean kakashiPoseDetected() {
        boolean angle = isRightArmIsStraight()
                && isLeftArmIsStraight()
                && muscleRightForearm().isParallel(new Muscle(1.0f, 0.0f, 0.0f))
                && muscleLeftForearm().isParallel(new Muscle(1.0f, 0.0f, 0.0f));
        boolean position = (rightHand().getY() > head().getY())
                && (leftHand().getY() > head().getY());

        return angle && position;
    }

    /**
     * <pre>
     *       | ○ |
     *       - | -
     *         |
     *        / \
     * </pre>
     * @return
     */
    public boolean showtimePoseDetected() {
        boolean angle = isRightArmIsBentRightAngle()
                && isLeftArmIsBentRightAngle()
                && isBothForearmIsStraight();
        boolean position = (rightHand().getY() < rightElbow().getY())
                && (leftHand().getY() < leftElbow().getY());

        return angle && position;
    }
}
