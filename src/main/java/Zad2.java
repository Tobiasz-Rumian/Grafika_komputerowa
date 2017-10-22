import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

import javax.swing.JFrame;

public class Zad2 implements GLEventListener {

   @Override
   public void display(GLAutoDrawable drawable) {
      final GL2 gl = drawable.getGL().getGL2();
      gl.glColor3f(0f, 1f, 0f);
      gl.glBegin(GL2.GL_POLYGON);
      gl.glVertex3f(0.75f, 0.75f, 0);
      gl.glVertex3f(-0.75f, 0.75f, 0);
      gl.glVertex3f(-0.75f, -0.75f, 0);
      gl.glVertex3f(0.75f, -0.75f, 0);
      gl.glVertex3f(0.75f, 0.75f, 0);
      gl.glEnd();
      gl.glFlush();
   }

   @Override
   public void dispose(GLAutoDrawable arg0) {
   }

   @Override
   public void init(GLAutoDrawable arg0) {
   }

   @Override
   public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
   }

   public static void main(String[] args) {
      final GLProfile profile = GLProfile.get(GLProfile.GL2);
      GLCapabilities capabilities = new GLCapabilities(profile);
      final GLCanvas glcanvas = new GLCanvas(capabilities);
      Zad2 zad2 = new Zad2();
      glcanvas.addGLEventListener(zad2);
      glcanvas.setSize(400, 400);
      final JFrame frame = new JFrame("Rhombus");
      frame.getContentPane().add(glcanvas);
      frame.setSize(frame.getContentPane().getPreferredSize());
      frame.setVisible(true);
   }
}
