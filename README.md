# Budjetointisovellus

Harjoitustyö kevään 2018 kurssille *Ohjelmistotekniikan menetelmät (OTM)*.

## Versiot
### Nykyinen
* [Pre-release build 2 (Viikko 6)](https://github.com/otsha/otm-harjoitustyo/releases/tag/week6)

### Vanhemmat
* [Pre-release build 1 (Viikko 5)](https://github.com/otsha/otm-harjoitustyo/releases/tag/week5)

## Dokumentaatio
* [Käyttöohje](https://github.com/otsha/otm-harjoitustyo/blob/master/documentation/userguide.md)

* [Määrittelydokumentti](https://github.com/otsha/otm-harjoitustyo/blob/master/documentation/description.md)

* [Arkkitehtuurikuvaus](https://github.com/otsha/otm-harjoitustyo/blob/master/documentation/architecture.md)

* [Työaikaseuranta](https://github.com/otsha/otm-harjoitustyo/blob/master/documentation/log.md)

## Komentorivitoiminnot
### Testaus
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

### .jar-pakkauksen generointi
Suorita maven-projektin juurikansiossa ``OTM_BudgetingApp`` komento

```
mvn package
```

Suorituskelpoinen .jar-tiedosto löytyy nyt ***alikansiosta** ``target``, ja sen suorittaminen onnistuu käskyllä

```
java -jar OTM_BudgetingApp-1.0-SNAPSHOT.jar
```
(**HUOM!** Muistathan siirtyä oikeaan kansioon ennen suorittamista)

### JavaDoc
JavaDocin saat generoitua suorittamalla maven-projektin juurikansiossa ``OTM_BudgetingApp`` komennon

```
mvn javadoc:javadoc
```
JavaDoc löytyy nyt osoitteesta ``target/site/apidocs/`` ja voit tarkastella sitä selaimellasi avaamalla tiedoston (esim. chromium-selaimella):
```
chromium-browser target/site/apidocs/index.html
```

