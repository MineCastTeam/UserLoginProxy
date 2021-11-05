package xyz.minecast.userloginproxy;

import java.nio.file.Path;
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

public class UserLoginConfig {

    public static PluginConfigManager<PluginConfig> configManager;
    public static List<String> authServers;
    public static String messageReloaded;
    public static String playerMessageKicked;
    public static String consoleMessageKicked;

    public static void createConfig(Path configFolder) {
        configManager = PluginConfigManager.create(configFolder, "config.yml", PluginConfig.class);
        reloadConfig();
    }

    public static void reloadConfig() {
        configManager.reloadConfig();
        authServers = configManager.getConfigData().getAuthServers();
        messageReloaded = configManager.getConfigData().getMessageReloaded();
        consoleMessageKicked = configManager.getConfigData().getConsoleMessageKicked();
        playerMessageKicked = configManager.getConfigData().getPlayerMessageKicked();
    }

}
