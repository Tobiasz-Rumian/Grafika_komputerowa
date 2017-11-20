package lab4;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;
import javafx.geometry.Point3D;

import javax.swing.JFrame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.nio.FloatBuffer;

import static com.jogamp.newt.event.MouseEvent.BUTTON1;
import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_FRONT;
import static com.jogamp.opengl.GL.GL_LINES;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_CONSTANT_ATTENUATION;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LIGHT0;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LINEAR_ATTENUATION;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_POSITION;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_QUADRATIC_ATTENUATION;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SHININESS;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SPECULAR;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import static java.awt.event.MouseEvent.BUTTON3;

public class Zad1 implements GLEventListener, MouseMotionListener, MouseListener {
   private Point3D theta = new Point3D(0.0, 0.0, 0.0);
   private Point3D viewer = new Point3D(0.0, 0.0, 10.0);
   private static double pix2angle;
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

   public static void main(String[] args) {
      final GLProfile profile = GLProfile.get(GLProfile.GL2);
      GLCapabilities capabilities = new GLCapabilities(profile);
      final GLCanvas glcanvas = new GLCanvas(capabilities);
      Zad1 zad1 = new Zad1();
      glcanvas.addGLEventListener(zad1);
      glcanvas.addMouseListener(zad1);
      glcanvas.addMouseMotionListener(zad1);
      glcanvas.setSize(800, 800);
      final JFrame frame = new JFrame("Zad1");
      FPSAnimator animator = new FPSAnimator(25);
      animator.add(glcanvas);
      animator.start();
      frame.getContentPane().add(glcanvas);
      frame.setSize(frame.getContentPane().getPreferredSize());
      frame.setVisible(true);
   }

   @Override
   public void init(GLAutoDrawable drawable) {
      GL2 gl2 = drawable.getGL().getGL2();
      gl2.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
      float mat_ambient[] = {1.0f, 1.0f, 1.0f, 1.0f};
      float mat_diffuse[] = {1.0f, 1.0f, 1.0f, 1.0f};
      float mat_specular[] = {1.0f, 1.0f, 1.0f, 1.0f};
      float mat_shininess = 20.0f;
      float light_position[] = {0.0f, 0.0f, 10.0f, 1.0f};
      float light_ambient[] = {0.1f, 0.1f, 0.1f, 1.0f};
      float light_diffuse[] = {1.0f, 1.0f, 1.0f, 1.0f};
      float light_specular[] = {1.0f, 1.0f, 1.0f, 1.0f};
      float att_constant = 1.0f;
      float att_linear = 0.05f;
      float att_quadratic = 0.001f;
      gl2.glMaterialfv(GL_FRONT, GL_SPECULAR, FloatBuffer.wrap(mat_specular));
      gl2.glMaterialfv(GL_FRONT, GL_AMBIENT, FloatBuffer.wrap(mat_ambient));
      gl2.glMaterialfv(GL_FRONT, GL_DIFFUSE, FloatBuffer.wrap(mat_diffuse));
      gl2.glMaterialf(GL_FRONT, GL_SHININESS, mat_shininess);
      gl2.glLightfv(GL_LIGHT0, GL_AMBIENT, FloatBuffer.wrap(light_ambient));
      gl2.glLightfv(GL_LIGHT0, GL_DIFFUSE, FloatBuffer.wrap(light_diffuse));
      gl2.glLightfv(GL_LIGHT0, GL_SPECULAR, FloatBuffer.wrap(light_specular));
      gl2.glLightfv(GL_LIGHT0, GL_POSITION, FloatBuffer.wrap(light_position));
      gl2.glLightf(GL_LIGHT0, GL_CONSTANT_ATTENUATION, att_constant);
      gl2.glLightf(GL_LIGHT0, GL_LINEAR_ATTENUATION, att_linear);
      gl2.glLightf(GL_LIGHT0, GL_QUADRATIC_ATTENUATION, att_quadratic);
      gl2.glShadeModel(GL_SMOOTH);
      gl2.glEnable(GL_LIGHTING);
      gl2.glEnable(GL_LIGHT0);
      gl2.glEnable(GL_DEPTH_TEST);
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
      glu.gluLookAt(viewer.getX(), viewer.getY(), viewer.getZ(), 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
      axes(gl2);
      if (status == 1) {
         theta = theta.add(deltaX * pix2angle, deltaY * pix2angle, 0.0);
      } else if (status == 2) {
         theta = theta.add(0.0, 0.0, deltaZoom * pix2angle);
      }
      gl2.glRotated(theta.getX(), 0.0, 1.0, 0.0);
      gl2.glRotated(theta.getY(), 1.0, 0.0, 0.0);
      gl2.glRotated(theta.getZ(), 0.0, 0.0, 0.0);
      gl2.glColor3d(1.0, 1.0, 1.0);
      glut.glutSolidTeapot(3.0);
      gl2.glFlush();
   }

   @Override
   public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
      GL2 gl2 = drawable.getGL().getGL2();
      GLU glu = GLU.createGLU(gl2);
      pix2angle = 360.0 / (float) width;
      gl2.glMatrixMode(GL_PROJECTION);
      gl2.glLoadIdentity();
      glu.gluPerspective(70.0, 1.0, 1.0, 30.0);
      if (width <= height) {
         gl2.glViewport(0, (height - width) / 2, width, width);
      } else {
         gl2.glViewport((width - height) / 2, 0, height, height);
      }
      gl2.glMatrixMode(GL_MODELVIEW);
      gl2.glLoadIdentity();
   }

   @Override
   public void mouseClicked(MouseEvent e) {

   }

   @Override
   public void mousePressed(MouseEvent e) {
      if (e.getButton() == BUTTON1) {
         xPosOld = e.getX();
         yPosOld = e.getY();
         status = 1;
      } else if (e.getButton() == BUTTON3) {
         zoom = e.getY();
         status = 2;
      }
   }

   @Override
   public void mouseReleased(MouseEvent e) {
      if (e.getButton() == BUTTON1 || e.getButton() == BUTTON3) {
         status = 0;
      }
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
      xPosOld = e.getX();
      deltaY = e.getY() - yPosOld;
      yPosOld = e.getY();
      deltaZoom = e.getY() - zoom;
      zoom = e.getY();
   }

   @Override
   public void mouseMoved(MouseEvent e) {

   }
}
