package lab3;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import javafx.geometry.Point3D;

import javax.swing.*;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

public class Zad1 implements GLEventListener {
    private static double viewer[] = {3.0, 3.0, 10.0};

    private void axes(GL2 gl2) {
        Point3D x_min = new Point3D(-5.0, 0.0, 0.0);
        Point3D x_max = new Point3D(5.0, 0.0, 0.0);
        Point3D y_min = new Point3D(0.0, -5.0, 0.0);
        Point3D y_max = new Point3D(0.0, 5.0, 0.0);
        Point3D z_min = new Point3D(0.0, 0.0, -5.0);
        Point3D z_max = new Point3D(0.0, 0.0, 5.0);
        gl2.glColor3f(1.0f, 0.0f, 0.0f);
        setVertex(x_min, x_max, gl2);
        gl2.glColor3f(0.0f, 1.0f, 0.0f);
        setVertex(y_min, y_max, gl2);
        gl2.glColor3f(0.0f, 0.0f, 1.0f);
        setVertex(z_min, z_max, gl2);
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
        glu.gluLookAt(viewer[0], viewer[1], viewer[2], 0.0, 0.0, 0.0, 1.0, 1.0, 0.0);
        axes(gl2);
        gl2.glColor3f(1.0f, 1.0f, 1.0f);
        glut.glutWireTeapot(3.0);
        gl2.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
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
        glcanvas.setSize(800, 800);
        final JFrame frame = new JFrame("Zad3");
        // FPSAnimator animator = new FPSAnimator(25);
        // animator.add(glcanvas);
        // animator.start();
        frame.getContentPane().add(glcanvas);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setVisible(true);
    }
}
