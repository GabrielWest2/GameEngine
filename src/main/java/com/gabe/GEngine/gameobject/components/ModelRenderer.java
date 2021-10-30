package com.gabe.GEngine.gameobject.components;

import com.gabe.GEngine.rendering.Material;
import com.gabe.GEngine.rendering.RawModel;
import com.gabe.GEngine.gameobject.Component;

public class ModelRenderer extends Component {
    private RawModel model;
    private Material material;

    public ModelRenderer(){

    }

    public ModelRenderer(RawModel model, Material material){
        this.model = model;
        this.material = material;
    }

    public RawModel getModel() {
        return model;
    }

    public void setModel(RawModel model) {
        this.model = model;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
