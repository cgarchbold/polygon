
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;


public class Image extends JPanel implements MouseListener, MouseMotionListener, ActionListener {
    private Polygon poly;
    private Color color;
    private Timer timer;

    private ArrayList<Point> points;

    private Point curr_clicked;
    private boolean mouse_pressed, poly_fill, animating, render_boundary, render_bspline;

    private int rotation_degrees;

    public Image(){
        points = new ArrayList<Point>();
        timer = new Timer(10, this); // every 10 ms
        timer.addActionListener(this);
        rotation_degrees = 0;

        color = Color.RED;
        poly_fill = false;
        animating = false;
        render_boundary = true;
        render_bspline = false;


        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void setRender_boundary(boolean render_boundary){this.render_boundary = render_boundary;}
    public void setRender_bspline(boolean render_bspline){this.render_bspline = render_bspline;}

    public void setColor(Color color) {
        this.color = color;
        repaint();
    }
    public void setPoly_fill(boolean fill){
        this.poly_fill = fill;
        repaint();
    }
    public void play(){
        animating = true;
        timer.start();
        rotation_degrees = 0;
    }
    public void stop(){
        animating = false;
        timer.stop();
        rotation_degrees = 0;
        repaint();
    }

    public boolean isPoly_fill() {return poly_fill;}

    //Calculates the centroid of the objects list of points
    //Returns the centroid as a Point Object
    public Point centroid()  {
        double centroidX = 0, centroidY = 0;
        for(Point p : points) {
            centroidX += p.getX();
            centroidY += p.getY();
        }
        return new Point((int)(centroidX / points.size()), (int)(centroidY / points.size()));
    }
    @Override
    //Overriding paint function
    //Uses graphics2d for transformation
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        //preform rotation transformation
        AffineTransform oldXForm = g2d.getTransform();
        Point centroid =  centroid();
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(rotation_degrees), centroid.x, centroid.y);
        g2d.transform(transform);

        //drawing polygon, boundaries or filled
        g2d.setColor(color);
        make_polygon(points);
        if (poly_fill)
            g2d.fillPolygon(poly);
        else {
            if(render_boundary)
                g2d.drawPolygon(poly);
            if(render_bspline){
                CubicCurve2D curve  = new CubicCurve2D.Double();
                Point2D[] curve_points = get_2dPointArray(points);
                //curve.setCurve(curve_points,0);
                //g2d.draw(curve);
                bspline(g,curve_points);
            }
        }

        //draw vertices, only when not animating
        g2d.setColor(Color.BLACK);
        if(!animating) {
            for (Point p : points)
                g2d.fillRect(p.x - 2, p.y - 2, 4, 4);
        }
        //return transform
        g2d.setTransform(oldXForm);
    }

    // Sourced from the Multimedia Technology Laboratory National Technical University of Athens
    // Link : http://www.medialab.ntua.gr/education/ComputerGraphics/JavaExercises/Java/Bspline.java
    // Originally from Section 4.2 of Ammeraal, L. (1998) Computer Graphics for Java Programmers, Chichester: John Wiley.
    // Comments added to demonstrate understanding
    void bspline(Graphics g, Point2D[] P) {
        //declare parameters
        int m = 50, n = P.length;
        //varaibles to track starts and control points
        double xA, yA, xB, yB, xC, yC, xD, yD,
                a0, a1, a2, a3, b0, b1, b2, b3, x=0, y=0, x0, y0;
        boolean first = true;

        //for each point, between first and last
        for (int i=1; i<n-2; i++)
        //Declare control point instantiation
        {  xA=P[i-1].getX(); xB=P[i].getX(); xC=P[i+1].getX(); xD=P[i+2].getX();
            yA=P[i-1].getY(); yB=P[i].getY(); yC=P[i+1].getY(); yD=P[i+2].getY();

            //cubic parameters
            a3=(-xA+3*(xB-xC)+xD)/6; b3=(-yA+3*(yB-yC)+yD)/6;
            a2=(xA-2*xB+xC)/2;       b2=(yA-2*yB+yC)/2;
            a1=(xC-xA)/2;            b1=(yC-yA)/2;
            a0=(xA+4*xB+xC)/6;       b0=(yA+4*yB+yC)/6;

            //iterate m times, caculating x and y based on the control, draw small line segments
            for (int j=0; j<=m; j++)
            {  x0 = x; y0 = y;
                double t = (double)j/(double)m;
                x = ((a3*t+a2)*t+a1)*t+a0;
                y = ((b3*t+b2)*t+b1)*t+b0;
                if (first) first = false;
                else
                    g.drawLine((int)(x0), (int)(y0), (int)(x), (int)(y));
            }
        }
    }

    public Point2D[] get_2dPointArray(ArrayList<Point> point_list){
        Point2D[] points = new Point2D[point_list.size()];
        int c = 0;
        for(Point p: point_list){
            Point2D p2 = new Point2D.Double();
            p2.setLocation(p.x,p.y);
            points[c] = p2;
            c++;
        }
        return points;
    }

    //create polygon object for drawing
    public void make_polygon(ArrayList<Point> point_list){
        poly = new Polygon();
        for(Point p: point_list){
            poly.addPoint(p.x, p.y);
        }
    }

    // Checks if a point is near a already placed vertex
    // returns the close vertex or original point if none found
    public Point check_point(Point q){
        Point nearest = q;
        for(Point p: points)
            if(q.distance(p)<=5)
                nearest = p;
        return nearest;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!animating) {
            mouse_pressed = true;
            int x = e.getX();
            int y = e.getY();
            if (curr_clicked == null) {
                Point clik = new Point(x, y);
                curr_clicked = check_point(clik);
                if (!points.contains(curr_clicked))
                    points.add(curr_clicked);
            }
            curr_clicked.setLocation(x, y);
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        curr_clicked = null;
        mouse_pressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(mouse_pressed && !animating){
            int x = e.getX();
            int y = e.getY();
            curr_clicked.setLocation(x,y);
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //The Animation Handling
        if(e.getSource() == timer){
            rotation_degrees += 1;
            repaint();
            if(rotation_degrees > 360){
                stop();
            }
        }
    }
}
