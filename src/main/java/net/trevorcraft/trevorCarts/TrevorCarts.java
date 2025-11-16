package net.trevorcraft.trevorCarts;

import net.trevorcraft.trevorCarts.Listeners.OnCartCreateListener;
import net.trevorcraft.trevorCarts.Listeners.OnCartMoveListener;
import net.trevorcraft.trevorCarts.Listeners.OnSignCreateListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class TrevorCarts extends JavaPlugin {

    @Override
    public void onEnable() {
        var pm = getServer().getPluginManager();
        pm.registerEvents(new OnCartCreateListener(this), this);
        pm.registerEvents(new OnCartMoveListener(this), this);
        pm.registerEvents(new OnSignCreateListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
