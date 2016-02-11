/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiantwarsgraphics;

import aiantwars.IAntInfo;
import app2dapi.graphics.Canvas;
import app2dapi.graphics.Color;

/**
 *
 * @author Tobias Grundtvig
 */
public interface IAntDrawer
{
    public void drawAnt(Canvas canvas, IAntInfo antInfo);
    public Color getHitPointColor();
    public Color getActionColor();
    public Color getFoodColor();
    public Color getDamageColor();
}
