package xyz.minecast.userloginproxy.velocity;

import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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

public class ReloadCommand implements SimpleCommand {

    @Override
    public void execute(final Invocation invocation) {
        UserLoginConfig.reloadConfig();
        invocation.source().sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(UserLoginConfig.messageReloaded));
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("ulp.reload");
    }
}