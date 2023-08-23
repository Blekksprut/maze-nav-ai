<h1>Instructions</h1>
<ol>
  <li>Open a command prompt</li>
  <li>Navigate to the RobotMaze folder in command line</li>
  <li>
    Enter the arguments required to run the program as demonstrated below:
    <br />
    Format = (bat file) (test file) (search method) e.g.:
    <br />
    RobotNav.bat RobotNav-test1.txt BFS
    <br />
    <br />
    Available test files:
    <br />
    RobotNav-test1.txt
    <br />
    RobotNav-test2.txt
    <br />
    RobotNav-test3.txt
    <br />
    RobotNav-test4.txt
    <br />
    <br />
    Available search methods:
    <br />
    "BFS" - Breadth First Search
    <br />
    "DFS" - Depth First Search
    <br />
    "GBFS" - Greedy Best First Search
    <br />
    "CUS1" - Iterative Deepening Depth First Search
    <br />
    "CUS2" - Iterative Deepening A Star Search
  </li>
  <li>Now enter the number of goals you woold like to visit in this search</li>
</ol>

<h1>Key</h1>
<ul>
  <li>█ = Wall</li>
  <li>o = Robot origin</li>
  <li>^ > V < = Directional solution path</li>
  <li>G = Goal</li>
  <li>s = Searched but not a part of the solution path</li>
  <li>x = Robot current location</li>
</ul>
