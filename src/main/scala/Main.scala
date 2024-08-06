import bendyland.blnb.notebook.*
import scala.io.StdIn.*

@main def hello(): Unit =
  println("Welcome to blnb!")
  var mainLoop = true
  var notebooks = List.empty[Notebook]
  while mainLoop do
    var input = ""
    println("Please enter a command:")
    input = readLine()
    input match
      case x if x.contains("create") => 
        notebooks = createNewNotebook(notebooks, input)
      case x if x.contains("show") => 
        println("Which notebook would you like to see?")
        listNotebooks(notebooks)
      case x if x.contains("exit") => 
        println("Exiting...\nGoodbye")
        mainLoop = false
      case  _ => println("Unknown command.")

def createNewNotebook(notebooks: List[Notebook], input: String): List[Notebook] = 
  var result = List.empty[Notebook]
  val words = input.split(" ")
  if words.size > 1 then
    val nb = Notebook(words(1))
    result = notebooks :+ nb
  else
    println("Please enter a name for your notebook:")
    val temp = readLine()
    val nb = Notebook(temp)
    result = notebooks :+ nb
  println("Notebook created!")
  result

def listNotebooks(notebooks: List[Notebook]) = 
  println("Notebooks:")
  notebooks.foreach { nb =>
    println(s"${nb.name}" )
  }

