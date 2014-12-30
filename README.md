Planet-Trading-Game
===================

# Ideas List
- Systems have chance for no planets to spawn, just  star.
- Generate stars in systems.
- Stars have temperature and size values.
- Advanced planet generation: generate planet types based of distance from star and sizes.
- Load in planet types from file?
- Define what makes a planet type (temp, composition), assign planet types based off those values.
- Starting planets: When player is assigned a starting planet, the server picks a random planet and replaces the values with starting planet values.

# Terraforming
- TODO: explain teraforming...

# Servers
- Server Lists
  - Two possible ways to list servers.
    - User manually adds server ip to server list.
    - Client checks if server is online, server responds with server name, ping and player count.
  - OR
    - When a server is turned on, it tries to connect to a lobby server. 
    - This lists all online servers.
    - Client connects to lobby server and receives a list of all active servers.
- Registration
  - User registers username and password on website, stores info in database.
  - Client requires username and password to login.
  - Client checks with database (PHP POST/GET) to validate.
  - If details are correct, player can join servers.
- If two players with the same IP try and connect, do not allow the 2nd user to connect.
- Message 1st user saying "Multiple logins from the same IP detected", or something like that.

# Stats
- total_trades: The total amount of trades placed
- total_trades_sold: The total amount of trades that have completely sold, doesn't count portions of a trade being sold.
- trades_quantity: The total amount of resources sold.
- credits_exchanged: The total amount of credits exchanged between players.

# Maps
- Map Types
  - Star System Map - Shows planets around a star.
  - Sector Map - Shows systems in a sector.
  - Region - Shows sectors in region.
  - Galaxy - Shows regions in galaxy.
- Access the map screen from a planet menu, like the marketplace.
- Start on local map, press buttons to "zoom out".
- Star System Map
  - Client sends request to server.
  - Server finds all planets in star system, sends back to client.
  - Client puts them onto map.
  - When client hovers over a planet, request planet data from server.
- Client side saving
  - More efficient way to load maps.
  - Does not apply for star system maps.
  - When loading large map, save data to folder in APPDATA location.
  - Tell server that player has map, don't downlaod again.
- Scanning
  - All objects in the galaxy have to be scanned to be identified.
  - Basic scanners obtain basic information.
  - Advanced scanners retreve more information can can discoverrare objects like wormholes.
  - If a player tries to look at a planet, they see "Unscanned". Given the option to scan it.

# Marketplace
- The Marketplace Screen
  - This screen shows all trades.
  - The main page shows ALL the newest trades that have been placed (like /r/new).
  - The main page is split into two. Planet Trades and Goods Trades.
  - All trades have a time limit.
    - Player can choose to rase and lower time limit.
      - Has min and max time limit.
  - Trades that aren't sold are removed, the player will get their items back.
  - Filters
    - Can filter trades by region/sector/star system.
    - Can filter by time left.
    - Can filter by price/quantity.
    - Can filter by item/name/type.
    - Can filter by seller.
  - Trade info to display.
    - Planets
      - Planet name
      - Location (could load map)
      - List of buildings/upgrades/resources/stored resources.
    - Goods
      - Quantity
      - Price each
    - All Trades
      - Price
      - Time left
      - Seller
- Submiting a Trade
  - Planet Trading
    - You can only sell a planet if you own more than one planet.
    - If you have all but one of your planets up for trade, you cannot sell the last planet you own.
    - Planet cost is calculated by the buildings, upgrades and resources on it.
    - While a planet is up for sale, EVERYTHING WILL STOP.
    - Nothing will be processed, goods cannot enter or leave the planet.
    - Lockdown.
    - Will stay locked down till its sold or noone buys it.
    - Sell button creates PlanetTrade object.
      - Stores planet, seller, price and start time.
  - Goods Trading
    - Players place goods for sale from whatever storage depo it is in.
    - They choose the quantity of goods to sell.
    - The price has a min/max value based off global marketplace stats.
      - Min and max price is set every 12h based off stats from the previous 12h of selling.
      - Starting price and min/max increment is set by server config.
      - May set floor and ceiling price as well, they are limiter values. Cannot go over or under.
    - Player chooses quantity and price per unit, presses SELL button.
    - Sell button creates new TradeOffer object.
      - Stores item, price, quantity, seller and start time.
    - The goods are removed from storage and moved to the 'marketplace'.
- Making a trade
  - A player must buy an item before the time runs out.
  - The buyer can choose to either buy all of the goods or a portion.
    - If a portion is selected, the amount is removed from the trade.
    - The seller receives a portion of the credits.
    - The price of the item will not change.
  - Once the trade is complete the items will transfer from the 'marketplace' to the buyer.
  - The seller will receive their credits in their Stored Credits.
  - The trade is removed from the marketplace.
- Misc
  - The marketplace folder stores all data on trades.
    - Only stores trades that are complete, not offers that fail to sell.
    - Sell offer packets are saved to the marketplace folder.
    - All transactions will be logged.
     - Who sold what, and how much, for how much, to who, when.
    - Logs go into a temporary folder, this holds 12h worth of transactions.
     - Server will go over the transactions and calculate the new prices of goods. 
    - The 12h logs are then either moved to a perminant log folder for storage OR deleted.
       - Choose to save trade logs perminantly from server config.
  - Stats
    - Hourly/daily/weekly stats.
    - Total credits exchanged.
    - Total goods/planets exchanged.
    - Most goods sold in one transaction.
    - Most expensive transaction.
    - Player that spent the most (possibly don't want to give away players financial information).
    - GRAPHS!
  - Item Lookup
    - Shows stats for certain items (not to buy them)
    - Current price
    - Min/max price
    - Average prices over time
    - Quantity sold

# Chat
- Global Chat
  - One big chat channel.
- Area Chat
  - Specific chat channels for regions, sectors and star systems.
- Custom Chat
  - Chat channels that players create.
  - They give it a name and they join it.
  - They then give other people the name to join.
  - Can make it public or private.
  - Public custom channels are listed.
  - Private channels can only be joined if you have the name.
  - Custom channels are removed when the last person leaves.
