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

import com.google.common.base.MoreObjects;
import org.kitteh.spectastic.Spectastic;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

import javax.annotation.Nonnull;

public class ImmutablePastLocationData extends AbstractImmutableData<ImmutablePastLocationData, PastLocationData> {
    private final String world;
    private final double x, y, z;

    public ImmutablePastLocationData(String world, double x, double y, double z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.registerGetters();
    }

    @Override
    protected void registerGetters() {
        this.registerFieldGetter(Spectastic.PAST_LOCATION_WORLD, () -> this.world);
        registerKeyValue(Spectastic.PAST_LOCATION_WORLD, this::world);

        this.registerFieldGetter(Spectastic.PAST_LOCATION_X, () -> this.x);
        registerKeyValue(Spectastic.PAST_LOCATION_X, this::x);

        this.registerFieldGetter(Spectastic.PAST_LOCATION_Y, () -> this.y);
        registerKeyValue(Spectastic.PAST_LOCATION_Y, this::y);

        this.registerFieldGetter(Spectastic.PAST_LOCATION_Z, () -> this.z);
        registerKeyValue(Spectastic.PAST_LOCATION_Z, this::z);
    }

    @Nonnull
    public ImmutableValue<String> world() {
        return Sponge.getRegistry().getValueFactory().createValue(Spectastic.PAST_LOCATION_WORLD, PastLocationData.FALLBACK_WORLD, this.world).asImmutable();
    }

    @Nonnull
    public ImmutableValue<Double> x() {
        return Sponge.getRegistry().getValueFactory().createValue(Spectastic.PAST_LOCATION_X, PastLocationData.FALLBACK_X, this.x).asImmutable();
    }

    @Nonnull
    public ImmutableValue<Double> y() {
        return Sponge.getRegistry().getValueFactory().createValue(Spectastic.PAST_LOCATION_Y, PastLocationData.FALLBACK_Y, this.y).asImmutable();
    }

    @Nonnull
    public ImmutableValue<Double> z() {
        return Sponge.getRegistry().getValueFactory().createValue(Spectastic.PAST_LOCATION_Z, PastLocationData.FALLBACK_Z, this.z).asImmutable();
    }

    @Nonnull
    @Override
    public PastLocationData asMutable() {
        return new PastLocationData(this.world, this.x, this.y, this.z);
    }

    @Override
    public int getContentVersion() {
        return PastLocationData.VERSION;
    }

    @Nonnull
    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew().set(Spectastic.PAST_LOCATION_WORLD, this.world).set(Spectastic.PAST_LOCATION_X, this.x).set(Spectastic.PAST_LOCATION_Y, this.y).set(Spectastic.PAST_LOCATION_Z, this.z);
    }

    @Nonnull
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("world", this.world).add("x", this.x).add("y", this.y).add("z", this.z).toString();
    }
}
