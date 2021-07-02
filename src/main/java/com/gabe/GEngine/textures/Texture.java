package com.gabe.GEngine.textures;

import com.gabe.GEngine.Loader;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;


public class Texture {
    private int id;
    private int width;
    private int height;

    public Texture(int width, int height, Loader loader){
        id = loader.createTexture();
        glBindTexture(GL13.GL_TEXTURE_2D, id);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL13.GL_TEXTURE_2D, 0, GL13.GL_RGB, width, height,
                0, GL13.GL_RGB, GL13.GL_UNSIGNED_BYTE, 0);
    }

    public Texture(String fileName, Loader loader) {
        BufferedImage bi;
        try {
            File file = new File("assets/images/" + fileName + ".png");
            System.out.println("Loading image: " + file.getAbsolutePath());
            bi = ImageIO.read(file);
            width = bi.getWidth();
            height = bi.getHeight();

            int[] pixels_raw = new int[width * height * 4];
            pixels_raw = bi.getRGB(0, 0, width, height, null, 0, width);

            ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);
            for(int i = 0; i < width; i ++) {
                for(int j = 0; j < height; j ++) {
                    int pixel = pixels_raw[i * width + j];
                    pixels.put((byte) ((pixel >> 16) & 0xFF));
                    pixels.put((byte) ((pixel >> 8) & 0xFF));
                    pixels.put((byte) (pixel & 0xFF));
                    pixels.put((byte) ((pixel >> 24) & 0xFF));
                }
            }

            pixels.flip();

            id = loader.createTexture();
            glBindTexture(GL13.GL_TEXTURE_2D, id);
            GL13.glTexParameterf(GL13.GL_TEXTURE_2D, GL13.GL_TEXTURE_MIN_FILTER, GL13.GL_NEAREST);
            GL13.glTexParameterf(GL13.GL_TEXTURE_2D, GL13.GL_TEXTURE_MAG_FILTER, GL13.GL_NEAREST);

            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, 16.0f);

            glTexImage2D(GL13.GL_TEXTURE_2D, 0, GL13.GL_RGBA, width, height, 0, GL13.GL_RGBA, GL13.GL_UNSIGNED_BYTE, pixels);
        } catch (IOException e) {


            e.printStackTrace();
        }
    }

    public int getID() {
        return this.id;
    }
}
