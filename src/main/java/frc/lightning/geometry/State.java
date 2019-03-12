package frc.lightning.geometry;

import frc.lightning.util.Interpolable;

public interface State<S> extends Interpolable<S> {
    double distance(final S other);

    boolean equals(final Object other);

    String toString();
}
