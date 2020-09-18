# Java Coding Assignment

## Zadání:

Business chce na titulní straně aplikace ukazovat “trending” dokumenty za poslední týden - tj. ty, jejichž otevíranost u uživatelů není zanedbatelná a má rostoucí tendenci. Analytické oddělení chce mít možnost pohledu na tyto “trending” dokumenty v libovolném historickém období. 

Navrhněte a napište microservice, která bude tuto funkcionalitu zajišťovat - sbírat data o otevíranosti dokumentů (API si navrhněte) a poskytovat výstup dle zadání. Funkcionalitu microservice demonstrujte integračním testem, které si potřebné prostředí (databázi, atd.) vytvoří pomocí Test Containers - https://www.testcontainers.org/).  Tip - použijte nějakou event queue.

## Testing

Integrační test celého procesu je v TrendingDocsControllerIT
