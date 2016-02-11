/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiantwarsgraphics;

import app2dapi.animation.LinearChaser;
import app2dapi.animation.Visual;
import app2dapi.animation.impl.LinearChaserImpl;
import app2dapi.animation.impl.interpolators.DoubleInterpolator;
import app2dapi.animation.impl.interpolators.Point2DEuclideanInterpolator;
import app2dapi.geometry.G2D;
import app2dapi.geometry.G2D.Point2D;
import app2dapi.graphics.Canvas;
import app2dapi.graphics.Color;

/**
 *
 * @author Tobias Grundtvig
 */
public class TextVisual implements Visual<Canvas>
{
    private final LinearChaser<Point2D> pos;
    private final LinearChaser<Double> size;
    private final String text;
    private final Color color;

    public TextVisual(G2D g2d, String text, Color color, Point2D startPos, Point2D endPos, Double startSize, Double endSize, double duration)
    {
        this.color = color;
        this.text = text;
        pos = new LinearChaserImpl<>(new Point2DEuclideanInterpolator(g2d),1);
        size = new LinearChaserImpl<>(new DoubleInterpolator(), 1);
        pos.setCurrent(startPos);
        pos.setGoal(endPos);
        pos.setDuration(duration);
        size.setCurrent(startSize);
        size.setGoal(endSize);
        size.setDuration(duration);
    }
    
    @Override
    public void start(double time)
    {
        pos.update(time);
        size.update(time);
    }

    @Override
    public boolean isFinished()
    {
        return pos.atGoal() && size.atGoal();
    }

    @Override
    public void update(double time)
    {
        pos.update(time);
        size.update(time);
    }

    @Override
    public void draw(Canvas canvas)
    {
        canvas.setColor(color);
        canvas.drawText(pos.getCurrent(), text, size.getCurrent(), true, true);
    }
}
