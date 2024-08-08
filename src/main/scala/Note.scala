package bendyland.blnb.note

class Note(val id: Int, var text: String):
	def editText(newText: String) = 
		this.text = newText
