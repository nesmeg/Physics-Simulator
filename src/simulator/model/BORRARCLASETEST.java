package simulator.model;

import simulator.model.Body;
import simulator.misc.Vector2D;
import simulator.model.*;

/*
************************** BORRAR LA CLASE ENTERA *******************************************
*/

public class BORRARCLASETEST {
    public static void main(String[] args) {
        PhysicsSimulator sim1 = new PhysicsSimulator(1.5, new NoForce());
        Body b1 = new Body("1", new Vector2D(4, 7), new Vector2D(5, 6), new Vector2D(3.5, 5), 3.45);
        sim1.addBody(b1);
        System.out.println(sim1.toString());
    }
}

//NOOOOOOOOOOOOOOOOOOOO ENTREGARRRRRRRRRRRRRRRRRRRRRRRRRR
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!