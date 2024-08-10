package bendyland.blnb.note

import upickle.default.{ReadWriter => RW, macroRW}

case class Note(val id: Int, var text: String):
	def editText(newText: String) = 
		this.text = newText

object Note:
	implicit val rw: RW[Note] = macroRW