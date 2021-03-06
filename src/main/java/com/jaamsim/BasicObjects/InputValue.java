/*
 * JaamSim Discrete Event Simulation
 * Copyright (C) 2015 Ausenco Engineering Canada Inc.
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
package com.jaamsim.BasicObjects;

import java.util.ArrayList;

import com.jaamsim.Commands.KeywordCommand;
import com.jaamsim.Graphics.TextBasics;
import com.jaamsim.Samples.SampleProvider;
import com.jaamsim.input.Input;
import com.jaamsim.input.InputAgent;
import com.jaamsim.input.InputErrorException;
import com.jaamsim.input.Keyword;
import com.jaamsim.input.KeywordIndex;
import com.jaamsim.input.Output;
import com.jaamsim.input.Parser;
import com.jaamsim.input.UnitTypeInput;
import com.jaamsim.input.ValueInput;
import com.jaamsim.ui.GUIFrame;
import com.jaamsim.units.Unit;
import com.jaamsim.units.UserSpecifiedUnit;

public class InputValue extends TextBasics implements SampleProvider {

	@Keyword(description = "The unit type for the input value.",
	         exampleList = {"DistanceUnit"})
	protected final UnitTypeInput unitType;

	@Keyword(description = "The numerical value for the input.",
	         exampleList = {"1.5 km"})
	protected final ValueInput valInput;

	private boolean suppressUpdate = false; // prevents the white space in the edited text from changing

	{
		unitType = new UnitTypeInput("UnitType", "Key Inputs", UserSpecifiedUnit.class);
		unitType.setRequired(true);
		this.addInput(unitType);

		valInput = new ValueInput("Value", "Key Inputs", 0.0d);
		valInput.setUnitType(UserSpecifiedUnit.class);
		this.addInput(valInput);
	}

	public InputValue() {
		setSavedText(valInput.getDefaultString());
	}

	@Override
	public void updateForInput(Input<?> in) {
		super.updateForInput(in);

		if (in == unitType) {
			setUnitType(unitType.getUnitType());
			if (valInput.isDefault())
				setSavedText(valInput.getDefaultString());
			return;
		}

		if (in == valInput) {
			if (!suppressUpdate)
				setSavedText(valInput.getValueString());
			if (valInput.isDefault())
				setSavedText(valInput.getDefaultString());
			suppressUpdate = false;
			return;
		}
	}

	private void setUnitType(Class<? extends Unit> ut) {
		valInput.setUnitType(ut);
	}

	@Override
	public void acceptEdits() {
		try {
			suppressUpdate = true;
			ArrayList<String> tokens = new ArrayList<>();
			Parser.tokenize(tokens, getEditText(), true);
			KeywordIndex kw = new KeywordIndex(valInput.getKeyword(), tokens, null);
			InputAgent.storeAndExecute(new KeywordCommand(this, kw));
			super.acceptEdits();
		}
		catch (InputErrorException e) {
			GUIFrame.invokeErrorDialog("Input Error", e.getMessage());
			suppressUpdate = false;
		}
	}

	@Override
	public void handleSelectionLost() {
		super.handleSelectionLost();

		// Stop editing, even if the inputs were not accepted successfully
		cancelEdits();
	}

	@Override
	public Class<? extends Unit> getUnitType() {
		return unitType.getUnitType();
	}

	@Override
	public Class<? extends Unit> getUserUnitType() {
		return unitType.getUnitType();
	}

	@Output(name = "Value",
	        description = "The present value for this input.",
	        unitType = UserSpecifiedUnit.class)
	@Override
	public double getNextSample(double simTime) {
		return valInput.getValue();
	}

	@Override
	public double getMeanValue(double simTime) {
		return valInput.getValue();
	}

	@Override
	public double getMinValue() {
		return valInput.getValue();
	}

	@Override
	public double getMaxValue() {
		return valInput.getValue();
	}

}
