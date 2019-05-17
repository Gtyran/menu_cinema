#!/bin/sh
#
# script pour scanner les versions de JDK / Tomcat installés sur les serveurs
rougefonce='\e[0;31m\e[1m'
vertclair='\e[1;32m\e[5m\e[1m'
bleuclair='\e[1;34m\e[5m\e[1m'
cyanclair='\e[1;36m\e[5m\e[1m'
blanc='\e[25m\e[1;37m'
orange='\e[0;33m\e[5m\e[1m'
jaune='\e[1;33m\e[5m\e[1m'

clear
echo "${blanc}============================================ Connexion =============================================  "
echo "        ,-.                                                                                      "
echo "        \`-'                                                                                      "
echo "        /|\                                                                                      "
echo "         |                   ,--------------.            ,------------.          ,--------------."
echo "        / \                  |Client Angular|            |Serveur java|          |${rougefonce}MySQL Database${blanc}|"
echo "      Client                 \`------+-------'            \`-----+------'          \`------+-------'"
echo "        | saisir infos de connexion |                          |                        |        "
echo "        | (login,  pwd, ect.)       |                          |                        |        "
echo "        | -------------------------->                          |                        |        "
echo "        |                           |     infos client         |                        |        "
echo "        |                           |     (id, nom, prénom)    |                        |        "
echo "        |                           | ------------------------->                        |        "
echo "        |                           |                          |     client existe ?    |        "
echo "        |                           |                          | ----------------------->        "
echo "        |                           |                          |           ${jaune}nom${blanc}          |        "
echo "        |                           |                          | ${jaune}<-----------------------${blanc}        "
echo "        |                           |                          |     créer le client    |        "
echo "        |                           |                          | ----------------------->        "
echo "        |                           |                          |                        |        "
echo "        |                           |      client logué !      |                        |        "
echo "        |                           | <-------------------------                        |        "
echo "        |                           |                          |                        |        "
echo "        |                           | mise à jour infos client |                        |        "
echo "        |                           | (nom, prénom, adresse...)|                        |        "
echo "        |                           | ------------------------->                        |        "
echo "        |                           |                          |      UPDATE infos      |        "
echo "        |                           |                          | ----------------------->        "
echo "        |                           |   Infos client en JSON   |                        |        "
echo "        |                           | <-------------------------                        |        "
echo "        |                           |                          |                        |        "
echo "        |                           |          menu ?          |                        |        "
echo "        |                           | ------------------------->                        |        "
echo "        |                           |  Liste des plats en JSON |                        |        "
echo "        |                           | <-------------------------                        |        "
echo "        |                           |                          |                        |        "
echo "        |       page de choix       |                          |                        |        "
echo "        | <--------------------------                          |                        |        "
echo "      Client                 ,------+-------.            ,-----+------.          ,------+-------."
echo "        ,-.                  |Client Angular|            |Serveur java|          |${rougefonce}MySQL Database${blanc}|"
echo "        \`-'                  \`--------------'            \`------------'          \`--------------'"
echo "        /|\                                                                                      "
echo "         |                                                                                       "
echo "        / \                                                                                      "