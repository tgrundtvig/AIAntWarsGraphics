/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiantwarsgraphics;

import app2dapi.geometry.G2D;
import app2dapi.geometry.G2D.Point2D;
import app2dapi.graphics.Canvas;
import app2dapi.graphics.Color;
import app2dapi.graphics.ColorFactory;

/**
 *
 * @author Tobias Grundtvig
 */
public class MenuDrawer
{
    private final MenuListener listener;
    private final G2D g2d;
    private final Color borderColor;
    private final Color backGroundColor;
    private final Color textColor;
    private final Color hooverBackGroundColor;
    private final Color hooverTextColor;
    private final Color clickBackGroundColor;
    private final Color clickTextColor;
    private final double border;
    private final double height;
    private String[] choices;
    private double width;
    private int hover;
    private int pressed;

    public MenuDrawer(G2D g2d, ColorFactory cf, double textHeight, MenuListener listener)
    {
        this.border = 0.1 * textHeight;
        this.g2d = g2d;
        this.listener = listener;
        /*
        borderColor = cf.getBlue();
        backGroundColor = cf.getBlack();
        textColor = cf.getBlue();
        hooverBackGroundColor = cf.newColor(0.2f, 0.2f, 0.2f);
        hooverTextColor = cf.getBlue();
        clickBackGroundColor = cf.newColor(0.4f, 0.4f, 0.4f);
        clickTextColor = cf.getBlue();
        */
        borderColor = cf.getBlue();
        backGroundColor = cf.getBlack();
        textColor = cf.getBlue();
        hooverBackGroundColor = cf.getBlue();
        hooverTextColor = cf.getBlack();
        clickBackGroundColor = cf.newColor(0.4f, 0.4f, 1.0f);
        clickTextColor = cf.newColor(0.2f, 0.2f, 0.2f);
        this.height = textHeight;
        choices = null;
        width = -1.0;
        hover = -1;
        pressed = -1;
    }
    
    public void setChoices(String[] choices)
    {
        this.choices = new String[choices.length];
        for(int i = 0; i < choices.length; ++i)
        {
            this.choices[i] = choices[choices.length - i - 1];
        }
        width = -1.0;
    }
    
    public void onMouseMoved(Point2D mousePos)
    {
        hover = calculatePos(mousePos);
        if(pressed >= 0)
        {
            if(hover != pressed)
            {
                pressed = -1;
            }
        }
    }
    
    public void onMousePressed(Point2D mousePos)
    {
        pressed = calculatePos(mousePos);
    }
    
    public void onMouseReleased(Point2D mousePos)
    {
        int released = calculatePos(mousePos);
        if(pressed >= 0 && released == pressed)
        {
            choices = null;
            width = -1.0;
            listener.onChoice(pressed);
        }
        pressed = -1;
    }

    public void draw(Canvas canvas)
    {
        if(choices != null && choices.length > 0)
        {
            if(width < 0.0)
            {
                calculateWidth(canvas);
            }
            G2D.Point2D upperRight = g2d.newPoint2D(width + 2 * border,
                                                    choices.length * (height + border) + border);
            canvas.setColor(borderColor);
            canvas.drawFilledRectangle(g2d.origo(), upperRight);
            G2D.Point2D lowerLeft;
            for(int i = 0; i < choices.length; ++i)
            {
                int j = choices.length - i - 1;
                lowerLeft = g2d.newPoint2D(border, i * (border + height) + border);
                upperRight = g2d.newPoint2D(border + width,
                                            lowerLeft.y() + height);
                G2D.Point2D middle = g2d.newPoint2D(
                  0.5 * (lowerLeft.x() + upperRight.x()),
                  0.5 * (lowerLeft.y() + upperRight.y()));
                if(pressed == j)
                {
                    canvas.setColor(clickBackGroundColor);
                }
                else if(hover == j)
                {
                    canvas.setColor(hooverBackGroundColor);
                }
                else
                {
                    canvas.setColor(backGroundColor);
                }
                canvas.drawFilledRectangle(lowerLeft, upperRight);
                if(pressed == j)
                {
                    canvas.setColor(clickTextColor);
                }
                else if(hover == j)
                {
                    canvas.setColor(hooverTextColor);
                }
                else
                {
                    canvas.setColor(textColor);
                }
                canvas.drawText(middle, choices[i], height, true, true);
            }
        }
    }

    public int calculatePos(Point2D pos)
    {
        double x = pos.x();
        double y = pos.y();
        if(x < border || x > border + width
           || y < border || y > (border + height) * choices.length)
        {
            return -1;
        }
        double d = y / (border + height);
        int i = (int) d;
        double ly = y - (i * (border + height));
        if(ly < border)
        {
            return -1;
        }
        return choices.length - i - 1;
    }
    
    public double getHight()
    {
        return (border + height) * choices.length + border;
    }

    private void calculateWidth(Canvas canvas)
    {
        width = 0.0;
        for(int i = 0; i < choices.length; ++i)
        {
            double tmp = canvas.getTextWidth(choices[i], height);
            if(tmp > width)
            {
                width = tmp;
            }
        }
        width += height;
    }
}
