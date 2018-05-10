# Testaus

## Sisällysluettelo
  - [Testausohjeet](#testausohjeet)
  - [Mitä testataan?](#mitä-testataan)
  - [Miten testataan?](#miten-testataan)
    - [Dao-luokat](#dao-luokat)
    - [Logiikka](#logiikka)
    - [Data](#data)
  - [Testauksen heikkouksia](#testauksen-heikkouksia)

## Testausohjeet
**jUnit**-testit voi suorittaa maven-projektin juurikansiossa ``OTM_BudgetingApp`` käskyllä

```
  mvn test jacoco:report
```

Testikattavuusraportin sijainti:

```
  target/site/jacoco/index.html
```

**Checkstyle**-testit saa suoritettua samassa juurikansiossa käskyllä

```
  mvn jxr:jxr checkstyle:checkstyle
```

...ja raportin sijainti:

```
  target/site/checkstyle.html
```

## Mitä testataan?
Sovelluksen jUnit-testit testaavat pakkausten ``dao``, ``data`` ja ``logic`` sisältöä. JavaFX:llä toteutettua käyttöliittymää (pakkauksissa ``main`` ja ``ui``) ei testata.

## Miten testataan?
### Dao-luokat
Sovelluksen tietokantatoiminnallisuudesta vastaavan ``dao`` -pakkauksen luokkia ``PlanDao``, ``CategoryDao`` ja ``ExpenseDao`` testaavat vastaavasti testiluokat ``PlanDaoTest``, ``CategoryDaoTest`` ja ``ExpenseDaoTest``.

Dao-testit toimivat pääsääntöisesti niin, että ne luovat ennen jokaista yksikkötestiä testitietokannan ``test.db``, joka on rakenteeltaan identtinen sovelluksen käyttämän tietokannan kanssa. Yksikkötestit testaavat tiedon oikeellista tallentamista, hakemista ja poistamista dao-luokkien metodein. Testitietokantatiedosto poistetaan jokaisen testin jälkeen ennen seuraavan testin suorittamista.

### Logiikka
Sovelluslogiikasta vastaavan ``logic`` -pakkauksen luokan ``PlanHandler`` testaamisesta vastaa testiluokka ``PlanHandlerTest``.

``PlanHandlerTest`` testaa dao-luokkien yhteistoimintaa sovelluksen toiminnallisuuksien kontekstissa (esim. saadaanko suunnitelmaan merkittyjen kulujen summaksi oikea luku, sisältääkö käyttöliittymälle palautettava ObservableList-olio oikeat nimet). Kuten dao-testit, se luo ennen jokaista yksikkötestiä tietokantatiedoston ``test.db`` ja poistaa sen testin jälkeen.

### Data
Sovelluksen käyttämillä tietueilla (``Plan``, ``Category``, ``Expense``) on hyvin vähän omaa toiminnallisuutta. Testit yksinkertaisesti testaavat getterien ja setterien toimivuutta.

``Database`` -luokan testaaminen tapahtuu välillisesti ``dao`` -luokkien testauksen kautta, joten sillä ei ole omia testejä.

## Testauksen heikkouksia
Ehkä suurin testauksen heikkous on se, että SQL-virhetapauksia (``SQLException``) ei testata erikseen. Tämä johtaa suureen haarautumien testauksen puutteeseen etenkin ``PlanHandler`` -luokassa.
