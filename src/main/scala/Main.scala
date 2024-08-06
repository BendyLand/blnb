import bendyland.blnb.notebook.*

@main def hello(): Unit =
  println("Hello blnb!")
  val test = Notebook("TestNB")  
  test.addNote("This is a test")
  test.displayNotes

