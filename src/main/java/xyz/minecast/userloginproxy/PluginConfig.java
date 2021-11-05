package xyz.minecast.userloginproxy;

import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfKey;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter.Order;

import java.util.List;

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

public interface PluginConfig {

    @Order(1)
    @ConfKey("auth-servers")
    @ConfComments("Auth servers with UserLogin plugin")
    @ConfDefault.DefaultStrings({"lobby", "lobby2"})
    List<String> getAuthServers();

    @Order(2)
    @ConfKey("reloaded")
    @ConfDefault.DefaultString("&bConfiguration reloaded!")
    String getMessageReloaded();

    @Order(3)
    @ConfKey("kicked-console")
    @ConfDefault.DefaultString("&c{player} tried to fake plugin message!")
    String getConsoleMessageKicked();

    @Order(4)
    @ConfKey("kicked-player")
    @ConfDefault.DefaultString("&cYou tried to fake plugin message!")
    String getPlayerMessageKicked();
}
