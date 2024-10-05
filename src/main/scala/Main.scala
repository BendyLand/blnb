import bendyland.blnb.notebook.*
import bendyland.blnb.note.*
import java.io
import scala.io.StdIn.*
import scala.util.{Try, Success, Failure}
import scala.collection.mutable.*
import upickle.default.*
import java.io.PrintWriter

@main def run() =
	println("Welcome to blnb!")
	mainLoop()

def mainLoop() = 
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
			case x if x.contains("remove") =>
				val original = notebooks.size
				notebooks = removeNotebook(notebooks)
				if notebooks.size == original then
					println("Something went wrong.")
				else
					println("Notebook removed successfully!")
			case x if x.contains("erase") =>
				eraseNote(notebooks)
			case x if x.contains("exit") => 
				println("Saving notes to a file...")
				saveNotes(notebooks)
				println("Notebooks saved successfully!")
				println("Exiting...\nGoodbye")
				mainLoop = false
			case x if x.contains("save") =>
				println("Saving notes to file...")
				saveNotes(notebooks)
				println("Notebooks saved successfully!")
			case x if x.contains("read") =>
				println("Looking for saved notebooks...")
				val maybeNotes = readSavedNotes()
				maybeNotes match
					case None => println("No saved notebooks found.")
					case Some(x) => 
						notebooks = x
						println("Notebooks read successfully!")
			case x if x.contains("help") =>
				showHelpMenu()
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

def eraseNote(notebooks: List[Notebook]) = 
	println("Which notebook would you like to erase from? (Type the name)")
	showNotebooks(notebooks, "")
	val name = readLine() 
	val validNames = notebooks.map(_.name) 
	name match 
		case x if validNames.contains(x) => 
			println("Which note would you like to erase?")
			val nb = notebooks.filter(_.name == name)(0)
			nb.displayNotes
			val original = nb.notes.size
			val idStr = readLine()
			var id = Try(idStr.toInt).toOption
			var idInt = 0
			id match
				case None =>
					println("Invalid ID format.")
				case Some(x) =>
					idInt = x
			nb.eraseNote(idInt)
			if nb.notes.size == original then
				println("Something went wrong erasing your note.")
			else
				println("Note erased successfully!")
		case _: String => 
			println("Invalid notebook name.")

def removeNotebook(notebooks: List[Notebook]): List[Notebook] = 
	println("Which notebook would you like to remove? (Type the name)")
	showNotebooks(notebooks, "")
	val name = readLine() 
	val validNames = notebooks.map(_.name) 
	name match 
		case x if validNames.contains(x) => 
			notebooks.filter(_.name != name)
		case _: String => 
			println("Invalid notebook name.")
			notebooks

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
			println("Note added successfully!")

def saveNotes(notebooks: List[Notebook]) = 
	var result = Map.empty[String, List[Note]]
	for note <- notebooks do
		result(note.name) = note.notes
	val json = upickle.default.write(result)
	new PrintWriter("notebooks.json") { write(json); close }

def showHelpMenu() = 
	val commands = List(
		"new <opt notebook name>", 
		"write <opt notebook name>", 
		"show <opt notebook name>", 
		"remove (to remove a full notebook)",
		"erase (to erase a single note)",
		"save", 
		"exit",
	)
	println("Welcome to the blnb help menu!")
	for command <- commands do
		println(command)

def readSavedNotes(): Option[List[Notebook]] = 
	var result = List.empty[Notebook] 
	var file = Map.empty[String, List[Note]] 
	val tryContents = Try(scala.io.Source.fromFile("notebooks.json").mkString)
	tryContents match
		case Success(contents) =>
			file = upickle.default.read[Map[String, List[Note]]](contents)
			if !file.isEmpty then
				file.foreach { (name, lst) =>
					var nb = Notebook(name)
					for item <- lst do
						nb.addNote(item)
					result = result :+ nb
				}
				Some(result)
			else
				None
		case Failure(_) => 
			None
	