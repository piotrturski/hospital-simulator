package net.piotrturski.edgelab.hospital;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
class FlyingSpaghettiMonster {

    private final double actProbability;

    FlyingSpaghettiMonster(@Value("${flying-monster.act-probability}") double actProbability) {
        this.actProbability = actProbability;
    }

    public State mightShowNoodlyPower(State initial) {
        return ThreadLocalRandom.current().nextDouble() < actProbability && initial == State.Dead  // <0, 1)
            ? State.Healthy : initial;
    }

}
