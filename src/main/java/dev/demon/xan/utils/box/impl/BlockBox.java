package dev.demon.xan.utils.box.impl;

import dev.demon.xan.utils.box.BoundingBox;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.List;


@Getter
@Setter
public abstract class BlockBox {
    private Material material;
    private BoundingBox original;

    BlockBox(Material material, BoundingBox original) {
        this.material = material;
        this.original = original;
    }

    abstract List<BoundingBox> getBox(Block block);
}
