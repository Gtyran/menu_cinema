## Client Angular
Disponible a distance sur : http://83.166.245.248/ 

## Serveur Java
Disponible a distance sur : http://83.166.245.248:8090/
Utilisez MAVEN pour compiler et exécuter ce serveur.

``` sh
mvn compile
mvn exec:java &
```

Kill old processus for server
``` sh
ps -aux | grep maven
```

# Urls dispo

## 1. a. [méthode : POST] Authentification client + création client si non existant dans la BDD - MySQL
`/api/authentification`
**Paramètres POST :**
```
id		:	W5k3KtrHn9cRvK5iHQRubuOrHCz3
nom     :	TestN
prenom	:	TestP
```

Exemple de réponse:
``` json
{
    "id": "qBrNB8Jg6CaWYJpR3NKRT9UH3DT2",
    "nom": "Cash",
    "prenom": " Develop",
    "email": "",
    "adresse": "",
    "photo": "",
    "tel": "",
    "points": "0.0"
}
```
Les champs suivants ne sont ni accessibles en modification ni en lecteur (cf diagramme de classe) :
	- champEmail
	- champTel
	- champPoints

## b. [méthode : GET] Recuperation des infos d'un client
`/api/authentification` ou `/api/client`
**Paramètres GET:**
```
id      : W5k3KtrHn9cRvK5iHQRubuOrHCz3
```

Exemple de réponse:
``` json
{
    "id": "qBrNB8Jg6CaWYJpR3NKRT9UH3DT2",
    "nom": "Cash",
    "prenom": " Develop",
    "email": "mail@mail.ml",
    "adresse": "",
    "photo": "url de la photo",
    "tel": "",
    "points": "0.0"
}
```

## c. [méthode : PUT] Modification d'un client
`/api/client`
**Paramètres POST :**
```
id      : W5k3KtrHn9cRvK5iHQRubuOrHCz3
nom     : nouveau nom
prenom  : nouveau prenom
email   : mail@mail.ml
tel     : 07 08 09 06 03
adresse : nouvelle adresse
```

Exemple de réponse:
``` json
{
    "id": "W5k3KtrHn9cRvK5iHQRubuOrHCz3",
    "nom": "nouveau nom",
    "prenom": "nouveau prenom",
    "email": "mail@mail.ml",
    "adresse": "nouvelle adresse",
    "photo": "url de la photo",
    "tel": "07 08 09 06 03",
    "points": "0.0"
}
```

# 2. [méthode : GET] Liste des plats disponibles
`/api/menu?id=W5k3KtrHn9cRvK5iHQRubuOrHCz3`

**Paramètres GET:**
```
id      : W5k3KtrHn9cRvK5iHQRubuOrHCz3
```

```json
[
    {
        "id": "1",
        "type": "ENTREE",
        "prix": "7.24",
        "ingredients": [
            "Champignon",
            "Oeuf"
        ]
    },
	...
]
```

# 3. [méthode : POST] Enregistrer une commande client
`/api/commande`

**Paramètres POST :** les paramètres `idsFilm` et `idsPlat` sont des listes de string contenant respectivement des id de film et de plat

```
idclient            :   qBrNB8Jg6CaWYJpR3NKRT9UH3DT2
idsFilm             :   3, 5, 6
idsPlat             :   3,3,3,3,3,3,5,5
adresseLivraison    :   Une adresse 222
point_utilise       :   true                            [boolean: false/true]
```

Exemple de réponse:
```xml
    <?xml version="1.0" encoding="UTF-8" standalone="no"?>
    <facture>
        <idcommande>6</idcommande>
        <client>qBrNB8Jg6CaWYJpR3NKRT9UH3DT2</client>
        <date>2019-05-08 13:12:51.309</date>
        <films>
            <film>3</film>
            <film>5</film>
            <film>6</film>
        </films>
        <plats>
            <plat id="3">
                <quantite>6</quantite>
                <prix>8.0</prix>
            </plat>
            <plat id="5">
                <quantite>2</quantite>
                <prix>5.0</prix>
            </plat>
        </plats>
        <points>6</points>
        <reduction>0.0</reduction>
        <sous-total>69.25</sous-total>
        <total>69.25</total>
    </facture>
```

# 4. [méthode : GET] Liste des commandes du client possandant l'idclient `W5B3KcrHnUcmPK5itQRubuOrHCz1`
`/api/commande?idclient=W5B3KcrHnUcmPK5itQRubuOrHCz1`

**Paramètres GET:**
```
idclient      : qBrNB8Jg6CaWYJpR3NKRT9UH3DT2
```

```json
[
    {
        "id": 1,
        "idclient": "qBrNB8Jg6CaWYJpR3NKRT9UH3DT2",
        "date": "2019-05-08 00:08:39",
        "idFilms": [
            3,
            5,
            6
        ],
        "idPlats": [
            {
                "id": 3,
                "quantite": 6
            },
            {
                "id": 5,
                "quantite": 2
            }
        ],
        "reduction": 0,
        "prix": 69.25,
        "adresseLivraison": "Une adresse 222"
    },
    ...
]
```

# 5. [méthode : GET] Retourner une commandes du client possandant l'idclient `W5B3KcrHnUcmPK5itQRubuOrHCz1` et la commande d'id : 2
`/api/commande?idclient=qBrNB8Jg6CaWYJpR3NKRT9UH3DT2&id=2`

**Paramètres GET:**
```
idclient      : qBrNB8Jg6CaWYJpR3NKRT9UH3DT2
id            : 2
```

```json
{
    "id": 2,
    "idclient": "qBrNB8Jg6CaWYJpR3NKRT9UH3DT2",
    "date": "2019-05-08 00:09:17",
    "idFilms": [
        3,
        5,
        6
    ],
    "idPlats": [
        {
            "id": 3,
            "quantite": 6
        },
        {
            "id": 5,
            "quantite": 2
        }
    ],
    "reduction": 2,
    "sous-total": 69.25,
    "total": 67.25,
    "adresseLivraison": "Une adresse 222"
}
```

# 6. [méthode : GET] Retourner les suggestions de film pour le plat numéro 3

`/api/suggestions/films?id=qBrNB8Jg6CaWYJpR3NKRT9UH3DT2&id_menu=3`

**Paramètres GET:**
```
id              : qBrNB8Jg6CaWYJpR3NKRT9UH3DT2
id_menu         : 2
```

Exemple de reponse
```
[
    3,
    5,
    6,
    445629,
    447404,
    449562,
    456740,
    457799,
    487297,
    537915
]
```


# 7. [méthode : GET] Retourner les suggestions de plat pour le film numéro 449562

`/api/suggestions/plats?id=qBrNB8Jg6CaWYJpR3NKRT9UH3DT2&id_film=449562`

**Paramètres GET:**
```
id              : qBrNB8Jg6CaWYJpR3NKRT9UH3DT2
id_film         : 449562
```

Exemple de reponse
```
[3, 5]
```

# 8. [méthode : GET] Retourner les meilleurs ventes de plats

`/api/commande?idclient=qBrNB8Jg6CaWYJpR3NKRT9UH3DT2&bestsellerplats=1`

**Paramètres GET:**
```
idclient                : qBrNB8Jg6CaWYJpR3NKRT9UH3DT2
bestsellerplats         : 1
```

Exemple de reponse

```json
[
    {
        "id": "3",
        "nombre_vente": "30"
    },
    {
        "id": "1",
        "nombre_vente": "10"
    },
    {
        "id": "5",
        "nombre_vente": "10"
    },
    {
    ...
]
```


## clone branch
git clone -b IterationMehdi2 --single-branch https://gitlab.com/dlst/l3m_menucinema.git
git clone -b clientIteration3 --single-branch https://gitlab.com/dlst/l3m_menucinema.git

[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)


   [dill]: <https://github.com/joemccann/dillinger>
   [git-repo-url]: <https://github.com/joemccann/dillinger.git>
   [john gruber]: <http://daringfireball.net>
   [df1]: <http://daringfireball.net/projects/markdown/>
   [markdown-it]: <https://github.com/markdown-it/markdown-it>
   [Ace Editor]: <http://ace.ajax.org>
   [node.js]: <http://nodejs.org>
   [Twitter Bootstrap]: <http://twitter.github.com/bootstrap/>
   [jQuery]: <http://jquery.com>
   [@tjholowaychuk]: <http://twitter.com/tjholowaychuk>
   [express]: <http://expressjs.com>
   [AngularJS]: <http://angularjs.org>
   [Gulp]: <http://gulpjs.com>

   [PlDb]: <https://github.com/joemccann/dillinger/tree/master/plugins/dropbox/README.md>
   [PlGh]: <https://github.com/joemccann/dillinger/tree/master/plugins/github/README.md>
   [PlGd]: <https://github.com/joemccann/dillinger/tree/master/plugins/googledrive/README.md>
   [PlOd]: <https://github.com/joemccann/dillinger/tree/master/plugins/onedrive/README.md>
   [PlMe]: <https://github.com/joemccann/dillinger/tree/master/plugins/medium/README.md>
   [PlGa]: <https://github.com/RahulHP/dillinger/blob/master/plugins/googleanalytics/README.md>
