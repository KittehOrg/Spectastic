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

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Spectate!
 */
class SpecCommand implements CommandExecutor {
    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource commandSource, @Nonnull CommandContext commandContext) throws CommandException {
        if (!(commandSource instanceof Player)) {
            commandSource.sendMessage(Text.of(TextColors.RED, "You can't spectate, silly!"));
            return CommandResult.empty();
        }
        Player player = (Player) commandSource;
        GameMode gameMode = player.get(Keys.GAME_MODE).orElse(GameModes.SURVIVAL); // TODO fallback
        Optional<String> pastGameMode = player.get(Spectastic.PAST_GAMEMODE);
        GameMode newGameMode;
        if (pastGameMode.isPresent()) {
            newGameMode = Sponge.getRegistry().getType(GameMode.class, pastGameMode.get()).orElse(GameModes.SURVIVAL); // TODO fallback
            player.remove(Spectastic.PAST_GAMEMODE);
        } else if (gameMode.equals(GameModes.SPECTATOR)) {
            player.sendMessage(Text.of(TextColors.AQUA, "You are manually spectating! Ask an administrator to set you back."));
            return CommandResult.success();
        } else {
            System.out.println("Offering: \"" + gameMode.getId() + "\"");
            System.out.println(player.offer(new PastGameModeData(gameMode.getId())));
            newGameMode = GameModes.SPECTATOR;
        }
        player.offer(Keys.GAME_MODE, newGameMode);
        player.sendMessage(Text.of(TextColors.AQUA, "You are now in " + newGameMode.getName()));
        return CommandResult.success();
    }
}
