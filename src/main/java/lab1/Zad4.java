package lab1;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

import javax.swing.JFrame;
import java.util.Random;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL2.GL_POLYGON;

public class Zad4 implements GLEventListener {
   private static final int DEPTH = 2;
   private static final float PERTURBATION_RATIO = 5f;
   private static final float INITIAL_SIZE = 150.0f;
   private static final float INITIAL_CORD = -INITIAL_SIZE / 2;

   private Random random = new Random();
   private boolean needToClear = true;

   private void emptyPolygon(GL2 gl, float x, float y, float side) {
      gl.glColor4f(0.5f, 1.0f, 0.83f, 1.0f);
      gl.glBegin(GL_POLYGON);
      gl.glVertex2f((x + side) / 100, (y + side) / 100);
      gl.glVertex2f((x + side) / 100, (y + 2 * side) / 100);
      gl.glVertex2f((x + 2 * side) / 100, (y + 2 * side) / 100);
      gl.glVertex2f((x + 2 * side) / 100, (y + side) / 100);
      gl.glEnd();
   }

   private byte getRandomByte() {
      return (byte) random.nextInt(255);
   }

   private float getRandomFloat(float min, float max) {
      return random.nextFloat() * (max - min) + min;
   }

   private void justDraw(GL2 gl, float x, float y, float side) {
      gl.glBegin(GL_POLYGON);
      addVertexWithColor(
              gl,
              (x + getRandomFloat(-PERTURBATION_RATIO, PERTURBATION_RATIO)) / 100,
              (y + getRandomFloat(-PERTURBATION_RATIO, PERTURBATION_RATIO)) / 100);
      addVertexWithColor(
              gl,
              (x + getRandomFloat(-PERTURBATION_RATIO, PERTURBATION_RATIO)) / 100,
              (y + side + getRandomFloat(-PERTURBATION_RATIO, PERTURBATION_RATIO)) / 100);
      addVertexWithColor(
              gl,
              (x + side + getRandomFloat(-PERTURBATION_RATIO, PERTURBATION_RATIO)) / 100,
              (y + side + getRandomFloat(-PERTURBATION_RATIO, PERTURBATION_RATIO)) / 100);
      addVertexWithColor(
              gl,
              (x + side + getRandomFloat(-PERTURBATION_RATIO, PERTURBATION_RATIO)) / 100,
              (y + getRandomFloat(-PERTURBATION_RATIO, PERTURBATION_RATIO)) / 100);
      gl.glEnd();
   }

   private void addVertexWithColor(GL2 gl, float x, float y){
      gl.glColor3ub(getRandomByte(), getRandomByte(), getRandomByte());
      gl.glVertex2f(x, y);
   }

   private void drawInitialPolygon(GL2 gl, float x, float y, float side) {
      justDraw(gl, x, y, side);
   }

   private void handlePolygon(GL2 gl, float x, float y, float side, int depth) {
      side /= 3;
      emptyPolygon(gl, x, y, side);
      justDraw(gl, x, y, side);
      justDraw(gl, x, y + side, side);
      justDraw(gl, x, y + 2 * side, side);
      justDraw(gl, x + side, y + 2 * side, side);
      justDraw(gl, x + 2 * side, y + 2 * side, side);
      justDraw(gl, x + 2 * side, y + side, side);
      justDraw(gl, x + 2 * side, y, side);
      justDraw(gl, x + side, y, side);
      depth += 1;
      if (depth <= DEPTH) {
         if (needToClear && depth == DEPTH) {
            needToClear = false;
            gl.glClear(GL_COLOR_BUFFER_BIT);
         }
         handlePolygon(gl, x, y, side, depth);
         handlePolygon(gl, x, y + side, side, depth);
         handlePolygon(gl, x, y + 2 * side, side, depth);
         handlePolygon(gl, x + side, y + 2 * side, side, depth);
         handlePolygon(gl, x + 2 * side, y + 2 * side, side, depth);
         handlePolygon(gl, x + 2 * side, y + side, side, depth);
         handlePolygon(gl, x + 2 * side, y, side, depth);
         handlePolygon(gl, x + side, y, side, depth);
      }
   }


   @Override
   public void init(GLAutoDrawable drawable) {
      drawable.getGL().glClearColor(0.5f, 1.0f, 0.83f, 1.0f);//zielony
   }

   @Override
   public void dispose(GLAutoDrawable drawable) {

   }

   @Override
   public void display(GLAutoDrawable drawable) {
      GL gl = drawable.getGL();
      GL2 gl2 = drawable.getGL().getGL2();
      gl.glClear(GL.GL_COLOR_BUFFER_BIT);
      drawInitialPolygon(gl2, INITIAL_CORD, INITIAL_CORD, INITIAL_SIZE);
      handlePolygon(gl2, INITIAL_CORD, INITIAL_CORD, INITIAL_SIZE, 0);
      gl.glFlush();
   }

   @Override
   public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
   }

   public static void main(String[] args) {
      final GLProfile profile = GLProfile.get(GLProfile.GL2);
      GLCapabilities capabilities = new GLCapabilities(profile);
      final GLCanvas glcanvas = new GLCanvas(capabilities);
      Zad4 zad4 = new Zad4();
      glcanvas.addGLEventListener(zad4);
      glcanvas.setSize(400, 400);
      final JFrame frame = new JFrame("lab1.Zad4");
      frame.getContentPane().add(glcanvas);
      frame.setSize(frame.getContentPane().getPreferredSize());
      frame.setVisible(true);
   }
}
