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

package org.apache.commons.math4.examples.ga.mathfunctions;

import org.apache.commons.math4.ga.GeneticAlgorithm;
import org.apache.commons.math4.ga.chromosome.BinaryChromosome;
import org.apache.commons.math4.ga.chromosome.Chromosome;
import org.apache.commons.math4.ga.convergence.StoppingCondition;
import org.apache.commons.math4.ga.convergence.UnchangedBestFitness;
import org.apache.commons.math4.ga.crossover.OnePointBinaryCrossover;
import org.apache.commons.math4.ga.internal.stats.PopulationStatisticalSummaryImpl;
import org.apache.commons.math4.ga.listener.ConvergenceListener;
import org.apache.commons.math4.ga.listener.PopulationStatisticsLogger;
import org.apache.commons.math4.ga.mutation.BinaryMutation;
import org.apache.commons.math4.ga.population.ListPopulation;
import org.apache.commons.math4.ga.population.Population;
import org.apache.commons.math4.ga.selection.TournamentSelection;
import org.apache.commons.math4.ga.stats.PopulationStatisticalSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents an optimizer for a 2-dimensional math function using
 * genetic algorithm.
 */

public final class MathFunctionOptimizer {
    /** length of chromosome. **/
    private static final int CHROMOSOME_LENGTH_PER_DIMENSION = 12;
    /** instance of logger. **/
    private final Logger logger = LoggerFactory.getLogger(MathFunctionOptimizer.class);

    /**
     * Optimizes the population.
     * @param dimension                               dimension
     * @param crossoverRate                           crossover rate
     * @param mutationRate                            mutation rate
     * @param elitismRate                             elitism rate
     * @param tournamentSize                          tournament size
     * @param generationCountWithUnchangedBestFitness no of generation evolved with
     *                                                unchanged best fitness
     * @param populationSize                          size of population
     * @param graphPlotter                            graphPlotter
     */
    public void optimize(int dimension,
            double crossoverRate,
            double mutationRate,
            double elitismRate,
            int tournamentSize,
            int generationCountWithUnchangedBestFitness,
            int populationSize,
            GraphPlotter graphPlotter,
            String graphName) {

        // initialize a new genetic algorithm
        final GeneticAlgorithm<Coordinates> ga = new GeneticAlgorithm<Coordinates>(
                new OnePointBinaryCrossover<Coordinates>(), crossoverRate, new BinaryMutation<Coordinates>(),
                mutationRate, new TournamentSelection<Coordinates>(tournamentSize), elitismRate,
                new GraphicalLogger<Coordinates>(graphPlotter, graphName));

        // stopping condition
        final StoppingCondition<Coordinates> stopCond = new UnchangedBestFitness<>(
                generationCountWithUnchangedBestFitness);

        // run the algorithm
        final Population<Coordinates> finalPopulation = ga.evolve(getInitialPopulation(dimension, populationSize),
                stopCond, Runtime.getRuntime().availableProcessors());

        // best chromosome from the final population
        final Chromosome<Coordinates> bestFinal = finalPopulation.getFittestChromosome();

        logger.info(bestFinal.toString());
    }

    private static Population<Coordinates> getInitialPopulation(int dimension, int populationSize) {
        final Population<Coordinates> population = new ListPopulation<>(populationSize);

        for (int i = 0; i < populationSize; i++) {
            population.addChromosome(BinaryChromosome
                    .<Coordinates>randomChromosome(dimension * CHROMOSOME_LENGTH_PER_DIMENSION, coordinate -> {
                        double sumOfSquare = 0.0;
                        for (double value : coordinate.getValues()) {
                            sumOfSquare += Math.pow(value - 10, 2);
                        }
                        return -Math.pow(sumOfSquare, .25) *
                                (Math.pow(Math.sin(50 * Math.pow(sumOfSquare, .1)), 2) + 1);

                    }, chromosome -> {
                            final BinaryChromosome<Coordinates> binaryChromosome =
                                    (BinaryChromosome<Coordinates>) chromosome;
                            final long length = binaryChromosome.getLength();
                            final double[] coordinates = new double[dimension];
                            for (int j = 0; j < dimension; j++) {
                                final int start = j * CHROMOSOME_LENGTH_PER_DIMENSION;
                                final String dimensionStrValue =
                                    binaryChromosome.getStringRepresentation(start,
                                                                             start + CHROMOSOME_LENGTH_PER_DIMENSION);
                                coordinates[j] = Integer.parseInt(dimensionStrValue, 2) / 100d;
                            }

                            return new Coordinates(coordinates);
                        }));
        }
        return population;
    }
}

class GraphicalLogger<P> implements ConvergenceListener<P> {

    /** instance of log4j logger. **/
    private static final Logger LOGGER = LoggerFactory.getLogger(PopulationStatisticsLogger.class);

    private GraphPlotter graphPlotter;
    
    private String graphName;
    
    public GraphicalLogger(GraphPlotter graphPlotter, String graphName) {
    	this.graphPlotter = graphPlotter;
    	this.graphName = graphName;
	}
    
    /**
     * Logs the population statistics during the process of convergence.
     */
    @Override
    public void notify(int generation, Population<P> population) {
        final PopulationStatisticalSummary<P> populationStatisticalSummary = new PopulationStatisticalSummaryImpl<>(
                population);
        LOGGER.info(
                "Population statistics for generation {} ::: Mean Fitness: {}, Max Fitness: {}, Fitness Variance: {}",
                generation, populationStatisticalSummary.getMeanFitness(), populationStatisticalSummary.getMaxFitness(),
                populationStatisticalSummary.getFitnessVariance());
        graphPlotter.addDataPoint(graphName, generation, Math.abs(populationStatisticalSummary.getMaxFitness()));
    }
}

