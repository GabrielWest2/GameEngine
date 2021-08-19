package com.gabe.GEngine.gameobject;

import com.gabe.GEngine.gameobject.components.ModelRenderer;
import com.gabe.GEngine.gameobject.components.Transform;

import java.util.ArrayList;
import java.util.List;

public abstract class Component {
    public static final  List<Class> components = new ArrayList<>();

    public static void initComponents(){
        addComponent(Transform.class);
        addComponent(ModelRenderer.class);
    }

    public static void addComponent(Class clazz){
        components.add(clazz);
    }
}
