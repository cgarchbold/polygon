
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;


public class Image extends JPanel implements MouseListener {
    private Polygon poly;

    //point array
    ArrayList<Point> points;

    Point curr_clicked;
    boolean mouse_pressed;

    public Image(){
        points = new ArrayList<Point>();

        addMouseListener(this);
    }

    public void paint(Graphics g){
        super.paint(g);

        //draw points in array
        for(Point p: points)
            g.fillRect(p.x-2, p.y-2, 4, 4);

        //draw lines connecting them
        g.setColor(Color.BLUE);
        for(int i=0; i< points.size()-1;i++){
            Point s = points.get(i);
            Point e = points.get(i+1);
            g.drawLine(s.x,s.y,e.x,e.y);
            //System.out.println("line drawn ("+s.x+", "+s.y+"):("+e.x+", "+e.y+")");
        }
        //if points size is greater than 2, connect the ends
        if(points.size() > 2) {
            Point s = points.get(points.size() - 1);
            Point e = points.get(0);
            g.drawLine(s.x,s.y,e.x,e.y);
            //System.out.println("line drawn ("+s.x+", "+s.y+"):("+e.x+", "+e.y+")");
        }

    }

    public Point check_point(Point q){
        Point nearest = q;

        for(Point p: points){
            if(q.distance(p)<=2)
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
}
