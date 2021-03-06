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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Button;
import java.util.Map;
import java.util.Objects;

import org.junit.Test;

import com.jmethods.catatumbo.EntityManagerException;
import com.jmethods.catatumbo.entities.Cat;
import com.jmethods.catatumbo.entities.Contact;
import com.jmethods.catatumbo.entities.Customer;
import com.jmethods.catatumbo.entities.OptimisticLock1;
import com.jmethods.catatumbo.entities.OptimisticLockBad1;
import com.jmethods.catatumbo.entities.OptimisticLockBad2;
import com.jmethods.catatumbo.entities.StringField;
import com.jmethods.catatumbo.entities.StringId;
import com.jmethods.catatumbo.entities.Task;
import com.jmethods.catatumbo.entities.TaskName;

/**
 * @author Sai Pullabhotla
 *
 */
public class EntityIntrospectorTest {

	@Test
	public void testIntrospect_Embedded() {
		EntityMetadata metadata = EntityIntrospector.introspect(Customer.class);
		System.out.println(metadata);
		System.out.println("************");
		metadata = EntityIntrospector.introspect(Customer.class);
		System.out.println(metadata);

	}

	@Test
	public void testIntrospect_OptimisticLock1() {
		EntityMetadata metadata = EntityIntrospector.introspect(OptimisticLock1.class);
		assertTrue(Objects.equals(metadata.getVersionMetadata(), metadata.getPropertyMetadata("version")));
	}

	@Test(expected = EntityManagerException.class)
	public void testIntrospect_OptimisticLock_StringField() {
		EntityMetadata metadata = EntityIntrospector.introspect(OptimisticLockBad1.class);
	}

	@Test(expected = EntityManagerException.class)
	public void testIntrospect_OptimisticLock_TwoVersionFields() {
		EntityMetadata metadata = EntityIntrospector.introspect(OptimisticLockBad2.class);
	}

	@Test
	public void testIntrospect_Listeners1() {
		EntityMetadata metadata = EntityIntrospector.introspect(StringField.class);
		assertNull(metadata.getEntityListenersMetadata().getCallbacks());
	}

	@Test
	public void testIntrospect_Listeners2() {
		EntityMetadata metadata = EntityIntrospector.introspect(Cat.class);
		EntityListenersMetadata elm = metadata.getEntityListenersMetadata();
		assertEquals(1, elm.getCallbacks().size());
		assertEquals(3, elm.getCallbacks(CallbackType.PRE_INSERT).size());
	}

	@Test
	public void testIntrospect_MultiThreaded() {
		class MyRunnable implements Runnable {
			private Class<?> clazz;

			public MyRunnable(Class<?> clazz) {
				this.clazz = clazz;
			}

			@Override
			public void run() {
				EntityMetadata metadata = EntityIntrospector.introspect(clazz);
				System.out.println(metadata);
			}
		}
		;
		Thread[] threads = new Thread[4];
		Class<?>[] entityClasses = { StringId.class, StringField.class, StringId.class, StringField.class };
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new MyRunnable(entityClasses[i]));
			threads[i].start();
		}
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testIntrospect_Embedded_Imploded() {
		EntityMetadata entityMetadata = EntityIntrospector.introspect(Contact.class);
		Map<EmbeddedField, EmbeddedMetadata> embeddedMetadataMap = entityMetadata.getEmbeddedMetadataMap();
		assertEquals(3, embeddedMetadataMap.size());
	}

	@Test
	public void testIntrospect_Task() {
		EntityMetadata entityMetadata = EntityIntrospector.introspect(Task.class);
		assertEquals("Task", entityMetadata.getKind());
		assertEquals(4, entityMetadata.getPropertyMetadataCollection().size());
	}

	@Test
	public void testIntrospect_TaskName() {
		EntityMetadata entityMetadata = EntityIntrospector.introspect(TaskName.class);
		assertEquals("Task", entityMetadata.getKind());
		assertEquals(1, entityMetadata.getPropertyMetadataCollection().size());
	}

	@Test(expected = EntityManagerException.class)
	public void testIntrospect_Button() {
		try {
			EntityMetadata entityMetadata = EntityIntrospector.introspect(Button.class);
		} catch (EntityManagerException exp) {
			System.err.println(exp.getMessage());
			throw exp;
		}
	}

}
