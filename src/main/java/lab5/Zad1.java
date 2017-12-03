package lab5;

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

import static com.jogamp.newt.event.MouseEvent.BUTTON1;
import static com.jogamp.newt.event.MouseEvent.BUTTON3;
import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_LINES;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

public class Zad1 implements GLEventListener, KeyListener, MouseMotionListener, MouseListener {

   private final int N = 25;
   private Point3D[][] tab = new Point3D[N][N];
   private Point3D[][] col = new Point3D[N][N];
   private int model = 3;
   private static float[] viewer= {0.0f, 0.0f, 10.0f};
   private static float thetax = 0.0f;
   private static float thetay = 0.0f;
   private static float theta_zoom = 0.0f;// k?t obrotu obiektu
   private static float pix2angle;     // przelicznik pikseli na stopnie
   private static int status = 0;      // stan klawiszy myszy
   private static int xPosOld =0;       // poprzednia pozycja kursora myszy
   private static int yPosOld =0;
   private static int zoom = 0;
   private static int deltaX = 0;       // ró?nica pomi?dzy pozycj? bie??c?
   private static int deltaY = 0;                             // i poprzedni? kursora myszy
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



   public static void main(String[] args) {
      final GLProfile profile = GLProfile.get(GLProfile.GL2);
      GLCapabilities capabilities = new GLCapabilities(profile);
      final GLCanvas glcanvas = new GLCanvas(capabilities);
      Zad1 zad1 = new Zad1();
      glcanvas.addGLEventListener(zad1);
      glcanvas.addMouseListener(zad1);
      glcanvas.addMouseMotionListener(zad1);
      glcanvas.addKeyListener(zad1);
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
      drawable.getGL().getGL2().glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
   }

   @Override
   public void dispose(GLAutoDrawable drawable) {

   }

   @Override
   public void display(GLAutoDrawable drawable) {
      GL2 gl2 = drawable.getGL().getGL2();
      GLUT glut = new GLUT();
      GLU glu = GLU.createGLU(gl2);
      gl2.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
      gl2.glLoadIdentity();
      glu.gluLookAt(viewer[0],viewer[1],viewer[2], 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
      axes(gl2);
      //glRotatef(theta[0], 1.0, 0.0, 0.0);
      //glRotatef(theta[1], 0.0, 1.0, 0.0);
      //glRotatef(theta[2], 0.0, 0.0, 1.0);
      //glColor3f(1.0f, 1.0f, 1.0f); // Ustawienie koloru rysowania na bia³y
      //glRotated(60.0, 1.0, 1.0,1.0);
      if(status == 1)                    // je?li lewy klawisz myszy wci?ni?ty
      {
         thetax += deltaX * pix2angle;
         thetay += deltaY * pix2angle;

      }  else if(status ==2){
         theta_zoom += deltaZoom *pix2angle;

      }// do ró?nicy po?o?e? kursora myszy

      gl2.glRotated(thetax, 0.0, 1.0, 0.0);
      gl2.glRotated(thetay, 1.0, 0.0, 0.0);
      gl2.glRotated(theta_zoom, 0.0, 0.0, 0.0);
      gl2.glColor3f(1.0f, 1.0f, 1.0f);
// Ustawienie koloru rysowania na bia?y

      glut.glutWireTeapot(3.0);
// Narysowanie czajnika

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
      if(e.getButton() == BUTTON1) {
         xPosOld = e.getX();
         yPosOld = e.getY();
         status = 1;
      }
      else if(e.getButton() == BUTTON3){
         zoom = e.getY();
         status = 2;
      }
   }

   @Override
   public void mouseReleased(MouseEvent e) {
      status=0;
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
      deltaZoom = e.getY()-zoom;
      zoom = e.getY();
   }

   @Override
   public void mouseMoved(MouseEvent e) {

   }
}
