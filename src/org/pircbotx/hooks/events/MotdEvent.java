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
 * This event is dispatched when the Motd is finished being sent. Motd lines are
 * separated by <code>\n</code>
 * @author Leon Blakey <lord.quackstar at gmail.com>
 */

public class MotdEvent<T extends HeufyBot> extends Event<T> {
	protected final String motd;
	/**
	 * Default constructor to setup object. Timestamp is automatically set
	 * to current time as reported by {@link System#currentTimeMillis() }
	 * @param motd The full motd separated by newlines (<code>\n</code>)
	 */
	public MotdEvent(T bot, String motd) {
		super(bot);
		this.motd = motd;
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
	public String getMotd() {
		return this.motd;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "MotdEvent(motd=" + this.getMotd() + ")";
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof MotdEvent)) return false;
		final MotdEvent<?> other = (MotdEvent<?>)o;
		if (!other.canEqual((java.lang.Object)this)) return false;
		if (!super.equals(o)) return false;
		if (this.getMotd() == null ? other.getMotd() != null : !this.getMotd().equals((java.lang.Object)other.getMotd())) return false;
		return true;
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof MotdEvent;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * PRIME + super.hashCode();
		result = result * PRIME + (this.getMotd() == null ? 0 : this.getMotd().hashCode());
		return result;
	}
}