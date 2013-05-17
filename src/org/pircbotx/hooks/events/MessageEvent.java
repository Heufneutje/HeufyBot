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
import org.pircbotx.hooks.types.GenericMessageEvent;
/**
 * Used whenever a message is sent to a channel.
 * @author Leon Blakey <lord.quackstar at gmail.com>
 */

public class MessageEvent<T extends HeufyBot> extends Event<T> implements GenericMessageEvent<T> {
	protected final Channel channel;
	protected final User user;
	protected final String message;
	/**
	 * Default constructor to setup object. Timestamp is automatically set
	 * to current time as reported by {@link System#currentTimeMillis() }
	 * @param channel The channel to which the message was sent.
	 * @param user The user who sent the message.
	 * @param message The actual message sent to the channel.
	 */
	public MessageEvent(T bot, Channel channel, User user, String message) {
		super(bot);
		this.channel = channel;
		this.user = user;
		this.message = message;
	}
	/**
	 * Respond with a channel message in
	 * <code>user: message</code> format to
	 * the user that sent the message
	 * @param response The response to send
	 */
	@Override
	public void respond(String response) {
		getBot().sendMessage(getChannel(), getUser(), response);
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
	public String getMessage() {
		return this.message;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "MessageEvent(channel=" + this.getChannel() + ", user=" + this.getUser() + ", message=" + this.getMessage() + ")";
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof MessageEvent)) return false;
		final MessageEvent<?> other = (MessageEvent<?>)o;
		if (!other.canEqual((java.lang.Object)this)) return false;
		if (!super.equals(o)) return false;
		final java.lang.Object this$channel = this.getChannel();
		final java.lang.Object other$channel = other.getChannel();
		if (this$channel == null ? other$channel != null : !this$channel.equals(other$channel)) return false;
		final java.lang.Object this$user = this.getUser();
		final java.lang.Object other$user = other.getUser();
		if (this$user == null ? other$user != null : !this$user.equals(other$user)) return false;
		final java.lang.Object this$message = this.getMessage();
		final java.lang.Object other$message = other.getMessage();
		if (this$message == null ? other$message != null : !this$message.equals(other$message)) return false;
		return true;
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof MessageEvent;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * PRIME + super.hashCode();
		final java.lang.Object $channel = this.getChannel();
		result = result * PRIME + ($channel == null ? 0 : $channel.hashCode());
		final java.lang.Object $user = this.getUser();
		result = result * PRIME + ($user == null ? 0 : $user.hashCode());
		final java.lang.Object $message = this.getMessage();
		result = result * PRIME + ($message == null ? 0 : $message.hashCode());
		return result;
	}
}