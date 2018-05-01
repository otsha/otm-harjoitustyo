# Arkkitehtuuri
## Sisällysluettelo
- [Arkkitehtuurin rakenne](#rakenne)
  - [Sovelluksen arkkitehtuuri pakkauskaaviona](#sovelluksen-arkkitehtuuri-pakkauskaaviona)
- [Logiikka](#logiikka)
- [Tietokanta](#tietokanta)
  - [Tietokannan sanallinen kuvaus](#tietokannan-sanallinen-kuvaus)
  - [Sovelluksen tietokantakaavio](#sovelluksen-tietokantakaavio)
  - [CREATE TABLE -lauseet](#create-table--lauseet)
- [Toiminnallisuudet](#toiminnallisuudet)
  - [Uuden suunnitelman luominen](#uuden-suunnitelman-luominen)

## Rakenne
**Sovelluksen arkkitehtuuri on nelitasoinen.** 
- JavaFX:llä toteutettu käyttöliittymä sijaitsee ylimmällä tasolla pakkauksen `ui` luokassa `SceneController`. 
  - *Sovelluksen käyttöliittymän käynnistää `budgetingapp` -pakkauksen luokka `Main`. Se kuitenkin käytännössä kuuluu samalle tasolle muun käyttöliittymän kanssa ja eriytys on tehty ainoastaan sovelluksen tiedostorakenteen selkeyden vuoksi.*

- Sovelluslogiikasta on vastuussa pakkauksen `logic` luokka `PlanHandler`.
- Sovelluksen itsensä käsittelemät tietueet (korkeamman tason mallit tietokantatauluista) sijaitsevat pakkauksessa `data`. Myös tietokantaan yhteyden muodostava luokka `Database` sijaitsee tässä pakkauksessa.
- Alimmalla tasolla pakkaus `dao` vastaa sovelluksen ja SQL-tietokannan välisestä kommunikaatiosta liittyen tietojen pysyväistalletukseen.

### Sovelluksen arkkitehtuuri pakkauskaaviona
![Sovelluksen arkkitehtuuri](https://github.com/otsha/otm-harjoitustyo/blob/master/documentation/week5_architecture.png)

## Logiikka
Sovelluksen logiikkaa käsittelee ``logic`` -pakkauksen luokka ``PlanHandler``, joka käsittelee käyttöliittymän pyyntöjä. PlanHandlerilla on käytössään yksi instanssi jokaista Dao-luokkaa, joita se käyttää apunaan välittäessään tietoa käyttöliittymän ja tietokannan välillä.

Muutamia PlanHandlerin käyttöliittymälle tarjoamia metodeita:
- ``getAllPlans()`` palauttaa kaikkien tietokannan budjettisuunnitelmien nimet *ObservableList*-oliona, jota käytetään käyttöliittymän *ListView* -näkymien täyttämiseen. Vastaavat metodit kategorioille ja kuluille ovat ``getAllCategories()`` ja ``getAllExpenses()``.
- ``createPlan(String name, String budget)`` validoi käyttäjän käyttöliittymään antaman syötteen, siis tarkistaa, etteivät kentät ole tyhjiä ja että budjetin määrä voidaan muuttaa Double-tyyppiseksi ja pyytää sitten PlanDaoa tallentamaan uuden suunnitelman tietokantaan. Kategorioille ja kuluille vastaavat metodit ovat ``createCategory(String name, String allocation)`` ja ``createExpense(String name, String amount)``.
- ``getUsed()`` palauttaa summan kaikista kuluista kyseisen suunnitelman sisällä. Yksittäisen kategorian kulujen summan saa metodilla ``getUsedByCategory(Category c)``.

## Tietokanta
### Tietokannan sanallinen kuvaus
Sovelluksen tiedot tallennetaan pysyvästi sen juurikansioon tietokantatiedostoon ``database.db``.

Tietokantaan yhdeyden ottaminen tapahtuu luokan ``data.Database.java`` avulla. Jos tietokantaa ei ole vielä luotu tai siitä puuttuu tauluja, hoitaa ``Database`` tietokannan alustuksen automaattisesti. Tietojen varsinaisesta tallettamisesta vastaavat ``dao`` -pakkauksen luokat ``PlanDao``, ``CategoryDao`` ja ``ExpenseDao``.

Tietokannassa on kolme tietokohdetta:
- **Budjettisuunnitelma:** Koko suunnitelma, jolla on nimi ja budjetti
- **Kategoria:** Budjettisuunnitelma jakautuu kategorioihin, joilla on jokin niitä kuvaava nimi ja joille varataan jokin määrä varoja.
- **Kulu:** Kategoriaan voi lisätä kuluja varojen todellisen käytön perusteella. Kululla on jokin sitä kuvaava nimi ja siihen kulunut määrä rahaa.

Budjettisuunnitelmien nimet ovat uniikkeja. Kategorioiden nimet ovat uniikkeja suunnitelmien sisällä, joihin ne kuuluvat. Vastaavasti kulujen nimet ovat uniikkeja kategorioiden sisällä.

### Sovelluksen tietokantakaavio
![Sovelluksen tietokantakaavio](https://camo.githubusercontent.com/c47126d18f09c883dde8de9d2a721639c7d64578/68747470733a2f2f79756d6c2e6d652f64323665633663652e706e67)
### CREATE TABLE -lauseet
**Plan (Budjettisuunnitelma):**
```SQL
CREATE TABLE IF NOT EXISTS Plan (
    id integer PRIMARY KEY,
    name varchar(255), 
    budget float
);
```

**Category (Kategoria):**
```SQL
CREATE TABLE IF NOT EXISTS Category (
    id integer PRIMARY KEY, 
    plan_id integer, 
    name varchar(255), 
    allocated float, 
    FOREIGN KEY (plan_id) REFERENCES Plan(id)
);
```

**Expense (Kulu):**
```SQL
CREATE TABLE IF NOT EXISTS Expense (
    id integer PRIMARY KEY,
    category_id integer,
    name varchar(255),
    amount float,
    FOREIGN KEY (category_id) REFERENCES Category(id)
);

```

## Toiminnallisuudet
Kuvataan muutamaa sovelluksen päätoiminnallisuutta sekvenssikaavioita apuna käyttäen.

### Uuden suunnitelman luominen
![Sekvenssikaavio: uusi suunnitelma](https://github.com/otsha/otm-harjoitustyo/blob/master/documentation/seqDiagram_creating_a_plan_v2.png)

Kun käyttäjä klikkaa aloitusnäkymässä "Create", eli haluaa luoda uuden budjettisuunnitelman, vaihtaa ``SceneController`` näkymäksi (scene) lomakkeen, jolla uusi suunnitelma luodaan. Lomakkeessa on kaksi kenttää; ensimmäiseen syötetään budjettisuunnitelman haluttu nimi ja toiseen varsinainen budjetti. "Create" -painiketta klikkaamalla lomakkeen tiedot lähetetään ``PlanHandler`` -luokalle, joka tarkistaa, ettei kumpikaan kentistä ole tyhjä ja että jälkimmäisen kentän sisältö voidaan muuttaa desimaaliluvuksi (``Double``). Jos tämä onnistuu, pyytää PlanHandler luokkaa ``PlanDao`` luomaan tietokantaan uuden suunnitelman käyttäjän antamilla parametreilla. Jos tietokantaan tallentaminen onnistuu (eli tietokantaan saadaan yhteys eikä siellä ole jo samannimistä suunnitelmaa), palauttaa PlanDao ``true`` ja PlanHandler pyytää sitä vielä hakemaan juuri luodun suunnitelman tietokannasta. Lopuksi PlanHandler palauttaa juuri luodun suunnitelman käyttöliittymälle, joka vaihtaa näkymäksi suunnitelman muokkausnäkymän.
