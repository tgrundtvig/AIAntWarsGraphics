/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiantwarsgraphics;

import aiantwars.EAntType;
import aiantwars.IAntAI;
import aiantwars.IOnGameFinished;
import aiantwars.ITeamInfo;
import aiantwars.impl.AntWarsGameCtrl;
import aiantwars.impl.Board;
import aiantwars.impl.Score;
import aiantwars.impl.TeamInfo;
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
import app2dapi.panandzoom2dapp.PanAndZoomInit;
import app2dapi.panandzoom2dapp.PanAndZoomToolKit;
import java.util.Map;

/**
 *
 * @author Tobias Grundtvig
 */
public class AIAntWarsApp implements PanAndZoom2DApp, IOnGameFinished
{
    //private HumanGraphicsAI humanAI;
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
    private IAntAI[] ais;
    private BoardPos[] positions;

    public AIAntWarsApp(Board board, IAntAI[] ais, BoardPos[] positions)
    {
        if(ais.length < 2) throw new RuntimeException("There must be at least AI's");
        if(ais.length != positions.length) throw new RuntimeException("The number of AI's and positions does not match");
        this.board = board;
        this.ais = ais;
        this.positions = positions;
    }
    
    @Override
    public PanAndZoomInit initialize(PanAndZoomToolKit tk, double aspectRatio)
    {
        g2d = tk.g2d();
        cf = tk.cf();
        this.sizeHUD = g2d.newDimension2D(1000, 1000.0 / aspectRatio);
        mouseHUDPos = g2d.origo();
        mouseWorldPos = g2d.origo();
        //humanAI = new HumanGraphicsAI(g2d, cf);
        this.pointer = new MousePointer(g2d, cf);
        graphicsAntFactory = new GraphicsAntWarsGUI(g2d, cf, sizeHUD);
        graphicsBoard = new GraphicsBoard(g2d, board, cf, new LocationDrawer(g2d, cf.newColor(0.8f, 0.75f, 0.75f), cf.newColor(0.545f, 0.27f, 0.075f), cf.getBlack(), cf.getYellow()));
        
        AntWarsGameCtrl ctrl = new AntWarsGameCtrl(graphicsAntFactory, board, this);
        
        for(int i = 0; i < ais.length; ++i)
        {
            TeamInfo team = new TeamInfo((i+1), "Team " + (i + 1));
            ctrl.createAnt(team, EAntType.QUEEN, ais[i], board.getLocation(positions[i].getX(), positions[i].getY()), positions[i].getDirection());
        }
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
        graphicsAntFactory.update(time*10);
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
        //humanAI.draw(canvas);
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
        //humanAI.onMouseMoved(mouseWorldPos);
    }

    @Override
    public void onMousePressed(Point2D mouseHUDPos, Point2D mouseWorldPos)
    {
        this.mouseHUDPos = mouseHUDPos;
        this.mouseWorldPos = mouseWorldPos;
        //humanAI.onMousePressed(mouseWorldPos);
    }

    @Override
    public void onMouseReleased(Point2D mouseHUDPos, Point2D mouseWorldPos)
    {
        this.mouseHUDPos = mouseHUDPos;
        this.mouseWorldPos = mouseWorldPos;
        //humanAI.onMouseReleased(mouseWorldPos);
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

    @Override
    public void onGameFinished(Map<ITeamInfo, Score> scores)
    {
        for(Map.Entry<ITeamInfo, Score> entry : scores.entrySet())
        {
            ITeamInfo team = entry.getKey();
            Score score = entry.getValue();
            System.out.println("Team: " + team.getTeamName() + " Score: " + score);
        }
    }
}
