package dev.demon.xan.base.tinyprotocol.packet.out;

import dev.demon.xan.base.tinyprotocol.api.NMSObject;
import dev.demon.xan.base.tinyprotocol.api.Packet;
import dev.demon.xan.base.tinyprotocol.api.ProtocolVersion;
import dev.demon.xan.base.tinyprotocol.api.packets.reflections.Reflections;
import dev.demon.xan.base.tinyprotocol.api.packets.reflections.types.WrappedClass;
import dev.demon.xan.base.tinyprotocol.api.packets.reflections.types.WrappedField;
import dev.demon.xan.base.tinyprotocol.api.packets.reflections.types.WrappedMethod;
import dev.demon.xan.base.tinyprotocol.packet.types.BaseBlockPosition;
import dev.demon.xan.base.tinyprotocol.reflection.FieldAccessor;
import dev.demon.xan.utils.box.ReflectionUtil;
import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

@Getter
public class WrappedOutBlockChange extends NMSObject {
    private static final String packet = Packet.Server.BLOCK_CHANGE;

    //1.7.10 and below
    private static FieldAccessor<Integer> legacyX;
    private static FieldAccessor<Integer> legacyY;
    private static FieldAccessor<Integer> legacyZ;
    private static WrappedField blockChangeBlockField;
    private static WrappedField blockDataIntField;
    private static WrappedMethod getDataMethod;

    //1.8 and newer
    private static FieldAccessor<Object> blockPosAccessor;
    private static WrappedField iBlockDataField;


    private static WrappedClass blockChangeClass = Reflections.getNMSClass(packet);
    private static WrappedClass nmsBlockClass = Reflections.getNMSClass("Block");
    private static WrappedClass craftBlockClass = Reflections.getCBClass("CraftBlock");

    private BaseBlockPosition position;

    public WrappedOutBlockChange(Object packet) {
        super(packet);
    }

    @Override
    public void updateObject() {

    }

    public WrappedOutBlockChange(Block block) {
        if(ProtocolVersion.getGameVersion().isAbove(ProtocolVersion.V1_7_10)) {
            setPacket(packet, block.getX(), block.getY(), block.getZ(), ReflectionUtil.getWorldHandle(block.getWorld()));
        } else {
            setPacket(packet, ReflectionUtil.getWorldHandle(block.getWorld()), new BaseBlockPosition(block.getX(), block.getY(), block.getZ()).getAsBlockPosition());
        }
    }

    @Override
    public void process(Player player, ProtocolVersion version) {
        if (ProtocolVersion.getGameVersion().isBelow(ProtocolVersion.V1_8)) {
            position = new BaseBlockPosition(fetch(legacyX), fetch(legacyY), fetch(legacyZ));
        } else {
            position = new BaseBlockPosition(fetch(blockPosAccessor));
        }
    }

    static {
        if(ProtocolVersion.getGameVersion().isBelow(ProtocolVersion.V1_8)) {
            legacyX = fetchField(packet, int.class, 0);
            legacyY = fetchField(packet, int.class, 1);
            legacyZ = fetchField(packet, int.class, 2);


            blockChangeBlockField = blockChangeClass.getFirstFieldByType(nmsBlockClass.getParent());
            blockDataIntField = blockChangeClass.getFieldByName("data");
            getDataMethod = Reflections.getNMSClass("World").getMethod("getData", int.class, int.class, int.class);
        } else {
            blockPosAccessor = fetchField(packet, Object.class, 0);
            iBlockDataField = blockChangeClass.getFieldByType(ReflectionUtil.iBlockData, 0);
        }
    }
}
