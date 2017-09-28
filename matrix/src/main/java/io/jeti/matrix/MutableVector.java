package io.jeti.linalg.matrix;

import io.jeti.linalg.matrix.Settable.Settable1;
import io.jeti.linalg.matrix.utils.Check;
import java.util.ArrayList;
import java.util.List;

/**
 * A pure Java, mutable implementation of the {@link MutVec} interface.
 */
public class MutableVector implements MutVec<MutableVector>, Settable1<Double> {

    /*
     * --------------------------------------------------
     *
     * Private Fields
     *
     * --------------------------------------------------
     */
    private final Settable1<Double> data;
    private final int               numels;
    private final int               from;
    private final int               stride;

    /*
     * --------------------------------------------------
     *
     * Constructors
     *
     * --------------------------------------------------
     */
    @Override
    public MutableVector newInstance(int elems, Filler filler) {
        return new MutableVector(elems, filler);
    }

    /**
     * See {@link #newInstance(int, Filler)}.
     */
    public MutableVector(int elems, Filler filler) {
        Check.positive(elems);
        List<Double> tmp = new ArrayList<>(elems);
        for (int i = 0; i < elems; i++) {
            tmp.add(filler.apply(i));
        }
        this.numels = elems;
        this.data = new Settable.List<>(tmp);
        this.from = 0;
        this.stride = 1;
    }

    /**
     * See {@link #newInstance(int)}.
     */
    public MutableVector(int elems) {
        this(elems, index -> 0d);
    }

    /**
     * See {@link #newInstance(int, Number)}.
     */
    public MutableVector(int elems, final Number val) {
        this(elems, index -> val.doubleValue());
    }

    /**
     * See {@link #newInstance(List)}.
     */
    public MutableVector(final List<Number> data) {
        this(data.size(), index -> data.get(index).doubleValue());
    }

    /**
     * See {@link #newInstance(Number[])}.
     */
    public MutableVector(final Number[] data) {
        this(data.length, index -> data[index].doubleValue());
    }

    /**
     * A hidden constructor which does not copy the input array. This
     * constructor is critical for constructing views of the vector.
     */
    private MutableVector(final MutableVector vec, int from, int to, int stride) {

        /* Make sure that selection is valid. */
        this.numels = checkSelection(from, to, stride, vec.size());
        this.data = vec;
        this.from = from;
        this.stride = stride;
    }

    /**
     * @return A {@link Vec} with the specified number of elements, where all of
     *         the elements are set to 1.
     */
    public static MutableVector ones(int elems) {
        return new MutableVector(elems, index -> 1d);
    }

    /**
     * @return A {@link Vec} with the specified number of elements, where all of
     *         the elements are set to 0.
     */
    public static MutableVector zeros(int elems) {
        return new MutableVector(elems, index -> 0d);
    }

    /**
     * @return A {@link Vec} with the specified number of elements, where each
     *         entry is a uniform random number in [0,1].
     */
    public static MutableVector rand(int elems) {
        return new MutableVector(elems, index -> random.nextDouble());
    }

    /**
     * @return A {@link Vec} with the specified number of elements, where each
     *         entry is a Gaussian random number drawn from a distribution with
     *         mean 0 and variance 1.
     */
    public static MutableVector randn(int elems) {
        return new MutableVector(elems, index -> random.nextGaussian());
    }

    /*
     * --------------------------------------------------
     *
     * Getters
     *
     * --------------------------------------------------
     */
    @Override
    public final int size() {
        return numels;
    }

    @Override
    public final Double get(int elem) {
        return data.get(index(elem, from, stride, size()));
    }

    @Override
    public MutableVector get(int from, int to, int stride) {
        return new MutableVector(this, from, to, stride);
    }

    /*
     * --------------------------------------------------
     *
     * Setters
     *
     * --------------------------------------------------
     */
    @Override
    public void set(int element, Number val) {
        set(element, val.doubleValue());
    }

    @Override
    public Double set(int elem, Double val) {
        return data.set(index(elem, from, stride, size()), val);
    }

    /*
     * --------------------------------------------------
     *
     * Other Functions
     *
     * --------------------------------------------------
     */
    @Override
    public final String toString() {
        return asString();
    }
}