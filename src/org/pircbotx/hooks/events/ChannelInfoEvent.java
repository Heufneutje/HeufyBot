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

import java.util.Set;
import org.pircbotx.HeufyBot;
import org.pircbotx.ChannelListEntry;
import org.pircbotx.hooks.Event;
/**
 * After calling the listChannels() method in PircBotX, the server
 * will start to send us information about each channel on the
 * server. You may listen for this event in order to receive the
 * information about each channel as soon as it is received.
 * <p>
 * Note that certain channels, such as those marked as hidden,
 * may not appear in channel listings.
 * @author Leon Blakey <lord.quackstar at gmail.com>
 * @see HeufyBot#listChannels()
 * @see HeufyBot#listChannels(java.lang.String)
 */

public class ChannelInfoEvent<T extends HeufyBot> extends Event<T> {
	protected final Set<ChannelListEntry> list;
	/**
	 * Default constructor to setup object. Timestamp is automatically set
	 * to current time as reported by {@link System#currentTimeMillis() }
	 * @param list A list of ChannelList Entries
	 */
	public ChannelInfoEvent(T bot, Set<ChannelListEntry> list) {
		super(bot);
		this.list = list;
	}
	/**
	 * Respond by sending a <b>raw line</b> to the server.
	 * @param response The response to send
	 */
	@Override
	public void respond(String response) {
		getBot().sendRawLine(response);
	}
	
	@java.lang.SuppressWarnings("all")
	public Set<ChannelListEntry> getList() {
		return this.list;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "ChannelInfoEvent(list=" + this.getList() + ")";
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof ChannelInfoEvent)) return false;
		final ChannelInfoEvent<?> other = (ChannelInfoEvent<?>)o;
		if (!other.canEqual((java.lang.Object)this)) return false;
		if (!super.equals(o)) return false;
		final java.lang.Object this$list = this.getList();
		final java.lang.Object other$list = other.getList();
		if (this$list == null ? other$list != null : !this$list.equals(other$list)) return false;
		return true;
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof ChannelInfoEvent;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * PRIME + super.hashCode();
		final java.lang.Object $list = this.getList();
		result = result * PRIME + ($list == null ? 0 : $list.hashCode());
		return result;
	}
}