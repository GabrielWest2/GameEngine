package com.gabe.GEngine.gameobject.components;

import com.gabe.GEngine.gameobject.Component;

public class BoxCollider extends Component {
    private float xSize, ySize, zSize;


    public BoxCollider(){

    }
    
    public BoxCollider(float xSize, float ySize, float zSize){
        this.xSize = xSize;
    }

    public float getXSize() {
        return xSize;
    }

    public void setXSize(float xSize) {
        this.xSize = xSize;
    }

    public float getYSize() {
        return ySize;
    }

    public void setYSize(float ySize) {
        this.ySize = ySize;
    }

    public float getZSize() {
        return zSize;
    }

    public void setZSize(float zSize) {
        this.zSize = zSize;
    }
}
