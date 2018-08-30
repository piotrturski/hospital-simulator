package net.piotrturski.edgelab.view;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import net.piotrturski.edgelab.hospital.Drug;
import net.piotrturski.edgelab.hospital.Hospital;
import net.piotrturski.edgelab.hospital.State;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.collections.impl.tuple.Tuples;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static net.piotrturski.edgelab.hospital.Drug.*;
import static net.piotrturski.edgelab.hospital.State.*;
import static net.piotrturski.edgelab.hospital.State.Dead;

@RequiredArgsConstructor
@Service
public class ConsoleController {

    private final Hospital hospital;

    public String processInput(String... args) {

        final String errMsg = "required 2 parameters: state list and drug list";
        Preconditions.checkArgument(args.length <= 2, errMsg);

        return StreamEx.of(args).append("").limit(2)
                .pairMap(
                        (states, drugs) -> Tuples.pair(parseStates(states), parseDrugs(drugs))
                )
                .filter(pair -> pair.getOne().isPresent() && pair.getTwo().isPresent())
                .findFirst()
                .map(pair -> Tuples.pair(pair.getOne().get(), pair.getTwo().get()))
                .map(pair -> hospital.simulate(pair.getOne(), pair.getTwo()))
                .map(this::formatOutput)
                .orElseThrow(() -> new IllegalArgumentException(errMsg));
    }

    private final List<String> statesPresentationOrder = StreamEx.of("F","H", "D", "T","X").toImmutableList();
    private final Map<String, State> stateRepresentation = EntryStream.zip(
            statesPresentationOrder, newArrayList(Fever,Healthy,Diabetes,Tuberculosis,Dead)).toImmutableMap();

    String formatOutput(List<State> patients) {
        Map<State, Long> countByState = StreamEx.of(patients).groupingBy(Function.identity(), Collectors.counting());

        return StreamEx.of(statesPresentationOrder)
                .map(str -> str+":"+countByState.getOrDefault(stateRepresentation.get(str), 0L))
                .joining(",");
    }

    Optional<List<State>> parseStates(String states) {
        return split(states)
                .map(stateRepresentation::get)
                .toListAndThen(parsed -> parsed.contains(null) ? Optional.empty() : Optional.of(parsed));
    }

    Optional<Set<Drug>>  parseDrugs(String drugs) {
        return split(drugs)
                .map(str -> {switch (str) {
                    case "As": return Aspirin;
                    case "An": return Antibiotic;
                    case "I": return Insulin;
                    case "P": return Paracetamol;
                    default: return null; // error
                }})
                .toSetAndThen(parsed -> parsed.contains(null) ? Optional.empty() : Optional.of(parsed));
    }

    private StreamEx<String> split(String toSplit) {
        return StringUtils.isEmpty(toSplit) ? StreamEx.empty() : StreamEx.split(toSplit, ',', false);
    }
}
