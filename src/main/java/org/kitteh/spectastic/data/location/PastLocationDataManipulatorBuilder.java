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
package org.kitteh.spectastic.data.location;

import org.kitteh.spectastic.Spectastic;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import javax.annotation.Nonnull;
import java.util.Optional;

public class PastLocationDataManipulatorBuilder extends AbstractDataBuilder<PastLocationData> implements DataManipulatorBuilder<PastLocationData, ImmutablePastLocationData> {
    public static final int VERSION = 1;

    public PastLocationDataManipulatorBuilder() {
        super(PastLocationData.class, VERSION);
    }

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
    public Optional<PastLocationData> buildContent(@Nonnull DataView container) throws InvalidDataException {
        Optional<String> world;
        Optional<Double> x, y, z;
        if ((world = container.getString(Spectastic.PAST_LOCATION_WORLD.getQuery())).isPresent() &&
                (x = container.getDouble(Spectastic.PAST_LOCATION_X.getQuery())).isPresent() &&
                (y = container.getDouble(Spectastic.PAST_LOCATION_Y.getQuery())).isPresent() &&
                (z = container.getDouble(Spectastic.PAST_LOCATION_Z.getQuery())).isPresent()) {
            return Optional.of(new PastLocationData(world.get(), x.get(), y.get(), z.get()));
        }
        return Optional.empty();
    }
}
