package io.jeti.linalg.matrix;

import io.jeti.linalg.matrix.utils.tests.MatTest;

public class MatrixTest extends MatTest {

    @Override
    public Mat getInstance() {
        return new Matrix(1, 1, 1);
    }
}
