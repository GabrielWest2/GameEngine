package com.gabe.GEngine.gameobject.components;

import com.gabe.GEngine.Material;
import com.gabe.GEngine.RawModel;
import com.gabe.GEngine.gameobject.Component;
import com.gabe.GEngine.rendering.RenderEntity;

public class ModelRenderer extends Component {
    private RawModel model;
    private Material material;

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
