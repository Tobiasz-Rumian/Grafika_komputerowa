package lab3;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;
import javafx.geometry.Point3D;

import javax.swing.*;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

public class Zad2 implements GLEventListener, MouseListener, MouseMotionListener {
    private static double viewer[] = {0.0, 0.0, 10.0};
    private static float theta = 0.0f;
    private static float pix2angle;
    private static int status = 0;
    private static int xPosOld = 0;
    private static int deltaX = 0;


    private void axes(GL2 gl2) {
        Point3D xMin = new Point3D(-5.0, 0.0, 0.0);
        Point3D xMax = new Point3D(5.0, 0.0, 0.0);
        Point3D yMin = new Point3D(0.0, -5.0, 0.0);
        Point3D yMax = new Point3D(0.0, 5.0, 0.0);
        Point3D zMin = new Point3D(0.0, 0.0, -5.0);
        Point3D zMax = new Point3D(0.0, 0.0, 5.0);
        gl2.glColor3f(1.0f, 0.0f, 0.0f);
        setVertex(xMin, xMax, gl2);
        gl2.glColor3f(0.0f, 1.0f, 0.0f);
        setVertex(yMin, yMax, gl2);
        gl2.glColor3f(0.0f, 0.0f, 1.0f);
        setVertex(zMin, zMax, gl2);
    }

    private void setVertex(Point3D cordMin, Point3D cordMax, GL2 gl2) {
        gl2.glBegin(GL_LINES);
        gl2.glVertex3d(cordMin.getX(), cordMin.getY(), cordMin.getZ());
        gl2.glVertex3d(cordMax.getX(), cordMax.getY(), cordMax.getZ());
        gl2.glEnd();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        drawable.getGL().getGL2().glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl2 = drawable.getGL().getGL2();
        GLU glu = GLU.createGLU(gl2);
        GLUT glut = new GLUT();
        gl2.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        gl2.glLoadIdentity();
        glu.gluLookAt(viewer[0], viewer[1], viewer[2], 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
        axes(gl2);
        if(status == 1) theta += deltaX *pix2angle;
        gl2.glRotated(theta, 0.0, 1.0, 0.0);
        gl2.glColor3f(1.0f, 1.0f, 1.0f);
        glut.glutWireTeapot(3.0);
        gl2.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        pix2angle = 360.0f/(float)width;
        GL2 gl2 = drawable.getGL().getGL2();
        GLU glu = GLU.createGLU(gl2);
        gl2.glMatrixMode(GL_PROJECTION);
        gl2.glLoadIdentity();
        glu.gluPerspective(70, 1.0, 1.0, 30.0);
        if (width <= height) gl2.glViewport(0, (height - width) / 2, width, width);
        else gl2.glViewport((width - height) / 2, 0, height, height);
        gl2.glMatrixMode(GL_MODELVIEW);
        gl2.glLoadIdentity();
    }

    public static void main(String[] args) {
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        final GLCanvas glcanvas = new GLCanvas(capabilities);
        Zad3 zad3 = new Zad3();
        glcanvas.addGLEventListener(zad3);
        glcanvas.addMouseListener(zad3);
        glcanvas.addMouseMotionListener(zad3);
        glcanvas.setSize(800, 800);
        final JFrame frame = new JFrame("Zad3");
         FPSAnimator animator = new FPSAnimator(25);
         animator.add(glcanvas);
         animator.start();
        frame.getContentPane().add(glcanvas);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setVisible(true);
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {

    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            xPosOld = e.getX();
            status = 1;
        }
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) status = 0;
    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {

    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {

    }

    @Override
    public void mouseDragged(java.awt.event.MouseEvent e) {
        if(status==1){
            deltaX =e.getX()- xPosOld;     // obliczenie różnicy położenia kursora myszy
            xPosOld =e.getX();            // podstawienie bieżącego położenia jako poprzednie
        }
        else status=0;
    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {

    }
}
