package com.gabe.GEngine.rendering;

import com.gabe.GEngine.Material;
import com.gabe.GEngine.MatrixMath;
import com.gabe.GEngine.gameobject.Component;
import com.gabe.GEngine.gameobject.GameObject;
import com.gabe.GEngine.gameobject.components.ModelRenderer;
import com.gabe.GEngine.gameobject.components.Transform;
import com.gabe.GEngine.rendering.display.DisplayManager;
import com.gabe.GEngine.rendering.shaders.ShaderProgram;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.*;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {
    private float FOV = 70;
    private final float NEAR_PLANE = 0.1f;
    private final float FAR_PLANE = 1000f;
    private Matrix4f projectionMatrix;
    private boolean updateProjection = true;
    private final Camera camera;
    private HashMap<Material, List<RenderEntity>> entityBatches;

    public Renderer(Camera camera){
        this.camera = camera;
        entityBatches = new HashMap<>();
        createProjectionMatrix();
    }

    public void batchEntities(List<RenderEntity> entities){

        entityBatches = new HashMap<>();
        //Put entities into lists based on their material
        for(RenderEntity e : entities){
            Material material = e.getMaterial();
            if(entityBatches.containsKey(material)) {
                List<RenderEntity> entityList = new ArrayList<>(entityBatches.get(material));
                entityList.add(e);
                entityBatches.put(material, entityList);
            }
            else{
                entityBatches.put(material, Collections.singletonList(e));
            }
        }
    }

    public void render(List<GameObject> objects) {
        List<RenderEntity> renderEntities = new ArrayList<>();
        for(GameObject object : objects){
            Material material = null;
            RawModel model = null;
            Transform transform = null;
            for(Component component : object.getComponents()){
                if(!(component instanceof ModelRenderer || component instanceof Transform))
                    break;
                if(component instanceof ModelRenderer) {
                    ModelRenderer modelRenderer = (ModelRenderer) component;
                    material = modelRenderer.getMaterial();
                    model = modelRenderer.getModel();
                }else{
                    transform = (Transform) component;
                }
            }
            if(!(material == null || model == null || transform == null)){
                renderEntities.add(new RenderEntity(transform.getPosition(), transform.getRotation(), transform.getScale(), model, material));
            }
        }
        batchEntities(renderEntities);
        //glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        for(Material material : entityBatches.keySet()) {
            ShaderProgram shader = material.getShader();
            shader.start();
            if(updateProjection)
                shader.loadProjectionMatrix(projectionMatrix);
            shader.loadViewMatrix(camera);
            for(RenderEntity entity : entityBatches.get(material)){
                renderEntity(entity);
            }
            shader.stop();
        }
        updateProjection = false;
    }

    public void renderEntity(RenderEntity entity){
        RawModel model = entity.getModel();
        Material material = entity.getMaterial();
        ShaderProgram shader = material.getShader();
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        Matrix4f transformationMatrix = MatrixMath.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
        shader.loadColor(material.getColor());
        material.bindTexture();
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    private void createProjectionMatrix(){
        float aspectRatio = (float) DisplayManager.getWidth() / (float) DisplayManager.getHeight();
        //float aspectRatio = (float) 1920 / (float) 1080;
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustrum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.set(0, 0, x_scale);
        projectionMatrix.set(1, 1, y_scale);
        projectionMatrix.set(2, 2, -((FAR_PLANE + NEAR_PLANE) / frustrum_length));
        projectionMatrix.set(2, 3, -1);
        projectionMatrix.set(3, 2, -((2 * NEAR_PLANE * FAR_PLANE) / frustrum_length));
        projectionMatrix.set(3, 3, 0);
    }
    public void updateProjectionMatrix(){
        createProjectionMatrix();
        this.updateProjection = true;
    }
}
