package net.piotrturski.edgelab.hospital;

import org.eclipse.collections.api.set.ImmutableSet;

interface Treatment {

    /**
     * return null when rule doesn't apply. Optional would be cleaner but it would clutter simple rules
     *
     * ImmutableSet to achieve better expression in rules definition (due to lack of extension methods in java)
     * it's internal interface so any changes will not be a problem
     */
    State apply(State initialState, ImmutableSet<Drug> drugs);

}
