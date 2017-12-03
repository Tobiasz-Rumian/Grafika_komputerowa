package lab5;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TargaReader {
   public static BufferedImage getImage(String fileName) throws IOException {
      BufferedImage img = null;
      try {
         img = ImageIO.read(new File(fileName));
      } catch (IOException e) {
      }
      return img;
   }
}