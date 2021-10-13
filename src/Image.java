
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
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
        else
            g2d.drawPolygon(poly);

        //draw vertices, only when not animating
        g2d.setColor(Color.BLACK);
        if(!animating) {
            for (Point p : points)
                g2d.fillRect(p.x - 2, p.y - 2, 4, 4);
        }
        //return transform
        g2d.setTransform(oldXForm);
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
