OnlineLog
====================
This is a plugin for the minecraft bukkit server mod.<br>
It will log which and how many players are online to a mysql database.<br>
You can use this data to create fancy charts on your web page (:

One row will be written to the database table each time the online players change.<br/>
For example when someone logs in or logs out.

[Take a look at this live example](http://mcteam.org/statistics)

How to install
----------
* Download the latest release: [https://github.com/oloflarsson/minecraft-online-log/tree/master/releases](https://github.com/oloflarsson/minecraft-online-log/tree/master/releases)<br>
* Put OnlineLog.jar in the plugins folder.
* [Download the mysql driver](http://www.mysql.com/downloads/connector/j/) and put it in your server folder.
* Create this table:<br>
<pre>CREATE TABLE IF NOT EXISTS `onlinelog` (
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `playercount` smallint(5) unsigned NOT NULL,
  `playernames` varchar(4096) CHARACTER SET ascii NOT NULL,
  KEY `timestamp` (`timestamp`),
) ENGINE=MyISAM DEFAULT CHARSET=latin1;</pre>
* Start and stop the server. The plugin will create a default config file.
* Modify the configuration file.

How to use the data
----------
Take a look at the usage examples.

License
----------
This project has a LGPL license just like the Bukkit project.

