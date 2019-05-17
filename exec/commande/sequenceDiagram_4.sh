#!/bin/sh
#
# script pour scanner les versions de JDK / Tomcat installés sur les serveurs
rougefonce='\e[0;31m\e[1m'
vertclair='\e[1;32m\e[5m\e[1m'
vert='\e[32m\e[1m'
bleuclair='\e[1;34m\e[5m\e[1m'
cyanclair='\e[1;36m\e[5m\e[1m'
blanc='\e[25m\e[1;37m'
orange='\e[0;33m\e[1m'
jaune='\e[1;33m\e[5m\e[1m'

clear
echo "${blanc}================================================ Passer une commande ================================================="
echo "        ,-.                                                                                                      "
echo "        \`-'                                                                                                      "
echo "        /|\                                                                                                      "
echo "         |             ,--------------.          ,------------.          ,------------.          ,--------------."
echo "        / \            |Client Angular|          |${rougefonce}Serveur Java${blanc}|          |XML Database|          |MySQL Database|"
echo "      Client           \`------+-------'          \`-----+------'          \`-----+------'          \`------+-------'"
echo "        |  Passe la commande  |                        |                       |                        |        "
echo "        | -------------------->                        |                       |                        |        "
echo "        |                     |   envoie la commande   |                       |                        |        "
echo "        |                     | ----------------------->                       |                        |        "
echo "        |                     |  ,-----------------!.  |                       |                        |        "
echo "        |                     |  |id client        |_\ |                       |                        |        "
echo "        |                     |  |liste id liste     | |                       |                        |        "
echo "        |                     |  |id plats fimls     | |                       |                        |        "
echo "        |                     |  |adresse livraison  | |                       |                        |        "
echo "        |                     |  \`-------------------' |                       |                        |        "
echo "        |                     |                        |       id plats ?      |                        |        "
echo "        |                     |                        | ---------------------->                        |        "
echo "        |                     |                        |                       |                        |        "
echo "        |                     |                        |       prix plats      |                        |        "
echo "        |                     |                        | <----------------------                        |        "
echo "        |                     |                        |  ,-------------------!.                        |        "
echo "        |                     |                        |  |Les prix des filmls|_\                       |        "
echo "        |                     |                        |  |sont forfaitaires    |                       |        "
echo "        |                     |                        |  \`---------------------'                       |        "
echo "        |                     |                        |             ${vertclair}enregistrement commande${blanc}            |        "
echo "        |                     |                        | ${vertclair}----------------------------------------------->${blanc}        "
echo "        |                     |                        |                       |                        |        "
echo "        |                     |                        |               bien enregistrée ?               |        "
echo "        |                     |                        | <-----------------------------------------------        "
echo "        |                     |         facture        |                       |                        |        "
echo "        |                     | <-----------------------                       |                        |        "
echo "        |                     |  ,-----------!.        |                       |                        |        "
echo "        |                     |  |Fichier XML|_\       |                       |                        |        "
echo "        |                     |  \`-------------'       |                       |                        |        "
echo "        |   page de commande  |                        |                       |                        |        "
echo "        | <--------------------                        |                       |                        |        "