package net.trevorcraft.trevorCarts.Listeners;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import net.trevorcraft.trevorCarts.TrevorCarts;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import java.time.Duration;
import java.util.Dictionary;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OnCartMoveListener implements Listener {
    private final int[] xModifier = { -1, 0, 1 };
    private final int[] yModifier = { -2, -1, 0, 1, 2 };
    private final int[] zModifier = { -1, 0, 1 };

    private final Set<SignKey> activeSigns = ConcurrentHashMap.newKeySet();

    private TrevorCarts plugin;

    public OnCartMoveListener(TrevorCarts plugin) {
        this.plugin =  plugin;
    }

    @EventHandler
    public void onVehicleMove(VehicleMoveEvent event) {
        if(!(event.getVehicle() instanceof Minecart cart)) {
            return;
        }

        var cartX = cart.getLocation().getBlockX();
        var cartY = cart.getLocation().getBlockY();
        var cartZ = cart.getLocation().getBlockZ();
        var world = cart.getWorld();
        var worldId = world.getUID();

        for(int xMod : xModifier) {
            for(int yMod : yModifier) {
                for(int zMod : zModifier) {
                    var blockX = cartX + xMod;
                    var blockY = cartY + yMod;
                    var blockZ = cartZ + zMod;

                    var block = cart.getWorld().getBlockAt(blockX, blockY, blockZ);

                    var material = block.getType();

                    if(!Tag.SIGNS.isTagged(material)) {
                        continue;
                    }

                    var sign = (Sign) block.getState();
                    var side = sign.getSide(Side.FRONT);

                    var line0 = PlainTextComponentSerializer.plainText().serialize(side.line(0));

                    if(!line0.equalsIgnoreCase("[TrevorCarts]")) {
                        continue;
                    }

                    SignKey key = new SignKey(worldId, blockX, blockY, blockZ);

                    if (!activeSigns.add(key)) {
                        continue;
                    }

                    plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                        activeSigns.remove(key);
                    }, 40L);

                    var line1 = PlainTextComponentSerializer.plainText().serialize(side.line(1));
                    var line2 = PlainTextComponentSerializer.plainText().serialize(side.line(2));
                    var line3 = PlainTextComponentSerializer.plainText().serialize(side.line(3));

                    if(line1.equalsIgnoreCase("MaxSpeed")) {
                        var specifiedMaxSpeed = 8.0;
                        try {
                            specifiedMaxSpeed = Double.parseDouble(line2);
                        } catch (NumberFormatException ignored) {

                        }

                        cart.setMaxSpeed(specifiedMaxSpeed / 20);
                    }

                    if(line1.equalsIgnoreCase("ArrivalMsg")) {
                        var mainTitle = Component.text("Arriving at " + line2, NamedTextColor.AQUA);
                        var subTitle = Component.text("Get ready to exit your minecart!");
                        var times = Title.Times.times(Duration.ofMillis(200), Duration.ofMillis(2000), Duration.ofMillis(200));
                        var title = Title.title(mainTitle, subTitle, times);
                        var sound = Sound.sound(Key.key("block.note_block.flute"), Sound.Source.MASTER, 1f, 1.1f);

                        for(Entity passenger : cart.getPassengers()) {
                            passenger.showTitle(title);
                            passenger.playSound(sound);
                        }
                    }

                    if(line1.equalsIgnoreCase("DepartureMsg")) {
                        var mainTitle = Component.text("Next arriving at " + line2, NamedTextColor.AQUA);
                        var subTitle = Component.text("Enjoy the ride!");
                        var times = Title.Times.times(Duration.ofMillis(200), Duration.ofMillis(2000), Duration.ofMillis(200));
                        var title = Title.title(mainTitle, subTitle, times);
                        var sound = Sound.sound(Key.key("block.note_block.flute"), Sound.Source.MASTER, 1f, 0.8f);

                        for(Entity passenger : cart.getPassengers()) {
                            passenger.showTitle(title);
                            passenger.playSound(sound);
                        }
                    }
                }
            }
        }
    }

    private static final class SignKey {
        private final UUID worldId;
        private final int x;
        private final int y;
        private final int z;

        private SignKey(UUID worldId, int x, int y, int z) {
            this.worldId = worldId;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SignKey)) return false;
            SignKey that = (SignKey) o;
            return x == that.x && y == that.y && z == that.z && worldId.equals(that.worldId);
        }

        @Override
        public int hashCode() {
            int result = worldId.hashCode();
            result = 31 * result + x;
            result = 31 * result + y;
            result = 31 * result + z;
            return result;
        }
    }
}
