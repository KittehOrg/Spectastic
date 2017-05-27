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
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.KeyFactory;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

/**
 * Spectate in style.
 */
@Plugin(id = "spectastic", name = "Spectastic", version = "1.1.1")
public class Spectastic {
    public static final String PERMISSION_SPEC = "spectastic.spec";
    public static final Key<Value<String>> PAST_GAME_MODE = KeyFactory.makeSingleKey(TypeToken.of(String.class), new TypeToken<Value<String>>(){}, DataQuery.of("PastGameMode"), "spectastic:past_gamemode", "Spectastic: Past game mode");
    public static final Key<Value<String>> PAST_LOCATION_WORLD = KeyFactory.makeSingleKey(TypeToken.of(String.class), new TypeToken<Value<String>>(){}, DataQuery.of("PastLocationWorld"), "spectastic:past_world", "Spectastic: Past world");
    public static final Key<Value<Double>> PAST_LOCATION_X = KeyFactory.makeSingleKey(TypeToken.of(Double.class), new TypeToken<Value<Double>>(){}, DataQuery.of("PastLocationX"), "spectastic:past_x", "Spectastic: Past x");
    public static final Key<Value<Double>> PAST_LOCATION_Y = KeyFactory.makeSingleKey(TypeToken.of(Double.class), new TypeToken<Value<Double>>(){}, DataQuery.of("PastLocationY"), "spectastic:past_y", "Spectastic: Past y");
    public static final Key<Value<Double>> PAST_LOCATION_Z = KeyFactory.makeSingleKey(TypeToken.of(Double.class), new TypeToken<Value<Double>>(){}, DataQuery.of("PastLocationZ"), "spectastic:past_z", "Spectastic: Past z");

    @Inject
    private Game game;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        Sponge.getDataManager().register(PastGameModeData.class, ImmutablePastGameModeData.class, new PastGameModeDataManipulatorBuilder());
        Sponge.getDataManager().register(PastLocationData.class, ImmutablePastLocationData.class, new PastLocationDataManipulatorBuilder());
    }

    @Listener
    public void onGameInit(GameInitializationEvent event) {
        CommandSpec spectasticSpec = CommandSpec.builder().permission(PERMISSION_SPEC).executor(new SpecCommand()).build();
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
}
