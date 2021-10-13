import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
*  This class defines a gui for the polygon program.
*
* */

public class Gui extends JFrame implements ActionListener {

    JMenuBar menuBar;
    JMenu File, attributes;
    JMenuItem bspline,boundary, both;
    JButton reset, color;
    JButton fill, play, stop ;
    JLabel animation_sec;
    Image i;

    public Gui(){
        i = new Image();
        add(i);

        menuBar = new JMenuBar();
        File = new JMenu("File");
        attributes = new JMenu("Attributes");
        bspline = new JMenuItem("B-Spline");
        boundary = new JMenuItem("Boundary");
        both = new JMenuItem("Boundary & B-Spline...");
        attributes.add(bspline);
        attributes.add(boundary);
        attributes.add(both);

        reset = new JButton("Reset");
        color = new JButton("Set Color");
        fill = new JButton("Fill Poly");
        animation_sec = new JLabel("Control:");
        play = new JButton("Play");
        stop = new JButton("Stop");

        reset.addActionListener(this);
        color.addActionListener(this);
        fill.addActionListener(this);
        play.addActionListener(this);
        stop.addActionListener(this);
        bspline.addActionListener(this);
        boundary.addActionListener(this);
        both.addActionListener(this);

        menuBar.add(File);
        menuBar.add(attributes);
        menuBar.add(reset);
        menuBar.add(color);
        menuBar.add(fill);
        menuBar.add(animation_sec);
        menuBar.add(play);
        menuBar.add(stop);
        this.setJMenuBar(menuBar);

        fill.setOpaque(true);
        menuBar.setOpaque(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(800,800);
        setVisible(true);
    }

    public void reset(){
        remove(i);
        i = new Image();
        add(i);
        this.revalidate();
    }


    public static void main(String[] args) {
        Gui g = new Gui();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == reset){
            reset();
        }
        if(e.getSource() == color){
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            i.setColor(newColor);
        }
        if(e.getSource() == fill){
            if(i.isPoly_fill()){
                i.setPoly_fill(false);
                fill.setBackground(null);
                attributes.setVisible(true);
            }
            else{
                i.setPoly_fill(true);
                fill.setBackground(Color.LIGHT_GRAY);
                attributes.setVisible(false);
            }
        }
        if(e.getSource() == stop){
            i.stop();
        }
        if(e.getSource() == play){
            i.play();
        }
        if(e.getSource() == both){
            i.setRender_boundary(true);
            i.setRender_bspline(true);
        }
        if(e.getSource()==boundary){
            i.setRender_boundary(true);
            i.setRender_bspline(false);
        }
        if(e.getSource()==bspline){
            i.setRender_boundary(false);
            i.setRender_bspline(true);
        }

    }
}
