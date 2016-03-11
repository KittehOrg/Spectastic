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

import com.google.inject.Inject;
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
@Plugin(id = "org.kitteh.spectastic", name = "Spectastic", version = "1.0.0-SNAPSHOT")
public class Spectastic {
    public static final String PERMISSION_SPEC = "spectastic.spec";
    public static final Key<Value<String>> PAST_GAMEMODE = KeyFactory.makeSingleKey(String.class, Value.class, DataQuery.of("PastGameMode"));
    public static final String FALLBACK = "survival"; // TODO fallback

    @Inject
    private Game game;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        Sponge.getDataManager().register(PastGameModeData.class, ImmutablePastGameModeData.class, new PastGameModeDataManipulatorBuilder());
    }

    @Listener
    public void onGameInit(GameInitializationEvent event) {
        CommandSpec spectasticSpec = CommandSpec.builder().permission(PERMISSION_SPEC).executor(new SpecCommand()).build();
        this.game.getCommandManager().register(this, spectasticSpec, "spectate", "spec");
    }

    @Listener
    public void playerJoin(ClientConnectionEvent.Join event) {
        Player player = event.getTargetEntity();
        Optional<String> pastGameMode = player.get(Spectastic.PAST_GAMEMODE);
        if (!player.hasPermission(PERMISSION_SPEC) && pastGameMode.isPresent()) {
            GameMode newGameMode = Sponge.getRegistry().getType(GameMode.class, pastGameMode.get()).orElse(GameModes.SURVIVAL); // TODO fallback
            player.offer(Keys.GAME_MODE, newGameMode);
            player.remove(Spectastic.PAST_GAMEMODE);
            player.sendMessage(Text.of(TextColors.AQUA, "Having lost permission to /spectate, you are now in " + newGameMode.getName()));
        }
    }
}
