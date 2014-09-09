/*
 * JaamSim Discrete Event Simulation
 * Copyright (C) 2014 Ausenco Engineering Canada Inc.
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
package com.jaamsim.events;

/**
 * Holder class for event data used by the event monitor to schedule future
 * events.
 */
final class Event {
	EventNode node;
	ProcessTarget target;
	EventHandle handle;
	Event next;

	/**
	 * Constructs a new event object for the given node, target and (optional) handle
	 */
	Event(EventNode n, ProcessTarget t, EventHandle h) {
		node = n;
		target = t;
		handle = h;
	}
}
