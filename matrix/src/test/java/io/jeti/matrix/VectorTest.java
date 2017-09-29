package io.jeti.matrix;

import io.jeti.linalg.matrix.Vec;
import io.jeti.linalg.matrix.utils.tests.VecTest;

public class VectorTest extends VecTest {

    @Override
    public Vec getInstance() {
        return new Vector(1);
    }
}
