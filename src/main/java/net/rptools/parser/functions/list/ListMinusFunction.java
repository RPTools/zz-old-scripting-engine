/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 * 
 */
package net.rptools.parser.functions.list;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import net.rptools.lib.datavalue.DataType;
import net.rptools.lib.datavalue.DataValue;
import net.rptools.lib.datavalue.DataValueFactory;
import net.rptools.parser.ScriptContext;
import net.rptools.parser.functions.FunctionDefinition;
import net.rptools.parser.functions.FunctionDefinitionBuilder;
import net.rptools.parser.functions.ScriptFunction;

/**
 * Implements the list.union script function.
 *
 * This function takes multiple lists and removes the items in the second and subsequent
 * lists from the items in the first.
 *
 */
public class ListMinusFunction implements ScriptFunction {
	
	/** The singleton instance. */
	private static final ListMinusFunction INSTANCE = new ListMinusFunction();
	
	/** The function definition for the list script function. */
	private FunctionDefinition functionDefinition;
	
	/**
	 * Creates a new LsitFunction. 
	 */
	private ListMinusFunction() {
		functionDefinition = new FunctionDefinitionBuilder().setName("list.minus")
                .setReturnType(DataType.LIST).addListVarargsParameter("values").toFunctionDefinition();
	}

	/**
	 * Gets the singleton instance for the ListFunction.
	 * 
	 * @return the instance of ListFunction.
	 */
	public static ListMinusFunction getListFunction() {
		return INSTANCE;
	}

	@Override
	public FunctionDefinition getDefinition() {
		return functionDefinition;
	}

	@Override
	public DataValue call(ScriptContext context, Map<String, DataValue> args) {
		DataValue values = args.get("values");
		if (values.asList().size() == 0) {
			return DataValueFactory.listValue(new ArrayList<DataValue>());
		}
		
		Set<DataValue> result = new LinkedHashSet<>();
		
		// First add all of the first list
		result.addAll(values.asList().get(0).asList());
		
		boolean firstTime = true;
		for (DataValue list : values.asList()) {
			if (firstTime) {
				firstTime = false;
			} else {
				// Remove the values in the other lists
				result.removeAll(list.asList());
			}
		}
		return DataValueFactory.listValue(result);

	}
	
}
