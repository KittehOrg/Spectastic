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
package org.kitteh.spectastic.data.gamemode;

import com.google.common.base.Objects;
import org.kitteh.spectastic.Spectastic;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

import javax.annotation.Nonnull;

public class ImmutablePastGameModeData extends AbstractImmutableData<ImmutablePastGameModeData, PastGameModeData> {
    private final String pastGameMode;

    public ImmutablePastGameModeData() {
        this(PastGameModeData.FALLBACK_GAME_MODE);
    }

    public ImmutablePastGameModeData(String pastGameMode) {
        this.pastGameMode = pastGameMode;
        this.registerGetters();
    }

    @Override
    protected void registerGetters() {
        this.registerFieldGetter(Spectastic.PAST_GAME_MODE, () -> this.pastGameMode);
        registerKeyValue(Spectastic.PAST_GAME_MODE, this::pastGameMode);
    }

    @Nonnull
    public ImmutableValue<String> pastGameMode() {
        return Sponge.getRegistry().getValueFactory().createValue(Spectastic.PAST_GAME_MODE, PastGameModeData.FALLBACK_GAME_MODE, this.pastGameMode).asImmutable();
    }

    @Nonnull
    @Override
    public PastGameModeData asMutable() {
        return new PastGameModeData(this.pastGameMode);
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Nonnull
    @Override
    public DataContainer toContainer() {
        return new MemoryDataContainer().set(Spectastic.PAST_GAME_MODE, this.pastGameMode);
    }

    @Nonnull
    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("pastGameMode", this.pastGameMode).toString();
    }
}
