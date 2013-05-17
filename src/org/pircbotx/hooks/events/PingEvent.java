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

import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.Event;
import org.pircbotx.HeufyBot;
import org.pircbotx.hooks.CoreHooks;
import org.pircbotx.hooks.types.GenericCTCPCommand;
/**
 * This event is dispatched whenever we receive a PING request from another
 * user.
 * <p>
 * {@link CoreHooks} automatically responds correctly. Unless {@link CoreHooks}
 * is removed from the {@link HeufyBot#getListenerManager() bot's ListenerManager},
 * Listeners of this event should <b>not</b> send a response as the user will get
 * two responses
 * @author Leon Blakey <lord.quackstar at gmail.com>
 */

public class PingEvent<T extends HeufyBot> extends Event<T> implements GenericCTCPCommand<T> {
	protected final User user;
	protected final Channel channel;
	protected final String pingValue;
	/**
	 * Default constructor to setup object. Timestamp is automatically set
	 * to current time as reported by {@link System#currentTimeMillis() }
	 * @param user The user that sent the PING request.
	 * @param channel The channel that received the ping request. A value of <code>null</code>
	 * means the target was us.
	 * @param pingValue The value that was supplied as an argument to the PING command.
	 */
	public PingEvent(T bot, User user, Channel channel, String pingValue) {
		super(bot);
		this.user = user;
		this.channel = channel;
		this.pingValue = pingValue;
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
	
	@java.lang.SuppressWarnings("all")
	public String getPingValue() {
		return this.pingValue;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "PingEvent(user=" + this.getUser() + ", channel=" + this.getChannel() + ", pingValue=" + this.getPingValue() + ")";
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof PingEvent)) return false;
		final PingEvent<?> other = (PingEvent<?>)o;
		if (!other.canEqual((java.lang.Object)this)) return false;
		if (!super.equals(o)) return false;
		final java.lang.Object this$user = this.getUser();
		final java.lang.Object other$user = other.getUser();
		if (this$user == null ? other$user != null : !this$user.equals(other$user)) return false;
		final java.lang.Object this$channel = this.getChannel();
		final java.lang.Object other$channel = other.getChannel();
		if (this$channel == null ? other$channel != null : !this$channel.equals(other$channel)) return false;
		final java.lang.Object this$pingValue = this.getPingValue();
		final java.lang.Object other$pingValue = other.getPingValue();
		if (this$pingValue == null ? other$pingValue != null : !this$pingValue.equals(other$pingValue)) return false;
		return true;
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof PingEvent;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * PRIME + super.hashCode();
		final java.lang.Object $user = this.getUser();
		result = result * PRIME + ($user == null ? 0 : $user.hashCode());
		final java.lang.Object $channel = this.getChannel();
		result = result * PRIME + ($channel == null ? 0 : $channel.hashCode());
		final java.lang.Object $pingValue = this.getPingValue();
		result = result * PRIME + ($pingValue == null ? 0 : $pingValue.hashCode());
		return result;
	}
}