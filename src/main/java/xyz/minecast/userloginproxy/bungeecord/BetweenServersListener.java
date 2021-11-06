package xyz.minecast.userloginproxy.bungeecord;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import xyz.minecast.userloginproxy.UserLoginConfig;

/*      UserLoginProxy
        Copyright (C) 2021 MineCastTeam

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

public class BetweenServersListener implements Listener {

    private final UserLoginBungee plugin;

    public BetweenServersListener(UserLoginBungee plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPluginMessage(final PluginMessageEvent event) {

        if (event.isCancelled()) {
            return;
        }

        if (!event.getTag().equals("userlogin:returned")) {
            return;
        }

        if (event.getSender() instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer)event.getSender();
            String formattedConsoleMessageKicked = UserLoginConfig.consoleMessageKicked.replace("{player}", player.getDisplayName());
            ProxyServer proxy = plugin.getProxy();
            // send message to all players and Bungeecord console
            proxy.broadcast(TextComponent.fromLegacyText(formattedConsoleMessageKicked));
            // disconnect player with reason
            player.disconnect(TextComponent.fromLegacyText(UserLoginConfig.playerMessageKicked));
        }

    }

    @EventHandler
    public void onJoin(ServerSwitchEvent event){

        if (!event.getPlayer().getServer().isConnected()) {
            return;
        }

        if (!UserLoginConfig.authServers.contains(event.getPlayer().getServer().getInfo().getName())) {
            return;
        }

        String uuid = event.getPlayer().getUniqueId().toString();
        if (event.getFrom() == null) {
            plugin.sendBungeecordPluginMsg(event.getPlayer().getServer(), uuid, false);
            return;
        }

        plugin.sendBungeecordPluginMsg(event.getPlayer().getServer(), uuid, true);

    }
}
