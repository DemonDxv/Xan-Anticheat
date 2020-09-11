package dev.demon.xan.utils.version;

import dev.demon.xan.Xan;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VersionUtil {

    private Version version;

    public VersionUtil() {
        String bukkitVersion = Xan.getInstance().getBukkitVersion();
        if (bukkitVersion.contains("1_8")) version = Version.V1_8;
        if (bukkitVersion.contains("1_7")) version = Version.V1_7;
    }

    public enum Version {
        V1_8,
        V1_7
    }
}
