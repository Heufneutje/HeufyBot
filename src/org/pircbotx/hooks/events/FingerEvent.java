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

import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.CoreHooks;
import org.pircbotx.hooks.Event;
import org.pircbotx.HeufyBot;
import org.pircbotx.hooks.types.GenericCTCPCommand;
/**
 * This event is dispatched whenever we receive a FINGER request.
 *  <p>
 * {@link CoreHooks} automatically responds correctly. Unless {@link CoreHooks}
 * is removed from the {@link HeufyBot#getListenerManager() bot's ListenerManager},
 * Listeners of this event should <b>not</b> send a response as the user will get
 * two responses
 * @author Leon Blakey <lord.quackstar at gmail.com>
 */

public class FingerEvent<T extends HeufyBot> extends Event<T> implements GenericCTCPCommand<T> {
	protected final User user;
	protected final Channel channel;
	/**
	 * Default constructor to setup object. Timestamp is automatically set
	 * to current time as reported by {@link System#currentTimeMillis() }
	 * @param user The user that sent the FINGER request.
	 * @param channel The target channel of the FINGER request 
	 */
	public FingerEvent(T bot, User user, Channel channel) {
		super(bot);
		this.user = user;
		this.channel = channel;
	}
	/**
	 * Respond with a CTCP response to the user
	 * @param response The response to send 
	 */
	@Override
	public void respond(String response) {
		getBot().sendCTCPResponse(getUser(), response);
	}
	
	@java.lang.SuppressWarnings("all")
	public User getUser() {
		return this.user;
	}
	
	@java.lang.SuppressWarnings("all")
	public Channel getChannel() {
		return this.channel;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "FingerEvent(user=" + this.getUser() + ", channel=" + this.getChannel() + ")";
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof FingerEvent)) return false;
		final FingerEvent<?> other = (FingerEvent<?>)o;
		if (!other.canEqual((java.lang.Object)this)) return false;
		if (!super.equals(o)) return false;
		if (this.getUser() == null ? other.getUser() != null : !this.getUser().equals((java.lang.Object)other.getUser())) return false;
		if (this.getChannel() == null ? other.getChannel() != null : !this.getChannel().equals((java.lang.Object)other.getChannel())) return false;
		return true;
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof FingerEvent;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * PRIME + super.hashCode();
		result = result * PRIME + (this.getUser() == null ? 0 : this.getUser().hashCode());
		result = result * PRIME + (this.getChannel() == null ? 0 : this.getChannel().hashCode());
		return result;
	}
}