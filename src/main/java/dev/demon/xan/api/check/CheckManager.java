package dev.demon.xan.api.check;

import dev.demon.xan.impl.checks.combat.autoclicker.*;
import dev.demon.xan.impl.checks.combat.killaura.*;
import dev.demon.xan.impl.checks.combat.reach.*;
import dev.demon.xan.impl.checks.combat.velocity.*;
import dev.demon.xan.impl.checks.movement.flight.*;
import dev.demon.xan.impl.checks.movement.speed.*;
import dev.demon.xan.impl.checks.player.badpackets.*;
import dev.demon.xan.impl.checks.player.timer.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckManager {
    private static final Class[] checks = new Class[] {

      /*      //Combat
            AutoClickerA.class,
            AutoClickerB.class,
            AutoClickerC.class,
            AutoClickerD.class,
            AutoClickerE.class,
            AutoClickerF.class,
            AutoClickerG.class,
            AutoClickerH.class,

            KillauraA.class,
            KillauraB.class,
            KillauraC.class,
            KillauraD.class,
            KillauraE.class,
            KillauraF.class,
            KillauraG.class,*/

            ReachA.class,

            VelocityA.class,
            VelocityB.class,
            VelocityC.class,
            VelocityD.class,


            //Movement,
            FlightA.class,
            FlightB.class,
            FlightC.class,
            FlightD.class,
            FlightE.class,
            FlightF.class,

            SpeedA.class,
            SpeedB.class,


            //Player
            TimerA.class,

            BadPacketsA.class,
            BadPacketsB.class,
            BadPacketsC.class,
            BadPacketsD.class,
      /*      BadPacketsE.class,
            BadPacketsF.class,
            BadPacketsG.class,
            BadPacketsH.class,
            BadPacketsI.class,
            BadPacketsJ.class,
            BadPacketsK.class,*/


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
