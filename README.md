Instructions
1. Open a command prompt
2. Navigate to the RobotMaze folder in command line
3. Enter the arguments required to run the program as demonstrated below:
Format = (bat file) (test file) (search method) e.g.:
RobotNav.bat RobotNav-test1.txt BFS

  Available test files:
  RobotNav-test1.txt
  RobotNav-test2.txt
  RobotNav-test3.txt
  RobotNav-test4.txt
  
  Available search methods:
  "BFS" - Breadth First Search
  "DFS" - Depth First Search
  "GBFS" - Greedy Best First Search
  "CUS1" - Iterative Deepening Depth First Search
  "CUS2" - Iterative Deepening A Star Search

4. Now enter the number of goals you would like to visit in this search

Key
█ = Wall
o = Robot origin
^ > V < = Directional solution path
G = Goal
s = Searched but not a part of the solution path
x = Robot current location
