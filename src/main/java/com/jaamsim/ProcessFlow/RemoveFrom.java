/*
 * JaamSim Discrete Event Simulation
 * Copyright (C) 2014 Ausenco Engineering Canada Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jaamsim.ProcessFlow;

import java.util.ArrayList;

import com.jaamsim.Samples.SampleConstant;
import com.jaamsim.Samples.SampleInput;
import com.jaamsim.basicsim.Entity;
import com.jaamsim.input.InterfaceEntityInput;
import com.jaamsim.input.Keyword;
import com.jaamsim.units.DimensionlessUnit;

public class RemoveFrom extends Unpack {

	@Keyword(description = "The maximum number of entities to remove from the container.",
	         exampleList = {"2", "DiscreteDistribution1", "this.attrib" })
	private final SampleInput numberOfEntities;

	@Keyword(description = "The next object to which the processed EntityContainer is passed.",
			exampleList = {"Queue1"})
	protected final InterfaceEntityInput<Linkable> nextForContainers;

	{
		numberOfEntities = new SampleInput("NumberOfEntities", "Key Inputs", new SampleConstant(1.0));
		numberOfEntities.setUnitType(DimensionlessUnit.class);
		numberOfEntities.setEntity(this);
		numberOfEntities.setValidRange(0, Double.POSITIVE_INFINITY);
		this.addInput(numberOfEntities);

		nextForContainers = new InterfaceEntityInput<>(Linkable.class, "NextForContainers", "Key Inputs", null);
		nextForContainers.setRequired(true);
		this.addInput(nextForContainers);
	}

	@Override
	protected void disposeContainer(EntityContainer c) {
		if( nextForContainers.getValue() != null )
			nextForContainers.getValue().addEntity(c);
	}

	@Override
	protected int getNumberToRemove() {
		return (int) numberOfEntities.getValue().getNextSample(this.getSimTime());
	}

	// LinkDisplayable
	@Override
	public ArrayList<Entity> getDestinationEntities() {
		ArrayList<Entity> ret = super.getDestinationEntities();
		Linkable l = nextForContainers.getValue();
		if (l != null && (l instanceof Entity)) {
			ret.add((Entity)l);
		}
		return ret;
	}

}
