# MTGGoldfish Crawler
*Created By James Buckland*
### Purpose
This was an experiment started from the need to search card prices online quickly. Simply create a list of cards in a txt file with each seperate card on a unique line and watch as the program searches away and returns a CSV with all the cards' paper prices from MTGGoldfish !
### How to Run program
`java -jar GoldfishCrawler.jar <input file> <output file>`
 - input file must be a .txt file
 - Output file must be a .csv file
#### Prerequisite 
- Must have Java on current machine
- Must have up to date chrome driver (Given to you in the folder)

#### Config
- `blacklist.txt` is a file for black listing any sets from Magic: the Gathering. By default most online sets have been blacklisted to speed up the search algorith 
