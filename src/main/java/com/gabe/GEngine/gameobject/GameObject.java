package com.gabe.GEngine.gameobject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameObject {

    private String name = "GameObject";
    private List<Component> components = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void addComponents(List<Component> components) {
        this.components.addAll(components);
    }
    public void addComponents(Component... components) {
        this.components.addAll(Arrays.asList(components));
    }

    public void addComponent(Component component) {
        this.components.add(component);
    }
}
