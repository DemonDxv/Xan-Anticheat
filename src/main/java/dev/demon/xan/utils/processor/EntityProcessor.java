package dev.demon.xan.utils.processor;

import dev.demon.xan.Xan;
import dev.demon.xan.utils.time.RunUtils;
import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Getter
public class EntityProcessor {

    private Map<UUID, List<Entity>> vehicles = new ConcurrentHashMap<>();
    private BukkitTask task;

    private void runEntityProcessor() {
        Xan.getInstance().getEntities().keySet().forEach((uuid) -> vehicles.put(uuid, Xan.getInstance().getEntities().get(uuid).stream().filter(entity -> entity instanceof Vehicle).collect(Collectors.toList())));
    }

    public void start() {
        task = RunUtils.taskTimerAsync(this::runEntityProcessor, Xan.getInstance(), 0L, 10L);
    }
}