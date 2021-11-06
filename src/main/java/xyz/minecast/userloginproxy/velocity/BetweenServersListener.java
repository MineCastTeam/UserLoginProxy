package xyz.minecast.userloginproxy.velocity;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import xyz.minecast.userloginproxy.UserLoginConfig;

import static xyz.minecast.userloginproxy.velocity.UserLoginVelocity.RETURNED_CHANNEL;

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

public class BetweenServersListener {

    private final UserLoginVelocity plugin;

    public BetweenServersListener(final UserLoginVelocity plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onPluginMessageEvent(PluginMessageEvent event) {

        if(!event.getIdentifier().equals(RETURNED_CHANNEL)){
            return;
        }

        event.setResult(PluginMessageEvent.ForwardResult.handled());
        if(event.getSource() instanceof Player){
            Player player = (Player)event.getSource();
            String formattedConsoleMessageKicked = UserLoginConfig.consoleMessageKicked.replace("{player}", player.getUsername());
            ProxyServer proxy = plugin.getProxy();
            // send message to all players and Velocity console
            proxy.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(formattedConsoleMessageKicked));
            proxy.getConsoleCommandSource().sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(formattedConsoleMessageKicked));
            // disconnect player with reason
            player.disconnect(LegacyComponentSerializer.legacyAmpersand().deserialize(UserLoginConfig.playerMessageKicked));
        }

    }

    @Subscribe(order = PostOrder.FIRST)
    public void onServerConnect(ServerPostConnectEvent event) {

        if (event.getPlayer().getCurrentServer().isEmpty()) {
            return;
        }

        if (!UserLoginConfig.authServers.contains(event.getPlayer().getCurrentServer().get().getServerInfo().getName())) {
            return;
        }

        String uuid = event.getPlayer().getUniqueId().toString();

        if (event.getPreviousServer() == null) {
            plugin.sendVelocityPluginMsg(event.getPlayer().getCurrentServer().get(), uuid, false);
            return;
        }

        plugin.sendVelocityPluginMsg(event.getPlayer().getCurrentServer().get(), uuid, true);

    }
}
