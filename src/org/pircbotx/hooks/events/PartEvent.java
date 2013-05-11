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
import org.pircbotx.hooks.Event;
import org.pircbotx.HeufyBot;
/**
 * This event is dispatched whenever someone (possibly us) parts a channel
 * which we are on.
 * @author Leon Blakey <lord.quackstar at gmail.com>
 */

public class PartEvent<T extends HeufyBot> extends Event<T> {
	protected final Channel channel;
	protected final User user;
	protected final String reason;
	/**
	 * Default constructor to setup object. Timestamp is automatically set
	 * to current time as reported by {@link System#currentTimeMillis() }
	 * @param channel The channel which somebody parted from.
	 * @param user The user who parted from the channel.
	 */
	public PartEvent(T bot, Channel channel, User user, String reason) {
		super(bot);
		this.channel = channel;
		this.user = user;
		this.reason = reason;
	}
	/**
	 * Respond by sending a message to the channel
	 * @param response The response to send 
	 */
	@Override
	public void respond(String response) {
		getBot().sendMessage(getChannel(), response);
	}
	
	@java.lang.SuppressWarnings("all")
	public Channel getChannel() {
		return this.channel;
	}
	
	@java.lang.SuppressWarnings("all")
	public User getUser() {
		return this.user;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getReason() {
		return this.reason;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "PartEvent(channel=" + this.getChannel() + ", user=" + this.getUser() + ", reason=" + this.getReason() + ")";
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof PartEvent)) return false;
		final PartEvent<?> other = (PartEvent<?>)o;
		if (!other.canEqual((java.lang.Object)this)) return false;
		if (!super.equals(o)) return false;
		if (this.getChannel() == null ? other.getChannel() != null : !this.getChannel().equals((java.lang.Object)other.getChannel())) return false;
		if (this.getUser() == null ? other.getUser() != null : !this.getUser().equals((java.lang.Object)other.getUser())) return false;
		if (this.getReason() == null ? other.getReason() != null : !this.getReason().equals((java.lang.Object)other.getReason())) return false;
		return true;
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof PartEvent;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * PRIME + super.hashCode();
		result = result * PRIME + (this.getChannel() == null ? 0 : this.getChannel().hashCode());
		result = result * PRIME + (this.getUser() == null ? 0 : this.getUser().hashCode());
		result = result * PRIME + (this.getReason() == null ? 0 : this.getReason().hashCode());
		return result;
	}
}