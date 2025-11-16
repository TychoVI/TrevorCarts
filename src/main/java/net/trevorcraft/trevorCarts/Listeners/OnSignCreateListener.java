package net.trevorcraft.trevorCarts.Listeners;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.time.Duration;

public class OnSignCreateListener implements Listener {
    private static final MiniMessage MM = MiniMessage.miniMessage();

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if(event.getLine(0).equalsIgnoreCase("[TrevorCarts]")) {

            switch (event.getLine(1).toLowerCase()) {
                case "maxspeed": {
                    var maxSpeed = 100.0;
                    try {
                        maxSpeed = Double.parseDouble(event.getLine(2));
                    } catch (NumberFormatException exception) {
                        var message = Component.text("[TrevorCarts] ", NamedTextColor.AQUA)
                                .append(Component.text("Invalid value for MaxSpeed it should between 0 and 50!", NamedTextColor.RED));
                        event.getPlayer().sendMessage(message);
                        return;
                    }

                    if(maxSpeed > 50.0) {
                        var message = Component.text("[TrevorCarts] ", NamedTextColor.AQUA)
                                .append(Component.text("Invalid value for MaxSpeed it should between 0 and 50!", NamedTextColor.RED));
                        event.getPlayer().sendMessage(message);
                        return;
                    }

                    var header = Component.text("MaxSpeed", NamedTextColor.GREEN);
                    event.line(1, header);
                    break;
                }

                case "arrivalmsg": {
                    if(event.getLine(2).isBlank()) {
                        var message = Component.text("[TrevorCarts] ", NamedTextColor.AQUA)
                                .append(Component.text("You need to specify the arrival station's name on line 3 of the sign!", NamedTextColor.RED));
                        event.getPlayer().sendMessage(message);
                        return;
                    }

                    var header = Component.text("ArrivalMsg", NamedTextColor.RED);
                    event.line(1, header);
                    break;
                }

                case "departuremsg": {
                    if(event.getLine(2).isBlank()) {
                        var message = Component.text("[TrevorCarts] ", NamedTextColor.AQUA)
                                .append(Component.text("You need to specify the next station's name on line 3 of the sign!", NamedTextColor.RED));
                        event.getPlayer().sendMessage(message);
                        return;
                    }

                    var header = Component.text("DepartureMsg", NamedTextColor.RED);
                    event.line(1, header);
                    break;
                }

                default: {
                    var message = Component.text("[TrevorCarts] ", NamedTextColor.AQUA)
                            .append(Component.text("No valid sign type! Valid types are: 'MaxSpeed', 'ArrivalMsg' and 'DepartureMsg'", NamedTextColor.RED));
                    event.getPlayer().sendMessage(message);
                    return;
                }
            }

            var header = Component.text("[TrevorCarts]", NamedTextColor.AQUA).decorate(TextDecoration.BOLD);
            event.line(0, header);
        }
    }
}
