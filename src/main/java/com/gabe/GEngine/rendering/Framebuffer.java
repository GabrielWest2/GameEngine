package com.gabe.GEngine.rendering;

import com.gabe.GEngine.utilities.Loader;
import com.gabe.GEngine.rendering.display.DisplayManager;
import com.gabe.GEngine.textures.Texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class Framebuffer {
    private static int frameBufferObject = 0;
    private static Texture currentFrame;
    private static Loader texLoader;

    public static void SetupFrameBuffer(Loader loader){
        texLoader = loader;
        //Gen framebuffer
        frameBufferObject = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferObject);

        //Creates currentFrame texture
        currentFrame = new Texture(DisplayManager.getWidth(), DisplayManager.getHeight(), loader);

        //Store color data in GL_COLOR_ATTACHMENT0 of currentFrame
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, currentFrame.getID(), 0);

        //Store depth info (just in case)
        int rbo = glGenRenderbuffers();
        //Store 32b of depth data
        glBindRenderbuffer(GL_RENDERBUFFER, rbo);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32F, DisplayManager.getWidth(), DisplayManager.getHeight());
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rbo);

        //Check if framebuffer is incomplete
        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE){
            System.err.println("Framebuffer was incomplete (FBO: " + frameBufferObject+", RBO: "+rbo + ", TEXID: " + currentFrame.getID()+")");
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

   /* public static void ResizeFrameBuffer(){
        unbind();

        frameBufferObject = glGenFramebuffers();
        //Creates currentFrame texture
        currentFrame = new Texture(DisplayManager.getWidth(), DisplayManager.getHeight(), texLoader);

        //Store color data in GL_COLOR_ATTACHMENT0 of currentFrame
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, currentFrame.getID(), 0);

        //Store depth info (just in case)
        int rbo = glGenRenderbuffers();
        //Store 32b of depth data
        glBindRenderbuffer(GL_RENDERBUFFER, rbo);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32F, DisplayManager.getWidth(), DisplayManager.getHeight());
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rbo);

        //Check if framebuffer is incomplete
        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE){
            System.err.println("Framebuffer was incomplete (FBO: " + frameBufferObject+", RBO: "+rbo + ", TEXID: " + currentFrame.getID()+")");
        }

        bind();
    }*/

    public static void bind(){
        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferObject);
    }

    public static void unbind(){
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public static int getFrameBufferObject() {
        return frameBufferObject;
    }

    public static Texture getCurrentFrame() {
        return currentFrame;
    }

}
