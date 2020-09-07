package dev.demon.xan.utils.block;

import dev.demon.xan.utils.box.BoundingBox;
import lombok.Getter;
import org.bukkit.block.Block;

/**
 * Created on 06/06/2020 Package me.jumba.sparky.util.block
 */
@Getter
public class BlockEntry {

    private Block block;
    private BoundingBox boundingBox;

    public BlockEntry(Block block, BoundingBox boundingBox) {
        this.block = block;
        this.boundingBox = boundingBox;
    }
}
