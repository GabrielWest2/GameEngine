package com.gabe.GEngine.gameobject;

import com.gabe.GEngine.gameobject.components.Transform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameObject {

    private String name = "GameObject";
    private GameObject parent = null;

    public GameObject(){
        this.components.add(new Transform());
    }

    public GameObject(String name, Component... components){
        this.components.add(new Transform());
        this.name = name;
        this.components.addAll(Arrays.asList(components));
    }

    private List<Component> components = new ArrayList<>();

    public String getName() {
        return name;
    }

    public GameObject setName(String name) {
        this.name = name;
        return this;
    }

    public List<Component> getComponents() {
        return components;
    }

    public GameObject addComponents(List<Component> components) {
        this.components.addAll(components);
        return this;
    }
    public GameObject addComponents(Component... components) {
        this.components.addAll(Arrays.asList(components));
        return this;
    }

    public GameObject addComponent(Component component) {
        this.components.add(component);
        return this;
    }

    public GameObject getParent() {
        return parent;
    }

    public GameObject setParent(GameObject parent) {
        this.parent = parent;
        return this;
    }
}
