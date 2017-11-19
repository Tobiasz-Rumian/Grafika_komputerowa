package lab2;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

import javax.swing.JFrame;

import static com.jogamp.opengl.GL.GL_LINES;

public class Zad2 implements GLEventListener {
   private GLU glu = new GLU();
   private GLUT glut = new GLUT();

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


   @Override
   public void init(GLAutoDrawable drawable) {
      drawable.getGL().glClearColor(0.0f, 1.0f, 1.0f, 0.0f);
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
      gl.glRotated(60.0, 0.0, 0.0, 1.0);  // Obrót o 60 stopni
      gl.glColor3f(0.5f, 0.5f, 0.5f); // Ustawienie koloru rysowania na biały
      glut.glutWireTeapot(1f); // Narysowanie obrazu czajnika do herbaty
      gl.glFlush();
   }

   @Override
   public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
      GL2 gl = drawable.getGL().getGL2();
      if (height <= 0)  height = 1;
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
      Zad2 zad2 = new Zad2();
      glcanvas.addGLEventListener(zad2);
      glcanvas.setSize(300, 300);
      final JFrame frame = new JFrame("Zad3");
      frame.getContentPane().add(glcanvas);
      frame.setSize(frame.getContentPane().getPreferredSize());
      frame.setVisible(true);
   }
}
