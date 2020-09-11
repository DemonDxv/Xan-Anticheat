package dev.demon.xan.utils.math;

import com.google.common.util.concurrent.AtomicDouble;
import dev.demon.xan.Xan;
import dev.demon.xan.base.user.User;
import dev.demon.xan.utils.box.BoundingBox;
import dev.demon.xan.utils.location.CustomLocation;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class MathUtil {

    public static Map<EntityType, Vector> entityDimensions;

    public MathUtil() {
        entityDimensions = new HashMap<>();
        entityDimensions.put(EntityType.WOLF, new Vector(0.31, 0.8, 0.31));
        entityDimensions.put(EntityType.SHEEP, new Vector(0.45, 1.3, 0.45));
        entityDimensions.put(EntityType.COW, new Vector(0.45, 1.3, 0.45));
        entityDimensions.put(EntityType.PIG, new Vector(0.45, 0.9, 0.45));
        entityDimensions.put(EntityType.MUSHROOM_COW, new Vector(0.45, 1.3, 0.45));
        entityDimensions.put(EntityType.WITCH, new Vector(0.31, 1.95, 0.31));
        entityDimensions.put(EntityType.BLAZE, new Vector(0.31, 1.8, 0.31));
        entityDimensions.put(EntityType.PLAYER, new Vector(0.3, 1.8, 0.3));
        entityDimensions.put(EntityType.VILLAGER, new Vector(0.31, 1.8, 0.31));
        entityDimensions.put(EntityType.CREEPER, new Vector(0.31, 1.8, 0.31));
        entityDimensions.put(EntityType.GIANT, new Vector(1.8, 10.8, 1.8));
        entityDimensions.put(EntityType.SKELETON, new Vector(0.31, 1.8, 0.31));
        entityDimensions.put(EntityType.ZOMBIE, new Vector(0.31, 1.8, 0.31));
        entityDimensions.put(EntityType.SNOWMAN, new Vector(0.35, 1.9, 0.35));
        entityDimensions.put(EntityType.HORSE, new Vector(0.7, 1.6, 0.7));
        entityDimensions.put(EntityType.ENDER_DRAGON, new Vector(1.5, 1.5, 1.5));

        entityDimensions.put(EntityType.ENDERMAN, new Vector(0.31, 2.9, 0.31));
        entityDimensions.put(EntityType.CHICKEN, new Vector(0.2, 0.7, 0.2));
        entityDimensions.put(EntityType.OCELOT, new Vector(0.31, 0.7, 0.31));
        entityDimensions.put(EntityType.SPIDER, new Vector(0.7, 0.9, 0.7));
        entityDimensions.put(EntityType.WITHER, new Vector(0.45, 3.5, 0.45));
        entityDimensions.put(EntityType.IRON_GOLEM, new Vector(0.7, 2.9, 0.7));
        entityDimensions.put(EntityType.GHAST, new Vector(2, 4, 2));

    }

    public static double hypot2(double... values) {
        AtomicDouble squaredSum = new AtomicDouble(0D);

        Arrays.stream(values).forEach(value -> squaredSum.getAndAdd(Math.pow(value, 2D)));

        return Math.sqrt(squaredSum.get());
    }

    public static int floor(double var0) {
        int var2 = (int) var0;
        return var0 < var2 ? var2 - 1 : var2;
    }

    public static int getPotionEffectLevel(Player player, PotionEffectType pet) {
        for (PotionEffect pe : player.getActivePotionEffects()) {
            if (pe.getType().getName().equalsIgnoreCase(pet.getName())) {
                return pe.getAmplifier() + 1;
            }
        }
        return 0;
    }

    public static BoundingBox getHitbox(LivingEntity entity, CustomLocation l, User user) {
        float d = (float) user.getMovementData().getDeltaXZ();
        Vector dimensions = MathUtil.entityDimensions.getOrDefault(entity.getType(), new Vector(0.4, 2, 0.4));
        return new BoundingBox(0, 0, 0, 0, 0, 0).add((float) l.getX(), (float) l.getY(), (float) l.getZ()).grow((float) dimensions.getX(), (float) dimensions.getY(), (float) dimensions.getZ()).grow(.1f, 0.1f, .1f)
                .grow((entity.getVelocity().getY() > 0 ? 0.15f : 0) + d / 1.25f, 0, (entity.getVelocity().getY() > 0 ? 0.15f : 0) + d / 1.25f);
    }

    public static BoundingBox getHitbox2(LivingEntity entity, Vector l, User user) {
        Vector dimensions = MathUtil.entityDimensions.getOrDefault(entity.getType(), new Vector(0.4, 2, 0.4));
        return new BoundingBox(0, 0, 0, 0, 0, 0).add((float) l.getX(), (float) l.getY(), (float) l.getZ()).grow((float) dimensions.getX(), (float) dimensions.getY(), (float) dimensions.getZ()).grow(.1f, 0.1f, .1f);
    }

    public static double moveFlying(User user, CustomLocation to, boolean lastGround) {

        float strafe = 0.98F, forward = 0.98F;
        float f = strafe * strafe + forward * forward;
        float friction;

        float var3 = (0.6F * 0.91F);
        float getAIMoveSpeed = 0.1F;

        if (user.getMovementData().isSprinting()) {
            getAIMoveSpeed = 0.13000001F;
        }

        float var4 = 0.16277136F / (var3 * var3 * var3);

        if (lastGround) {
            friction = getAIMoveSpeed * var4;
        } else {
            friction = 0.026F;
        }

        if (f >= 1.0E-4F) {
            f = (float) Math.sqrt(f);
            if (f < 1.0F) {
                f = 1.0F;
            }
            f = friction / f;
            strafe = strafe * f;
            forward = forward * f;
            float f1 = (float) Math.sin(to.getYaw() * (float) Math.PI / 180.0F);
            float f2 = (float) Math.cos(to.getYaw() * (float) Math.PI / 180.0F);
            float motionXAdd = (strafe * f2 - forward * f1);
            float motionZAdd = (forward * f2 + strafe * f1);
            return Math.hypot(motionXAdd, motionZAdd);
        }

        return 0;
    }




    public static double hypot(double... value) {
        double total = 0;

        for (double val : value) {
            total += (val * val);
        }

        return Math.sqrt(total);
    }

    /**
     * Gets the angle between {@param from} and {@param to} and subtracts with the direction of {@param to}
     *
     * @param from The from location
     * @param to   The to location
     * @return The move angle
     */
    public static double getMoveAngle(CustomLocation from, CustomLocation to) {
        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();

        double moveAngle = Math.toDegrees(Math.atan2(dz, dx)) - 90D; // have to subtract by 90 because minecraft does it

        return Math.abs(wrapAngleTo180_double(moveAngle - to.getYaw()));
    }

    public static double wrapAngleTo180_double(double value) {
        value %= 360D;

        if (value >= 180D)
            value -= 360D;

        if (value < -180D)
            value += 360D;

        return value;
    }


    public static double getHorizontalDistance(CustomLocation from, CustomLocation to) {
        double deltaX = to.getX() - from.getX();
        double deltaZ = to.getZ() - from.getZ();
        return Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
    }


    public static boolean looked(Location from, Location to) {
        return (from.getYaw() != 0 && to.getYaw() != 0) || (from.getPitch() != 0 && from.getPitch() != 0);
    }

    public static boolean looked(CustomLocation from, CustomLocation to) {
        return (from.getYaw() != 0 && to.getYaw() != 0) || (from.getPitch() != 0 && from.getPitch() != 0);
    }

    public static long getDelta(long one, long two) {
        return Math.abs(one - two);
    }

    public static double getDelta(double one, double two) {
        return Math.abs(one - two);
    }

    public static float getDelta(float one, float two) {
        return Math.abs(one - two);
    }

    public static long gcd(long current, long previous) {
        return (previous <= 16384L) ? current : gcd(previous, current % previous);
    }

    public static <T extends Number> T getMode(Collection<T> collect) {
        Map<T, Integer> repeated = new HashMap<>();

        //Sorting each value by how to repeat into a map.
        collect.forEach(val -> {
            int number = repeated.getOrDefault(val, 0);

            repeated.put(val, number + 1);
        });

        //Calculating the largest value to the key, which would be the mode.
        return (T) repeated.keySet().stream()
                .map(key -> new Tuple<>(key, repeated.get(key))) //We map it into a Tuple for easier sorting.
                .max(Comparator.comparing(tup -> tup.two, Comparator.naturalOrder()))
                .orElseThrow(NullPointerException::new).one;
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd2 = new BigDecimal(value);
        bd2 = bd2.setScale(places, RoundingMode.HALF_UP);
        return bd2.doubleValue();
    }
}
