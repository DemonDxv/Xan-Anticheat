package dev.demon.xan.base.check;

import dev.demon.xan.base.check.impl.combat.autoclicker.*;
import dev.demon.xan.base.check.impl.combat.killaura.*;
import dev.demon.xan.base.check.impl.combat.velocity.*;
import dev.demon.xan.base.check.impl.movement.flight.*;
import dev.demon.xan.base.check.impl.movement.speed.*;
import dev.demon.xan.base.check.impl.player.badpackets.*;
import dev.demon.xan.base.check.impl.player.timer.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckManager {
    private static final Class[] checks = new Class[] {

            //Combat
            AutoClickerA.class,

            KillauraA.class,
            KillauraB.class,
            KillauraC.class,
            KillauraD.class,
            KillauraE.class,

            VelocityA.class,
            VelocityB.class,


            //Movement
            FlightA.class,
            FlightB.class,
            FlightC.class,
            FlightD.class,
            FlightE.class,
            FlightF.class,

            SpeedA.class,



            //Player
            TimerA.class,

            BadPacketsA.class,
            BadPacketsB.class

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
