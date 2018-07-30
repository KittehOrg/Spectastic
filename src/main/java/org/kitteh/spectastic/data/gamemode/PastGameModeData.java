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
package org.kitteh.spectastic.data.gamemode;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import org.kitteh.spectastic.Spectastic;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

import javax.annotation.Nonnull;
import java.util.Optional;

public class PastGameModeData extends AbstractData<PastGameModeData, ImmutablePastGameModeData> {
    public static final String FALLBACK_GAME_MODE = "world";

    private String pastGameMode;

    public PastGameModeData() {
        this(FALLBACK_GAME_MODE);
    }

    public PastGameModeData(String pastGameMode) {
        this.pastGameMode = pastGameMode;
        this.registerGettersAndSetters();
    }

    @Nonnull
    public Value<String> pastGameMode() {
        return Sponge.getRegistry().getValueFactory().createValue(Spectastic.PAST_GAME_MODE, FALLBACK_GAME_MODE, this.pastGameMode);
    }

    @Override
    protected void registerGettersAndSetters() {
        this.registerFieldGetter(Spectastic.PAST_GAME_MODE, () -> this.pastGameMode);
        this.registerFieldSetter(Spectastic.PAST_GAME_MODE, value -> this.pastGameMode = Preconditions.checkNotNull(value));
        this.registerKeyValue(Spectastic.PAST_GAME_MODE, this::pastGameMode);
    }

    @Nonnull
    @Override
    public Optional<PastGameModeData> fill(@Nonnull DataHolder dataHolder, @Nonnull MergeFunction overlap) {
        return Optional.empty(); // YOLO
    }

    @Nonnull
    @Override
    public Optional<PastGameModeData> from(@Nonnull DataContainer container) {
        if (!container.contains(Spectastic.PAST_GAME_MODE.getQuery())) {
            return Optional.empty();
        }
        this.pastGameMode = container.getString(Spectastic.PAST_GAME_MODE.getQuery()).get();
        return Optional.of(this);
    }

    @Nonnull
    @Override
    public PastGameModeData copy() {
        return new PastGameModeData(this.pastGameMode);
    }

    @Nonnull
    @Override
    public ImmutablePastGameModeData asImmutable() {
        return new ImmutablePastGameModeData(this.pastGameMode);
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Nonnull
    @Override
    public DataContainer toContainer() {
        return super.toContainer().set(Spectastic.PAST_GAME_MODE, this.pastGameMode);
    }

    @Nonnull
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("pastGameMode", this.pastGameMode).toString();
    }
}
