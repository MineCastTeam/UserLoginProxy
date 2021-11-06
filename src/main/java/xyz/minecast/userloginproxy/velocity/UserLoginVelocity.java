package xyz.minecast.userloginproxy.velocity;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.ChannelMessageSink;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import org.slf4j.Logger;
import xyz.minecast.userloginproxy.UserLoginConfig;

import java.nio.file.Path;

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

@Plugin(
        id = "userloginproxy",
        name = "UserLoginProxy",
        version = "@version@",
        description = "Autologin feature for UserLogin, using Velocity proxy",
        authors = "BratishkaErik"
)
public class UserLoginVelocity {

    private static Logger logger;
    private static ProxyServer proxy;

    public static MinecraftChannelIdentifier RETURNED_CHANNEL = MinecraftChannelIdentifier.create("userlogin", "returned");

    @Inject
    private UserLoginVelocity(ProxyServer proxy, Logger logger, CommandManager commandManager, @DataDirectory Path configFolder) {
        UserLoginConfig.createConfig(configFolder);
        UserLoginVelocity.proxy = proxy;
        UserLoginVelocity.logger = logger;
        CommandMeta meta = commandManager.metaBuilder("userloginproxy")
                .aliases("ulp")
                .build();
        commandManager.register(meta, new ReloadCommand());
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        proxy.getChannelRegistrar().register(RETURNED_CHANNEL);
        proxy.getEventManager().register(this, new BetweenServersListener(this));
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent e) {
        proxy.getEventManager().unregisterListeners(this);
        proxy.getChannelRegistrar().unregister(RETURNED_CHANNEL);
    }


    public void sendVelocityPluginMsg(ChannelMessageSink receiver, String uuid, boolean returned) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(uuid);
        out.writeBoolean(returned);
        receiver.sendPluginMessage(RETURNED_CHANNEL, out.toByteArray());
    }

    public ProxyServer getProxy() {
        return proxy;
    }

}
