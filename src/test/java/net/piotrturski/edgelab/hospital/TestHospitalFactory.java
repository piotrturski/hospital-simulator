package net.piotrturski.edgelab.hospital;

public class TestHospitalFactory {

    static public Hospital buildHospital() {
        return new Hospital(
                new RulesProvider().getRules(),
                new FlyingSpaghettiMonster(0d));
    }
}
