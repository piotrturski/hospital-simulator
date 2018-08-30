package net.piotrturski.edgelab.hospital;

import org.eclipse.collections.impl.factory.Sets;
import org.junit.Test;

import java.util.Collections;

import static net.piotrturski.edgelab.hospital.State.Dead;
import static net.piotrturski.edgelab.hospital.State.Diabetes;
import static net.piotrturski.edgelab.hospital.State.Healthy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class HospitalWithFlyingSpaghettiMonsterTest {

    Hospital hospital = new Hospital(
                            new RulesProvider().getRules(),
                            new FlyingSpaghettiMonster(1));

    @Test
    public void flying_spaghetti_monster_should_sometimes_resurrect_dead_ones() {
        assertThat(
                hospital.simulateSinglePatient(Dead, Sets.immutable.of()))
                .isEqualTo(Healthy);

    }

    @Test
    public void flying_spaghetti_monster_should_sometimes_resurrect_those_that_just_died() {
        assertThat(
                hospital.simulateSinglePatient(Diabetes, Sets.immutable.of()))
                .isEqualTo(Healthy);

    }


}