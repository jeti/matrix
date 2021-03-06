package io.jeti.matrix;

import io.jeti.linalg.matrix.Mat;
import io.jeti.linalg.matrix.MutMat;
import io.jeti.linalg.matrix.utils.Check;
import io.jeti.matrix.Settable.List2;
import io.jeti.matrix.Settable.Settable2;
import java.util.ArrayList;
import java.util.List;

/**
 * A pure Java, immutable implementation of the {@link Mat} interface.
 */
public class MutableMatrix implements MutMat<MutableMatrix>, Settable2<Double> {

    /*
     * --------------------------------------------------
     *
     * Private Fields
     *
     * --------------------------------------------------
     */
    private final Settable2<Double> data;

    private final int               rows;
    private final int               rowFrom;
    private final int               rowStride;

    private final int               cols;
    private final int               colFrom;
    private final int               colStride;

    /*
     * --------------------------------------------------
     *
     * Constructors
     *
     * --------------------------------------------------
     */
    @Override
    public MutableMatrix newInstance(int rows, int cols, Filler filler) {
        return new MutableMatrix(rows, cols, filler);
    }

    /**
     * See {@link #newInstance(int, int, Filler)}.
     */
    public MutableMatrix(int rows, int cols, Filler filler) {
        Check.positive(rows);
        Check.positive(cols);
        List<List<Double>> tmp = new ArrayList<>(rows);
        for (int r = 0; r < rows; r++) {
            List<Double> row = new ArrayList<>(cols);
            for (int c = 0; c < rows; c++) {
                row.add(filler.apply(r, c));
            }
            tmp.add(row);
        }
        this.data = new List2<>(tmp);
        this.rows = rows;
        this.rowFrom = 0;
        this.rowStride = 1;
        this.cols = cols;
        this.colFrom = 0;
        this.colStride = 1;
    }

    /**
     * See {@link #newInstance(int)}.
     */
    public MutableMatrix(int rows) {
        this(rows, 1, (row, col) -> 0d);
    }

    /**
     * See {@link #newInstance(int, int)}.
     */
    public MutableMatrix(int rows, int cols) {
        this(rows, cols, (row, col) -> 0d);
    }

    /**
     * See {@link #newInstance(int, int, Number)}.
     */
    public MutableMatrix(int rows, int cols, final Number val) {
        this(rows, cols, (row, col) -> val.doubleValue());
    }

    /**
     * See {@link #newInstance(List)}.
     */
    public MutableMatrix(final List<List<Number>> data) {
        this(data.size(), data.get(0).size(), (row, col) -> data.get(row).get(col).doubleValue());
    }

    /**
     * See {@link #newInstance(Number[][])}.
     */
    public MutableMatrix(final Number[][] data) {
        this(data.length, data[0].length, (row, col) -> data[row][col].doubleValue());
    }

    private MutableMatrix(MutableMatrix matrix, int fromRow, int toRow, int rowStride, int fromCol,
            int toCol, int colStride) {

        /* Make sure that selection is valid. */
        this.rows = checkSelection(fromRow, toRow, rowStride, matrix.rows());
        this.cols = checkSelection(fromCol, toCol, colStride, matrix.cols());

        /* Checks indicate that the inputs are valid. */
        this.data = matrix;
        this.rowFrom = fromRow;
        this.rowStride = rowStride;
        this.colFrom = fromCol;
        this.colStride = colStride;
    }

    /**
     * @return A (rows x cols) Matrix, where all values are set to 1.
     */
    public static MutableMatrix ones(int rows, int cols) {
        return new MutableMatrix(rows, cols, (row, col) -> 1d);
    }

    /**
     * @return A (rows x cols) Matrix, where all values are set to 0.
     */
    public static MutableMatrix zeros(int rows, int cols) {
        return new MutableMatrix(rows, cols, (row, col) -> 0d);
    }

    /**
     * @return A (rows x rows) identity Matrix.
     */
    public static MutableMatrix I(int rows) {
        return new MutableMatrix(rows, rows, (row, col) -> row == col ? 1d : 0d);
    }

    /**
     * @return A (rows x cols) Matrix of uniform random number in [0,1].
     */
    public static MutableMatrix rand(int rows, int cols) {
        return new MutableMatrix(rows, cols, (row, col) -> random.nextDouble());
    }

    /**
     * @return A (rows x cols) Matrix of Gaussian random number drawn from a
     *         distribution with mean 0 and variance 1.
     */
    public static MutableMatrix randn(int rows, int cols) {
        return new MutableMatrix(rows, cols, (row, col) -> random.nextGaussian());
    }

    /*
     * --------------------------------------------------
     *
     * Getters
     *
     * --------------------------------------------------
     */
    @Override
    public final int rows() {
        return rows;
    }

    @Override
    public final int cols() {
        return cols;
    }

    @Override
    public final Double get(int row, int col) {
        int r = index(row, rowFrom, rowStride, rows());
        int c = index(col, colFrom, colStride, cols());
        return data.get(r, c);
    }

    @Override
    public MutableMatrix get(int fromRow, int toRow, int rowStride, int fromCol, int toCol,
            int colStride) {
        return new MutableMatrix(this, fromRow, toRow, rowStride, fromCol, toCol, colStride);
    }

    /*
     * --------------------------------------------------
     *
     * Setters
     *
     * --------------------------------------------------
     */
    @Override
    public void set(int row, int col, Number val) {
        set(row, col, val.doubleValue());
    }

    @Override
    public Double set(int row, int col, Double val) {
        int r = index(row, rowFrom, rowStride, rows());
        int c = index(col, colFrom, colStride, cols());
        return data.set(r, c, val);
    }

    /*
     * --------------------------------------------------
     *
     * Matrix/Matrix Operations
     *
     * --------------------------------------------------
     */
    @Override
    public final MutableMatrix times(final MutableMatrix B) {
        if (rows() == 1 && cols() == 1) {
            return B.times(get(0, 0));
        } else if (B.rows() == 1 && B.cols() == 1) {
            return times(B.get(0, 0));
        } else {
            Check.zero(cols() - B.rows());
            return newInstance(rows(), B.cols(), (row, col) -> {
                double sum = 0.0;
                for (int i = 0; i < cols(); i++) {
                    sum += get(row, i) * B.get(i, col);
                }
                return sum;
            });
        }
    }

    /*
     * --------------------------------------------------
     *
     * Other Functions
     *
     * --------------------------------------------------
     */
    @Override
    public String toString() {
        return asString();
    }

    @Override
    public int size() {
        return rows() * cols();
    }
}