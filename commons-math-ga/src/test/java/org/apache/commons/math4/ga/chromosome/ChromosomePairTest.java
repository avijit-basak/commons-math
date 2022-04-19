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
package org.apache.commons.math4.ga.chromosome;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class ChromosomePairTest {

    @Test
    public void testChromosomePair() {
        AbstractChromosome<String> chromosome1 = new AbstractChromosome<String>(c -> 0, c -> "0") {
            @Override
            public double getFitness() {
                return 1;
            }
        };
        AbstractChromosome<String> chromosome2 = new AbstractChromosome<String>(c -> 1, c -> "1") {
            @Override
            public double getFitness() {
                return 1;
            }
        };
        ChromosomePair<String> chromosomePair = new ChromosomePair<>(chromosome1, chromosome2);

        Assertions.assertTrue(chromosome1.isSame((AbstractChromosome<String>) chromosomePair.getFirst()));
        Assertions.assertTrue(chromosome2.isSame((AbstractChromosome<String>) chromosomePair.getSecond()));
    }

}
