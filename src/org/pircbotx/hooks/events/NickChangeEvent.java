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

import org.pircbotx.User;
import org.pircbotx.hooks.Event;
import org.pircbotx.HeufyBot;
/**
 * This event is dispatched whenever someone (possibly us) changes nick on any
 * of the channels that we are on.
 * @author Leon Blakey <lord.quackstar at gmail.com>
 */

public class NickChangeEvent<T extends HeufyBot> extends Event<T> {
	protected final String oldNick;
	protected final String newNick;
	protected final User user;
	/**
	 * Default constructor to setup object. Timestamp is automatically set
	 * to current time as reported by {@link System#currentTimeMillis() }
	 * @param oldNick The old nick.
	 * @param newNick The new nick.
	 * @param user The user that changed their nick
	 */
	public NickChangeEvent(T bot, String oldNick, String newNick, User user) {
		super(bot);
		this.oldNick = oldNick;
		this.newNick = newNick;
		this.user = user;
	}
	/**
	 * Respond by sending a <i>private message</i> to the user's new nick
	 * @param response The response to send 
	 */
	@Override
	public void respond(String response) {
		getBot().sendMessage(getNewNick(), response);
	}
	
	@java.lang.SuppressWarnings("all")
	public String getOldNick() {
		return this.oldNick;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getNewNick() {
		return this.newNick;
	}
	
	@java.lang.SuppressWarnings("all")
	public User getUser() {
		return this.user;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "NickChangeEvent(oldNick=" + this.getOldNick() + ", newNick=" + this.getNewNick() + ", user=" + this.getUser() + ")";
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof NickChangeEvent)) return false;
		final NickChangeEvent<?> other = (NickChangeEvent<?>)o;
		if (!other.canEqual((java.lang.Object)this)) return false;
		if (!super.equals(o)) return false;
		if (this.getOldNick() == null ? other.getOldNick() != null : !this.getOldNick().equals((java.lang.Object)other.getOldNick())) return false;
		if (this.getNewNick() == null ? other.getNewNick() != null : !this.getNewNick().equals((java.lang.Object)other.getNewNick())) return false;
		if (this.getUser() == null ? other.getUser() != null : !this.getUser().equals((java.lang.Object)other.getUser())) return false;
		return true;
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof NickChangeEvent;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * PRIME + super.hashCode();
		result = result * PRIME + (this.getOldNick() == null ? 0 : this.getOldNick().hashCode());
		result = result * PRIME + (this.getNewNick() == null ? 0 : this.getNewNick().hashCode());
		result = result * PRIME + (this.getUser() == null ? 0 : this.getUser().hashCode());
		return result;
	}
}