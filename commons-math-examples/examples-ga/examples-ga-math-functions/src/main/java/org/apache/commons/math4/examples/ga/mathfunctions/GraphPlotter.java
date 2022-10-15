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

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public final class GraphPlotter extends JFrame {

    /** Chart to display the data. */
    private JFreeChart chart;

    private static class Point {

        private double x;

        private double y;

        Point(double x, double y) {
            super();
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + " )";
        }
    }

    /** A collection of data. */
    private XYSeriesCollection dataset = new XYSeriesCollection();

    public GraphPlotter(String plotSubject, String xAxisLabel, String yAxisLabel) {
        super(plotSubject);

        JPanel chartPanel = createChartPanel(plotSubject, xAxisLabel, yAxisLabel);
        add(chartPanel, BorderLayout.CENTER);

        setSize(640, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setVisible(true);
    }

    public void addDataPoint(String graphName, int generation, double fitness) {
        addDataPoint(graphName, new Point(generation, fitness));
        setVisible(true);
    }

    public void saveAsImage(String filePath) {
        try {
            ChartUtilities.saveChartAsJPEG(new File(filePath), chart, 1920, 1080);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addDataPoint(String graphName, Point p) {
        XYSeries series = null;
        try {
            series = dataset.getSeries(graphName);
        } catch (Exception e) {
            series = new XYSeries(graphName);
            dataset.addSeries(series);
        }
        series.add(p.x, p.y);
    }

    private JPanel createChartPanel(String chartTitle, String xAxisLabel, String yAxisLabel) {

        boolean showLegend = true;
        boolean createURL = false;
        boolean createTooltip = false;

        chart = ChartFactory.createXYLineChart(chartTitle, xAxisLabel, yAxisLabel, dataset, PlotOrientation.VERTICAL,
                showLegend, createTooltip, createURL);
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        plot.setRenderer(renderer);

        return new ChartPanel(chart);

    }

}
