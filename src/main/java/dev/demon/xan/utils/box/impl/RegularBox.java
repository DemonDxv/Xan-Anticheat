package dev.demon.xan.utils.box.impl;

import dev.demon.xan.utils.box.BoundingBox;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Collections;
import java.util.List;

public class RegularBox extends BlockBox {
    public RegularBox(Material material, BoundingBox original) {
        super(material, original);
    }

    @Override
    List<BoundingBox> getBox(Block block) {
        return Collections.singletonList(getOriginal().add(block.getLocation().toVector()));
    }
}
