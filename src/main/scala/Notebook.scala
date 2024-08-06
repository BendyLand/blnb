package bendyland.blnb.notebook

import bendyland.blnb.note.*
import scala.collection.mutable.*

class Notebook(name: String): 
  var notes = List.empty[Note]
  def getName = name

  def addNote(text: String) = 
    val note = Note(text)
    notes = notes :+ note

  def displayNotes =
    println("Displaying notes...")
    for i <- 0 until notes.size do
      println(notes(i).text)
      
