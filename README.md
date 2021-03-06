# WauzCore
[![Build Status](https://github.com/SevenDucks/WauzCore/actions/workflows/maven.yml/badge.svg)](https://github.com/SevenDucks/WauzCore/actions/workflows/maven.yml)
[![Discord](https://img.shields.io/discord/212147184999596032)](https://discord.gg/dhHJp5a)
[![CodeFactor](https://www.codefactor.io/repository/github/sevenducks/wauzcore/badge)](https://www.codefactor.io/repository/github/sevenducks/wauzcore)
[![Codebeat](https://codebeat.co/badges/54809851-9b0b-4970-a486-754de395c884)](https://codebeat.co/projects/github-com-sevenducks-wauzcore-master)
[![JDK](https://img.shields.io/badge/Java-OpenJDK%2011-orange.svg)](https://adoptopenjdk.net/index.html)
[![Minecraft](https://img.shields.io/badge/Minecraft-PaperMC%201.16.5-orange.svg)](https://papermc.io/downloads#Paper-1.16)
[![CodeSize](https://img.shields.io/github/languages/code-size/SevenDucks/WauzCore)](https://shields.io/category/size)

<a href="https://seven-ducks.com/delseyria.html"><img src="https://seven-ducks.com/assets/images/banner-delseyria.png"/></a> 

## :sunrise_over_mountains: Delseyria Reborn - The future of Minecraft RPGs
Delve into a fascinating world, full of dark dungeons, vicious monsters and mythical treasures!

There currently is a demo in development. The test server is not publicly accessible though. [Seven Ducks](https://github.com/SevenDucks) plans to create a full [MMORPG experience in Minecraft](https://seven-ducks.com/delseyria.html) by 2022, all without the need for mods or data packs. Unlike Wynncraft it will be completely open source and provide APIs to add your own Skills or Equipment, so we hope it's worth the wait. All content will be released on the DelseyriaRPG Minecraft server, as soon as the first stable build is ready.

:speech_balloon: Developer Discord: https://discord.gg/dhHJp5a

:bird: Developer Twitter: https://twitter.com/wauzmons

## :crown: Advanced Minecraft MMORPG Engine
Equipment, Skills, Quests, Shops, Dungeons, Bosses, Pets, Achievements and much more!

With the new module system, you can implement partial features of the engine in your own plugins. It requires you to run the same server version as the engine and at least moderate programming skills.

To get started, download the newest package from GitHub and put the .jar in your server's plugin folder and in the dependencies of your own plugin. Create a [Server Configuration](./examples/Server.yml) under plugins/WauzCore/Server.yml and remove the "main" and any other module you don't want to use from the list. With the main module disabled, you can savely remove any configuration option below the modules section and don't have to worry about your server trying to connect to Delseyria. The documentation of all usable modules can be found below.

:hamster: Pet Module: [modules/PETS.md (configKey = pets)](./modules/PETS.md)

:orange_book: Javadoc: https://sevenducks.github.io/WauzCore/
