package dev.demon.xan.base.check;

import dev.demon.xan.base.check.impl.movement.speed.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckManager {
    private static final Class[] checks = new Class[] {

            SpeedA.class,

    };

    public static List<Check> loadChecks() {
        List<Check> checklist = new ArrayList<>();
        Arrays.asList(checks).forEach(check -> {
            try {
                checklist.add((Check) check.getConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return checklist;
    }

    public static CheckInfo getCheckInfo(Check check) {
        return check.getClass().getAnnotation(CheckInfo.class);
    }
}
