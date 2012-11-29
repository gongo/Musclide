package musclide;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import musclide.event.MuscleEvent;
import musclide.sensor.Entrance;
import musclide.sensor.MuscleRadar;
import musclide.sensor.MuscleSensor;
import musclide.sensor.Speaker;
import musclide.slide.MainSlide;
import musclide.slide.OpeningSlide;
import musclide.slide.Slide;
import org.OpenNI.StatusException;

import java.io.File;

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

/**
 * Muscle player on Stage
 */
public class Ring extends HBox {

    /**
     * リングを見守る監視カメラ
     */
    private MuscleSensor sensor = MuscleSensor.instance();

    /**
     * 選手入場口
     */
    private Entrance entrance = Entrance.instance();

    /**
     * スライド
     */
    private OpeningSlide opening = new OpeningSlide(this) {
        {
            setMinSize(640, 480);
        }
    };
    private MainSlide mainScreen = new MainSlide(this) {
        {
            setMinSize(640, 480);
        }
    };

    /**
     * 選手監視
     */
    private MuscleRadar radar;

    /**
     * リングに立てるのはただ一人
     */
    private Speaker speaker = null;

    /**
     * 試合開始?リプレイ再生?
     */
    VBox sidebar = new VBox();

    Ring() {
        entrance.setSensor(sensor);

        Button startButton = new Button("Entering");
        Button replayButton = new Button("Reply");

        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setMinWidth(384);
        sidebar.setSpacing(10.0);
        sidebar.getChildren().addAll(startButton, replayButton);

        getChildren().addAll(opening, sidebar);

        startButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                sensor.start();
                gameStart();
            }
        });

        replayButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                final FileChooser chooser = new FileChooser();
                chooser.setTitle("select oni file");
                chooser.setInitialDirectory(new File(System.getProperty("user.home")));
                chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("oni file", "*.oni"));

                File path = chooser.showOpenDialog(null);

                if (path == null) {
                    return;
                }

                sensor.replay(path.getPath());
                gameStart();
            }
        });

        opening.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                final DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("select directory");
                chooser.setInitialDirectory(new File(System.getProperty("user.home")));
                File path = chooser.showDialog(null);

                if (path == null) {
                    return;
                }

                mainScreen.openDirectory(path);
                getChildren().set(0, mainScreen);
            }
        });


        // Sensor update timeline
        new AnimationTimer() {
            @Override
            public void handle(long l) {
                try {
                    sensor.monitoring();
                } catch (StatusException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        // user pose detection timeline
        TimelineBuilder.create()
                .cycleCount(Timeline.INDEFINITE)
                .keyFrames(
                        new KeyFrame(
                                new Duration(1000),
                                new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        if (speaker == null || !speaker.isTracking()) {
                                            return;
                                        }

                                        if (speaker.owataPoseDetected()) {
                                            firePosingDetected(MuscleEvent.Pose.NEXT);
                                        } else if (speaker.hangerPoseDetected()) {
                                            firePosingDetected(MuscleEvent.Pose.PREV);
                                        } else if (speaker.showtimePoseDetected()) {
                                            firePosingDetected(MuscleEvent.Pose.FIRST);
                                        } else if (speaker.kakashiPoseDetected()) {
                                            firePosingDetected(MuscleEvent.Pose.LAST);
                                        } else {
                                            firePosingDetected(MuscleEvent.Pose.STANDBY);
                                        }
                                    }
                                }
                        )
                ).build().play();
    }

    private void firePosingDetected(MuscleEvent.Pose pose) {
        MuscleEvent event = new MuscleEvent();
        event.pose = pose;
        Event.fireEvent(this, event);
    }

    public final void setOnPosingDetected(EventHandler<MuscleEvent> eventHandler) {
        this.addEventHandler(MuscleEvent.ANY, eventHandler);
    }

    private void gameStart() {
        speaker = entrance.entering(0);
        radar = new MuscleRadar(speaker);

        getChildren().remove(sidebar);
        getChildren().add(radar);
    }
}
