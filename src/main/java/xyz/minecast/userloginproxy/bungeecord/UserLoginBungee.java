package xyz.minecast.userloginproxy.bungeecord;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Plugin;
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

public final class UserLoginBungee extends Plugin {

    @Override
    public void onEnable() {
        UserLoginConfig.createConfig(getDataFolder().toPath());
        getProxy().registerChannel("userlogin:returned");
        getProxy().getPluginManager().registerListener(this, new BetweenServersListener(this));
        getProxy().getPluginManager().registerCommand(this, new ReloadCommand());
    }

    @Override
    public void onDisable() {
        getProxy().getPluginManager().unregisterCommand(new ReloadCommand());
        getProxy().getPluginManager().unregisterListener(new BetweenServersListener(this));
        getProxy().unregisterChannel("userlogin:returned");
    }

    public void sendBungeecordPluginMsg(Server receiver, String uuid, boolean returned) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(uuid);
        out.writeBoolean(returned);
        receiver.sendData("userlogin:returned", out.toByteArray());
    }
}
