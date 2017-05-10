package com.lc.utils;

import org.apache.commons.codec.binary.Base64;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by yangpeng<yangpeng01@vcredit.com>.
 * Date: 2016/9/8
 */

public class ImageUtils {

//    public static void main(String args[]) throws Exception {
//        BufferedImage img1 = ImageIO.read(new File("D:/1.jpg"));
//        BufferedImage img2 = ImageIO.read(new File("D:/2.jpg"));
//        BufferedImage joinedImg = twoImagesIntoOne(img1, img2);
//        ImageIO.write(joinedImg, "jpg", new File("D:/3.jpg"));
//    }

    public static byte[] decodeImage(String imageDataString) {
        return Base64.decodeBase64(imageDataString);
    }

    public static BufferedImage twoImagesIntoOne(BufferedImage img1, BufferedImage img2) {
        int offset = 20;
        int width = Math.max(img1.getWidth(), img2.getWidth());
        int height = img1.getHeight() + img2.getHeight() + offset;
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = newImage.createGraphics();
        Color oldColor = g2.getColor();
        g2.setPaint(Color.white);
        g2.fillRect(0, 0, width, height);
        g2.setColor(oldColor);
        g2.drawImage(img1, null, 0, 0);
        g2.drawImage(img2, null, 0, img1.getHeight() + offset);
        g2.dispose();
        return newImage;
    }


}
