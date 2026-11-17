# Rush Hour NxM

Kétszemélyes, parancssoros Rush Hour játék Javában (Programozási technológiák beadandó).

## Futtatás

```
mvn clean install
java -jar target/rush-hour-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## A játék szabályai

- A tábla N sorból és M oszlopból áll. Az oszlopokat betűk (a, b, c...), a sorokat számok (1, 2, 3...) jelölik.
- A járművek 2 vagy 3 mező hosszúak, és csak a saját tengelyük mentén mozoghatnak.
- A játékos célautója az **A**, a kijárata a tábla jobb szélén van (`>`).
- A gép célautója a **B**, a kijárata a tábla bal szélén van (`<`).
- A játékos kezd. A körében bármelyik járművet eltolhatja, akárhány szabad mezővel.
- A gép véletlenszerűen választ egy járművet, és ha az tud lépni, egy véletlen szabályos lépést tesz vele.
- Az nyer, akinek a célautója előbb eléri a saját kijáratát.

## Parancsok játék közben

| Parancs | Jelentés |
|---|---|
| `E le 2` | Az E jármű 2 mezővel lejjebb lép (irányok: `fel`, `le`, `balra`, `jobbra`) |
| `ment palya.txt` | Az aktuális pálya mentése fájlba |
| `kilep` | Kilépés a játszmából |

## Pályafájl

Induláskor a program a `map.txt` fájlt olvassa be. Ha ez hiányzik vagy hibás, egy beégetett alappálya indul.

A fájl minden sora a tábla egy sora: `.` az üres mező, a nagybetű egy jármű.
Az azonos betűk ugyanahhoz a járműhöz tartoznak.

```
.....C......
.D...C..EEE.
.D.AA...F...
..GG..H.F...
......H.BB..
.III........
```

## Felépítés

| Csomag | Feladat |
|---|---|
| `model` | Az adatok: pozíció, jármű, tábla, lépés (megváltoztathatatlan VO osztályok) |
| `service` | A játékszabályok: pálya értelmezése, lépés ellenőrzése, győzelem, gépi játékos |
| `persistence` | Fájl- és adatbázis-kezelés: pálya betöltése és mentése, ranglista (JDBC, H2) |
| `ui` | Konzolos be- és kimenet: tábla kirajzolása, lépés beolvasása |
| `game` | A játék vezérlése: menü és a játszma lebonyolítása |

A naplók a `rush-hour.log` fájlba kerülnek, az adatbázis a `rush-hour-db.mv.db` fájlba.
A tesztlefedettség jelentése a build után itt található: `target/site/jacoco/index.html`.
