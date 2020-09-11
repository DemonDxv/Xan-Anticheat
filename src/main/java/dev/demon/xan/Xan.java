package dev.demon.xan;

import dev.demon.xan.base.listeners.BukkitListeners;
import dev.demon.xan.base.listeners.ListenerManager;
import dev.demon.xan.base.event.EventManager;
import dev.demon.xan.base.event.impl.ServerShutdownEvent;
import dev.demon.xan.base.tinyprotocol.api.TinyProtocolHandler;
import dev.demon.xan.base.tinyprotocol.api.packets.reflections.Reflections;
import dev.demon.xan.base.tinyprotocol.api.packets.reflections.types.WrappedField;
import dev.demon.xan.base.user.UserManager;
import dev.demon.xan.utils.block.BlockUtil;
import dev.demon.xan.utils.box.BlockBoxManager;
import dev.demon.xan.utils.box.impl.BoundingBoxes;
import dev.demon.xan.utils.math.MathUtil;

import dev.demon.xan.utils.processor.EntityProcessor;
import dev.demon.xan.utils.reflection.CraftReflection;
import dev.demon.xan.utils.time.RunUtils;
import dev.demon.xan.utils.version.VersionUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

@Getter
@Setter
public class Xan extends JavaPlugin {

    @Getter
    private static Xan instance;

    private TinyProtocolHandler tinyProtocolHandler;
    private UserManager userManager;
    private ScheduledExecutorService executorService;
    private BlockBoxManager blockBoxManager;
    private BoundingBoxes boxes;
    private String bukkitVersion;
    private VersionUtil versionUtil;
    private BukkitListeners bukkitListener;
    private EventManager eventManager;

    private int currentTicks, lagStartCheck;
    private long lastServerTick, lastServerLag, lastServerStart;
    private boolean isLagging;

    private EntityProcessor entityProcessor = new EntityProcessor();
    private Map<UUID, List<Entity>> entities = new ConcurrentHashMap<>();
    private WrappedField entityList = Reflections.getNMSClass("World").getFieldByName("entityList");

    public static int banVL;

    public static String banMessage, banCommand, alertsMessage, alertsDev, permissionAlert, permissionPING,
            permissionCMD, permissionINFO;

    public static Boolean banEnabled, alertsEnabled, banMessageEnabled, enableDebug;

    File cfile;


    @Override
    public void onEnable() {
        instance = this;

        bukkitVersion = Bukkit.getServer().getClass().getPackage().getName().substring(23);

        Xan.getInstance().getEntityProcessor().start();

        tinyProtocolHandler = new TinyProtocolHandler();

        executorService = Executors.newSingleThreadScheduledExecutor();

        userManager = new UserManager();

        versionUtil = new VersionUtil();

        eventManager = new EventManager();

        Bukkit.getOnlinePlayers().forEach(player -> TinyProtocolHandler.getInstance().addChannel(player));


        RunUtils.taskTimer(() -> {
            for (World world : Bukkit.getWorlds()) {
                Object vWorld = CraftReflection.getVanillaWorld(world);

                List<Object> vEntities = Collections.synchronizedList(Xan.getInstance().getEntityList().get(vWorld));

                List<Entity> bukkitEntities = vEntities.parallelStream().map(CraftReflection::getBukkitEntity).collect(Collectors.toList());

                Xan.getInstance().getEntities().put(world.getUID(), bukkitEntities);
            }
        }, 2L, 2L);



        new BlockUtil();
        new ListenerManager();
        new MathUtil();

        Xan.getInstance().getServer().getPluginManager().registerEvents(new BukkitListeners(), Xan.getInstance());


        cfile = new File(getDataFolder(), "config.yml");
        saveDefaultConfig();
        loadConfiguration();


        this.blockBoxManager = new BlockBoxManager();
        this.boxes = new BoundingBoxes();

        super.onEnable();
    }

    @Override
    public void onDisable() {
        getEventManager().callEvent(new ServerShutdownEvent());
        Bukkit.getOnlinePlayers().forEach(player -> TinyProtocolHandler.getInstance().removeChannel(player));
        executorService.shutdownNow();;
    }


    private void loadConfiguration() {
        //Bans
        banMessage = instance.getConfig().getString("Bans.message");
        banCommand = instance.getConfig().getString("Bans.command");
        banEnabled = instance.getConfig().getBoolean("Bans.enabled");
        banVL = instance.getConfig().getInt("Bans.max-vl");
        banMessageEnabled = instance.getConfig().getBoolean("Bans.message-enabled");

        //Alerts
        alertsMessage = instance.getConfig().getString("Alerts.message");
        enableDebug = instance.getConfig().getBoolean("Alerts.debug");


        //Permission
        permissionAlert = instance.getConfig().getString("Permissions.alert");
        permissionPING = instance.getConfig().getString("Permissions.ping");
        permissionCMD = instance.getConfig().getString("Permissions.command");
        permissionINFO = instance.getConfig().getString("Permissions.info");

    }
}
