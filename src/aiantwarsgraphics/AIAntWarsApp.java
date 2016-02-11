/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiantwarsgraphics;

import aiantwars.EAntType;
import aiantwars.impl.AntWarsGameCtrl;
import aiantwars.impl.Board;
import aiantwars.impl.Location;
import aiantwars.impl.SimpleAI;
import aiantwars.impl.TeamInfo;
import app2dapi.Platform;
import app2dapi.geometry.G2D;
import app2dapi.geometry.G2D.Dimension2D;
import app2dapi.geometry.G2D.Point2D;
import app2dapi.geometry.G2D.Transformation2D;
import app2dapi.graphics.Canvas;
import app2dapi.graphics.Color;
import app2dapi.graphics.ColorFactory;
import app2dapi.input.charinput.CharInputEvent;
import app2dapi.input.keyboard.KeyPressedEvent;
import app2dapi.input.keyboard.KeyReleasedEvent;
import app2dapi.panandzoom2dapp.PanAndZoom2DApp;
import app2dapi.panandzoom2dapp.PanAndZoomAdapter;
import app2dapi.panandzoom2dapp.PanAndZoomInit;
import app2dapi.panandzoom2dapp.PanAndZoomToolKit;
import app2dpcimpl.PCPlatformImpl;
import java.util.Random;

/**
 *
 * @author Tobias Grundtvig
 */
public class AIAntWarsApp implements PanAndZoom2DApp
{
    private HumanGraphicsAI humanAI;
    private MousePointer pointer;
    private final Board board;
    private Dimension2D sizeHUD;

    private GraphicsBoard graphicsBoard;
    private GraphicsAntWarsGUI graphicsAntFactory;
    private G2D g2d;
    private ColorFactory cf;
    private Point2D mouseWorldPos;
    private Point2D mouseHUDPos;
    private Thread gameThread;

    public AIAntWarsApp(Board board)
    {
        this.board = board;
    }
    
    @Override
    public PanAndZoomInit initialize(PanAndZoomToolKit tk, double aspectRatio)
    {
        g2d = tk.g2d();
        cf = tk.cf();
        this.sizeHUD = g2d.newDimension2D(1000, 1000.0 / aspectRatio);
        mouseHUDPos = g2d.origo();
        mouseWorldPos = g2d.origo();
        humanAI = new HumanGraphicsAI(g2d, cf);
        this.pointer = new MousePointer(g2d, cf);
        graphicsAntFactory = new GraphicsAntWarsGUI(g2d, cf, sizeHUD);
        graphicsBoard = new GraphicsBoard(g2d, board, cf, new LocationDrawer(g2d, cf.newColor(0.8f, 0.75f, 0.75f), cf.newColor(0.545f, 0.27f, 0.075f), cf.getBlack(), cf.getYellow()));
        
        AntWarsGameCtrl ctrl = new AntWarsGameCtrl(graphicsAntFactory, board);
        
        
        
        TeamInfo team1 = new TeamInfo(1, "Team 1");
        TeamInfo team2 = new TeamInfo(2, "Team 2");
        TeamInfo team3 = new TeamInfo(3, "Team 3");
        TeamInfo team4 = new TeamInfo(4, "Team 4");
        //ctrl.createAnt(team1, EAntType.QUEEN, new SimpleAI(), board.getLocation(0, 0), 1);
        ctrl.createAnt(team1, EAntType.QUEEN, humanAI, board.getLocation(0, 0), 1);
        ctrl.createAnt(team2, EAntType.QUEEN, new SimpleAI(), board.getLocation(board.getSizeX()-1, board.getSizeY()-1), 3);
        ctrl.createAnt(team3, EAntType.QUEEN, new SimpleAI(), board.getLocation(0, board.getSizeY()-1), 1);
        ctrl.createAnt(team4, EAntType.QUEEN, new SimpleAI(), board.getLocation(board.getSizeX()-1, 0), 3);
        gameThread = new Thread(ctrl);
        gameThread.start();
        return new PanAndZoomInit(  g2d.origo(),
                                    g2d.newPoint2D(1000.0, 1000.0 / aspectRatio),
                                    g2d.origo(),
                                    g2d.newPoint2D(board.getSizeX()*100, board.getSizeY()*100),
                                    g2d.newPoint2D(board.getSizeX()*50, board.getSizeY()*50),
                                    Math.max(board.getSizeX()*100, board.getSizeY()*100*aspectRatio),
                                    100.0,
                                    Math.max(board.getSizeX()*100, board.getSizeY()*100*aspectRatio));
    }

    @Override
    public boolean showMouseCursor()
    {
        return false;
    }

    @Override
    public boolean update(double time)
    {
        graphicsAntFactory.update(time);
        return true;
    }
    
    @Override
    public Color getBackgroundColor()
    {
        return cf.getBlack();
    }

    @Override
    public void drawWorld(Canvas canvas)
    {
        graphicsBoard.draw(canvas);
        graphicsAntFactory.drawAnts(canvas);   
        humanAI.draw(canvas);
    }
    
    @Override
    public void drawHUD(Canvas canvas)
    {
        graphicsAntFactory.drawHUD(canvas);
        Transformation2D parent = canvas.getTransformation();
        Transformation2D t = g2d.translateOrigoToPoint(mouseHUDPos);
        canvas.setTransformation(g2d.combine(parent, t));
        pointer.draw(canvas);
    }

    @Override
    public void destroy()
    {
        //This is a hack to kill the dangeling gameThread
        //Without a lot of cleanup code...
        gameThread.stop();
    }
    
    @Override
    public void onMouseMoved(Point2D mouseHUDPos, Point2D mouseWorldPos)
    {
        this.mouseHUDPos = mouseHUDPos;
        this.mouseWorldPos = mouseWorldPos;
        humanAI.onMouseMoved(mouseWorldPos);
    }

    @Override
    public void onMousePressed(Point2D mouseHUDPos, Point2D mouseWorldPos)
    {
        this.mouseHUDPos = mouseHUDPos;
        this.mouseWorldPos = mouseWorldPos;
        humanAI.onMousePressed(mouseWorldPos);
    }

    @Override
    public void onMouseReleased(Point2D mouseHUDPos, Point2D mouseWorldPos)
    {
        this.mouseHUDPos = mouseHUDPos;
        this.mouseWorldPos = mouseWorldPos;
        humanAI.onMouseReleased(mouseWorldPos);
    }

    @Override
    public void onKeyPressed(KeyPressedEvent e)
    {
        
    }

    @Override
    public void onKeyReleased(KeyReleasedEvent e)
    {
        
    }

    @Override
    public void onCharInput(CharInputEvent event)
    {
        
    }
    
    //////////////////////////////////////////////////////////////////////
    // Start up the app
    //////////////////////////////////////////////////////////////////////
    
    public static void main(String[] args)
    {
        Random rnd = new Random();
        int xSize = 16;
        int ySize = 9;
        Board board = new Board(xSize, ySize);
        for(int y = 0; y < ySize; ++y)
        {
            for(int x = 0; x < xSize; ++x)
            {
                Location loc = board.getLocation(x, y);
                loc.setFoodCount(rnd.nextInt(5));
                int tmp = rnd.nextInt(100);
                if(tmp > 60)
                {
                    loc.setFilled(true);
                    if(tmp > 90)
                    {
                        loc.setRock(true);
                    }
                }
            }
        }
        
        //Clear board for starting pos
        Location loc = board.getLocation(0, 0);
        loc.setRock(false);
        loc.setFilled(false);
        loc = board.getLocation(0, 1);
        loc.setRock(false);
        loc.setFilled(false);
        loc = board.getLocation(1, 0);
        loc.setRock(false);
        loc.setFilled(false);
        
        
        loc = board.getLocation(board.getSizeX()-1, board.getSizeY()-1);
        loc.setRock(false);
        loc.setFilled(false);
        loc = board.getLocation(board.getSizeX()-2, board.getSizeY()-1);
        loc.setRock(false);
        loc.setFilled(false);
        loc = board.getLocation(board.getSizeX()-1, board.getSizeY()-2);
        loc.setRock(false);
        loc.setFilled(false);
        
        loc = board.getLocation(0, board.getSizeY()-1);
        loc.setRock(false);
        loc.setFilled(false);
        loc = board.getLocation(1, board.getSizeY()-1);
        loc.setRock(false);
        loc.setFilled(false);
        loc = board.getLocation(0, board.getSizeY()-2);
        loc.setRock(false);
        loc.setFilled(false);
        
        loc = board.getLocation(board.getSizeX()-1, 0);
        loc.setRock(false);
        loc.setFilled(false);
        loc = board.getLocation(board.getSizeX()-2, 0);
        loc.setRock(false);
        loc.setFilled(false);
        loc = board.getLocation(board.getSizeX()-1, 1);
        loc.setRock(false);
        loc.setFilled(false);
        
        Platform p = new PCPlatformImpl(true);
        PanAndZoom2DApp app = new AIAntWarsApp(board);
        p.runApplication(new PanAndZoomAdapter(app));
    }
}
