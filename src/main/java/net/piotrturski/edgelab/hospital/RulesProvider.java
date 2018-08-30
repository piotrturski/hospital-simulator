package net.piotrturski.edgelab.hospital;

import one.util.streamex.StreamEx;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.List;

import static net.piotrturski.edgelab.hospital.Drug.*;
import static net.piotrturski.edgelab.hospital.State.*;

/**
 * static rule provider. If number of rules grows, we can distribute rules definition and collect
 * them all here using spring's collections autowiring
 */
@Configuration
class RulesProvider {

    private final List<Treatment> treatmentRules = StreamEx.<Treatment>of(

        // (state, drugs) -> ...
            (s, d) -> s == Fever && d.contains(Aspirin) ? Healthy : null,
            (s, d) -> s == Tuberculosis && d.contains(Antibiotic)? Healthy : null,
            (s, d) -> s == Diabetes ? (d.contains(Insulin) ? Diabetes : Dead) : null,
            (s, d) -> s == Fever && d.contains(Paracetamol) ? Healthy : null,

            (s, d) -> s == Healthy && d.containsAllArguments(Insulin, Antibiotic) ? Fever : null,

            (s, d) -> s != Dead && d.containsAllArguments(Paracetamol, Aspirin) ? Dead : null

        ).toImmutableList();

    @Bean
    public Collection<Treatment> getRules() {
        return treatmentRules;
    }

}
