package lab2;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.sun.javafx.geom.Vec3f;

import javax.swing.JFrame;

import static com.jogamp.opengl.GL.GL_LINES;
import static com.jogamp.opengl.GL.GL_POINTS;

public class Zad3 implements GLEventListener {
   private GLU glu = new GLU();
   private static final int DIVISION = 10;
   private Vec3f[][] matrix = new Vec3f[DIVISION][DIVISION];
   private int model = 1;


   private void Axes(GLAutoDrawable drawable) {
      GL2 gl2 = drawable.getGL().getGL2();
      gl2.glColor3f(1.0f, 0.0f, 0.0f);  // kolor rysowania osi - czerwony
      gl2.glBegin(GL_LINES); // rysowanie osi x
      gl2.glVertex3f(-0.5f, 0.0f, 0.0f);
      gl2.glVertex3f(0.5f, 0.0f, 0.0f);
      gl2.glEnd();
      gl2.glColor3f(0.0f, 1.0f, 0.0f);  // kolor rysowania - zielony
      gl2.glBegin(GL_LINES);  // rysowanie osi y
      gl2.glVertex3f(0.0f, -0.5f, 0.0f);
      gl2.glVertex3f(0.0f, 0.5f, 0.0f);
      gl2.glEnd();
      gl2.glColor3f(0.0f, 0.0f, 1.0f);  // kolor rysowania - niebieski
      gl2.glBegin(GL_LINES); // rysowanie osi z
      gl2.glVertex3f(0.0f, 0.0f, -0.5f);
      gl2.glVertex3f(0.0f, 0.0f, 0.5f);
      gl2.glEnd();
   }


   private void createParametricField() {
      int x = 0, y = 0;
      for (double i = -1; i <= 1; i += 2d / (DIVISION - 1)) {
         for (double j = -1; j <= 1; j += 2d / (DIVISION - 1)) {
            matrix[x][y] = new Vec3f(calculateX(i, j), calculateY(i, j), calculateZ(i, j));
            y++;
         }
         x++;
         y = 0;
      }
      x = 0;
   }

   private float calculateX(double u, double v) {
      return (float) ((-90d * Math.pow(u, 5) + 225d * Math.pow(u, 4) - 270d * Math.pow(u, 3) + 180d * Math.pow(u, 2) - 45d * u) * Math.cos(2 * Math.PI * v));
   }

   private float calculateY(double u, double v) {
      return (float) (160d * Math.pow(u, 4) - 320d * Math.pow(u, 3) + 160d * Math.pow(u, 2));
   }

   private float calculateZ(double u, double v) {
      return (float) ((-90d * Math.pow(u, 5) + 225d * Math.pow(u, 4) - 270d * Math.pow(u, 3) + 180d * Math.pow(u, 2) - 45d * u) * Math.sin(2 * Math.PI * v));
   }

   @Override
   public void init(GLAutoDrawable drawable) {
      drawable.getGL().glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
      createParametricField();
   }

   @Override
   public void dispose(GLAutoDrawable drawable) {

   }

   @Override
   public void display(GLAutoDrawable drawable) {
      final GL2 gl = drawable.getGL().getGL2();
      gl.glClear(GL.GL_COLOR_BUFFER_BIT);
      gl.glTranslatef(0f, 0f, -2.5f);
      Axes(drawable);
      egg(gl);
      gl.glFlush();
   }

   void keys(char key, int x, int y)
   {
      if(key == 'p') model = 1;
      if(key == 'w') model = 2;
      if(key == 's') model = 3;

       // przerysowanie obrazu sceny
   }


   private void egg(GL2 gl){
      gl.glColor3f(0.0f, 1.0f, 0.0f); // Ustawienie koloru rysowania na biały
      gl.glBegin(GL_POINTS); // rysowanie osi x
      for (Vec3f vert[] : matrix) {
         for (Vec3f v : vert) {
            gl.glVertex3f(v.x / 500, v.y / 500, v.z / 500);
         }
      }
      gl.glEnd();
   }


   @Override
   public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
      GL2 gl = drawable.getGL().getGL2();
      if (height <= 0) {
         height = 1;
      }
      final float h = (float) width / (float) height;
      gl.glViewport(0, 0, width, height);
      gl.glMatrixMode(GL2.GL_PROJECTION);
      gl.glLoadIdentity();
      glu.gluPerspective(45.0f, h, 1.0, 20.0);
      gl.glMatrixMode(GL2.GL_MODELVIEW);
      gl.glLoadIdentity();
   }

   public static void main(String[] args) {
      final GLProfile profile = GLProfile.get(GLProfile.GL2);
      GLCapabilities capabilities = new GLCapabilities(profile);
      final GLCanvas glcanvas = new GLCanvas(capabilities);
      Zad3 zad3 = new Zad3();
      glcanvas.addGLEventListener(zad3);
      glcanvas.setSize(300, 300);
      final JFrame frame = new JFrame("Zad2");
      frame.getContentPane().add(glcanvas);
      frame.setSize(frame.getContentPane().getPreferredSize());
      frame.setVisible(true);
   }
}
