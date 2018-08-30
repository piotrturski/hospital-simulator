package net.piotrturski.edgelab.hospital;

import com.google.common.collect.Lists;
import one.util.streamex.StreamEx;
import org.assertj.core.api.AbstractComparableAssert;
import org.eclipse.collections.impl.factory.Sets;
import org.junit.Test;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static net.piotrturski.edgelab.hospital.Drug.*;
import static net.piotrturski.edgelab.hospital.State.*;
import static net.piotrturski.edgelab.hospital.TestHospitalFactory.buildHospital;
import static org.assertj.core.api.Assertions.assertThat;

public class HospitalTest {

    Hospital hospital = buildHospital();

    @Test
    public void aspirin_should_cure_fever() {

        assertThat_state_after_treatment(Fever, Aspirin).isEqualTo(Healthy);
    }

    @Test
    public void antibiotic_should_cure_tuberculosis() {
        
        assertThat_state_after_treatment(Tuberculosis, Antibiotic).isEqualTo(Healthy);
    }

    @Test
    public void sick_without_proper_treatment_should_remain_sick() {

        assertThat_state_after_treatment(Tuberculosis, Aspirin).isEqualTo(Tuberculosis);
        assertThat_state_after_treatment(Tuberculosis).isEqualTo(Tuberculosis);

    }

    @Test
    public void insulin_should_prevent_diabetic_from_dying() {

        assertThat_state_after_treatment(Diabetes, Insulin).isEqualTo(Diabetes);
        assertThat_state_after_treatment(Diabetes, Aspirin).isEqualTo(Dead);
        assertThat_state_after_treatment(Diabetes).isEqualTo(Dead);
    }

    @Test
    public void insulin_mixed_with_antibiotic_should_give_fever_to_healthy_people() {

        assertThat_state_after_treatment(Healthy, Insulin, Antibiotic).isEqualTo(Fever);
    }

    @Test
    public void paracetamol_should_cure_fever() {

        assertThat_state_after_treatment(Fever, Paracetamol).isEqualTo(Healthy);
    }

    @Test
    public void paracetamol_mixed_with_aspirin_should_kill() {

        StreamEx.of(State.values()).forEach(
            state -> assertThat_state_after_treatment(state, Paracetamol, Aspirin).isEqualTo(Dead)
        );
    }

    @Test
    public void patient_should_change_state_only_once() {
        assertThat_state_after_treatment(Tuberculosis, Antibiotic,Insulin).isEqualTo(Healthy);
    }

    @Test
    public void rules_causing_death_should_take_precedence() {
        assertThat_state_after_treatment(Fever, Paracetamol, Aspirin).isEqualTo(Dead);
        assertThat_state_after_treatment(Diabetes, Insulin, Paracetamol, Aspirin).isEqualTo(Dead);
    }


    @Test
    public void should_apply_same_treatment_to_each_patient() {
        assertThat(
            hospital.simulate(newArrayList(Fever, Diabetes), newHashSet(Paracetamol)))
                .containsExactly(Healthy, Dead);
    }

    private AbstractComparableAssert<?, State> assertThat_state_after_treatment(State state, Drug... drugs) {
        State finalState = hospital.simulateSinglePatient(state, Sets.immutable.of(drugs));
        return assertThat(finalState);
    }
}