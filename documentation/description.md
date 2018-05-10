# Projektin kuvaus

*Tässä dokumentissa esitellään lyhyesti sovelluksen aihe, toteutus ja käyttötarkoitukset. Yksityiskohtaisempaa tietoa sovelluksen toiminnasta tarjoaa [sovelluksen arkkitehtuuridokumentti](https://github.com/otsha/otm-harjoitustyo/blob/master/documentation/architecture.md).*

**Aihe:** Budjetointisovellus

**Toteutus:** Java + JavaFX (käyttöliittymä) + JDBC (SQL-tietokanta)

## Sovelluksen tarkoitus
Sovelluksella käyttäjä voi luoda ja tallentaa budjettisuunnitelmia, tarkastella käyttökohteiden prosentuaalista osuutta kokonaisbudjetista ja pitää kirjaa budjetin yli- tai alijäämäisyydestä. Vain yksi käyttäjärooli.

## Toiminnallisuus
- JavaFX-käyttöliittymä ja tietojen tallentaminen tietokantaan.
- Budjettisuunnitelman luominen ja poistaminen
- Osa-alueiden lisääminen ja poistaminen budjettisuunnitelman sisällä
- Kulujen lisääminen ja poistaminen osa-alueittain
- Budjettisuunnitelmaan liittyvien tilastojen tarkastelu
  - Mahdollisuus osa-alueiden tarkasteluun ja vertailuun osa-aluelle varatun varamäärän ja siihen todellisuudessa käytettyjen varojen välillä.
  - Meneekö suunnitelma yli budjetin? Kuinka paljon varoja jää, jos ei mene?
  - Budjettisuunnitelman visualisointi
    - Miten budjetin jakaminen (varaaminen) jakautuu eri osa-alueittain
    - Miten budjetin varojen käyttö jakautuu eri osa-alueittain
    
*Tarkempaa tietoa sovelluksen eri toiminnallisuuksien käyttämisestä [käyttöohjeessa](https://github.com/otsha/otm-harjoitustyo/blob/master/documentation/userguide.md).*

## Sovelluksen heikkouksia
- Käyttöliittymäkoodi on yksinomaan yhdessä luokassa. Sen toiminnallisuutta on toki eritelty erillisiin metodeihin, mutta esimerkiksi näkymien täyttämisen olisi ehkä voinut ulkoistaa täysin jollekin toiselle luokalle.
- Sovelluksen käyttöliittymä on rujo eikä esteettisesti kovin silmää miellyttävä. Elementeille olisi voinut jättää enemmän tilaa hengittää.
- Sovelluslogiikka on suppea, ja lähinnä validoi syötteitä, siirtelee sovelluksen käsittelemää tietoa listasta toiseen tai suorittaa yksinkertaisia yhteenlaskuja.
  - Sovelluslogiikkaa, kuten käyttöliittymää, olisi voinut jakaa useampaan luokkaan. Erillinen validoija ja suunnitelmien, kategorioiden ja kulujen käsittelijät erikseen?
