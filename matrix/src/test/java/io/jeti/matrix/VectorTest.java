package io.jeti.linalg.matrix;

import io.jeti.linalg.matrix.utils.tests.VecTest;

public class VectorTest extends VecTest {

    @Override
    public Vec getInstance() {
        return new Vector(1);
    }
}
