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

import org.pircbotx.DccChat;
import org.pircbotx.hooks.Event;
import org.pircbotx.HeufyBot;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.types.GenericDCCEvent;
/**
 * This event will be dispatched whenever a DCC Chat request is received.
 * This means that a client has requested to chat to us directly rather
 * than via the IRC server. This is useful for sending many lines of text
 * to and from the bot without having to worry about flooding the server
 * or any operators of the server being able to "spy" on what is being
 * said. By default there are no {@link Listener} for this event,
 * which means that all DCC CHAT requests will be ignored by default.
 *  <p>
 * If you wish to accept the connection, then you listen for this event
 * and call the {@link DccChat#accept()} method, which
 * connects to the sender of the chat request and allows lines to be
 * sent to and from the bot.
 *  <p>
 * Your bot must be able to connect directly to the user that sent the
 * request.
 *  <p>
 * Example:
 * <pre>
 *     DccChat chat = event.getChat();
 *     // Accept all chat, whoever it's from.
 *     chat.accept();
 *     chat.sendLine("Hello");
 *     String response = chat.readLine();
 *     chat.close();
 * </pre>
 * @author Leon Blakey <lord.quackstar at gmail.com>
 * @see DccChat
 */

public class IncomingChatRequestEvent<T extends HeufyBot> extends Event<T> implements GenericDCCEvent<T> {
	protected final DccChat chat;
	/**
	 * Default constructor to setup object. Timestamp is automatically set
	 * to current time as reported by {@link System#currentTimeMillis() }
	 * @param chat A DccChat object that represents the incoming chat request.
	 */
	public IncomingChatRequestEvent(T bot, DccChat chat) {
		super(bot);
		this.chat = chat;
	}
	/**
	 * Respond with a <i>private message</i> to the user that sent the request, 
	 * <b>not a message over dcc</b> since it might not of been accepted yet
	 * @param response The response to send 
	 */
	@Override
	public void respond(String response) {
		getBot().sendMessage(getChat().getUser(), response);
	}
	
	@java.lang.SuppressWarnings("all")
	public DccChat getChat() {
		return this.chat;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "IncomingChatRequestEvent(chat=" + this.getChat() + ")";
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof IncomingChatRequestEvent)) return false;
		final IncomingChatRequestEvent<?> other = (IncomingChatRequestEvent<?>)o;
		if (!other.canEqual((java.lang.Object)this)) return false;
		if (!super.equals(o)) return false;
		if (this.getChat() == null ? other.getChat() != null : !this.getChat().equals((java.lang.Object)other.getChat())) return false;
		return true;
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof IncomingChatRequestEvent;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * PRIME + super.hashCode();
		result = result * PRIME + (this.getChat() == null ? 0 : this.getChat().hashCode());
		return result;
	}
}