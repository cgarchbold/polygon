
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class Image extends JPanel implements MouseListener, MouseMotionListener, ActionListener {
    private Polygon poly;
    private Color color;
    private Timer timer;

    //point array  DONT NEED
    ArrayList<Point> points;

    Point curr_clicked;
    boolean mouse_pressed, poly_fill, animating;

    private int rotation_degrees;



    public Image(){
        points = new ArrayList<Point>();
        timer = new Timer(200, this); // every 200 ms
        rotation_degrees = 0;

        color = Color.RED;
        poly_fill = false;
        animating = false;

        addMouseListener(this);
        addMouseMotionListener(this);
    }

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
    }
    public void stop(){
        animating =false;
        timer.stop();
    }

    public boolean isPoly_fill() {return poly_fill;}

    public Point centroid()  {
        double centroidX = 0, centroidY = 0;

        for(Point p : points) {
            centroidX += p.getX();
            centroidY += p.getY();
        }
        return new Point((int)(centroidX / points.size()), (int)(centroidY / points.size()));
    }

    public void paint(Graphics g){
        super.paint(g);

        if(animating){

        }
        else {
            //draw lines connecting them
            g.setColor(color);

            make_polygon();
            if (poly_fill)
                g.fillPolygon(poly);
            else
                g.drawPolygon(poly);

            g.setColor(Color.BLACK);
            //draw points in array
            //if we arent animating
            for (Point p : points)
                g.fillRect(p.x - 2, p.y - 2, 4, 4);
        }
    }

    public void make_polygon(){
        poly = new Polygon();
        for(Point p: points){
            poly.addPoint(p.x, p.y);
        }
    }

    public Point check_point(Point q){
        Point nearest = q;

        for(Point p: points){
            //System.out.println(q.distance(p));
            if(q.distance(p)<=5)
                nearest = p;
        }
        return nearest;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        // If I did not click within a point
        //int x = e.getX();
        //int y = e.getY();
        //System.out.println(x+", "+y);
        //points.add(new Point(x,y));
        //repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouse_pressed = true;
        int x = e.getX();
        int y = e.getY();
        if(curr_clicked == null) {
            Point clik = new Point(x, y);
            curr_clicked = check_point(clik);
            if(!points.contains(curr_clicked))
                points.add(curr_clicked);
        }
        curr_clicked.setLocation(x, y);
        repaint();
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
        if(mouse_pressed){
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
        if(e.getSource() == timer){
            rotation_degrees +=1;
            repaint();
        }
    }
}
