/*
 * Copyright 2016 Sai Pullabhotla.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jmethods.catatumbo.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.cloud.datastore.Blob;
import com.google.cloud.datastore.Cursor;
import com.google.cloud.datastore.DateTime;
import com.google.cloud.datastore.GqlQuery;
import com.jmethods.catatumbo.DatastoreCursor;
import com.jmethods.catatumbo.DatastoreKey;
import com.jmethods.catatumbo.GeoLocation;

/**
 * Utility methods for GQL Queries.
 * 
 * @author Sai Pullabhotla
 *
 */
public class QueryUtils {

	/**
	 * Hide the default constructor.
	 */
	private QueryUtils() {
		// Do nothing
	}

	/**
	 * Applies the given positional bindings to the given query builder.
	 * 
	 * @param queryBuilder
	 *            the query builder
	 * @param positionalBindings
	 *            the positional bindings.
	 */
	static void applyPositionalBindings(GqlQuery.Builder<?> queryBuilder, Object... positionalBindings) {
		if (positionalBindings != null) {
			for (Object binding : positionalBindings) {
				addPositionalBinding(queryBuilder, binding);
			}
		}
	}

	/**
	 * Applies the given positional bindings to the given query builder.
	 * 
	 * @param queryBuilder
	 *            the query builder
	 * @param positionalBindings
	 *            the positional bindings.
	 */
	static void applyPositionalBindings(GqlQuery.Builder<?> queryBuilder, List<Object> positionalBindings) {
		if (positionalBindings != null) {
			for (Object binding : positionalBindings) {
				addPositionalBinding(queryBuilder, binding);
			}
		}
	}

	/**
	 * Adds the given binding to the given query builder's to the list of
	 * positional bindings.
	 * 
	 * @param queryBuilder
	 *            the query builder
	 * @param binding
	 *            the positional binding to add
	 */
	static void addPositionalBinding(GqlQuery.Builder<?> queryBuilder, Object binding) {
		if (binding == null) {
			throw new NullPointerException("Binding cannot be null. Use IS NULL in your query");
		}
		if (binding instanceof Short) {
			queryBuilder.addBinding((short) binding);
		} else if (binding instanceof Integer) {
			queryBuilder.addBinding((int) binding);
		} else if (binding instanceof Long) {
			queryBuilder.addBinding((long) binding);
		} else if (binding instanceof Float) {
			queryBuilder.addBinding((float) binding);
		} else if (binding instanceof Double) {
			queryBuilder.addBinding((double) binding);
		} else if (binding instanceof Boolean) {
			queryBuilder.addBinding((boolean) binding);
		} else if (binding instanceof Character) {
			queryBuilder.addBinding(String.valueOf((char) binding));
		} else if (binding instanceof String) {
			queryBuilder.addBinding((String) binding);
		} else if (binding instanceof Calendar) {
			queryBuilder.addBinding(DateTime.copyFrom((Calendar) binding));
		} else if (binding instanceof Date) {
			queryBuilder.addBinding(DateTime.copyFrom((Date) binding));
		} else if (binding instanceof byte[]) {
			queryBuilder.addBinding(Blob.copyFrom((byte[]) binding));
		} else if (binding instanceof DatastoreKey) {
			queryBuilder.addBinding(((DatastoreKey) binding).nativeKey());
		} else if (binding instanceof DatastoreCursor) {
			queryBuilder.addBinding(Cursor.fromUrlSafe(((DatastoreCursor) binding).getEncoded()));
		} else if (binding instanceof GeoLocation) {
			// @ToDo no support for GeoLocation in the gcloud API
		}
	}

	/**
	 * Applies the given positional bindings to the given query builder.
	 * 
	 * @param queryBuilder
	 *            the query builder
	 * @param namedBindings
	 *            the named bindings to apply
	 */
	static void applyNamedBindings(GqlQuery.Builder<?> queryBuilder, Map<String, Object> namedBindings) {
		if (namedBindings != null) {
			for (Map.Entry<String, Object> entry : namedBindings.entrySet()) {
				String bindingName = entry.getKey();
				Object bindingValue = entry.getValue();
				if (bindingValue instanceof Short) {
					queryBuilder.setBinding(bindingName, (short) bindingValue);
				} else if (bindingValue instanceof Integer) {
					queryBuilder.setBinding(bindingName, (int) bindingValue);
				} else if (bindingValue instanceof Long) {
					queryBuilder.setBinding(bindingName, (long) bindingValue);
				} else if (bindingValue instanceof Float) {
					queryBuilder.setBinding(bindingName, (float) bindingValue);
				} else if (bindingValue instanceof Double) {
					queryBuilder.setBinding(bindingName, (double) bindingValue);
				} else if (bindingValue instanceof Boolean) {
					queryBuilder.setBinding(bindingName, (boolean) bindingValue);
				} else if (bindingValue instanceof String) {
					queryBuilder.setBinding(bindingName, (String) bindingValue);
				} else if (bindingValue instanceof Calendar) {
					queryBuilder.setBinding(bindingName, DateTime.copyFrom((Calendar) bindingValue));
				} else if (bindingValue instanceof Date) {
					queryBuilder.setBinding(bindingName, DateTime.copyFrom((Date) bindingValue));
				} else if (bindingValue instanceof byte[]) {
					queryBuilder.setBinding(bindingName, Blob.copyFrom((byte[]) bindingValue));
				} else if (bindingValue instanceof DatastoreKey) {
					queryBuilder.setBinding(bindingName, ((DatastoreKey) bindingValue).nativeKey());
				} else if (bindingValue instanceof DatastoreCursor) {
					queryBuilder.setBinding(bindingName,
							Cursor.fromUrlSafe(((DatastoreCursor) bindingValue).getEncoded()));
				} else if (bindingValue instanceof GeoLocation) {
					// @ToDo no support for GeoLocation in the gcloud API
				}
			}
		}
	}

}
