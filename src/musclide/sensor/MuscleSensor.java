package musclide.sensor;

import javafx.application.Platform;
import org.OpenNI.*;

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
public final class MuscleSensor {
    protected Context context = null;
    private UserGenerator userGenerator;
    private DepthGenerator depthGenerator;
    private SkeletonCapability skeletonCapability;
    private final String configXML = getClass().getResource("/config/sensor.xml").getPath();


    /**
     * Replay
     */
    private Player player = null;

    /**
     * Singleton Constructor
     */
    private MuscleSensor() {}

    private static class MuscleSensorHolder {
        private static final MuscleSensor instance = new MuscleSensor();
    }

    public static MuscleSensor instance() {
        return MuscleSensorHolder.instance;
    }

    public void replay(String oniFile) {
        try {
            context = new Context();
            player = context.openFileRecordingEx(oniFile);
            player.setRepeat(true);
            launch();
        } catch (GeneralException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        OutArg<ScriptNode> scriptNodeOutArg = new OutArg<ScriptNode>();
        try {
            context = Context.createFromXmlFile(configXML, scriptNodeOutArg);
            launch();
        } catch (GeneralException e) {
            e.printStackTrace();
        }
    }

    public void launch() throws GeneralException {
        depthGenerator = DepthGenerator.create(context);
        userGenerator = UserGenerator.create(context);
        skeletonCapability = userGenerator.getSkeletonCapability();

        userGenerator.getNewUserEvent().addObserver(new IObserver<UserEventArgs>() {
            @Override
            public void update(IObservable<UserEventArgs> userEventArgsIObservable, UserEventArgs args) {
                try {
                    skeletonCapability.requestSkeletonCalibration(args.getId(), true);
                } catch (StatusException e) {
                    e.printStackTrace();
                }
            }
        });

        skeletonCapability.getCalibrationCompleteEvent().addObserver(new IObserver<CalibrationProgressEventArgs>() {
            @Override
            public void update(IObservable<CalibrationProgressEventArgs> Observable, CalibrationProgressEventArgs args) {
                System.out.println("Calibration complete: " + args.getStatus());
                try {
                    if (args.getStatus() == CalibrationProgressStatus.OK) {
                        System.out.println("starting tracking " + args.getUser());
                        skeletonCapability.startTracking(args.getUser());
                    } else if (args.getStatus() != CalibrationProgressStatus.MANUAL_ABORT) {
                        skeletonCapability.requestSkeletonCalibration(args.getUser(), true);
                    }
                } catch (StatusException e) {
                    e.printStackTrace();
                    Platform.exit();
                }
            }
        });

        skeletonCapability.setSkeletonProfile(SkeletonProfile.ALL);
        context.startGeneratingAll();
    }

    public void monitoring() throws StatusException {
        if (context != null) {
            context.waitAndUpdateAll();
        }
    }

    public boolean isTracking(int userId) {
        return skeletonCapability.isSkeletonTracking(userId);
    }

    public Muscle getSkeletonJointPosition(int id, SkeletonJoint joint) {
        try {
            SkeletonJointPosition realWorldPosition = skeletonCapability.getSkeletonJointPosition(id, joint);

            if (realWorldPosition.getConfidence() >= 0.5) {
                Point3D projectivePoint
                        = depthGenerator.convertRealWorldToProjective(realWorldPosition.getPosition());
                return new Muscle(projectivePoint);
            }

            return null;
        } catch (StatusException e) {
            e.printStackTrace();
            return null;
        }
    }

}
