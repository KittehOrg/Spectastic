/*
 * * Copyright (C) 2016 Matt Baxter http://kitteh.org
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.kitteh.spectastic;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import org.kitteh.spectastic.data.gamemode.ImmutablePastGameModeData;
import org.kitteh.spectastic.data.gamemode.PastGameModeData;
import org.kitteh.spectastic.data.gamemode.PastGameModeDataManipulatorBuilder;
import org.kitteh.spectastic.data.location.ImmutablePastLocationData;
import org.kitteh.spectastic.data.location.PastLocationData;
import org.kitteh.spectastic.data.location.PastLocationDataManipulatorBuilder;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Spectate in style.
 */
@Plugin(id = "spectastic", name = "Spectastic", version = "1.1.2-SNAPSHOT",
        authors = {"mbaxter"}, description = "Toggle between your current gamemode and spectator"
)
public class Spectastic {
    public static final String PERMISSION_SPEC = "spectastic.spec";
    public static final String PERMISSION_SPEC_RETURN = "spectastic.return";
    public static final String PERMISSION_SPEC_NEW = "spectastic.new";
    public static Key<Value<String>> PAST_GAME_MODE;
    public static Key<Value<String>> PAST_LOCATION_WORLD;
    public static Key<Value<Double>> PAST_LOCATION_X;
    public static Key<Value<Double>> PAST_LOCATION_Y;
    public static Key<Value<Double>> PAST_LOCATION_Z;

    @Inject
    private Game game;

    @Inject
    private PluginContainer container;

    @Listener
    public void onKeyRegistration(GameRegistryEvent.Register<Key<?>> event) {
        PAST_GAME_MODE = Key.builder()
                .type(new TypeToken<Value<String>>() {
                })
                .id("past_gamemode")
                .name("Spectastic: Past game mode")
                .query(DataQuery.of("PastGameMode"))
                .build();
        PAST_LOCATION_WORLD = Key.builder()
                .type(new TypeToken<Value<String>>() {
                })
                .id("past_world")
                .name("Spectastic: Past world")
                .query(DataQuery.of("PastLocationWorld"))
                .build();
        PAST_LOCATION_X = Key.builder()
                .type(new TypeToken<Value<Double>>() {
                })
                .id("past_x")
                .name("Spectastic: Past x")
                .query(DataQuery.of("PastLocationX"))
                .build();
        PAST_LOCATION_Y = Key.builder()
                .type(new TypeToken<Value<Double>>() {
                })
                .id("past_y")
                .name("Spectastic: Past y")
                .query(DataQuery.of("PastLocationY"))
                .build();
        PAST_LOCATION_Z = Key.builder()
                .type(new TypeToken<Value<Double>>() {
                })
                .id("past_z")
                .name("Spectastic: Past z")
                .query(DataQuery.of("PastLocationZ"))
                .build();
    }

    @Listener
    public void onDataRegistration(GameRegistryEvent.Register<DataRegistration<?, ?>> event) {
        DataRegistration.builder()
                .dataName("PastGameMode")
                .dataClass(PastGameModeData.class)
                .immutableClass(ImmutablePastGameModeData.class)
                .builder(new PastGameModeDataManipulatorBuilder())
                .manipulatorId("PastGameMode")
                .buildAndRegister(this.container);
        DataRegistration.builder()
                .dataName("PastLocation")
                .dataClass(PastLocationData.class)
                .immutableClass(ImmutablePastLocationData.class)
                .builder(new PastLocationDataManipulatorBuilder())
                .manipulatorId("PastLocation")
                .buildAndRegister(this.container);
    }

    @Listener
    public void onGameInit(GameInitializationEvent event) {
        CommandSpec spectasticNewSpec = CommandSpec.builder()
                .permission(PERMISSION_SPEC_NEW)
                .description(Text.of("Stop spectating in your new location!"))
                .executor(new SpecNewCommand(this))
                .build();
        CommandSpec spectasticReturnSpec = CommandSpec.builder()
                .permission(PERMISSION_SPEC_RETURN)
                .description(Text.of("Return without leaving spectating mode!"))
                .executor(new SpecReturnCommand(this))
                .build();
        CommandSpec spectasticSpec = CommandSpec.builder()
                .permission(PERMISSION_SPEC)
                .description(Text.of("Toggle spectating mode!"))
                .executor(new SpecCommand(this))
                .child(spectasticNewSpec, "new")
                .child(spectasticReturnSpec, "return")
                .build();
        this.game.getCommandManager().register(this, spectasticSpec, "spectate", "spec");
    }

    @Listener
    public void playerJoin(ClientConnectionEvent.Join event) {
        Player player = event.getTargetEntity();
        Optional<String> pastGameMode = player.get(Spectastic.PAST_GAME_MODE);
        if (!player.hasPermission(PERMISSION_SPEC) && pastGameMode.isPresent()) {
            GameMode newGameMode = Sponge.getRegistry().getType(GameMode.class, pastGameMode.get()).orElse(GameModes.SURVIVAL); // TODO fallback
            player.offer(Keys.GAME_MODE, newGameMode);
            player.remove(Spectastic.PAST_GAME_MODE);
            player.sendMessage(Text.of(TextColors.AQUA, "Having lost permission to /spectate, you are now in " + newGameMode.getName()));
        }
    }

    Optional<GameMode> getGameMode(@Nonnull Player player) {
        return player.get(Spectastic.PAST_GAME_MODE)
                .map(s -> Sponge.getRegistry().getType(GameMode.class, s).orElse(GameModes.SURVIVAL));// TODO fallback
    }

    Optional<Location<World>> getLocation(@Nonnull Player player) {
        Optional<String> pastWorld = player.get(Spectastic.PAST_LOCATION_WORLD);
        Optional<Double> pastX = player.get(Spectastic.PAST_LOCATION_X);
        Optional<Double> pastY = player.get(Spectastic.PAST_LOCATION_Y);
        Optional<Double> pastZ = player.get(Spectastic.PAST_LOCATION_Z);
        if (pastWorld.isPresent() && pastX.isPresent() && pastY.isPresent() && pastZ.isPresent()) {
            Optional<World> world = Sponge.getGame().getServer().getWorld(pastWorld.get());
            if (world.isPresent()) {
                double x = pastX.get();
                double y = pastY.get();
                double z = pastZ.get();
                return Optional.of(world.get().getLocation(x, y, z));
            }
        }
        return Optional.empty();
    }

    void erasePast(@Nonnull Player player) {
        player.remove(Spectastic.PAST_GAME_MODE);
        player.remove(Spectastic.PAST_LOCATION_WORLD);
        player.remove(Spectastic.PAST_LOCATION_X);
        player.remove(Spectastic.PAST_LOCATION_Y);
        player.remove(Spectastic.PAST_LOCATION_Z);
    }
}
