# CardTracker
Java program that uses a web scraper to update a sqlite database with magic the gathering cards. 

The website used for buying and selling cards in Europe only gives large stores access to its API, so selling tracking the price variation manually on every card you're selling becomes tedious.

database_update() scrapes info from cardmarket and compares it to the database. If theres too much difference between my sell price and the sell price on cardmarket, print the difference and name of the card


https://www.cardmarket.com/en/Magic


