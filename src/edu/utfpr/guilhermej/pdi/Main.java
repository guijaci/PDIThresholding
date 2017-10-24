package edu.utfpr.guilhermej.pdi;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {


    public static void main(String[] args) {
        try {
            String pathIn = "src/res/in.bmp";
            String pathOut = "src/res/out.bmp";
            int radius = 7;
            float bias = 2;
            try { pathIn = args[0];
            }catch (ArrayIndexOutOfBoundsException e){e.printStackTrace();}
            try { pathOut = args[1];
            }catch (ArrayIndexOutOfBoundsException e){e.printStackTrace();}
            try { radius = Integer.parseInt(args[2]);
            }catch (ArrayIndexOutOfBoundsException | NumberFormatException e){e.printStackTrace();}
            try { bias = Float.parseFloat(args[3]);
            }catch (ArrayIndexOutOfBoundsException | NumberFormatException e){e.printStackTrace();}

            File fileIn = new File(pathIn);
            File fileOut = new File(pathOut);
            BufferedImage imgIn = ImageIO.read(fileIn);
            BufferedImage imgOut = new BufferedImage(imgIn.getWidth(),imgIn.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

            for(int i = 0; i < imgIn.getWidth(); i++){
                for(int j = 0; j < imgIn.getHeight(); j++) {
                    int avg = getFramedAverage(i, j, radius, imgIn);
                    Color c = new Color(imgIn.getRGB(i, j));
                    int cur = getColorIntensity(c);
                    imgOut.setRGB(i, j, (avg < cur*bias ? Color.WHITE : Color.BLACK).getRGB());
                }
            }

            if (fileOut != null) {
                boolean exists = true;
                if(!fileOut.exists()) {
                    fileOut.getParentFile().mkdirs();
                    exists = fileOut.createNewFile();
                }
                if(exists)
                    ImageIO.write(imgOut, "bmp", fileOut);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getFramedAverage(int x, int y, int r, BufferedImage imgIn) {
        long s = 0;
        for(int i = x - r; i <= x + r; i++) {
            for (int j = y - r; j <= y + r; j++) {
                Color c = getColor(i, j, imgIn);
                s += getColorIntensity(c);
            }
        }
        int n = r*2+1;
        n*=n;
        return (int) s/n;
    }

    private static int getColorIntensity(Color c) {
        return (c.getRed() + c.getBlue() + c.getGreen())/3;
    }

    private static Color getColor(int x, int y, BufferedImage imgIn) {
        if(x < 0 || x >= imgIn.getWidth() || y < 0 || y >= imgIn.getHeight())
            return getColorInBorder(x, y, imgIn);
        return new Color(imgIn.getRGB(x, y));
    }

    private static Color getColorInBorder(int x, int y, BufferedImage imgIn) {
        x = x%(2*imgIn.getWidth());
        y = y%(2*imgIn.getHeight());
        if(x < 0) x += 2*imgIn.getWidth();
        if(y < 0) y += 2*imgIn.getHeight();
        if(x >= imgIn.getWidth()) x = 2*imgIn.getWidth() - x - 1;
        if(y >= imgIn.getHeight()) y = 2*imgIn.getHeight() - y - 1;
        return new Color(imgIn.getRGB(x, y));
    }
}
