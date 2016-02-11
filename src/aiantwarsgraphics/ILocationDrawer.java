/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiantwarsgraphics;

import aiantwars.ILocationInfo;
import app2dapi.graphics.Canvas;

/**
 *
 * @author Tobias Grundtvig
 */
public interface ILocationDrawer
{
    public void draw(Canvas canvas, ILocationInfo loc);
}
