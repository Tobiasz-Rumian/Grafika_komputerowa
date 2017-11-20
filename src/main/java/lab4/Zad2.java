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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.nio.FloatBuffer;
import java.util.Random;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_FRONT;
import static com.jogamp.opengl.GL.GL_LINES;
import static com.jogamp.opengl.GL.GL_POINTS;
import static com.jogamp.opengl.GL.GL_TRIANGLES;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_CONSTANT_ATTENUATION;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LIGHT0;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LIGHT1;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LINEAR_ATTENUATION;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_POSITION;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_QUADRATIC_ATTENUATION;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SHININESS;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SPECULAR;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class Zad2 implements GLEventListener, KeyListener, MouseMotionListener, MouseListener {

   private static final int N = 25;
   private Point3D tab[][] = new Point3D[N][N];
   private Point3D col[][] = new Point3D[N][N];
   private Point3D nor[][] = new Point3D[N][N];
   private static Point3D theta = new Point3D(0.0, 0.0, 0.0);
   private float light_pos[][] = {{0.0f, 0.0f, 10.0f, 1.0f}, {1.0f, 1.0f, 1.0f, 0.0f}};
   private float beta[][] = {{0.0f, 0.0f}, {0.0f, 0.0f}};
   private int model = 2;
   private static Point3D viewer = new Point3D(0.0, 0.0, 10.0);
   private static float pix2angle;
   private static int status = 0;
   private static int xPosOld = 0;
   private static int yPosOld = 0;
   private static int deltaX = 0;
   private static int deltaY = 0;
   private float thetaZoom = 10.0f;


   private void axes(GL2 gl2) {
      Point3D xMin = new Point3D(-5.0, 0.0, 0.0);
      Point3D xMax = new Point3D(5.0, 0.0, 0.0);
      Point3D yMin = new Point3D(0.0, -5.0, 0.0);
      Point3D yMax = new Point3D(0.0, 5.0, 0.0);
      Point3D zMin = new Point3D(0.0, 0.0, -5.0);
      Point3D zMax = new Point3D(0.0, 0.0, 5.0);
      setVertex(xMin, xMax, gl2);
      setVertex(yMin, yMax, gl2);
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
      for (int k = 0; k < N; k++) {
         for (int l = 0; l < N; l++) {
            gl2.glVertex3d(tab[k][l].getX(), tab[k][l].getY(), tab[k][l].getZ());
         }
      }
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

   private void eggsTriangles(GL2 gl2) {
      gl2.glBegin(GL_TRIANGLES);
      for (int i = 0; i < N - 1; i++)
         for (int j = 0; j < N - 1; j++) {
            gl2.glBegin(GL_TRIANGLES);
            gl2.glNormal3d(nor[i][j].getX(), nor[i][j].getY(), nor[i][j].getZ());
            gl2.glVertex3d(tab[i][j].getX(), tab[i][j].getY(), tab[i][j].getZ());
            gl2.glNormal3d(nor[i + 1][j].getX(), nor[i + 1][j].getY(), nor[i + 1][j].getZ());
            gl2.glVertex3d(tab[i + 1][j].getX(), tab[i + 1][j].getY(), tab[i + 1][j].getZ());
            gl2.glNormal3d(nor[i][j + 1].getX(), nor[i][j + 1].getY(), nor[i][j + 1].getZ());
            gl2.glVertex3d(tab[i][j + 1].getX(), tab[i][j + 1].getY(), tab[i][j + 1].getZ());
            gl2.glEnd();

            gl2.glBegin(GL_TRIANGLES);
            gl2.glNormal3d(nor[i + 1][j + 1].getX(), nor[i + 1][j + 1].getY(), nor[i + 1][j + 1].getZ());
            gl2.glTexCoord2f(0.5f, 1.0f);
            gl2.glVertex3d(tab[i + 1][j + 1].getX(), tab[i + 1][j + 1].getY(), tab[i + 1][j + 1].getZ());
            gl2.glNormal3d(nor[i + 1][j].getX(), nor[i + 1][j].getY(), nor[i + 1][j].getZ());
            gl2.glTexCoord2f(1.0f, 0.0f);
            gl2.glVertex3d(tab[i + 1][j].getX(), tab[i + 1][j].getY(), tab[i + 1][j].getZ());
            gl2.glNormal3d(nor[i][j + 1].getX(), nor[i][j + 1].getY(), nor[i][j + 1].getZ());
            gl2.glTexCoord2f(0.0f, 0.0f);
            gl2.glVertex3d(tab[i][j + 1].getX(), tab[i][j + 1].getY(), tab[i][j + 1].getZ());
            gl2.glEnd();
         }
      gl2.glEnd();
   }

   private void randCol() {
      Random random = new Random();
      for (int i = 0; i < N; i++)
         for (int j = 0; j < N; j++) {
            col[i][j] = new Point3D(
                    random.nextInt(100) / 1000f,
                    random.nextInt(100) / 1000f,
                    random.nextInt(100) / 1000f);
         }
      for (int i = 0; i < N; i++) col[i][N - 1] = col[N - i - 1][0];
   }
   private Point3D[][] calculatePoints() {
      Point3D[][] tab = new Point3D[200][200];
      float u, v;
      for (int i = 0; i < N; i++)
         for (int j = 0; j < N; j++) {
            u = (float) i / (N - 1);
            v = (float) j / (N - 1);
            tab[i][j] = new Point3D(
                    ((-90 * pow(u, 5.0f) + 225 * pow(u, 4.0f) - 270 * pow(u, 3.0f) + 180 * pow(u, 2.0f) - 45 * u) * cos(PI * v)),
                    (160 * pow(u, 4) - 320 * pow(u, 3) + 160 * pow(u, 2) - 5.0),
                    ((-90 * pow(u, 5.0f) + 225 * pow(u, 4.0f) - 270 * pow(u, 3.0f) + 180 * pow(u, 2.0f) - 45 * u) * sin(PI * v)));
         }
      return tab;
   }

   private Point3D[][] calculateNor(int length) {
      Point3D[][] tab = new Point3D[length][length];
      double u, v;
      double xu, xv, yu, yv, zu, zv;
      for (int i = 0; i < N; i++)
         for (int j = 0; j < N; j++) {
            u = (float) i / (N - 1);
            v = (float) j / (N - 1);

            xu = (-450 * pow(u, 4) + 900 * pow(u, 3) - 810 * pow(u, 2) + 360 * u - 45) * cos(PI * v);
            yu = 640 * pow(u, 3) - 960 * pow(u, 2) + 320 * u;
            zu = (-450 * pow(u, 4) + 900 * pow(u, 3) - 810 * pow(u, 2) + 360 * u - 45) * sin(PI * v);

            xv = PI * (90 * pow(u, 5) - 225 * pow(u, 4) + 270 * pow(u, 3) - 180 * pow(u, 2) + 45 * u) * sin(PI * v);
            yv = 0;
            zv = -PI * (90 * pow(u, 5) - 225 * pow(u, 4) + 270 * pow(u, 3) - 180 * pow(u, 2) + 45 * u) * cos(PI * v);
            tab[i][j] = new Point3D((yu * zv - zu * yv), (zu * xv - xu * zv), (xu * yv - yu * xv));
            double len = sqrt(pow(tab[i][j].getX(), 2) + pow(tab[i][j].getY(), 2) + pow(tab[i][j].getZ(), 2));
            tab[i][j] = new Point3D(tab[i][j].getX() / len, tab[i][j].getY() / len, tab[i][j].getZ() / len);
         }
      for (int i = N / 2; i < N; i++)
         for (int j = 0; j < N; j++)
            tab[i][j] = new Point3D(tab[i][j].getX() * -1, tab[i][j].getY() * -1, tab[i][j].getZ() * -1);
      randCol();
      return tab;
   }

   public static void main(String[] args) {
      final GLProfile profile = GLProfile.get(GLProfile.GL2);
      GLCapabilities capabilities = new GLCapabilities(profile);
      final GLCanvas glcanvas = new GLCanvas(capabilities);
      Zad2 zad2 = new Zad2();
      glcanvas.addGLEventListener(zad2);
      glcanvas.addMouseListener(zad2);
      glcanvas.addMouseMotionListener(zad2);
      glcanvas.addKeyListener(zad2);
      glcanvas.setSize(800, 800);
      final JFrame frame = new JFrame("Zad2");
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
      float mat_ambient[] = {0.1f, 0.1f, 0.1f, 1.0f};
      float mat_diffuse[] = {0.8f, 0.8f, 0.8f, 1.0f};
      float mat_specular[] = {0.8f, 0.8f, 0.8f, 1.0f};
      float mat_shininess = 50.0f;
      float light_position[] = {0.0f, 0.0f, 10.0f, 1.0f};
      float light0_ambient[] = {1.0f, 1.0f, 0.0f, 1.0f};
      float light0_diffuse[] = {1.0f, 1.0f, 0.0f, 1.0f};
      float light0_specular[] = {1.0f, 1.0f, 0.0f, 1.0f};
      float light1_ambient[] = {0.0f, 0.0f, 1.0f, 1.0f};
      float light1_diffuse[] = {0.0f, 0.0f, 1.0f, 1.0f};
      float light1_specular[] = {0.0f, 0.0f, 1.0f, 1.0f};
      float att_constant = 1.0f;
      float att_linear = 0.05f;
      float att_quadratic = 0.001f;
      gl2.glMaterialfv(GL_FRONT, GL_SPECULAR, FloatBuffer.wrap(mat_specular));
      gl2.glMaterialfv(GL_FRONT, GL_AMBIENT, FloatBuffer.wrap(mat_ambient));
      gl2.glMaterialfv(GL_FRONT, GL_DIFFUSE, FloatBuffer.wrap(mat_diffuse));
      gl2.glMaterialf(GL_FRONT, GL_SHININESS, mat_shininess);
      gl2.glLightfv(GL_LIGHT0, GL_AMBIENT, FloatBuffer.wrap(light0_ambient));
      gl2.glLightfv(GL_LIGHT0, GL_DIFFUSE, FloatBuffer.wrap(light0_diffuse));
      gl2.glLightfv(GL_LIGHT0, GL_SPECULAR, FloatBuffer.wrap(light0_specular));
      gl2.glLightfv(GL_LIGHT0, GL_POSITION, FloatBuffer.wrap(light_position));
      gl2.glLightfv(GL_LIGHT1, GL_AMBIENT, FloatBuffer.wrap(light1_ambient));
      gl2.glLightfv(GL_LIGHT1, GL_DIFFUSE, FloatBuffer.wrap(light1_diffuse));
      gl2.glLightfv(GL_LIGHT1, GL_SPECULAR, FloatBuffer.wrap(light1_specular));
      gl2.glLightfv(GL_LIGHT1, GL_POSITION, FloatBuffer.wrap(light_pos[1]));
      gl2.glLightf(GL_LIGHT0, GL_CONSTANT_ATTENUATION, att_constant);
      gl2.glLightf(GL_LIGHT0, GL_LINEAR_ATTENUATION, att_linear);
      gl2.glLightf(GL_LIGHT0, GL_QUADRATIC_ATTENUATION, att_quadratic);
      gl2.glLightf(GL_LIGHT1, GL_CONSTANT_ATTENUATION, att_constant);
      gl2.glLightf(GL_LIGHT1, GL_LINEAR_ATTENUATION, att_linear);
      gl2.glLightf(GL_LIGHT1, GL_QUADRATIC_ATTENUATION, att_quadratic);
      gl2.glShadeModel(GL_SMOOTH);
      gl2.glEnable(GL_LIGHTING);
      gl2.glEnable(GL_LIGHT0);
      gl2.glEnable(GL_LIGHT1);
      gl2.glEnable(GL_DEPTH_TEST);
      tab = calculatePoints();
      nor = calculateNor(nor.length);
   }

   @Override
   public void dispose(GLAutoDrawable drawable) {

   }
   private void spinEgg() {
      Random random = new Random();
      Point3D point3D = new Point3D((float) random.nextInt(100) / 1000f, (float) random.nextInt(100) / 1000f, (float) random.nextInt(100) / 1000f);
      theta = theta.subtract(point3D.getX(),point3D.getY(),0.0);
   }
   @Override
   public void display(GLAutoDrawable drawable) {
      GL2 gl2 = drawable.getGL().getGL2();
      GLU glu = GLU.createGLU(gl2);
      gl2.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
      gl2.glLoadIdentity();
      glu.gluLookAt(viewer.getX(), viewer.getY(), viewer.getZ(), 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
      axes(gl2);
      spinEgg();
      gl2.glRotated(theta.getX(), 1.0, 1.0, 0.0);
      gl2.glRotated(theta.getY(), 1.0, 1.0, 0.0);
      if (status == 1) {
         beta[0][0] += deltaX * pix2angle/15.0;
         beta[0][1] += deltaY * pix2angle/15.0;
      } else if (status == 2) {
         beta[1][0] += deltaX * pix2angle/15.0;
         beta[1][1] += deltaY * pix2angle/15.0;
      }
      for (int b = 0; b < 2; b++) {
         light_pos[b][0] = (float) (thetaZoom * cos(beta[b][0]) * cos(beta[b][1]));
         light_pos[b][1] = (float) (thetaZoom * sin(beta[b][1]));
         light_pos[b][2] = (float) (thetaZoom * sin(beta[b][0]) * cos(beta[b][1]));
      }
      gl2.glLightfv(GL_LIGHT0, GL_POSITION, FloatBuffer.wrap(light_pos[0]));
      gl2.glLightfv(GL_LIGHT1, GL_POSITION, FloatBuffer.wrap(light_pos[1]));

      if (model == 1) {
         eggsPoints(gl2);
      } else if (model == 2) {
         eggsMesh(gl2);
      } else {
         eggsTriangles(gl2);
      }
      //new GLUT().glutSolidTeapot(3.0);

      gl2.glFlush();
   }

   @Override
   public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
      GL2 gl2 = drawable.getGL().getGL2();
      GLU glu = GLU.createGLU(gl2);
      pix2angle = 360.0f / (float) width;
      gl2.glMatrixMode(GL_PROJECTION);
      gl2.glLoadIdentity();
      glu.gluPerspective(70.0, 1.0, 1.0, 30.0);
      if (width <= height) gl2.glViewport(0, (height - width) / 2, width, width);
      else gl2.glViewport((width - height) / 2, 0, height, height);
      gl2.glMatrixMode(GL_MODELVIEW);
      gl2.glLoadIdentity();
   }

   @Override
   public void keyTyped(KeyEvent e) {
      if (e.getKeyChar() == 'p') {
         model = 1;
      } else if (e.getKeyChar() == 'w') {
         model = 2;
      } else if (e.getKeyChar() == 's') {
         model = 3;
      }
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
      xPosOld = e.getX();
      yPosOld = e.getY();
        status = 2;
     }
   }

   @Override
   public void mouseReleased(MouseEvent e) {
      if (e.getButton() == com.jogamp.newt.event.MouseEvent.BUTTON1) {
         status = 0;
      } else if (e.getButton() == com.jogamp.newt.event.MouseEvent.BUTTON3) {
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
   }

   @Override
   public void mouseMoved(MouseEvent e) {

   }
}
