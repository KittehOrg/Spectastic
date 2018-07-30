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

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
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

public class PastLocationData extends AbstractData<PastLocationData, ImmutablePastLocationData> {
    // TODO improve
    public static final String FALLBACK_WORLD = "world";
    public static final double FALLBACK_X = 0;
    public static final double FALLBACK_Y = 70;
    public static final double FALLBACK_Z = 0;

    public static int VERSION = 1;

    private String world;
    private double x, y, z;

    public PastLocationData() {
        this(FALLBACK_WORLD, FALLBACK_X, FALLBACK_Y, FALLBACK_Z);
    }

    public PastLocationData(String world, double x, double y, double z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.registerGettersAndSetters();
    }

    @Nonnull
    public Value<String> world() {
        return Sponge.getRegistry().getValueFactory().createValue(Spectastic.PAST_LOCATION_WORLD, FALLBACK_WORLD, this.world);
    }

    @Nonnull
    public Value<Double> x() {
        return Sponge.getRegistry().getValueFactory().createValue(Spectastic.PAST_LOCATION_X, FALLBACK_X, this.x);
    }

    @Nonnull
    public Value<Double> y() {
        return Sponge.getRegistry().getValueFactory().createValue(Spectastic.PAST_LOCATION_Y, FALLBACK_Y, this.y);
    }

    @Nonnull
    public Value<Double> z() {
        return Sponge.getRegistry().getValueFactory().createValue(Spectastic.PAST_LOCATION_Z, FALLBACK_Z, this.z);
    }

    @Override
    protected void registerGettersAndSetters() {
        this.registerFieldGetter(Spectastic.PAST_LOCATION_WORLD, () -> this.world);
        this.registerFieldSetter(Spectastic.PAST_LOCATION_WORLD, value -> this.world = Preconditions.checkNotNull(value));
        this.registerKeyValue(Spectastic.PAST_LOCATION_WORLD, this::world);

        this.registerFieldGetter(Spectastic.PAST_LOCATION_X, () -> this.x);
        this.registerFieldSetter(Spectastic.PAST_LOCATION_X, value -> this.x = Preconditions.checkNotNull(value));
        this.registerKeyValue(Spectastic.PAST_LOCATION_X, this::x);

        this.registerFieldGetter(Spectastic.PAST_LOCATION_Y, () -> this.y);
        this.registerFieldSetter(Spectastic.PAST_LOCATION_Y, value -> this.y = Preconditions.checkNotNull(value));
        this.registerKeyValue(Spectastic.PAST_LOCATION_Y, this::y);

        this.registerFieldGetter(Spectastic.PAST_LOCATION_Z, () -> this.z);
        this.registerFieldSetter(Spectastic.PAST_LOCATION_Z, value -> this.z = Preconditions.checkNotNull(value));
        this.registerKeyValue(Spectastic.PAST_LOCATION_Z, this::z);
    }

    @Nonnull
    @Override
    public Optional<PastLocationData> fill(@Nonnull DataHolder dataHolder, @Nonnull MergeFunction overlap) {
        return Optional.empty(); // YOLO
    }

    @Nonnull
    @Override
    public Optional<PastLocationData> from(@Nonnull DataContainer container) {
        if (!container.contains(Spectastic.PAST_LOCATION_WORLD.getQuery()) ||
                !container.contains(Spectastic.PAST_LOCATION_X.getQuery()) ||
                !container.contains(Spectastic.PAST_LOCATION_Y.getQuery()) ||
                !container.contains(Spectastic.PAST_LOCATION_Z.getQuery())) {
            return Optional.empty();
        }
        this.world = container.getString(Spectastic.PAST_LOCATION_WORLD.getQuery()).get();
        this.x = container.getDouble(Spectastic.PAST_LOCATION_X.getQuery()).get();
        this.y = container.getDouble(Spectastic.PAST_LOCATION_Y.getQuery()).get();
        this.z = container.getDouble(Spectastic.PAST_LOCATION_Z.getQuery()).get();
        return Optional.of(this);
    }

    @Nonnull
    @Override
    public PastLocationData copy() {
        return new PastLocationData(this.world, this.x, this.y, this.z);
    }

    @Nonnull
    @Override
    public ImmutablePastLocationData asImmutable() {
        return new ImmutablePastLocationData(this.world, this.x, this.y, this.z);
    }

    @Override
    public int getContentVersion() {
        return VERSION;
    }

    @Nonnull
    @Override
    public DataContainer toContainer() {
        return super.toContainer().set(Spectastic.PAST_LOCATION_WORLD, this.world).set(Spectastic.PAST_LOCATION_X, this.x).set(Spectastic.PAST_LOCATION_Y, this.y).set(Spectastic.PAST_LOCATION_Z, this.z);
    }

    @Nonnull
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("world", this.world).add("x", this.x).add("y", this.y).add("z", this.z).toString();
    }
}
