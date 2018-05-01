# Käyttöopas
* [Projektin readme](https://github.com/otsha/otm-harjoitustyo/blob/master/README.md)

* [Projektin kuvaus](https://github.com/otsha/otm-harjoitustyo/blob/master/documentation/description.md)

## Sisällysluettelo
- 1 [Uuden budjettisuunnitelman luominen](#uuden-budjettisuunnitelman-luominen)
- 2 [Olemassaolevan budjettisuunnitelman avaaminen](#olemassaolevan-budjettisuunnitelman-avaaminen)
- 3 [Budjettisuunnitelman muokkaaminen](#budjettisuunnitelman-muokkaaminen)
  - 3.1 [Kategorian tietojen näyttäminen](#kategorian-tietojen-näyttäminen)
    - 3.1.1 [Kulujen lisääminen](#kulujen-lisääminen)
    - 3.1.2 [Kulujen poistaminen](#kulujen-poistaminen)
  - 3.2 [Kategorian lisääminen](#kategorian-lisääminen)
  - 3.3 [Kategorian poistaminen](#kategorian-poistaminen)
- 4 [Budjettisuunnitelman poistaminen](#budjettisuunnitelman-poistaminen)
- 5 [Budjettisuunnitelmaan liittyvät kaaviot](#budjettisuunnitelmaan-liittyvät-kaaviot)
  - 5.1 [Varatut kulut](#varatut-kulut)
  - 5.2 [Käytetyt varat](#käytetyt-varat)
  

Tämänhetkisessä versiossa käyttäjä voi luoda, poistaa, tarkastella ja muokata budjettisuunnitelmia.

## Uuden budjettisuunnitelman luominen
- Valitse sovelluksen ensimmäisestä näkymästä "New..."
- Syötä suunnitelmalle nimi ja budjetti (desimaaliluku amerikkalaisittain, muista siis piste pilkun sijaan!)
- Nyt sovellus avaa luodun suunnitelman, ja voit aloittaa sen muokkaamisen
- Takaisin päävalikkoon pääsee klikkaamalla vasemman yläkulman "<" -painiketta.

## Olemassaolevan budjettisuunnitelman avaaminen
- Valitse sovelluksen ensimmäisestä näkymästä sen suunnitelman nimi, jonka haluat avata.
- Valitse listauksen alta "Open..."
- Nyt sovellus näyttää valitun suunnitelman tiedot, joita voit myös muokata

## Budjettisuunnitelman muokkaaminen

### Kategorian tietojen näyttäminen
- Valitse ruudun vasemmalta puolelta haluamasi kategoria
- Nyt kategorian tiedot näytetään ruudun oikeassa reunassa.

#### Kulujen lisääminen
- Valitse ruudun vasemman reunan listauksesta kategoria, johon haluat lisätä kuluja.
- Syötä nyt ruudun oikean alakulman lomakkeeseen kulua kuvaava nimi ja sen määrä.
- Valitse "Create"
- Syöttämäsi kulu on nyt lisätty kategoriaan.

#### Kulujen poistaminen
- Valitse budjetin muokkausnäkymässä kategoria, josta haluat poistaa kulun
- Valitse ruudun oikeasta reunasta kulu, jonka haluat poistaa
- Klikkaa **kulujen** listauksen alta "Delete"
- Valitsemasi kulu on nyt poistettu tietokannasta.

### Kategorian lisääminen
- Syötä kategorialistauksen alla olevaan lomakkeeseen nimi uudelle kategorialle ja haluamasi varaus kokonaisbudjetista
- Valitse "Create"
- Luotu kategoria näytetään nyt listauksessa

### Kategorian poistaminen
- Valitse ruudun vasemman reunan listauksessa kategoria, jonka haluat poistaa
- Valitse listauksen alta "Delete"
- Valittu kategoria ja siihen liittyvät kulut on nyt poistettu tietokannasta

## Budjettisuunnitelman poistaminen
- Valitse sovelluksen ensimmäisestä näkymästä "Open an existing budget plan"
- Jos tietokannassa ei ole budjettisuunnitelmia, seuraava näkymä on tyhjä.
- Valitse suunnitelma, jonka haluat poistaa ja paina "Delete" -painiketta listauksen alla.
- Nyt valitsemasi suunnitelma on poistettu tietokannasta.
- Budjettisuunnitelman poistaminen poistaa myös siihen liittyvät kategoriat ja kulut.

## Budjettisuunnitelmaan liittyvät kaaviot
- Budjettisuunnitelman avattuasi voit muokkaamisen lisäksi tarkastella siitä muodostettuja kaavioita.
- Klikkaa muokkausnäkymässä ruudun yläreunasta "Viev charts"
- Sovellus näyttää oletuksena ympyrädiagrammin kulujen varaukseen liittyen
- Näkymästä pääsee takaisin päävalikkoon klikkaamalla yläreunan "<" -painiketta tai suunnitelma muokkausnäkymään klikkaamalla "Edit plan..." -painiketta.

### Varatut kulut
- Klikkaa ruudun yläreunasta "Fund allocation"
- Näkymän yläreunassa näytetään suunnitelman kokonaisbudjetti ja varaamattomien kulujen määrä
- Ympyräkaaviossa palaset esittävät kategorioita ja niiden varauksien suhdetta toisiinsa ja vielä varaamattomiin kuluihin.
- "Unallocated" tarkoittaa vielä budjettisuunnitelman puitteissa varattavissa olevien varojen määrää.
- Jos kaaviossa on "Unallocated" -palasen sijaan "Total overflow" -palanen, olet varannut budjettisi kategorioille enemmän varoja kuin mikä suunnitelmasi kokonaisbudjetti on. *Tällöin kaavion 100% on kokonaisvaraus ja kaavio ilman "Total overflow" -sektoria on kokonaisbudjettisi.*
- **HUOM!** Tämän kaavion sisältö on erillinen todellisista merkityistä kuluista.

### Käytetyt varat
- Klikkaa ruudun yläreunasta "Fund usage"
- Näkymän yläreunassa näytetään suunnitelman kokonaisbudjetti ja yhteensä käytettyjen varojen (kulujen) summa.
- Palanen, jonka nimi on "Unused" tarkoittaa vielä budjettisuunnitelman puitteissa käytettävissä olevien varojen määrää.
- Jos kaaviossa on "Unused" -palasen sijaan "Total overflow" -palanen, olet merkinnyt enemmän kuluja kuin mikä kokonaisbudjettisi on. *Tällöin kaavion 100% on kokonaiskulut ja kaavio ilman "Total overflow" -sektoria on kokonaisbudjettisi."
- **HUOM!** Tämän kaavion sisältö on erillinen kategorioille varatuista kuluista.
