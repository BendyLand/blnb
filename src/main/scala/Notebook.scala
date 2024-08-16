package bendyland.blnb.notebook

import bendyland.blnb.note.*
import scala.collection.mutable.*

class Notebook(val name: String): 
	var notes = List.empty[Note]
	var currentNote = 1

	def addNote(text: String|Note) = 
		text match
			case x: String =>
				val note = Note(currentNote, x)
				notes = notes :+ note
			case x: Note =>
				notes  = notes :+ x
		currentNote += 1

	def displayNotes =
		println(s"Displaying notes from '${this.name}':")
		for i <- 0 until notes.size do
			println(s"${notes(i).id}) ${notes(i).text}")
			