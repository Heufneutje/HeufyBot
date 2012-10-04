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
 * Used when the mode of a channel is set.
 *  <p>
 * You may find it more convenient to decode the meaning of the mode
 * string by using instead {@link OpEvent}, {@link VoiceEvent},
 * {@link SetChannelKeyEvent}, {@link RemoveChannelKeyEvent},
 * {@link SetChannelLimitEvent}, {@link RemoveChannelLimitEvent}, 
 * {@link SetChannelBanEvent} or {@link RemoveChannelBanEvent} as appropriate.
 *  <p>
 * @author Leon Blakey <lord.quackstar at gmail.com>
 */

public class ModeEvent<T extends HeufyBot> extends Event<T> {
	protected final Channel channel;
	protected final User user;
	protected final String mode;
	/**
	 * Default constructor to setup object. Timestamp is automatically set
	 * to current time as reported by {@link System#currentTimeMillis() }
	 * @param channel The channel that the mode operation applies to.
	 * @param user The user that set the mode.
	 * @param mode The mode that has been set.
	 */
	public ModeEvent(T bot, Channel channel, User user, String mode) {
		super(bot);
		this.channel = channel;
		this.user = user;
		this.mode = mode;
	}
	/**
	 * Respond by send a message in the channel to the user that set the mode
	 * in <code>user: message</code> format
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
	public String getMode() {
		return this.mode;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "ModeEvent(channel=" + this.getChannel() + ", user=" + this.getUser() + ", mode=" + this.getMode() + ")";
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof ModeEvent)) return false;
		final ModeEvent<?> other = (ModeEvent<?>)o;
		if (!other.canEqual((java.lang.Object)this)) return false;
		if (!super.equals(o)) return false;
		if (this.getChannel() == null ? other.getChannel() != null : !this.getChannel().equals((java.lang.Object)other.getChannel())) return false;
		if (this.getUser() == null ? other.getUser() != null : !this.getUser().equals((java.lang.Object)other.getUser())) return false;
		if (this.getMode() == null ? other.getMode() != null : !this.getMode().equals((java.lang.Object)other.getMode())) return false;
		return true;
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof ModeEvent;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * PRIME + super.hashCode();
		result = result * PRIME + (this.getChannel() == null ? 0 : this.getChannel().hashCode());
		result = result * PRIME + (this.getUser() == null ? 0 : this.getUser().hashCode());
		result = result * PRIME + (this.getMode() == null ? 0 : this.getMode().hashCode());
		return result;
	}
}