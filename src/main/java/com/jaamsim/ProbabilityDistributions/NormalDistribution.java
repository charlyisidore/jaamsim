/*
 * JaamSim Discrete Event Simulation
 * Copyright (C) 2013 Ausenco Engineering Canada Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package com.jaamsim.ProbabilityDistributions;

import com.jaamsim.input.ValueInput;
import com.jaamsim.units.Unit;
import com.jaamsim.units.UserSpecifiedUnit;
import com.sandwell.JavaSimulation.Keyword;

/**
 * Normal Distribution.
 * Adapted from A.M. Law, "Simulation Modelling and Analysis, 4th Edition", page 453.
 * Polar Method, Marsaglia and Bray (1964)
 */
public class NormalDistribution extends Distribution {

	@Keyword(description = "The mean of the normal distribution (ignoring the MinValue and MaxValue keywords).",
	         example = "NormalDist-1 Mean { 5.0 }")
	private final ValueInput meanInput;

	@Keyword(description = "The standard deviation of the normal distribution (ignoring the MinValue and MaxValue keywords).",
	         example = "NormalDist-1 StandardDeviation { 2.0 }")
	private final ValueInput standardDeviationInput;

	{
		meanInput = new ValueInput("Mean", "Key Inputs", 0.0d);
		meanInput.setUnitType(UserSpecifiedUnit.class);
		this.addInput(meanInput, true);

		standardDeviationInput = new ValueInput("StandardDeviation", "Key Inputs", 1.0d);
		standardDeviationInput.setUnitType(UserSpecifiedUnit.class);
		standardDeviationInput.setValidRange(0.0d, Double.POSITIVE_INFINITY);
		this.addInput(standardDeviationInput, true);
	}

	@Override
	protected void setUnitType(Class<? extends Unit> specified) {
		super.setUnitType(specified);
		meanInput.setUnitType(specified);
		standardDeviationInput.setUnitType(specified);
	}

	@Override
	protected double getNextNonZeroSample() {

		// Loop until we have a random x-y coordinate in the unit circle
		double w, v1, v2, sample;
		do {
			v1 = 2.0 * randomGenerator1.nextDouble() - 1.0;
			v2 = 2.0 * randomGenerator2.nextDouble() - 1.0;
			w = ( v1 * v1 ) + ( v2 * v2 );
		} while( w > 1.0 || w == 0.0 );

		// Calculate the normalised random sample
		// (normally distributed with mode = 0 and standard deviation = 1)
		sample = v1 * Math.sqrt( -2.0 * Math.log( w ) / w );

		// Adjust for the desired mode and standard deviation
		return meanInput.getValue() + ( sample * standardDeviationInput.getValue() );
	}

	@Override
	protected double getMeanValue() {
		return meanInput.getValue();
	}

	@Override
	protected double getStandardDeviation() {
		return  standardDeviationInput.getValue();
	}
}
