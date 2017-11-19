package lab2;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import javafx.geometry.Point3D;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import static java.lang.Math.*;

public class Zad3 implements GLEventListener, KeyListener {
    private static final int NUMBER_OF_VERTEXES = 100;
    private int model = 3;
    private static float[] theta = {0.0f, 0.0f, 0.0f};

    private void Axes(GL2 gl2) {
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

    private void eggsPoints(GL2 gl2) {
        Point3D[][] tab = calculatePoints(false);
        gl2.glColor3f(1.0f, 1.0f, 1.0f);
        gl2.glBegin(GL_POINTS);
        for (int k = 0; k < NUMBER_OF_VERTEXES; k++)
            for (int l = 0; l < NUMBER_OF_VERTEXES; l++)
                gl2.glVertex3d(tab[k][l].getX(), tab[k][l].getY() - 5.0f, tab[k][l].getZ());
        gl2.glEnd();
    }

    private Point3D[][] calculatePoints(boolean minus5) {
        Point3D[][] tab = new Point3D[200][200];
        float u, v;
        double y;
        for (int i = 0; i < NUMBER_OF_VERTEXES; i++)
            for (int j = 0; j < NUMBER_OF_VERTEXES; j++) {
                u = (float) i / (NUMBER_OF_VERTEXES - 1);
                v = (float) j / (NUMBER_OF_VERTEXES - 1);
                if (minus5) y = (160 * pow(u, 4) - 320 * pow(u, 3) + 160 * pow(u, 2) - 5.0);
                else y = (160 * pow(u, 4.0f) - 320 * pow(u, 3.0f) + 160 * pow(u, 2.0f));
                tab[i][j] = new Point3D(
                        ((-90 * pow(u, 5.0f) + 225 * pow(u, 4.0f) - 270 * pow(u, 3.0f) + 180 * pow(u, 2.0f) - 45 * u) * cos(PI * v)),
                        y,
                        ((-90 * pow(u, 5.0f) + 225 * pow(u, 4.0f) - 270 * pow(u, 3.0f) + 180 * pow(u, 2.0f) - 45 * u) * sin(PI * v)));
            }
        return tab;
    }

    private void eggsMesh(GLAutoDrawable drawable) {
        GL2 gl2 = drawable.getGL().getGL2();
        Point3D tab[][] = calculatePoints(true);
        gl2.glColor3f(1.0f, 1.0f, 1.0f);
        gl2.glBegin(GL_LINES);
        for (int i = 0; i < NUMBER_OF_VERTEXES - 1; ++i)
            for (int j = 0; j < NUMBER_OF_VERTEXES - 1; ++j) {
                gl2.glVertex3d(tab[i][j].getX(), tab[i][j].getY(), tab[i][j].getZ());
                gl2.glVertex3d(tab[i + 1][j].getX(), tab[i + 1][j].getY(), tab[i + 1][j].getZ());
                gl2.glVertex3d(tab[i][j].getX(), tab[i][j].getY(), tab[i][j].getZ());
                gl2.glVertex3d(tab[i][j + 1].getX(), tab[i][j + 1].getY(), tab[i][j + 1].getZ());
                gl2.glVertex3d(tab[i + 1][j].getX(), tab[i + 1][j].getY(), tab[i + 1][j].getZ());
                gl2.glVertex3d(tab[i][j + 1].getX(), tab[i][j + 1].getY(), tab[i][j + 1].getZ());
            }
        gl2.glEnd();
    }

    private void eggsTriangles(GLAutoDrawable drawable) {
        GL2 gl2 = drawable.getGL().getGL2();
        Point3D tab[][] = calculatePoints(true);
        Point3D col[][] = new Point3D[200][200];
        Random random = new Random();
        for (int i = 0; i < NUMBER_OF_VERTEXES; ++i)
            for (int j = 0; j < NUMBER_OF_VERTEXES; ++j) {
                col[i][j] = new Point3D(
                        random.nextInt(100) / 1000f,
                        random.nextInt(100) / 1000f,
                        random.nextInt(100) / 1000f);
            }
        gl2.glBegin(GL_TRIANGLES);
        for (int i = 0; i < NUMBER_OF_VERTEXES - 1; i++)
            for (int j = 0; j < NUMBER_OF_VERTEXES - 1; j++) {
                gl2.glColor3f(1.0f, 1.0f, 1.0f);
                gl2.glBegin(GL_TRIANGLES);
                addVertexWithColor(tab[i][j], col[i][j], gl2);
                addVertexWithColor(tab[i + 1][j], col[i + 1][j], gl2);
                addVertexWithColor(tab[i][j + 1], col[i][j + 1], gl2);
                gl2.glEnd();
                gl2.glColor3d(1.0, 1.0, 1.0);
                gl2.glBegin(GL_TRIANGLES);
                addVertexWithColor(tab[i + 1][j + 1], col[i + 1][j + 1], gl2);
                addVertexWithColor(tab[i + 1][j], col[i + 1][j], gl2);
                addVertexWithColor(tab[i][j + 1], col[i][j + 1], gl2);
                gl2.glEnd();
            }
        gl2.glEnd();
    }

    private void addVertexWithColor(Point3D vertex, Point3D color, GL2 gl2) {
        gl2.glColor3d(color.getX(), color.getY(), color.getZ());
        gl2.glVertex3d(vertex.getX(), vertex.getY(), vertex.getZ());
    }

    private void spinEgg() {
        theta[0] -= 0.1;
        if (theta[0] > 360.0) theta[0] -= 360.0;
        theta[1] -= 0.1;
        if (theta[1] > 360.0) theta[1] -= 360.0;
        theta[2] -= 0.1;
        if (theta[2] > 360.0) theta[2] -= 360.0;
    }

    public static void main(String[] args) {
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        final GLCanvas glcanvas = new GLCanvas(capabilities);
        Zad3 zad3 = new Zad3();
        glcanvas.addGLEventListener(zad3);
        glcanvas.addKeyListener(zad3);
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
    public void init(GLAutoDrawable drawable) {
        drawable.getGL().getGL2().glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        spinEgg();
        GL2 gl2 = drawable.getGL().getGL2();
        gl2.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        gl2.glLoadIdentity();
        Axes(gl2);
        gl2.glRotatef(theta[0], 1.0f, 0.0f, 0.0f);
        gl2.glRotatef(theta[1], 0.0f, 1.0f, 0.0f);
        gl2.glRotatef(theta[2], 0.0f, 0.0f, 1.0f);
        gl2.glColor3f(1.0f, 1.0f, 1.0f);
        gl2.glRotated(90.0, 0.0, 1.0, 0.0);
        if (model == 1) eggsPoints(gl2);
        else if (model == 2) eggsMesh(drawable);
        else eggsTriangles(drawable);
        gl2.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl2 = drawable.getGL().getGL2();
        float AspectRatio;
        if (height == 0) height = 1;
        gl2.glViewport(0, 0, width, height);
        gl2.glMatrixMode(GL_PROJECTION);
        gl2.glLoadIdentity();
        AspectRatio = (float) width / (float) height;

        if (width <= height) {
            gl2.glOrtho(-7.5, 7.5, -7.5 / AspectRatio, 7.5 / AspectRatio, 10.0, -10.0);
        } else {
            gl2.glOrtho(-7.5 * AspectRatio, 7.5 * AspectRatio, -7.5, 7.5, 10.0, -10.0);
        }
        gl2.glMatrixMode(GL_MODELVIEW);
        gl2.glLoadIdentity();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'p') model = 1;
        else if (e.getKeyChar() == 'w') model = 2;
        else if (e.getKeyChar() == 's') model = 3;
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
