import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

import javax.swing.JFrame;

public class Zad1 implements GLEventListener {

   @Override
   public void display(GLAutoDrawable arg0) {
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
      Zad1 zad1 = new Zad1();
      glcanvas.addGLEventListener(zad1);
      glcanvas.setSize(400, 400);
      final JFrame frame = new JFrame(" Basic Frame");
      frame.getContentPane().add(glcanvas);
      frame.setSize(frame.getContentPane().getPreferredSize());
      frame.setVisible(true);
   }
}
