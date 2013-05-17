// Generated by delombok at Fri May 17 11:49:19 CEST 2013
/**
 * Copyright (C) 2010-2013 Leon Blakey <lord.quackstar at gmail.com>
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PircBotX. If not, see <http://www.gnu.org/licenses/>.
 */
package org.pircbotx.hooks.events;

import org.pircbotx.hooks.Event;
import org.pircbotx.HeufyBot;
import org.pircbotx.UserSnapshot;
/**
 * This event is dispatched whenever someone (possibly us) quits from the
 * server. We will only observe this if the user was in one of the
 * channels to which we are connected.
 * @author Leon Blakey <lord.quackstar at gmail.com>
 */

public class QuitEvent<T extends HeufyBot> extends Event<T> {
	protected final UserSnapshot user;
	protected final String reason;
	/**
	 * Default constructor to setup object. Timestamp is automatically set
	 * to current time as reported by {@link System#currentTimeMillis() }
	 * @param user The user that quit from the server in snapshot form
	 * @param reason The reason given for quitting the server.
	 */
	public QuitEvent(T bot, UserSnapshot user, String reason) {
		super(bot);
		this.user = user;
		this.reason = reason;
	}
	/**
	 * Does NOT respond! This will throw an {@link UnsupportedOperationException}
	 * since we can't respond to a user that just quit
	 * @param response The response to send
	 */
	@Override
	public void respond(String response) {
		throw new UnsupportedOperationException("Attempting to respond to a user that quit");
	}
	
	@java.lang.SuppressWarnings("all")
	public UserSnapshot getUser() {
		return this.user;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getReason() {
		return this.reason;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "QuitEvent(user=" + this.getUser() + ", reason=" + this.getReason() + ")";
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof QuitEvent)) return false;
		final QuitEvent<?> other = (QuitEvent<?>)o;
		if (!other.canEqual((java.lang.Object)this)) return false;
		if (!super.equals(o)) return false;
		final java.lang.Object this$user = this.getUser();
		final java.lang.Object other$user = other.getUser();
		if (this$user == null ? other$user != null : !this$user.equals(other$user)) return false;
		final java.lang.Object this$reason = this.getReason();
		final java.lang.Object other$reason = other.getReason();
		if (this$reason == null ? other$reason != null : !this$reason.equals(other$reason)) return false;
		return true;
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof QuitEvent;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * PRIME + super.hashCode();
		final java.lang.Object $user = this.getUser();
		result = result * PRIME + ($user == null ? 0 : $user.hashCode());
		final java.lang.Object $reason = this.getReason();
		result = result * PRIME + ($reason == null ? 0 : $reason.hashCode());
		return result;
	}
}