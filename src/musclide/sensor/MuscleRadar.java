package musclide.sensor;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import org.OpenNI.StatusException;

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
public class MuscleRadar extends Pane {
    /**
     * 首から頭へ
     */
    private Line headLine = new Line();

    /**
     * 正中線(頭から腰にかけて)
     *
     * WAIST が今取れないので諦め
     */
    //private Line medianLine = new Line();

    /**
     * 右上腕(右肩から右肘にかけて)
     */
    private Line rightUpperArm = new Line();

    /**
     * 右前腕(右肘から右手にかけて)
     * */
    private Line rightForearm = new Line();

    /**
     * 左上腕(左肩から左肘にかけて)
     */
    private Line leftUpperArm = new Line();

    /**
     * 左前腕(左肘から左手にかけて)
     */
    private Line leftForearm = new Line();

    /**
     * 左上腿(左尻から左膝にかけて)
     */
    private Line leftUpperThigh = new Line();

    /**
     * 左下腿(左膝から左足にかけて)
     */
    private Line leftLowerThigh = new Line();

    /**
     * 右上腿(右尻から右膝にかけて)
     */
    private Line rightUpperThigh = new Line();

    /**
     * 右下腿(右膝から右足にかけて)
     */
    private Line rightLowerThigh = new Line();

    private Circle rightElbowPoint = new Circle(6.0);
    private Circle leftElbowPoint = new Circle(6.0);
    private Circle rightKneePoint = new Circle(6.0);
    private Circle leftKneePoint = new Circle(6.0);

    /**
     * 胴体
     */
    private Rectangle body;
    private PerspectiveTransform bodyTransform = new PerspectiveTransform();

    /**
     *
     */
    private Speaker speaker;

    /**
     * head
     */
    private Text headText;

    public MuscleRadar(final Speaker _speaker) {
        speaker = _speaker;

        setHead();
        setJointLine();
        setJointPoint();
        setBody();

        getTransforms().addAll(new Scale(0.5, 0.5),
                new Translate(0.0, 50.0));

        new AnimationTimer() {
            @Override
            public void handle(long l) {
                try {
                    speaker.update();
                } catch (StatusException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void setHead() {
        headText = new Text();
        headText.setText(Integer.toString(speaker.userId));
        headText.xProperty().bind(speaker.headXProperty());
        headText.yProperty().bind(speaker.headYProperty());
        getChildren().add(headText);
    }

    private void setJointLine() {
        setLineProperty(headLine,
                speaker.headXProperty(), speaker.headYProperty(),
                speaker.neckXProperty(), speaker.neckYProperty()
        );

        setLineProperty(rightUpperArm,
                speaker.rightShoulderXProperty(), speaker.rightShoulderYProperty(),
                speaker.rightElbowXProperty(), speaker.rightElbowYProperty()
        );

        setLineProperty(rightForearm,
                speaker.rightElbowXProperty(), speaker.rightElbowYProperty(),
                speaker.rightHandXProperty(), speaker.rightHandYProperty()
        );

        setLineProperty(leftUpperArm,
                speaker.leftShoulderXProperty(), speaker.leftShoulderYProperty(),
                speaker.leftElbowXProperty(), speaker.leftElbowYProperty()
        );

        setLineProperty(leftForearm,
                speaker.leftElbowXProperty(), speaker.leftElbowYProperty(),
                speaker.leftHandXProperty(), speaker.leftHandYProperty()
        );

        setLineProperty(rightUpperThigh,
                speaker.rightHipXProperty(), speaker.rightHipYProperty(),
                speaker.rightKneeXProperty(), speaker.rightKneeYProperty()
        );

        setLineProperty(rightLowerThigh,
                speaker.rightKneeXProperty(), speaker.rightKneeYProperty(),
                speaker.rightFootXProperty(), speaker.rightFootYProperty()
        );

        setLineProperty(leftUpperThigh,
                speaker.leftHipXProperty(), speaker.leftHipYProperty(),
                speaker.leftKneeXProperty(), speaker.leftKneeYProperty()
        );

        setLineProperty(leftLowerThigh,
                speaker.leftKneeXProperty(), speaker.leftKneeYProperty(),
                speaker.leftFootXProperty(), speaker.leftFootYProperty()
        );

        getChildren().addAll(
                rightUpperArm, rightForearm, leftUpperArm, leftForearm,
                leftUpperThigh, leftLowerThigh, rightUpperThigh, rightLowerThigh
        );
    }

    private void setLineProperty(Line line, DoubleProperty sx, DoubleProperty sy,
                                 DoubleProperty ex, DoubleProperty ey) {
        line.startXProperty().bind(sx);
        line.startYProperty().bind(sy);
        line.endXProperty().bind(ex);
        line.endYProperty().bind(ey);
    }

    private void setJointPoint() {
        rightElbowPoint.centerXProperty().bind(speaker.rightElbowXProperty());
        rightElbowPoint.centerYProperty().bind(speaker.rightElbowYProperty());

        leftElbowPoint.centerXProperty().bind(speaker.leftElbowXProperty());
        leftElbowPoint.centerYProperty().bind(speaker.leftElbowYProperty());

        rightKneePoint.centerXProperty().bind(speaker.rightKneeXProperty());
        rightKneePoint.centerYProperty().bind(speaker.rightKneeYProperty());

        leftKneePoint.centerXProperty().bind(speaker.leftKneeXProperty());
        leftKneePoint.centerYProperty().bind(speaker.leftKneeYProperty());

        getChildren().addAll(
                rightElbowPoint, leftElbowPoint,rightKneePoint, leftKneePoint
        );
    }

    private void setBody() {
        body = new Rectangle(0, 0, 300, 300);

        bodyTransform.llxProperty().bind(speaker.rightHipXProperty());
        bodyTransform.llyProperty().bind(speaker.rightHipYProperty());
        bodyTransform.ulxProperty().bind(speaker.rightShoulderXProperty());
        bodyTransform.ulyProperty().bind(speaker.rightShoulderYProperty());
        bodyTransform.urxProperty().bind(speaker.leftShoulderXProperty());
        bodyTransform.uryProperty().bind(speaker.leftShoulderYProperty());
        bodyTransform.lrxProperty().bind(speaker.leftHipXProperty());
        bodyTransform.lryProperty().bind(speaker.leftHipYProperty());

        body.setEffect(bodyTransform);
        getChildren().add(body);
    }
}
