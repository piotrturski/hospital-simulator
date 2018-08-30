package net.piotrturski.edgelab.hospital;

import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.impl.factory.Sets;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class Hospital {

    private final Collection<Treatment> treatments;
    private final FlyingSpaghettiMonster flyingSpaghettiMonster;

    public List<State> simulate(List<State> patients, Set<Drug> drugs) {

        ImmutableSet<Drug> convertedDrugs = Sets.immutable.ofAll(drugs);

        return StreamEx.of(patients)
                .map(initialState -> simulateSinglePatient(initialState, convertedDrugs))
                .toImmutableList();
    }

    State simulateSinglePatient(State initialState, ImmutableSet<Drug> drugs) {
        Set<State> states = StreamEx.of(treatments)
                .map(rule -> rule.apply(initialState, drugs))
                .nonNull()
                .toSet();


        State finalState = states.contains(State.Dead) ? State.Dead :
                states.stream().findFirst().orElse(initialState);

        return flyingSpaghettiMonster.mightShowNoodlyPower(finalState);
    }

}
