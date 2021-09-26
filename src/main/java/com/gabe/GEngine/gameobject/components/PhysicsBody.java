package com.gabe.GEngine.gameobject.components;

import com.gabe.GEngine.gameobject.Component;
import org.joml.Vector3f;

public class PhysicsBody extends Component {
    private Vector3f velocity;
    private Transform transform;

//       ________
//      |        |
//      |    O___|_____
//      |____|___|    |
//           |    O   |
//           |________|
//

    private void testCollision(BoxCollider self, Transform selfOrgin, BoxCollider other, Transform otherOrgin){
        float selfMinX = selfOrgin.getPosition().x - (self.getXSize() / 2f);
        float selfMaxX = selfOrgin.getPosition().x + (self.getXSize() / 2f);
        float selfMinY = selfOrgin.getPosition().y - (self.getYSize() / 2f);
        float selfMaxY = selfOrgin.getPosition().y + (self.getYSize() / 2f);
        float selfMinZ = selfOrgin.getPosition().z - (self.getZSize() / 2f);
        float selfMaxZ = selfOrgin.getPosition().z + (self.getZSize() / 2f);

        float otherMinX = otherOrgin.getPosition().x - (other.getXSize() / 2f);
        float otherMaxX = otherOrgin.getPosition().x + (other.getXSize() / 2f);
        float otherMinY = otherOrgin.getPosition().y - (other.getYSize() / 2f);
        float otherMaxY = otherOrgin.getPosition().y + (other.getYSize() / 2f);
        float otherMinZ = otherOrgin.getPosition().z - (other.getZSize() / 2f);
        float otherMaxZ = otherOrgin.getPosition().z + (other.getZSize() / 2f);

        //if(otherMaxX > selfMinX && otherMaxX < selfMaxX)
    }

    private void updatePosition(){

    }
}

