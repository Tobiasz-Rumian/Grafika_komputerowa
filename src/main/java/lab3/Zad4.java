package lab3;


import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import javafx.geometry.Point3D;

import javax.swing.*;
import java.awt.event.*;
import java.util.Random;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import static java.lang.Math.*;

public class Zad4 implements GLEventListener, KeyListener, MouseMotionListener, MouseListener {
    private final static int N = 50;
    private Point3D[][] tab = new Point3D[N][N];
    private Point3D[][] col = new Point3D[N][N];
    private int model = 2;
    private static Point3D viewer = new Point3D(0.0, 0.0, 10.0);
    private static float p = 1.0f;
    private static float thetaX = 0.0f;
    private static float thetaY = 0.0f;
    private static float thetaZoom = 10.0f;
    private static float pix2angle;
    private static int status = 0;
    private static int xPosOld = 0;
    private static int yPosOld = 0;
    private static int zoom = 0;
    private static int deltaX = 0;
    private static int deltaY = 0;
    private static int deltaZoom = 0;


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

    private void eggsPoints(GL2 gl2) {
        gl2.glColor3f(1.0f, 1.0f, 1.0f);
        gl2.glBegin(GL_POINTS);
        for (int k = 0; k < N; k++)
            for (int l = 0; l < N; l++)
                gl2.glVertex3d(tab[k][l].getX(), tab[k][l].getY(), tab[k][l].getZ());
        gl2.glEnd();
    }

    private void eggsMesh(GL2 gl2) {
        gl2.glColor3f(1.0f, 1.0f, 1.0f);
        gl2.glBegin(GL_LINES);
        for (int i = 0; i < N - 1; i++)
            for (int j = 0; j < N - 1; j++) {
                gl2.glVertex3d(tab[i][j].getX(), tab[i][j].getY(), tab[i][j].getZ());
                gl2.glVertex3d(tab[i + 1][j].getX(), tab[i + 1][j].getY(), tab[i + 1][j].getZ());
                gl2.glVertex3d(tab[i + 1][j + 1].getX(), tab[i + 1][j + 1].getY(), tab[i + 1][j + 1].getZ());
                gl2.glVertex3d(tab[i + 1][j].getX(), tab[i + 1][j].getY(), tab[i + 1][j].getZ());
            }
        gl2.glEnd();
    }

    private void addVertexWithColor(Point3D vertex, Point3D color, GL2 gl2) {
        gl2.glColor3d(color.getX(), color.getY(), color.getZ());
        gl2.glVertex3d(vertex.getX(), vertex.getY(), vertex.getZ());
    }

    private void eggsTriangles(GL2 gl2) {
        Point3D tab[][] = calculatePoints(true);
        Point3D col[][] = new Point3D[200][200];
        Random random = new Random();
        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j) {
                col[i][j] = new Point3D(
                        random.nextInt(100) / 1000f,
                        random.nextInt(100) / 1000f,
                        random.nextInt(100) / 1000f);
            }
        gl2.glBegin(GL_TRIANGLES);
        for (int i = 0; i < N - 1; i++)
            for (int j = 0; j < N - 1; j++) {
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

    private void randCol() {
        Random random = new Random();
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                col[i][j] = new Point3D(
                        random.nextInt(100) / 1000f,
                        random.nextInt(100) / 1000f,
                        random.nextInt(100) / 1000f);
        for (int i = 0; i < N; i++) col[i][N - 1] = col[N - i - 1][0];
    }


    private void spinEgg() {
        Random random = new Random();
        Point3D theta = new Point3D((float) random.nextInt(100) / 10000f, (float) random.nextInt(100) / 10000f, (float) random.nextInt(100) / 10000f);
        thetaX -= theta.getX();
        thetaY -= theta.getY();
    }

    private Point3D[][] calculatePoints(boolean minus5) {
        Point3D[][] tab = new Point3D[200][200];
        float u, v;
        double y;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                u = (float) i / (N - 1);
                v = (float) j / (N - 1);
                if (minus5) y = (160 * pow(u, 4) - 320 * pow(u, 3) + 160 * pow(u, 2) - 5.0);
                else y = (160 * pow(u, 4.0f) - 320 * pow(u, 3.0f) + 160 * pow(u, 2.0f));
                tab[i][j] = new Point3D(
                        ((-90 * pow(u, 5.0f) + 225 * pow(u, 4.0f) - 270 * pow(u, 3.0f) + 180 * pow(u, 2.0f) - 45 * u) * cos(PI * v)),
                        y,
                        ((-90 * pow(u, 5.0f) + 225 * pow(u, 4.0f) - 270 * pow(u, 3.0f) + 180 * pow(u, 2.0f) - 45 * u) * sin(PI * v)));
            }
        return tab;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        drawable.getGL().getGL2().glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        tab = calculatePoints(true);
        randCol();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        spinEgg();
        GL2 gl2 = drawable.getGL().getGL2();
        GLU glu = GLU.createGLU(gl2);
        gl2.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        gl2.glLoadIdentity();
        glu.gluLookAt(viewer.getX(), viewer.getY(), viewer.getZ(), 0.0, 0.0, 0.0, 0.0, p, 0.0);
        axes(gl2);
        if (status == 1) {
            thetaX += deltaX * pix2angle / 30.0;
            thetaY += deltaY * pix2angle / 30.0;
        } else if (status == 2) thetaZoom += deltaZoom / 10.0;
        //System.out.println(thetaX + " " + thetaY);
        if (thetaY > Math.PI) thetaY -= 2 * Math.PI;
        else if (thetaY <= -Math.PI) thetaY += 2 * Math.PI;
        if (thetaY > Math.PI / 2 || thetaY < -Math.PI / 2) p = -1.0f;
        else p = 1.0f;
        viewer = new Point3D(
                thetaZoom * cos(thetaX) * cos(thetaY),
                thetaZoom * sin(thetaY),
                thetaZoom * sin(thetaX) * cos(thetaY));
        if (model == 1) eggsPoints(gl2);
        else if (model == 2) eggsMesh(gl2);
        else eggsTriangles(gl2);
        gl2.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        pix2angle = 360.0f / (float) width;
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

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == com.jogamp.newt.event.MouseEvent.BUTTON1) {
            xPosOld = e.getX();
            yPosOld = e.getY();
            status = 1;
        } else if (e.getButton() == com.jogamp.newt.event.MouseEvent.BUTTON3) {
            zoom = e.getY();
            status = 2;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == com.jogamp.newt.event.MouseEvent.BUTTON1) status = 0;
        else if (e.getButton() == com.jogamp.newt.event.MouseEvent.BUTTON3) status = 0;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        deltaX = e.getX() - xPosOld;
        deltaY = e.getY() - yPosOld;
        xPosOld = e.getX();
        yPosOld = e.getY();
        deltaZoom = e.getY() - zoom;
        zoom = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public static void main(String[] args) {
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        final GLCanvas glcanvas = new GLCanvas(capabilities);
        Zad4 zad4 = new Zad4();
        glcanvas.addGLEventListener(zad4);
        glcanvas.addMouseListener(zad4);
        glcanvas.addMouseMotionListener(zad4);
        glcanvas.addKeyListener(zad4);
        glcanvas.setSize(800, 800);
        final JFrame frame = new JFrame("Zad4");
        FPSAnimator animator = new FPSAnimator(25);
        animator.add(glcanvas);
        animator.start();
        frame.getContentPane().add(glcanvas);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setVisible(true);
    }
}