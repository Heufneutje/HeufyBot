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
/**
 * This event is dispatched whenever a user sets the topic, or when
 * we join a new channel and discovers its topic.
 * @author Leon Blakey <lord.quackstar at gmail.com>
 */

public class TopicEvent<T extends HeufyBot> extends Event<T> {
	protected final Channel channel;
	protected final String topic;
	protected final User user;
	protected final boolean changed;
	protected final long date;
	/**
	 * Default constructor to setup object. Timestamp is automatically set
	 * to current time as reported by {@link System#currentTimeMillis() }
	 * @param channel The channel that the topic belongs to.
	 * @param topic The topic for the channel.
	 * @param user The user that set the topic.
	 * @param date When the topic was set (milliseconds since the epoch).
	 * @param changed True if the topic has just been changed, false if
	 * the topic was already there.
	 */
	public TopicEvent(T bot, Channel channel, String topic, User user, long date, boolean changed) {
		super(bot);
		this.channel = channel;
		this.topic = topic;
		this.user = user;
		this.changed = changed;
		this.date = date;
	}
	/**
	 * Respond with a channel message in
	 * <code>user: message</code> format to the
	 * user that set the message
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
	public String getTopic() {
		return this.topic;
	}
	
	@java.lang.SuppressWarnings("all")
	public User getUser() {
		return this.user;
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean isChanged() {
		return this.changed;
	}
	
	@java.lang.SuppressWarnings("all")
	public long getDate() {
		return this.date;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "TopicEvent(channel=" + this.getChannel() + ", topic=" + this.getTopic() + ", user=" + this.getUser() + ", changed=" + this.isChanged() + ", date=" + this.getDate() + ")";
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof TopicEvent)) return false;
		final TopicEvent<?> other = (TopicEvent<?>)o;
		if (!other.canEqual((java.lang.Object)this)) return false;
		if (!super.equals(o)) return false;
		final java.lang.Object this$channel = this.getChannel();
		final java.lang.Object other$channel = other.getChannel();
		if (this$channel == null ? other$channel != null : !this$channel.equals(other$channel)) return false;
		final java.lang.Object this$topic = this.getTopic();
		final java.lang.Object other$topic = other.getTopic();
		if (this$topic == null ? other$topic != null : !this$topic.equals(other$topic)) return false;
		final java.lang.Object this$user = this.getUser();
		final java.lang.Object other$user = other.getUser();
		if (this$user == null ? other$user != null : !this$user.equals(other$user)) return false;
		if (this.isChanged() != other.isChanged()) return false;
		if (this.getDate() != other.getDate()) return false;
		return true;
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof TopicEvent;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * PRIME + super.hashCode();
		final java.lang.Object $channel = this.getChannel();
		result = result * PRIME + ($channel == null ? 0 : $channel.hashCode());
		final java.lang.Object $topic = this.getTopic();
		result = result * PRIME + ($topic == null ? 0 : $topic.hashCode());
		final java.lang.Object $user = this.getUser();
		result = result * PRIME + ($user == null ? 0 : $user.hashCode());
		result = result * PRIME + (this.isChanged() ? 1231 : 1237);
		final long $date = this.getDate();
		result = result * PRIME + (int)($date >>> 32 ^ $date);
		return result;
	}
}