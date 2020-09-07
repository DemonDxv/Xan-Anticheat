package dev.demon.xan.base.tinyprotocol.packet.out;

import dev.demon.xan.base.tinyprotocol.api.ProtocolVersion;
import dev.demon.xan.base.tinyprotocol.packet.types.WrappedEnumParticle;
import dev.demon.xan.utils.box.ReflectionUtil;
import lombok.Getter;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

@Getter
public class WrappedPacketPlayOutWorldParticle {

    private WrappedEnumParticle type;
    private boolean j;
    private float x;
    private float y;
    private float z;
    private float xOffset;
    private float yOffset;
    private float zOffset;
    private float speed;
    private int amount;
    private int[] data;

    public WrappedPacketPlayOutWorldParticle(WrappedEnumParticle type, boolean var2, float x, float y, float z, float xOffset, float yOffset, float ZOffset, float speed, int amount, int... data) {
        this.type = type;
        this.j = var2;
        this.x = x;
        this.y = y;
        this.z = z;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = ZOffset;
        this.speed = speed;
        this.amount = amount;
        this.data = data;

    }

    public void sendPacket(Player player) {
        Object packet = null;

        if (ProtocolVersion.getGameVersion().isOrAbove(ProtocolVersion.V1_13)) {
            try {
                ReflectionUtil.CraftPlayer.getMethod("spawnParticle", Particle.class, double.class, double.class, double.class, int.class, double.class, double.class, double.class, double.class, Object.class).invoke(player, Particle.FLAME, x, y, z, amount, xOffset, yOffset, zOffset, speed, data);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        } else if(ProtocolVersion.getGameVersion().isOrAbove(ProtocolVersion.V1_8)) {
            try {
                packet = ReflectionUtil.getNMSClass("PacketPlayOutWorldParticles").getConstructor(ReflectionUtil.getNMSClass("EnumParticle"), boolean.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class, int[].class).newInstance(type.toNMS(), j, x, y, z, xOffset, yOffset, zOffset, speed, amount, data);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
                e1.printStackTrace();
            }
            Object pCon = ReflectionUtil.getFieldValue(ReflectionUtil.getFieldByName(ReflectionUtil.getNMSClass("EntityPlayer"), "playerConnection"), ReflectionUtil.getEntityPlayer(player));

            ReflectionUtil.getMethodValue(ReflectionUtil.getMethod(ReflectionUtil.getNMSClass("PlayerConnection"), "sendPacket", ReflectionUtil.getNMSClass("Packet")), pCon, packet);
        } else {
            try {
                packet = ReflectionUtil.getNMSClass("PacketPlayOutWorldParticles").getConstructor(String.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class).newInstance(type.toString(), x, y, z, xOffset, yOffset, zOffset, speed, amount);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
                e1.printStackTrace();
            }
            Object pCon = ReflectionUtil.getFieldValue(ReflectionUtil.getFieldByName(ReflectionUtil.getNMSClass("EntityPlayer"), "playerConnection"), ReflectionUtil.getEntityPlayer(player));

            ReflectionUtil.getMethodValue(ReflectionUtil.getMethod(ReflectionUtil.getNMSClass("PlayerConnection"), "sendPacket", ReflectionUtil.getNMSClass("Packet")), pCon, packet);
        }
    }
}