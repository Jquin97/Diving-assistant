package divingcalculations;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagePanel extends JPanel {

    private BufferedImage buffer;

    public ImagePanel(String path) {
        try {
            File file =new File(path);
            buffer = ImageIO.read(file);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(buffer,0,0,getWidth(),getHeight(),null);
    }
}
