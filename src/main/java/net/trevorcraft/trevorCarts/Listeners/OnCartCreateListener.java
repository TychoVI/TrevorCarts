package net.trevorcraft.trevorCarts.Listeners;

import net.trevorcraft.trevorCarts.TrevorCarts;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class OnCartCreateListener implements Listener {
    private static final double DEFAULT_MAX_SPEED = 0.4;
    private final NamespacedKey speedKey;

    public OnCartCreateListener(TrevorCarts plugin) {
        this.speedKey = new NamespacedKey(plugin, "max_speed");
    }

    @EventHandler
    public void onVehicleCreate(VehicleCreateEvent event) {
        if (event.getVehicle() instanceof Minecart cart) {
            setCartSpeed(cart, DEFAULT_MAX_SPEED);
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        for (Entity e : event.getChunk().getEntities()) {
            if (e instanceof Minecart cart) {
                // If we stored a speed, reapply it
                PersistentDataContainer pdc = cart.getPersistentDataContainer();
                Double saved = pdc.get(speedKey, PersistentDataType.DOUBLE);
                if (saved != null) {
                    cart.setMaxSpeed(saved);
                }
            }
        }
    }

    public void setCartSpeed(Minecart cart, double speed) {
        cart.setMaxSpeed(speed);
        cart.getPersistentDataContainer().set(speedKey, PersistentDataType.DOUBLE, speed);
    }
}
