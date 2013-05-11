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
package org.pircbotx;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
/**
 * Represents a User on the server. Contains all the available information about
 * the user as well as some useful delegate methods. 
 * status.
 * @since   PircBot 1.0.0
 * @author  Origionally by:
 *          <a href="http://www.jibble.org/">Paul James Mutton</a> for <a href="http://www.jibble.org/pircbot.php">PircBot</a>
 *          <p>Forked and Maintained by in <a href="http://pircbotx.googlecode.com">PircBotX</a>:
 *          Leon Blakey <lord.quackstar at gmail.com>
 */

public class User implements Comparable<User> {
	private String nick;
	private String realName = "";
	private String login = "";
	private String hostmask = "";
	private boolean away = false;
	private boolean ircop = false;
	private String server = "";
	private boolean identified = true;
	private int hops = 0;
	private final HeufyBot bot;
	protected final UUID uuid = UUID.randomUUID();
	
	protected User(HeufyBot bot, String nick) {
		this.bot = bot;
		this.nick = nick;
	}
	
	public void parseStatus(Channel chan, String prefix) {
		if (prefix.contains("@")) chan.ops.add(this);
		if (prefix.contains("+")) chan.voices.add(this);
		if (prefix.contains("%")) chan.halfOps.add(this);
		if (prefix.contains("~")) chan.owners.add(this);
		if (prefix.contains("&")) chan.superOps.add(this);
		setAway(prefix.contains("G")); //Assume here (H) if there is no G
		setIrcop(prefix.contains("*"));
	}
	
	void setNick(String nick) {
		//Replace nick in nick map
		synchronized (bot.userNickMap) {
			bot.userNickMap.remove(this.nick);
			bot.userNickMap.put(nick, this);
			this.nick = nick;
		}
	}
	
	public UserSnapshot generateSnapshot() {
		return new UserSnapshot(this);
	}
	/**
	 * Get all channels this user is a part of
	 * @return All channels this user is a part of
	 */
	public Set<Channel> getChannels() {
		return bot.getChannels(this);
	}
	/**
	 * Get all channels user has Operator status in
	 * Be careful when storing the result from this method as it may be out of date 
	 * by the time you use it again
	 * @return An <i>unmodifiable</i> Set (IE snapshot) of all channels Get all 
	 *         channels user has Operator status in
	 */
	public Set<Channel> getChannelsOpIn() {
		Set<Channel> channels = new HashSet<Channel>();
		for (Channel curChannel : bot.getChannels()) if (curChannel.isOp(this)) channels.add(curChannel);
		return Collections.unmodifiableSet(channels);
	}
	/**
	 * Get all channels user has Voice status in
	 * Be careful when storing the result from this method as it may be out of date 
	 * by the time you use it again
	 * @return An <i>unmodifiable</i> Set (IE snapshot) of all channels Get all 
	 *         channels user has Voice status in
	 */
	public Set<Channel> getChannelsVoiceIn() {
		Set<Channel> channels = new HashSet<Channel>();
		for (Channel curChannel : bot.getChannels()) if (curChannel.hasVoice(this)) channels.add(curChannel);
		return Collections.unmodifiableSet(channels);
	}
	/**
	 * Get all channels user has Owner status in
	 * Be careful when storing the result from this method as it may be out of date 
	 * by the time you use it again
	 * @return An <i>unmodifiable</i> Set (IE snapshot) of all channels Get all 
	 *         channels user has Owner status in
	 */
	public Set<Channel> getChannelsOwnerIn() {
		Set<Channel> channels = new HashSet<Channel>();
		for (Channel curChannel : bot.getChannels()) if (curChannel.isOwner(this)) channels.add(curChannel);
		return Collections.unmodifiableSet(channels);
	}
	/**
	 * Get all channels user has Half Operator status in
	 * Be careful when storing the result from this method as it may be out of date 
	 * by the time you use it again
	 * @return An <i>unmodifiable</i> Set (IE snapshot) of all channels Get all 
	 *         channels user has Half Operator status in
	 */
	public Set<Channel> getChannelsHalfOpIn() {
		Set<Channel> channels = new HashSet<Channel>();
		for (Channel curChannel : bot.getChannels()) if (curChannel.isHalfOp(this)) channels.add(curChannel);
		return Collections.unmodifiableSet(channels);
	}
	/**
	 * Get all channels user has Super Operator status in
	 * Be careful when storing the result from this method as it may be out of date 
	 * by the time you use it again
	 * @return An <i>unmodifiable</i> Set (IE snapshot) of all channels Get all 
	 *         channels user has Super Operator status in
	 */
	public Set<Channel> getChannelsSuperOpIn() {
		Set<Channel> channels = new HashSet<Channel>();
		for (Channel curChannel : bot.getChannels()) if (curChannel.isSuperOp(this)) channels.add(curChannel);
		return Collections.unmodifiableSet(channels);
	}
	/**
	 * Returns the result of calling the compareTo method on lowercased
	 * nicks. This is useful for sorting lists of User objects.
	 *
	 * @return the result of calling compareTo on lowercased nicks.
	 */
	@Override
	public int compareTo(User other) {
		return other.getNick().compareToIgnoreCase(getNick());
	}
	/**
	 * The exact server that this user is joined to
	 * @return The address of the server
	 */
	public String getServer() {
		return server;
	}
	/**
	 * The number of hops it takes to this user
	 * @return the hops
	 */
	public int getHops() {
		return hops;
	}
	
	public boolean isOped(Channel channel) {
		return channel.isOp(this) || channel.isOwner(this) || channel.isHalfOp(this);
	}
	
	@java.lang.SuppressWarnings("all")
	public String getNick() {
		return this.nick;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getRealName() {
		return this.realName;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getLogin() {
		return this.login;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getHostmask() {
		return this.hostmask;
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean isAway() {
		return this.away;
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean isIrcop() {
		return this.ircop;
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean isIdentified() {
		return this.identified;
	}
	
	@java.lang.SuppressWarnings("all")
	public HeufyBot getBot() {
		return this.bot;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "User(nick=" + this.getNick() + ", realName=" + this.getRealName() + ", login=" + this.getLogin() + ", hostmask=" + this.getHostmask() + ", away=" + this.isAway() + ", ircop=" + this.isIrcop() + ", server=" + this.getServer() + ", identified=" + this.isIdentified() + ", hops=" + this.getHops() + ", bot=" + this.getBot() + ", uuid=" + this.uuid + ")";
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof User)) return false;
		final User other = (User)o;
		if (!other.canEqual((java.lang.Object)this)) return false;
		if (this.getBot() == null ? other.getBot() != null : !this.getBot().equals((java.lang.Object)other.getBot())) return false;
		if (this.uuid == null ? other.uuid != null : !this.uuid.equals((java.lang.Object)other.uuid)) return false;
		return true;
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof User;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * PRIME + (this.getBot() == null ? 0 : this.getBot().hashCode());
		result = result * PRIME + (this.uuid == null ? 0 : this.uuid.hashCode());
		return result;
	}
	
	@java.lang.SuppressWarnings("all")
	void setRealName(final String realName) {
		this.realName = realName;
	}
	
	@java.lang.SuppressWarnings("all")
	void setLogin(final String login) {
		this.login = login;
	}
	
	@java.lang.SuppressWarnings("all")
	void setHostmask(final String hostmask) {
		this.hostmask = hostmask;
	}
	
	@java.lang.SuppressWarnings("all")
	void setAway(final boolean away) {
		this.away = away;
	}
	
	@java.lang.SuppressWarnings("all")
	void setIrcop(final boolean ircop) {
		this.ircop = ircop;
	}
	
	@java.lang.SuppressWarnings("all")
	void setServer(final String server) {
		this.server = server;
	}
	
	@java.lang.SuppressWarnings("all")
	void setIdentified(final boolean identified) {
		this.identified = identified;
	}
	
	@java.lang.SuppressWarnings("all")
	void setHops(final int hops) {
		this.hops = hops;
	}
}