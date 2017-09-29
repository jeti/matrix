package io.jeti.matrix;

import io.jeti.linalg.matrix.Mat;
import io.jeti.linalg.matrix.utils.tests.MatTest;

public class MatrixTest extends MatTest {

    @Override
    public Mat getInstance() {
        return new Matrix(1, 1, 1);
    }

    @org.junit.Test
    public void asd() throws Exception {
        newInstance();
    }
}