package com.bluebook.renderer;

import com.bluebook.camera.OrthographicCamera;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;
import com.sun.javafx.geom.Line2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.shape.ArcType;
import javafx.scene.transform.Rotate;

/**
 * GraphicsRendere is an attemt to create a wrapper for {@link GraphicsContext} to add functionality and also possibly create a alternative OpenGL renderer
 */
public class GraphicsRenderer {

    public GraphicsContext gc;

    private double scaledSquareHeight, scaledSquareWidth;

    public void save() {
        gc.save();
    }

    public void restore() {
        gc.restore();
    }

    public void setFill(RadialGradient radialGradient) {
        gc.setFill(radialGradient);
    }

    public void setGlobalBlendMode(BlendMode blendMode) {
        gc.setGlobalBlendMode(blendMode);
    }

    public void fillPolygon(double[] xs, double[] ys, int length) {
        gc.fillPolygon(xs, ys, length);
    }

    public GraphicsRenderer(GraphicsContext gc) {
        this.gc = gc;
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.RED);
        setScaleSquare();
    }

    private void setScaleSquare() {
        Vec2 scaleVec = GameSettings.getSquareScale();
        scaledSquareHeight = scaleVec.getY();
        scaledSquareWidth = scaleVec.getX();
    }

    public void setFillColor(Color color) {
        gc.setFill(color);
    }

    public void setStrokeColor(Color color) {
        gc.setStroke(color);
    }

    public void drawCircle(Vec2 position, double radius) {
        double xOff = OrthographicCamera.main == null ? 0 : OrthographicCamera.main.getX();
        double yOff = OrthographicCamera.main == null ? 0 : OrthographicCamera.main.getY();

        gc.fillArc(position.getX() + xOff - radius, position.getY() + yOff - radius, radius * 2,
            radius * 2, 0, 360, ArcType.CHORD);
    }

    public void strokeCircle(Vec2 position, double radius) {
        double xOff = OrthographicCamera.main == null ? 0 : OrthographicCamera.main.getX();
        double yOff = OrthographicCamera.main == null ? 0 : OrthographicCamera.main.getY();

        gc.strokeArc(position.getX() + xOff - radius, position.getY() + yOff - radius, radius * 2,
            radius * 2, 0, 360, ArcType.CHORD);
    }

    public void drawBox(Vec2 position, double width, double height) {
        double xOff = OrthographicCamera.main == null ? 0 : OrthographicCamera.main.getX();
        double yOff = OrthographicCamera.main == null ? 0 : OrthographicCamera.main.getY();

        width = width * scaledSquareHeight;
        height = height * scaledSquareHeight;

        gc.fillRect(position.getX() + xOff - width / 2, position.getY() + yOff - height / 2, width,
            height);
    }

    public void strokeBox(Vec2 position, double width, double height) {
        double xOff = OrthographicCamera.main == null ? 0 : OrthographicCamera.main.getX();
        double yOff = OrthographicCamera.main == null ? 0 : OrthographicCamera.main.getY();

        width = width * scaledSquareHeight;
        height = height * scaledSquareHeight;

        gc.strokeRect(position.getX() + xOff - width / 2, position.getY() + yOff - height / 2,
            width, height);
    }


    public void strokeBox(double x, double y, double width, double height) {
        strokeBox(new Vec2(x, y), width, height);
    }

    public void strokeLine(Vec2 start, Vec2 dest) {
        double xOff = OrthographicCamera.main == null ? 0 : OrthographicCamera.main.getX();
        double yOff = OrthographicCamera.main == null ? 0 : OrthographicCamera.main.getY();

        gc.strokeLine(start.getX() + xOff, start.getY() + yOff, dest.getX() + xOff,
            dest.getY() + yOff);
    }

    public void strokeLine(Line2D line) {
        double xOff = OrthographicCamera.main == null ? 0 : OrthographicCamera.main.getX();
        double yOff = OrthographicCamera.main == null ? 0 : OrthographicCamera.main.getY();

        gc.strokeLine(line.x1 + xOff, line.y1 + yOff, line.x2 + xOff, line.y2 + yOff);
    }

    private void renderImage(Vec2 position, Image img) {
        gc.drawImage(img, position.getX() - (scaledSquareWidth / 2f),
            position.getY() - (scaledSquareHeight / 2f), scaledSquareWidth, scaledSquareHeight);
    }

    public void drawImage(Image img, double x, double y, double width, double height) {
        gc.drawImage(img, x, y, width, height);
    }

    public void drawImage(Vec2 position, Image img) {
        renderImage(position, img);
    }

    public void drawImage(Vec2 position, double rotation, Image img) {
        gc.save();

        gc = rotateGraphicsContext(gc, position, rotation);
        drawImage(position, img);
        gc.restore();
    }

    public void rotate(Vec2 position, double angle) {
        gc = rotateGraphicsContext(gc, position, angle);
    }

    protected GraphicsContext rotateGraphicsContext(GraphicsContext gc, Vec2 position,
                                                    double rotateAngle) {
        Rotate r = new Rotate(rotateAngle, position.getX(), position.getY());
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        return gc;
    }

    private Vec2 addCameraOffset(Vec2 pos) {
        return Vec2.subtract(pos, OrthographicCamera.getOffset());
    }

    private Vec2 getPixelPosition(Vec2 pos) {
        return Vec2.multiply(pos, new Vec2(scaledSquareWidth, scaledSquareHeight));
    }
}
