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
package org.kitteh.spectastic.data.location;

import org.kitteh.spectastic.Spectastic;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import javax.annotation.Nonnull;
import java.util.Optional;

public class PastLocationDataManipulatorBuilder implements DataManipulatorBuilder<PastLocationData, ImmutablePastLocationData> {
    @Nonnull
    @Override
    public PastLocationData create() {
        return new PastLocationData();
    }

    @Nonnull
    @Override
    public Optional<PastLocationData> createFrom(@Nonnull DataHolder dataHolder) {
        return Optional.of(dataHolder.get(PastLocationData.class).orElse(new PastLocationData()));
    }

    @Nonnull
    @Override
    public Optional<PastLocationData> build(@Nonnull DataView container) throws InvalidDataException {
        // TODO check content version once bumped
        if (container.contains(Spectastic.PAST_LOCATION_WORLD) && container.contains(Spectastic.PAST_LOCATION_X) && container.contains(Spectastic.PAST_LOCATION_Y) && container.contains(Spectastic.PAST_LOCATION_Z)) {
            return Optional.of(new PastLocationData(
                    container.getString(Spectastic.PAST_LOCATION_WORLD.getQuery()).get(),
                    container.getDouble(Spectastic.PAST_LOCATION_X.getQuery()).get(),
                    container.getDouble(Spectastic.PAST_LOCATION_Y.getQuery()).get(),
                    container.getDouble(Spectastic.PAST_LOCATION_Z.getQuery()).get()));
        }
        return Optional.empty();
    }
}
