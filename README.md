# Budjetointisovellus

Harjoitustyö kevään 2018 kurssille *Ohjelmistotekniikan menetelmät (OTM)*.

## Dokumentaatio
* [Käyttöohje](https://github.com/otsha/otm-harjoitustyo/blob/master/documentation/userguide.md)

* [Määrittelydokumentti](https://github.com/otsha/otm-harjoitustyo/blob/master/documentation/description.md)

* [Arkkitehtuurikuvaus](https://github.com/otsha/otm-harjoitustyo/blob/master/documentation/architecture.md)

* [Työaikaseuranta](https://github.com/otsha/otm-harjoitustyo/blob/master/documentation/log.md)

## Testaus
**jUnit**-testit voi suorittaa maven-projektin juurikansiossa (OTM_BudgetingApp) käskyllä

`
  mvn test jacoco:report
`

Testikattavuusraportin sijainti:

`
  target/site/jacoco/index.html
`

**Checkstyle**-testit saa suoritettua samassa juurikansiossa käskyllä

`
  mvn jxr:jxr checkstyle:checkstyle
`

...ja raportin sijainti:

`
  target/site/checkstyle.html
`
