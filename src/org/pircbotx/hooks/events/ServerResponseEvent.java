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

import org.pircbotx.hooks.Event;
import org.pircbotx.HeufyBot;
import org.pircbotx.ReplyConstants;
/**
 *This is called when we receive a numeric response from the
 * IRC server.
 *  <p>
 * Numerics in the range from 001 to 099 are used for client-server
 * connections only and should never travel between servers.  Replies
 * generated in response to commands are found in the range from 200
 * to 399.  Error replies are found in the range from 400 to 599.
 *  <p>
 * For example, we can use this event to discover the topic of a
 * channel when we join it.  If we join the channel #test which
 * has a topic of &quot;I am King of Test&quot; then the response
 * will be &quot;<code>PircBotX #test :I Am King of Test</code>&quot;
 * with a code of 332 to signify that this is a topic.
 * (This is just an example - note that listening for {@link TopicEvent} is an 
 * easier way of finding the topic for a channel). Check the IRC RFC for the 
 * full list of other command response codes.
 *  <p>
 * PircBotX uses the class ReplyConstants, which contains
 * constants that you may find useful here.
 *
 * @see ReplyConstants
 * @author Leon Blakey <lord.quackstar at gmail.com>
 */

public class ServerResponseEvent<T extends HeufyBot> extends Event<T> {
	protected final int code;
	protected final String response;
	/**
	 * Default constructor to setup object. Timestamp is automatically set
	 * to current time as reported by {@link System#currentTimeMillis() }
	 * @param code The three-digit numerical code for the response.
	 * @param response The full response from the IRC server.
	 */
	public ServerResponseEvent(T bot, int code, String response) {
		super(bot);
		this.code = code;
		this.response = response;
	}
	/**
	 * Respond with a <i>raw line</i> to the server
	 * @param response The response to send 
	 */
	@Override
	public void respond(String response) {
		getBot().sendRawLine(response);
	}
	
	@java.lang.SuppressWarnings("all")
	public int getCode() {
		return this.code;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getResponse() {
		return this.response;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "ServerResponseEvent(code=" + this.getCode() + ", response=" + this.getResponse() + ")";
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof ServerResponseEvent)) return false;
		final ServerResponseEvent<?> other = (ServerResponseEvent<?>)o;
		if (!other.canEqual((java.lang.Object)this)) return false;
		if (!super.equals(o)) return false;
		if (this.getCode() != other.getCode()) return false;
		if (this.getResponse() == null ? other.getResponse() != null : !this.getResponse().equals((java.lang.Object)other.getResponse())) return false;
		return true;
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof ServerResponseEvent;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * PRIME + super.hashCode();
		result = result * PRIME + this.getCode();
		result = result * PRIME + (this.getResponse() == null ? 0 : this.getResponse().hashCode());
		return result;
	}
}