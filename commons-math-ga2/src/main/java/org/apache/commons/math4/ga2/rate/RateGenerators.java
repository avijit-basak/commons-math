/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.math4.ga2.rate;

import org.apache.commons.math4.ga2.ApplicationRate;

/**
 * Rate generator factory.
 */
public final class RateGenerators {
    /** Prevent instantiation of utility class. */
    private RateGenerators() {}

    /**
     * Creates a generator that always returns the given {@code value}.
     *
     * @param value Probability.
     * @return a new instance.
     */
    public static ApplicationRate constant(double value) {
        return new ConstantRate(value);
    }

    /**
     * Creates a generator that returns a linear interpolation.
     *
     * @param min Minimum probability.
     * @param max Maximum probability.
     * @return a new instance.
     */
    public static ApplicationRate averageRankLinear(double min,
                                                    double max) {
        return new AverageRankLinearRate(min, max);
    }
}
