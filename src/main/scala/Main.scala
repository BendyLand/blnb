import bendyland.blnb.notebook.*
import scala.io.StdIn.*

@main def run() =
	println("Welcome to blnb!")
	var mainLoop = true
	var notebooks = List.empty[Notebook]
	while mainLoop do
		var input = ""
		println("Please enter a command:")
		input = readLine()
		input match
			case x if x.contains("new") => 
				notebooks = createNewNotebook(notebooks, input)
			case x if x.contains("write") =>
				writeNote(notebooks, x)
			case x if x.contains("show") =>  
				showNotebooks(notebooks, x)
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
		println(s"${nb.name}")
	}

def getNotebook(notebooks: List[Notebook], name: String): Option[Notebook] =
	val result = notebooks.filter(_.name == name)
	if result.size > 0 then
		Some(result(0))
	else
		None

def showNotebooks(notebooks: List[Notebook], arg: String) = 
	if arg.contains(" ") then
		val parts = arg.split(" ")
		val maybeNb = getNotebook(notebooks, parts(1))
		maybeNb match
			case None => println("Notebook not found.")
			case Some(nb) => nb.displayNotes
	else 
		listNotebooks(notebooks)

def writeNote(notebooks: List[Notebook], arg: String) = 
	var name = ""
	if arg.contains(" ") then
		val parts = arg.split(" ")
		name = parts(1)
	else
		println("Which notebook would you like to write in?")
		listNotebooks(notebooks)
		name = readLine()
	val maybeNb = getNotebook(notebooks, name)
	maybeNb match
		case None => println("Notebook not found.")
		case Some(arg) => 
			println("Please enter your text:")
			val text = readLine()
			arg.addNote(text)
