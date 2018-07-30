/*
 * * Copyright (C) 2016-2018 Matt Baxter https://kitteh.org
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

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Spectate!
 */
class SpecReturnCommand implements CommandExecutor {
    private final Spectastic spectastic;

    SpecReturnCommand(@Nonnull Spectastic spectastic) {
        this.spectastic = spectastic;
    }

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource commandSource, @Nonnull CommandContext commandContext) {
        if (!(commandSource instanceof Player)) {
            commandSource.sendMessage(Text.of(TextColors.RED, "You can't spectate, silly!"));
            return CommandResult.empty();
        }
        Player player = (Player) commandSource;

        Optional<Location<World>> pastLocation = this.spectastic.getLocation(player);
        if (pastLocation.isPresent()) {
            player.setLocation(pastLocation.get());
            player.sendMessage(Text.of(TextColors.AQUA, "Returned!"));
        } else if (player.get(Keys.GAME_MODE).orElse(GameModes.SURVIVAL).equals(GameModes.SPECTATOR)) { // TODO fallback
            player.sendMessage(Text.of(TextColors.AQUA, "You are manually spectating! Ask an administrator to set you back."));
        } else if (this.spectastic.getGameMode(player).isPresent()) {
            player.sendMessage(Text.of(TextColors.AQUA, "Sorry, couldn't find past location data!"));
        } else {
            player.sendMessage(Text.of(TextColors.AQUA, "You are not currently spectating!"));
        }
        return CommandResult.success();
    }
}
