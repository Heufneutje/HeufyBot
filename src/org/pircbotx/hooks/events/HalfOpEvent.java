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
import org.pircbotx.hooks.types.GenericUserModeEvent;
/**
 * Called when a user (possibly us) gets halfop status granted in a channel. Note
 * that this isn't supported on all servers or may be used for something else
 * <p>
 * This is a type of mode change and therefor is also dispatched in a
 * {@link org.pircbotx.hooks.events.ModeEvent}
 * @author Leon Blakey <lord.quackstar at gmail.com>
 */

public class HalfOpEvent<T extends HeufyBot> extends Event<T> implements GenericUserModeEvent<T> {
	protected final Channel channel;
	protected final User source;
	protected final User recipient;
	protected final boolean isHalfOp;
	/**
	 * Default constructor to setup object. Timestamp is automatically set
	 * to current time as reported by {@link System#currentTimeMillis() }
	 * @param channel The channel in which the mode change took place.
	 * @param source The user that performed the mode change.
	 * @param recipient The nick of the user that got 'voiced'.
	 */
	public HalfOpEvent(T bot, Channel channel, User source, User recipient, boolean isHalfOp) {
		super(bot);
		this.channel = channel;
		this.source = source;
		this.recipient = recipient;
		this.isHalfOp = isHalfOp;
	}
	/**
	 * Respond by send a message in the channel to the user that set the mode
	 * (<b>Warning:</b> not to the user that got halfop status!) in
	 * <code>user: message</code>
	 * format
	 * @param response The response to send
	 */
	@Override
	public void respond(String response) {
		getBot().sendMessage(getChannel(), getSource(), response);
	}
	
	@java.lang.SuppressWarnings("all")
	public Channel getChannel() {
		return this.channel;
	}
	
	@java.lang.SuppressWarnings("all")
	public User getSource() {
		return this.source;
	}
	
	@java.lang.SuppressWarnings("all")
	public User getRecipient() {
		return this.recipient;
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean isHalfOp() {
		return this.isHalfOp;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "HalfOpEvent(channel=" + this.getChannel() + ", source=" + this.getSource() + ", recipient=" + this.getRecipient() + ", isHalfOp=" + this.isHalfOp() + ")";
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof HalfOpEvent)) return false;
		final HalfOpEvent<?> other = (HalfOpEvent<?>)o;
		if (!other.canEqual((java.lang.Object)this)) return false;
		if (!super.equals(o)) return false;
		final java.lang.Object this$channel = this.getChannel();
		final java.lang.Object other$channel = other.getChannel();
		if (this$channel == null ? other$channel != null : !this$channel.equals(other$channel)) return false;
		final java.lang.Object this$source = this.getSource();
		final java.lang.Object other$source = other.getSource();
		if (this$source == null ? other$source != null : !this$source.equals(other$source)) return false;
		final java.lang.Object this$recipient = this.getRecipient();
		final java.lang.Object other$recipient = other.getRecipient();
		if (this$recipient == null ? other$recipient != null : !this$recipient.equals(other$recipient)) return false;
		if (this.isHalfOp() != other.isHalfOp()) return false;
		return true;
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof HalfOpEvent;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * PRIME + super.hashCode();
		final java.lang.Object $channel = this.getChannel();
		result = result * PRIME + ($channel == null ? 0 : $channel.hashCode());
		final java.lang.Object $source = this.getSource();
		result = result * PRIME + ($source == null ? 0 : $source.hashCode());
		final java.lang.Object $recipient = this.getRecipient();
		result = result * PRIME + ($recipient == null ? 0 : $recipient.hashCode());
		result = result * PRIME + (this.isHalfOp() ? 1231 : 1237);
		return result;
	}
}