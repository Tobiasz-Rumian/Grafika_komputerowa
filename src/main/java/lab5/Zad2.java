package lab5;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import javafx.geometry.Point3D;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static com.jogamp.newt.event.MouseEvent.BUTTON1;
import static com.jogamp.newt.event.MouseEvent.BUTTON3;
import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_CULL_FACE;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_FRONT;
import static com.jogamp.opengl.GL.GL_LINEAR;
import static com.jogamp.opengl.GL.GL_LINES;
import static com.jogamp.opengl.GL.GL_TEXTURE_2D;
import static com.jogamp.opengl.GL.GL_TEXTURE_MAG_FILTER;
import static com.jogamp.opengl.GL.GL_TEXTURE_MIN_FILTER;
import static com.jogamp.opengl.GL.GL_TRIANGLES;
import static com.jogamp.opengl.GL.GL_TRIANGLE_FAN;
import static com.jogamp.opengl.GL.GL_UNSIGNED_BYTE;
import static com.jogamp.opengl.GL2ES1.GL_MODULATE;
import static com.jogamp.opengl.GL2ES1.GL_TEXTURE_ENV;
import static com.jogamp.opengl.GL2ES1.GL_TEXTURE_ENV_MODE;
import static com.jogamp.opengl.GL2ES3.GL_QUADS;
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
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class Zad2 implements GLEventListener, KeyListener, MouseMotionListener, MouseListener {
   private final int N = 25;
   private Point3D[][] tab = new Point3D[N][N];
   private Point3D[][] nor = new Point3D[N][N];
   private int which = 1;
   private int kk = 6;
   private static float viewer[] = {0.0f, 0.0f, 10.0f};
   private static float p = 1.0f;
   private static float thetax = 0.0f;
   private static float thetay = 0.0f;
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

   private void eggsTriangles(GL2 gl2) {
      float div = N * 1.0f;
      for (int i = 0; i < N - 1; i++) {
         for (int j = 0; j < N - 1; j++) {
            gl2.glBegin(GL_TRIANGLES);
            gl2.glNormal3d(nor[i][j].getX(), nor[i][j].getY(), nor[i][j].getZ());
            gl2.glTexCoord2f(i / div, j / div);
            gl2.glVertex3d(tab[i][j].getX(), tab[i][j].getY(), tab[i][j].getZ());
            gl2.glNormal3d(nor[i + 1][j].getX(), nor[i + 1][j].getY(), nor[i + 1][j].getZ());
            gl2.glTexCoord2f((i + 1) / div, j / div);
            gl2.glVertex3d(tab[i + 1][j].getX(), tab[i + 1][j].getY(), tab[i + 1][j].getZ());
            gl2.glNormal3d(nor[i][j + 1].getX(), nor[i][j + 1].getY(), nor[i][j + 1].getZ());
            gl2.glTexCoord2f(i / div, (j + 1) / div);
            gl2.glVertex3d(tab[i][j + 1].getX(), tab[i][j + 1].getY(), tab[i][j + 1].getZ());
            gl2.glEnd();
            gl2.glBegin(GL_TRIANGLES);
            gl2.glNormal3d(nor[i + 1][j + 1].getX(), nor[i + 1][j + 1].getY(), nor[i + 1][j + 1].getZ());
            gl2.glTexCoord2f((i + 1) / div, (j + 1) / div);
            gl2.glVertex3d(tab[i + 1][j + 1].getX(), tab[i + 1][j + 1].getY(), tab[i + 1][j + 1].getZ());
            gl2.glNormal3d(nor[i + 1][j].getX(), nor[i + 1][j].getY(), nor[i + 1][j].getZ());
            gl2.glTexCoord2f((i + 1) / div, j / div);
            gl2.glVertex3d(tab[i + 1][j].getX(), tab[i + 1][j].getY(), tab[i + 1][j].getZ());
            gl2.glNormal3d(nor[i][j + 1].getX(), nor[i][j + 1].getY(), nor[i][j + 1].getZ());
            gl2.glTexCoord2f(i / div, (j + 1) / div);
            gl2.glVertex3d(tab[i][j + 1].getX(), tab[i][j + 1].getY(), tab[i][j + 1].getZ());
            gl2.glEnd();
         }
      }


   }

   private Point3D[][] calculatePoints() {
      Point3D[][] tab = new Point3D[200][200];
      float u, v;
      for (int i = 0; i < N; i++) {
         for (int j = 0; j < N; j++) {
            u = (float) i / (N - 1);
            v = (float) j / (N - 1);
            tab[i][j] = new Point3D(
                    ((-90 * pow(u, 5.0f) + 225 * pow(u, 4.0f) - 270 * pow(u, 3.0f) + 180 * pow(u, 2.0f) - 45 * u) * cos(PI * v)),
                    (160 * pow(u, 4) - 320 * pow(u, 3) + 160 * pow(u, 2) - 5.0),
                    ((-90 * pow(u, 5.0f) + 225 * pow(u, 4.0f) - 270 * pow(u, 3.0f) + 180 * pow(u, 2.0f) - 45 * u) * sin(PI * v)));
         }
      }
      return tab;
   }

   private Point3D[][] calculateNor(int length) {
      Point3D[][] tab = new Point3D[length][length];
      double u, v;
      double xu, xv, yu, yv, zu, zv;
      for (int i = 0; i < N; i++) {
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
      }
      for (int i = N / 2; i < N; i++) {
         for (int j = 0; j < N; j++) {
            tab[i][j] = new Point3D(tab[i][j].getX() * -1, tab[i][j].getY() * -1, tab[i][j].getZ() * -1);
         }
      }
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

      tab = calculatePoints();
      nor = calculateNor(nor.length);
      try {
         BufferedImage image = ImageIO.read(new File("C:\\Users\\zekori96\\Documents\\Grafika1\\src\\main\\resources\\tekstury\\P6_t.tga"));
         gl2.glTexImage2D(
                 GL.GL_TEXTURE_2D,
                 0,
                 GL.GL_RGB,
                 image.getWidth(),
                 image.getHeight(),
                 0,
                 GL2.GL_RGB,
                 GL_UNSIGNED_BYTE,
                 ByteBuffer.wrap(((DataBufferByte) image.getRaster().getDataBuffer()).getData()));

      } catch (IOException e) {
         e.printStackTrace();
      }
      gl2.glEnable(GL_TEXTURE_2D);
      gl2.glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
      gl2.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
      gl2.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
      gl2.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
      float mat_ambient[] = {1.0f, 1.0f, 1.0f, 1.0f};
      float mat_diffuse[] = {1.0f, 1.0f, 1.0f, 1.0f};
      float mat_specular[] = {1.0f, 1.0f, 1.0f, 1.0f};
      float mat_shininess = 50.0f;
      float light_position[] = {0.0f, 0.0f, 10.0f, 1.0f};
      float light_ambient[] = {1.0f, 1.0f, 1.0f, 1.0f};
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
      if (which == 2) gl2.glEnable(GL_CULL_FACE);
      if (which == 1) gl2.glDisable(GL_CULL_FACE);
      gl2.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
      gl2.glLoadIdentity();
      glu.gluLookAt(viewer[0], viewer[1], viewer[2], 0.0, 0.0, 0.0, 0.0, p, 0.0);
      axes(gl2);
      if (which == 1) {
         if (status == 1) {
            thetax += deltaX * pix2angle / 30.0;
            thetay += deltaY * pix2angle / 30.0;
         } else
            if (status == 2) thetaZoom += deltaZoom / 10.0;
         if (thetay > Math.PI) thetay -= 2 * Math.PI;
         else
            if (thetay <= -Math.PI) thetay += 2 * Math.PI;
         if (thetay > Math.PI / 2 || thetay < -Math.PI / 2) p = -1.0f;
         else p = 1.0f;
         viewer[0] = (float) (thetaZoom * cos(thetax) * cos(thetay));
         viewer[1] = (float) (thetaZoom * sin(thetay));
         viewer[2] = (float) (thetaZoom * sin(thetax) * cos(thetay));
         eggsTriangles(gl2);
      } else {
         if (status == 1) {
            thetax += deltaX * pix2angle / 30.0;
            thetay += deltaY * pix2angle / 30.0;
         } else
            if (status == 2) thetaZoom += deltaZoom / 10.0;
         if (thetay > Math.PI) thetay -= 2 * Math.PI;
         else
            if (thetay <= -Math.PI) thetay += 2 * Math.PI;
         if (thetay > Math.PI / 2 || thetay < -Math.PI / 2) p = -1.0f;
         else p = 1.0f;

         viewer[0] = (float) (thetaZoom * cos(thetax) * cos(thetay));
         viewer[1] = (float) (thetaZoom * sin(thetay));
         viewer[2] = (float) (thetaZoom * sin(thetax) * cos(thetay));

         if (kk == 0) {
            gl2.glBegin(GL_QUADS);                              // draw square
            gl2.glTexCoord2f(0.0f, 0.0f);            // set color to green
            gl2.glVertex3f(-5.0f, -5.0f, 5.0f);
            gl2.glTexCoord2f(0.0f, 1.0f);               // set color to white
            gl2.glVertex3f(-5.0f, -5.0f, -5.0f);
            gl2.glTexCoord2f(1.0f, 1.0f);            // set color to blue
            gl2.glVertex3f(5.0f, -5.0f, -5.0f);
            gl2.glTexCoord2f(1.0f, 0.0f);            // set color to yellow
            gl2.glVertex3f(5.0f, -5.0f, 5.0f);
            gl2.glEnd();
         }

         if (kk == 1) {
            gl2.glBegin(GL_TRIANGLES);      // draw triangle
            gl2.glTexCoord2f(0.0f, 0.0f);
            gl2.glVertex3f(0.0f, 3.0f, 0.0f);
            gl2.glTexCoord2f(1.0f, 0.0f);
            gl2.glVertex3f(-5.0f, -5.0f, 5.0f);
            gl2.glTexCoord2f(0.0f, 1.0f);
            gl2.glVertex3f(5.0f, -5.0f, 5.0f);
            gl2.glEnd();
         }

         if (kk == 2) {
            gl2.glBegin(GL_TRIANGLES);
            gl2.glTexCoord2f(0.0f, 0.0f);
            gl2.glVertex3f(0.0f, 3.0f, 0.0f);
            gl2.glTexCoord2f(1.0f, 0.0f);
            gl2.glVertex3f(5.0f, -5.0f, 5.0f);
            gl2.glTexCoord2f(1.0f, 1.0f);
            gl2.glVertex3f(5.0f, -5.0f, -5.0f);
            gl2.glEnd();
         }

         if (kk == 3) {
            gl2.glBegin(GL_TRIANGLES);
            gl2.glTexCoord2f(0.0f, 0.0f);
            gl2.glVertex3f(0.0f, 3.0f, 0.0f);
            gl2.glTexCoord2f(1.0f, 0.0f);
            gl2.glVertex3f(-5.0f, -5.0f, -5.0f);
            gl2.glTexCoord2f(1.0f, 1.0f);
            gl2.glVertex3f(-5.0f, -5.0f, 5.0f);
            gl2.glEnd();
         }

         if (kk == 4) {
            gl2.glBegin(GL_TRIANGLES);
            gl2.glTexCoord2f(0.0f, 0.0f);
            gl2.glVertex3f(0.0f, 3.0f, 0.0f);
            gl2.glTexCoord2f(1.0f, 0.0f);
            gl2.glVertex3f(-5.0f, -5.0f, -5.0f);
            gl2.glTexCoord2f(1.0f, 1.0f);
            gl2.glVertex3f(-5.0f, -5.0f, 5.0f);
            gl2.glEnd();
         }

         if (kk == 5) {
            gl2.glBegin(GL_TRIANGLES);
            gl2.glTexCoord2f(0.0f, 0.0f);
            gl2.glVertex3f(0.0f, 3.0f, 0.0f);
            gl2.glTexCoord2f(1.0f, 0.0f);
            gl2.glVertex3f(-5.0f, -5.0f, 5.0f);
            gl2.glTexCoord2f(1.0f, 1.0f);
            gl2.glVertex3f(-5.0f, -5.0f, 5.0f);
            gl2.glEnd();
         }


         if (kk == 6) {
            gl2.glBegin(GL_TRIANGLE_FAN);      // draw triangle
            gl2.glTexCoord2f(0.0f, 0.0f);
            gl2.glVertex3f(0.0f, 3.0f, 0.0f);
            gl2.glTexCoord2f(1.0f, 0.0f);
            gl2.glVertex3f(-5.0f, -5.0f, 5.0f);
            gl2.glTexCoord2f(0.0f, 1.0f);
            gl2.glVertex3f(5.0f, -5.0f, 5.0f);
            gl2.glTexCoord2f(1.0f, 1.0f);
            gl2.glVertex3f(5.0f, -5.0f, -5.0f);
            gl2.glTexCoord2f(1.0f, 0.0f);
            gl2.glVertex3f(-5.0f, -5.0f, -5.0f);
            gl2.glTexCoord2f(0.0f, 1.0f);
            gl2.glVertex3f(-5.0f, -5.0f, 5.0f);
            gl2.glEnd();
            gl2.glBegin(GL_QUADS);                              // draw square
            gl2.glTexCoord2f(0.0f, 0.0f);            // set color to green
            gl2.glVertex3f(-5.0f, -5.0f, 5.0f);
            gl2.glTexCoord2f(0.0f, 1.0f);               // set color to white
            gl2.glVertex3f(-5.0f, -5.0f, -5.0f);
            gl2.glTexCoord2f(1.0f, 1.0f);            // set color to blue
            gl2.glVertex3f(5.0f, -5.0f, -5.0f);
            gl2.glTexCoord2f(1.0f, 0.0f);            // set color to yellow
            gl2.glVertex3f(5.0f, -5.0f, 5.0f);
            gl2.glEnd();
         }
      }
      gl2.glFlush();
   }

   @Override
   public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
      GL2 gl2 = drawable.getGL().getGL2();
      GLU glu = GLU.createGLU(gl2);
      pix2angle = 360.0f / (float) width;
      gl2.glMatrixMode(GL_PROJECTION);
      gl2.glLoadIdentity();
      glu.gluPerspective(100.0, 1.0, 1.0, 30.0);
      if (width <= height) gl2.glViewport(0, (height - width) / 2, width, width);
      else gl2.glViewport((width - height) / 2, 0, height, height);
      gl2.glMatrixMode(GL_MODELVIEW);
      gl2.glLoadIdentity();
   }

   @Override
   public void keyTyped(KeyEvent e) {
      if (e.getKeyChar() == 'z') kk = 0;
      if (e.getKeyChar() == 'x') kk = 1;
      if (e.getKeyChar() == 'c') kk = 2;
      if (e.getKeyChar() == 'v') kk = 3;
      if (e.getKeyChar() == 'b') kk = 4;
      if (e.getKeyChar() == 'n') kk = 5;
      if (e.getKeyChar() == 'm') kk = 6;
      if (e.getKeyChar() == 'q') which = 1;
      if (e.getKeyChar() == 'w') which = 2;
      System.out.println(e.getKeyChar());
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
      if (e.getButton() == BUTTON1) {
         xPosOld = e.getX();
         yPosOld = e.getY();
         status = 1;
      } else
         if (e.getButton() == BUTTON3) {
            zoom = e.getY();
            status = 2;
         }
   }

   @Override
   public void mouseReleased(MouseEvent e) {
      status = 0;
   }

   @Override
   public void mouseEntered(MouseEvent e) {

   }

   @Override
   public void mouseExited(MouseEvent e) {

   }

   @Override
   public void mouseDragged(MouseEvent e) {
      deltaX = e.getX() - xPosOld;    // obliczenie ró?nicy po?o?enia kursora myszy
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
