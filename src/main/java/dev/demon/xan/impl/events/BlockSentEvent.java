package dev.demon.xan.impl.events;

import dev.demon.xan.api.event.AnticheatEvent;
import dev.demon.xan.api.tinyprotocol.packet.types.BaseBlockPosition;
import dev.demon.xan.api.tinyprotocol.packet.types.enums.WrappedEnumDirection;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class BlockSentEvent extends AnticheatEvent {

    private WrappedEnumDirection face;
    private ItemStack itemStack;
    private BaseBlockPosition position;
    private float vecX, vecY, vecZ;

    public BlockSentEvent(float vecX, float vecY, float vecZ, WrappedEnumDirection face, BaseBlockPosition position, ItemStack itemStack) {
        this.vecX = vecX;
        this.vecY = vecY;
        this.vecZ = vecZ;
        this.face = face;
        this.position = position;
        this.itemStack = itemStack;
    }
}
