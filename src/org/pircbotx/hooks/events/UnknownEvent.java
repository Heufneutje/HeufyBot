// Generated by delombok at Thu Oct 04 13:11:43 CEST 2012
/**
 * Copyright (C) 2010-2011 Leon Blakey <lord.quackstar at gmail.com>
 *
 * This file is part of PircBotX.
 *
 * PircBotX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PircBotX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PircBotX.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.pircbotx.hooks.events;

import org.pircbotx.hooks.Event;
import org.pircbotx.HeufyBot;
/**
 * This event is dispatched whenever we receive a line from the server that
 * PircBotX has not been programmed to recognize.
 * @author Leon Blakey <lord.quackstar at gmail.com>
 */

public class UnknownEvent<T extends HeufyBot> extends Event<T> {
	protected final String line;
	/**
	 * Default constructor to setup object. Timestamp is automatically set
	 * to current time as reported by {@link System#currentTimeMillis() }
	 * @param line The raw line that was received from the server.
	 */
	public UnknownEvent(T bot, String line) {
		super(bot);
		this.line = line;
	}
	/**
	 * Responds by sending a <b>raw line</b> to the server.
	 * @param response The response to send 
	 */
	@Override
	public void respond(String response) {
		getBot().sendRawLine(response);
	}
	
	@java.lang.SuppressWarnings("all")
	public String getLine() {
		return this.line;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "UnknownEvent(line=" + this.getLine() + ")";
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof UnknownEvent)) return false;
		final UnknownEvent<?> other = (UnknownEvent<?>)o;
		if (!other.canEqual((java.lang.Object)this)) return false;
		if (!super.equals(o)) return false;
		if (this.getLine() == null ? other.getLine() != null : !this.getLine().equals((java.lang.Object)other.getLine())) return false;
		return true;
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof UnknownEvent;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * PRIME + super.hashCode();
		result = result * PRIME + (this.getLine() == null ? 0 : this.getLine().hashCode());
		return result;
	}
}